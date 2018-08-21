package com.axon.market.core.service.iconsumption;

import com.axon.market.common.bean.GreenPlumServerBean;
import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.constant.iconsumption.*;
import com.axon.market.common.domain.iconsumption.DixiaoResultDomain;
import com.axon.market.common.domain.iconsumption.DixiaoTaskDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.core.service.itag.RemoteServerService;
import com.axon.market.dao.mapper.iconsumption.IDixiaoResultMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Zhuwen on 2017/8/25.
 */
@Component("dixiaoTaskService")
public class DixiaoTaskService {
    private static final Logger LOG = Logger.getLogger(DixiaoTaskService.class.getName());

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Qualifier("DixiaoResultDao")
    @Autowired
    private IDixiaoResultMapper dixiaoResultMapper;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @Autowired
    @Qualifier("recommendationConfigBean")
    private RecommendationConfigBean recommendationConfigBean;

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("greenPlumServerBean")
    private GreenPlumServerBean greenPlumServerBean;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    private byte[] columnByte = {0x01};
    private String columnSeparator = new String(columnByte);

    public static DixiaoTaskService getInstance() {
        return (DixiaoTaskService) SpringUtil.getSingletonBean("dixiaoTaskService");
    }

    /**
     * 根据活动id、波次id以及目标客户群id查询任务信息
     *
     * @return
     */
    public List<DixiaoTaskDomain> queryDixiaoTaskByConfig(String saleid, String boid, Integer status) {
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("saleid", saleid);
        paras.put("boid", boid);
        if (status != null) {
            paras.put("status", status);
        }
        return dixiaoResultMapper.queryDixiaoTaskByConfig(paras);
    }

    /**
     * 根据活动id设置指定任务失效
     *
     * @return
     */
    public int setTaskInvalidBySaleID(String saleid) {
        return dixiaoResultMapper.setTaskInvalidBySaleID(saleid);
    }

    /**
     * 推送文件给风雷
     *
     * @param
     * @return
     */
    public void sendToFenglei() {
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("notify_ftp", 1);
        List<DixiaoTaskDomain> list = dixiaoResultMapper.queryValidDixiaoTaskByConfig(paras);
        if (list == null || list.isEmpty()) {

        } else {
            for (int i = 0; i < list.size(); i++) {
                DixiaoTaskDomain taskDomain = list.get(i);
                paras.put("taskid", taskDomain.getTaskid());
                paras.put("notify_ftp", 0);
                File configfile = new File(systemConfigBean.getDixiaoFileLocalPath() + taskDomain.getConfig_file_name());
                String configFileName = systemConfigBean.getDixiaoFileLocalPath() + taskDomain.getConfig_file_name();
                File userfile = new File(systemConfigBean.getDixiaoFileLocalPath() + taskDomain.getUser_file_name());
                String userFileName = systemConfigBean.getDixiaoFileLocalPath() + taskDomain.getUser_file_name();
                RemoteServerDomain uploadServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getFengleiServerHost(), recommendationConfigBean.getFengleiServerUser(), EncryptUtil.encryption(recommendationConfigBean.getFengleiServerPassword(), "market"), recommendationConfigBean.getFengleiServerPort(), recommendationConfigBean.getFengleiServerConnectType());
                if (configfile.exists()) {
                    if (!fileOperateService.uploadFile(uploadServerDomain, configFileName, "/DiXiao/" + taskDomain.getConfig_file_name())) {
                        paras.put("notify_ftp", -1);
                        LOG.error("ftp to fenglei failed, the file name is " + taskDomain.getConfig_file_name());
                    }
                } else {
                    paras.put("notify_ftp", -1);
                    LOG.error("the file ftp to fenglei is not existed, please check the file" + taskDomain.getConfig_file_name());
                }

                if (userfile.exists()) {
                    if (!fileOperateService.uploadFile(uploadServerDomain, userFileName, "/DiXiao/" + taskDomain.getUser_file_name())) {
                        paras.put("notify_ftp", -1);
                        LOG.error("ftp to fenglei failed, the file name is " + taskDomain.getUser_file_name());
                    }
                } else {
                    paras.put("notify_ftp", -1);
                    LOG.error("the file ftp to fenglei is not existed, please check the file" + taskDomain.getUser_file_name());
                }
                dixiaoResultMapper.modifyNotifyFtp(paras);
            }
        }
    }

    /**
     * 根据Mysql的任务表状态情况往GP库插任务状态数据
     *
     * @return
     */
    public void insertTaskStatusGP(Integer status) {
        List<DixiaoTaskDomain> list = queryDixiaoTaskByConfig("", "", status);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                DixiaoTaskDomain taskDomain = list.get(i);
                StringBuffer querySql = new StringBuffer();
                querySql.append("select count(*) from model.dixiao_task_status where sale_id = '").append(taskDomain.getSaleid());
                querySql.append("' and sale_boid_id= '").append(taskDomain.getBoid()).append("'");
                try {
                    if (greenPlumOperateService.queryRecordCount(querySql.toString()) < 1) {
                        StringBuffer insertSql = new StringBuffer();
                        insertSql.append("insert into model.dixiao_task_status(sale_id,sale_boid_id,status) values ('").append(taskDomain.getSaleid());
                        insertSql.append("','").append(taskDomain.getBoid()).append("',");
                        insertSql.append(DixiaoGPTaskStatusEnum.TASK_INIT.getValue()).append(")");
                        greenPlumOperateService.update(insertSql.toString());
                        LOG.info("Begin to update MAPID and ranktype,taskid is " + taskDomain.getTaskid());
                        //刷新GP库明细数据的mapid和level_type
                        updateMapIDGP(taskDomain);
                        LOG.info("Finish updating MAPID and ranktype,taskid is " + taskDomain.getTaskid());
                    }
                } catch (Exception e) {
                    LOG.error("SQL execute failed of task:dixiao_rule_match_task");
                }
            }
        }
    }

    /**
     * 根据任务状态，刷新明细表中的mapid和taskid
     *
     * @return
     */
    public void updateMapIDGP(DixiaoTaskDomain taskDomain) {
        try {
            //每一个GP任务找到对应的任务分配结果
            Map<String, Object> paras = new HashMap<String, Object>();
            paras.put("taskid", taskDomain.getTaskid());
            List<DixiaoResultDomain> result = dixiaoResultMapper.queryDixiaoResult(paras);
            if (result != null) {
                for (int j = 0; j < result.size(); j++) {
                    //根据每个分配结果更新明细表中对应的分配ID和taskid
                    DixiaoResultDomain resultDomain = result.get(j);
                    StringBuffer update = new StringBuffer();
                    update.append("update model.dixiao_list set mapid = ").append(resultDomain.getId()).append(",level_type='");
                    update.append(resultDomain.getRanktype()).append("' where taskid=").append(taskDomain.getTaskid()).append(" and area_code = '");
                    update.append(resultDomain.getArea()).append("' and pin_level= '").append(resultDomain.getRankid()).append("'");
                    if (greenPlumOperateService.update(update.toString()) != resultDomain.getMatchno()) {
                        LOG.info("update result is not matched with matchno, please check result id:" + resultDomain.getId());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("execute updateMapIDGP failed");
        }
    }

    /**
     * 根据任务状态，获取GP的dixiao_task_status数据
     *
     * @return
     */
    public List<DixiaoTaskDomain> getStatusList(int status) {
        final List<DixiaoTaskDomain> list = new ArrayList<DixiaoTaskDomain>();
        String querySql = "select sale_id,sale_boid_id from model.dixiao_task_status where status = " + status;
        greenPlumOperateService.query(querySql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                DixiaoTaskDomain taskDomain = new DixiaoTaskDomain();
                taskDomain.setSaleid(resultSet.getString("sale_id"));
                taskDomain.setBoid(resultSet.getString("sale_boid_id"));
                list.add(taskDomain);
            }
        }, 0);
        return list;
    }

    /**
     * 线下分配文件推送话+和更新统计信息
     *
     * @return
     */
    public void ftpAndUpdateOffline() {
        //将GP库任务为1的查询出来，后面作为匹配条件
        List<DixiaoTaskDomain> listGP = getStatusList(DixiaoGPTaskStatusEnum.TASK_BIGDATA_FINISH.getValue());
        Map<String, Integer> map = new HashMap<String, Integer>();
        if (listGP == null || listGP.isEmpty()) {
            LOG.info("No task finished by big data,quit");
            return;
        } else {
            for (int i = 0; i < listGP.size(); i++) {
                DixiaoTaskDomain taskGP = listGP.get(i);
                String keyGP = taskGP.getSaleid() + "," + taskGP.getBoid();
                map.put(keyGP, 1);
            }
        }

        List<DixiaoTaskDomain> list = queryDixiaoTaskByConfig("", "", DixiaoTaskStatusEnum.TASK_CHOOSE_FINISH_OFFLINE.getValue());
        Map<String, Object> paras = new HashMap<String, Object>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                DixiaoTaskDomain taskDomain = list.get(i);
                String key = taskDomain.getSaleid() + "," + taskDomain.getBoid();
                //页面分配已经完成，后台大数据还未处理完成，该推送任务只能继续等
                if (!map.containsKey(key)) {
                    LOG.info("Waiting for big data to handle task:" + taskDomain.getTaskid());
                    continue;
                }
                paras.put("taskid", taskDomain.getTaskid());
                if (!ftptoVoicePlusOffline(taskDomain)) {
                    paras.put("status", DixiaoTaskStatusEnum.TASK_PUSH_OFFLINE_FAIL.getValue());
                    dixiaoResultMapper.modifyTaskStatus(paras);
                    LOG.error("dixiao_file_ftp_offline_task failed,call ftptoVoicePlusOffline failed,taskid is:" + taskDomain.getTaskid());
                    continue;
                }

                if (!updateResult(taskDomain)) {
                    paras.put("status", DixiaoTaskStatusEnum.TASK_PUSH_OFFLINE_FAIL.getValue());
                    dixiaoResultMapper.modifyTaskStatus(paras);
                    LOG.error("dixiao_file_ftp_offline_task failed,call updateResult failed,taskid is:" + taskDomain.getTaskid());
                    continue;
                }
                paras.put("status", DixiaoTaskStatusEnum.TASK_PUSH_OFFLINE.getValue());
                dixiaoResultMapper.modifyTaskStatus(paras);

                //给线上省级管理员发送提醒短信
                String telephone = smsConfigBean.getDixiaoRemindertoOnlineProvinceContact();
                String message = MessageFormat.format("{0}", smsConfigBean.getDixiaoRemindertoOnlineProvinceSmsContent());
                sendSmsService.sendReminderNoticeSms(telephone, message);
                LOG.info("send msg successfully!telephone is:" + telephone + "message is " + message);
            }
        }
    }

    /**
     * 线下分配文件推送话+
     *
     * @return
     */
    public boolean ftptoVoicePlusOffline(DixiaoTaskDomain taskDomain) {
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("taskid", taskDomain.getTaskid());
        paras.put("isonline", DixiaoIsOnlineEnum.OFFLINE.getValue());
        paras.put("status", DixiaoAllocateStatusEnum.TASK_ALLOCATE.getValue());
        paras.put("method", DixiaoPushMethodEnum.METHOD_VOICEPLUS.getValue());
        String ranktype = taskDomain.getRanktype().equals("0")?"flow":"voice";
        String filedate = TimeUtil.formatDateToYMDHMS(new Date());
        final String userfilename = "dx_user_offline_" + ranktype + filedate + "_" + taskDomain.getSaleid();
        final String configfilename = "dx_config_offline_" + ranktype + filedate + "_" + taskDomain.getSaleid();
        String downloadFilename = greenPlumServerBean.getGpDataFilePath() + userfilename;
        String localFilename = systemConfigBean.getDixiaoFileLocalPath() + userfilename;
        String tempFilename = systemConfigBean.getDixiaoFileLocalPath() + userfilename + "temp";

        try {
            List<DixiaoResultDomain> result = dixiaoResultMapper.queryDixiaoResult(paras);
            StringBuffer mapid = new StringBuffer();
            mapid.append("");
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    mapid.append(result.get(i).getId()).append(",");
                }
                mapid.deleteCharAt(mapid.lastIndexOf(","));

                //有分配了需要先生成用户文件
                StringBuffer whereSql = new StringBuffer();
                whereSql.append("where taskid =").append(taskDomain.getTaskid());
                whereSql.append(" and mapid in (").append(mapid.toString()).append(") and business_hall_code is not null");
                List<String> columnlist = getcolumnList();
                LOG.info("dixiao_file_ftp_offline_task:start to generate file from GP database,file name is:"+downloadFilename);
                if (!greenPlumOperateService.downloadDataFromTable("model.dixiao_list", downloadFilename, columnlist, whereSql.toString(), columnSeparator)) {
                    LOG.error("download data from model.dixiao_list failed");
                    return false;
                }
                LOG.info("dixiao_file_ftp_offline_task:end to generate file from GP database,file name is:"+downloadFilename);

                //下载到本地
                String host = greenPlumServerBean.getGpServer();
                String user = greenPlumServerBean.getGpServerUser();
                String password = greenPlumServerBean.getGpServerPassword();
                String port = String.valueOf(greenPlumServerBean.getGpServerPort());
                RemoteServerDomain downloadServerDomain = remoteServerService.generateRemoteServerDomain(host, user, password, port, "sftp");
                LOG.info("dixiao_file_ftp_offline_task:start to download file to local,file name is:"+tempFilename);
                if (!fileOperateService.downloadFile(downloadServerDomain, downloadFilename, tempFilename)) {
                    LOG.error("download offline file failed,file name is:" + downloadFilename);
                    return false;
                }
                LOG.info("dixiao_file_ftp_offline_task:end to download file to local,file name is:"+tempFilename);

                //将号码解密，重新生成一份文件
                File tempfile = new File(tempFilename);
                File destfile = new File(localFilename);
                LineIterator iterator = FileUtils.lineIterator(tempfile, "UTF-8");
                List<String> lineList = new ArrayList<String>();
                int recordCount = 0;
                //防止下载的是个空文件，先写个空
                FileUtils.writeLines(destfile, "UTF-8", lineList, true);
                while(iterator.hasNext()){
                    recordCount++;
                    String line = iterator.next();
                    String[] column = line.split(columnSeparator);
                    String decrypt = axonEncrypt.decryptWithoutCountrycode(column[5]);
                    String newline = line.replace(column[5], decrypt);
                    lineList.add(newline);
                    if (recordCount%10000==0){
                        FileUtils.writeLines(destfile, "UTF-8", lineList, true);
                        lineList.clear();
                        recordCount = 0;
                    }
                }
                if (recordCount!=0){
                    FileUtils.writeLines(destfile, "UTF-8", lineList, true);
                    lineList.clear();
                }
                if (iterator != null)
                {
                    iterator.close();
                }
            } else {
                //没分配也要生成个空用户文件
                LOG.info("WARNING:no records to assign for task:" + taskDomain.getTaskid());
                File file = new File(localFilename);
                if (!file.createNewFile()){
                    LOG.error(localFilename+" already existed, please check!");
                    return false;
                }
            }

            //ftp到话+
            String configFileName = systemConfigBean.getDixiaoFileLocalPath() + taskDomain.getConfig_file_name();
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getVoiceplusServerHost(), recommendationConfigBean.getVoiceplusServerUser(), EncryptUtil.encryption(recommendationConfigBean.getVoiceplusServerPassword(), "market"), recommendationConfigBean.getVoiceplusServerPort(), recommendationConfigBean.getVoiceplusServerConnectType());

            LOG.info("dixiao_file_ftp_offline_task:start to upload config file to voice+,file name is:"+configFileName);
            if (!fileOperateService.uploadFile(remoteServerDomain, configFileName, "/" + configfilename)) {
                LOG.error("upload offline config file failed,file name is:" + configFileName);
                return false;
            }
            LOG.info("dixiao_file_ftp_offline_task:upload offline config file successfully,file name is:"+configFileName);

            LOG.info("dixiao_file_ftp_offline_task:start to upload user file to voice+,file name is:"+localFilename);
            if (!fileOperateService.uploadFile(remoteServerDomain, localFilename, "/" + userfilename)) {
                LOG.error("upload online user file failed,file name is:" + localFilename);
                return false;
            }
            LOG.info("dixiao_file_ftp_offline_task:upload offline user file successfully,file name is:"+localFilename);
        } catch (Exception e) {
            LOG.error("execute ftptoVoicePlusOffline failed");
            return false;
        }
        return true;
    }

    /**
     * 更新统计信息
     *
     * @return
     */
    public boolean updateResult(DixiaoTaskDomain taskDomain) {
        //集合A统计规则没有匹配上的
        StringBuffer querySql = new StringBuffer();
        querySql.append("select count(*) as matchno,area_code,pin_level from model.dixiao_list ");
        querySql.append("where taskid=").append(taskDomain.getTaskid()).append(" and BUSINESS_HALL_CODE is null group by area_code,pin_level");
        final Map<String, DixiaoResultDomain> offlineMap = new HashMap<String, DixiaoResultDomain>();

        try {
            greenPlumOperateService.query(querySql.toString(), new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet resultSet) throws SQLException {
                    DixiaoResultDomain result = new DixiaoResultDomain();
                    result.setMatchno(resultSet.getLong("matchno"));
                    result.setArea(resultSet.getString("area_code"));
                    result.setRankid(resultSet.getString("pin_level"));
                    String key = result.getArea() + "," + result.getRankid();
                    offlineMap.put(key, result);
                }
            }, 0);

            //集合B获取线下选好的且推送方式为发送话+的
            Map<String, Object> paras = new HashMap<String, Object>();
            paras.put("taskid", taskDomain.getTaskid());
            paras.put("status", DixiaoAllocateStatusEnum.TASK_ALLOCATE.getValue());
            paras.put("method",DixiaoPushMethodEnum.METHOD_VOICEPLUS.getValue());
            List<DixiaoResultDomain> result = dixiaoResultMapper.queryDixiaoResult(paras);
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    DixiaoResultDomain resultDomain = result.get(i);
                    String combine = resultDomain.getArea() + "," + resultDomain.getRankid();
                    //集合B中如果和集合A有交集，则该统计结果需要刷新
                    if (offlineMap.containsKey(combine)) {
                        long matchno = offlineMap.get(combine).getMatchno();
                        if (matchno > resultDomain.getMatchno()) {
                            LOG.error("the matchno of this record has problem, taskid is:" + resultDomain.getTaskid() + " id is:" + resultDomain.getId());
                            return false;
                        }
                        //更新matchno为B.matchno-A.matchno
                        dixiaoResultMapper.updateResultMatchno(resultDomain.getId(), resultDomain.getMatchno() - matchno);

                        //插一条新记录，新纪录的matchno为A.matchno
                        DixiaoResultDomain insertResult = getResultDomain(resultDomain, matchno);
                        dixiaoResultMapper.insertOneDixiaoResult(insertResult);

                        //dixiao_list的mapid更新
                        String updateSql = "update model.dixiao_list set mapid =" + insertResult.getId() + " where mapid = " + resultDomain.getId() + " and taskid = " + resultDomain.getTaskid() + " and BUSINESS_HALL_CODE is null";
                        greenPlumOperateService.update(updateSql);
                    } else {
                        //nothing to do
                    }
                }
            }
            //将没有分配到线下的统计更新为线上
            dixiaoResultMapper.updateResultToOnline(taskDomain.getTaskid());
        } catch (Exception e) {
            LOG.error("execute updateResult failed");
            return false;
        }
        return true;
    }

    /**
     * 线下分配文件推送话+和更新统计信息
     *
     * @return
     */
    public void ftpAndUpdateOnline() {
        List<DixiaoTaskDomain> list = queryDixiaoTaskByConfig("", "", DixiaoTaskStatusEnum.TASK_CHOOSE_FINISH_ONLINE.getValue());
        Map<String, Object> paras = new HashMap<String, Object>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                DixiaoTaskDomain taskDomain = list.get(i);
                paras.put("taskid", taskDomain.getTaskid());
                if (!updateListParntercode(taskDomain)) {
                    paras.put("status", DixiaoTaskStatusEnum.TASK_PUSH_ONLINE_FAIL.getValue());
                    dixiaoResultMapper.modifyTaskStatus(paras);
                    LOG.error("dixiao_file_ftp_online_task failed,call updateListParntercode failed,taskid is:" + taskDomain.getTaskid());
                    continue;
                }

                if (!ftptoVoicePlusOnline(taskDomain)) {
                    paras.put("status", DixiaoTaskStatusEnum.TASK_PUSH_ONLINE_FAIL.getValue());
                    dixiaoResultMapper.modifyTaskStatus(paras);
                    LOG.error("dixiao_file_ftp_online_task failed,call ftptoVoicePlusOnline failed,taskid is:" + taskDomain.getTaskid());
                    continue;
                }

                paras.put("status", DixiaoTaskStatusEnum.TASK_PUSH_ONLINE.getValue());
                dixiaoResultMapper.modifyTaskStatus(paras);
            }
        }
    }

    /**
     * 更新分配到的线上明细的合作伙伴编码
     *
     * @return
     */
    public boolean updateListParntercode(DixiaoTaskDomain taskDomain) {
        try {
            Map<String, Object> paras = new HashMap<String, Object>();
            paras.put("taskid", taskDomain.getTaskid());
            paras.put("status", DixiaoAllocateStatusEnum.TASK_ALLOCATE.getValue());
            paras.put("isonline", DixiaoIsOnlineEnum.ONLINE.getValue());
            paras.put("ftpflag", 0);
            List<DixiaoResultDomain> result = dixiaoResultMapper.queryDixiaoResult(paras);
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    DixiaoResultDomain resultDomain = result.get(i);
                    StringBuffer updateSql = new StringBuffer();
                    updateSql.append("update model.dixiao_list set business_hall_code = '" + resultDomain.getPartnercode() + "' where taskid = ");
                    updateSql.append(resultDomain.getTaskid()).append(" and mapid = ").append(resultDomain.getId());
                    greenPlumOperateService.update(updateSql.toString());
                }
            }
        } catch (Exception e) {
            LOG.error("execute updateListParntercode failed");
            return false;
        }
        return true;
    }

    /**
     * 线下分配文件推送话+
     *
     * @return
     */
    public boolean ftptoVoicePlusOnline(DixiaoTaskDomain taskDomain) {

        try {
            Map<String, Object> paras = new HashMap<String, Object>();
            paras.put("taskid", taskDomain.getTaskid());
            paras.put("isonline", DixiaoIsOnlineEnum.ONLINE.getValue());
            paras.put("status", DixiaoAllocateStatusEnum.TASK_ALLOCATE.getValue());
            paras.put("ftpflag", 0);
            String ranktype = taskDomain.getRanktype().equals("0")?"flow":"voice";
            String filedate = TimeUtil.formatDateToYMDHMS(new Date());
            final String userfilename = "dx_user_online_" + ranktype+ filedate + "_" + taskDomain.getSaleid();
            final String configfilename = "dx_config_online_" + ranktype + filedate + "_" + taskDomain.getSaleid();
            String localFilename = systemConfigBean.getDixiaoFileLocalPath() + userfilename;
            String tempFilename = systemConfigBean.getDixiaoFileLocalPath() + userfilename + "temp";

            List<DixiaoResultDomain> result = dixiaoResultMapper.queryDixiaoResult(paras);
            StringBuffer mapid = new StringBuffer();
            mapid.append("");
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    mapid.append(result.get(i).getId()).append(",");
                }
                mapid.deleteCharAt(mapid.lastIndexOf(","));
                StringBuffer whereSql = new StringBuffer();
                whereSql.append("where taskid =").append(taskDomain.getTaskid());
                whereSql.append(" and mapid in (").append(mapid.toString()).append(")");
                List<String> columnlist = getcolumnList();
                String downloadFilename = greenPlumServerBean.getGpDataFilePath() + userfilename;
                LOG.info("dixiao_file_ftp_offline_task:start to generate file from GP database,file name is:"+downloadFilename);
                if (!greenPlumOperateService.downloadDataFromTable("model.dixiao_list", downloadFilename, columnlist, whereSql.toString(), columnSeparator)) {
                    LOG.error("download online data from model.dixiao_list failed");
                    return false;
                }
                LOG.info("dixiao_file_ftp_offline_task:end to generate file from GP database,file name is:"+downloadFilename);

                //下载到本地
                String host = greenPlumServerBean.getGpServer();
                String user = greenPlumServerBean.getGpServerUser();
                String password = greenPlumServerBean.getGpServerPassword();
                String port = String.valueOf(greenPlumServerBean.getGpServerPort());
                RemoteServerDomain downloadServerDomain = remoteServerService.generateRemoteServerDomain(host, user, password, port, "sftp");
                LOG.info("dixiao_file_ftp_offline_task:start to download file to local,file name is:"+tempFilename);
                if (!fileOperateService.downloadFile(downloadServerDomain, downloadFilename, tempFilename)) {
                    LOG.error("download file failed,file name is:" + downloadFilename);
                    return false;
                }
                LOG.info("dixiao_file_ftp_offline_task:end to download file to local,file name is:"+tempFilename);

                //将号码解密，重新生成一份文件
                File tempfile = new File(tempFilename);
                File destfile = new File(localFilename);
                LineIterator iterator = FileUtils.lineIterator(tempfile, "UTF-8");
                List<String> lineList = new ArrayList<String>();
                int recordCount = 0;
                //防止下载的是个空文件，先写个空
                FileUtils.writeLines(destfile, "UTF-8", lineList, true);
                while(iterator.hasNext()){
                    recordCount++;
                    String line = iterator.next();
                    String[] column = line.split(columnSeparator);
                    String decrypt = axonEncrypt.decryptWithoutCountrycode(column[5]);
                    String newline = line.replace(column[5], decrypt);
                    lineList.add(newline);
                    if (recordCount%10000==0){
                        FileUtils.writeLines(destfile, "UTF-8", lineList, true);
                        lineList.clear();
                        recordCount = 0;
                    }
                }
                if (recordCount!=0){
                    FileUtils.writeLines(destfile, "UTF-8", lineList, true);
                    lineList.clear();
                }
                if (iterator != null)
                {
                    iterator.close();
                }
            } else {
                //线上没有分配，生成个空文件
                LOG.info("WARNING:no records to assign for task:" + taskDomain.getTaskid());
                File file = new File(localFilename);
                if (!file.createNewFile()){
                    LOG.error(localFilename+" already existed, please check!");
                    return false;
                }
            }

            //ftp到话+
            String configFileName = systemConfigBean.getDixiaoFileLocalPath() + taskDomain.getConfig_file_name();
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getVoiceplusServerHost(), recommendationConfigBean.getVoiceplusServerUser(), EncryptUtil.encryption(recommendationConfigBean.getVoiceplusServerPassword(), "market"), recommendationConfigBean.getVoiceplusServerPort(), recommendationConfigBean.getVoiceplusServerConnectType());

            LOG.info("dixiao_file_ftp_offline_task:start to upload config file to voice+,file name is:"+configFileName);
            if (!fileOperateService.uploadFile(remoteServerDomain, configFileName, "/" + configfilename)) {
                LOG.error("upload online config file failed,file name is:" + configFileName);
                return false;
            }
            LOG.info("dixiao_file_ftp_online_task:upload online config file successfully,file name is:"+configFileName);

            LOG.info("dixiao_file_ftp_offline_task:start to upload user file to voice+,file name is:"+localFilename);
            if (!fileOperateService.uploadFile(remoteServerDomain, localFilename, "/" + userfilename)) {
                LOG.error("upload online user file failed,file name is:" + localFilename);
                return false;
            }
            LOG.info("dixiao_file_ftp_online_task:upload online user file successfully,file name is:"+localFilename);

            //更新结果表状态为ftp成功
            dixiaoResultMapper.updateDixiaoFtpflag(taskDomain.getTaskid());
        } catch (Exception e) {
            LOG.error("execute ftptoVoicePlusOnline failed");
            return false;
        }
        return true;
    }

    /**
     * 获取线上统计结果
     *
     * @return
     */
    public DixiaoResultDomain getResultDomain(DixiaoResultDomain copyDomain, long matchno) {
        DixiaoResultDomain resultDomain = new DixiaoResultDomain();
        resultDomain.setMatchno(matchno);
        resultDomain.setTaskid(copyDomain.getTaskid());
        resultDomain.setMonthcode(copyDomain.getMonthcode());
        resultDomain.setRankid(copyDomain.getRankid());
        resultDomain.setRanktype(copyDomain.getRanktype());
        resultDomain.setArea(copyDomain.getArea());
        resultDomain.setIsonline(DixiaoIsOnlineEnum.ONLINE.getValue());
        resultDomain.setStatus(DixiaoAllocateStatusEnum.STATUS_UNALLOCATE.getValue());
        resultDomain.setIsnewest(1);
        return resultDomain;
    }

    /**
     * 获取字段封装到list
     *
     * @return
     */
    public List<String> getcolumnList() {
        List<String> list = new ArrayList<String>();
        String column = "sale_id,sale_boid_id,aim_sub_id,user_id,cust_id,phone,name,sex,area_code,sys_name,main_prod_name,main_prod_code,net_time,pin_prod_id,pin_charge_id,pin_level,lll_arpu,ll_arpu,l_arpu,a_arpu,a_volume,a_voice,is_roam,terminal,level_type,business_hall_code";
        String[] str = column.split(",");
        for (String tmp : str) {
            list.add(tmp);
        }
        return list;
    }

}
