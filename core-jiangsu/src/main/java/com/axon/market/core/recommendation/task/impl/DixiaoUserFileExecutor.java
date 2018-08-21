package com.axon.market.core.recommendation.task.impl;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.constant.iconsumption.DixiaoTaskStatusEnum;
import com.axon.market.common.domain.iconsumption.DixiaoResultDomain;
import com.axon.market.common.domain.iconsumption.DixiaoTaskDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.recommendation.task.GetRecommendationFileExecutor;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.greenplum.OperateFileDataToGreenPlum;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.iconsumption.DixiaoTaskService;
import com.axon.market.core.service.ilog.LogReminderService;
import com.axon.market.core.service.itag.RemoteServerService;
import com.axon.market.dao.mapper.iconsumption.IDixiaoResultMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zhuwen on 2017/7/4.
 */
@Service("dixiaoUserFileExecutor")
public class DixiaoUserFileExecutor implements GetRecommendationFileExecutor {
    private static final Logger LOG = Logger.getLogger(DixiaoUserFileExecutor.class.getName());

    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @Autowired
    @Qualifier("recommendationConfigBean")
    private RecommendationConfigBean recommendationConfigBean;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    private byte[] columnByte = {0x01};
    private String columnSeparator = new String(columnByte);

    @Qualifier("dixiaoTaskService")
    @Autowired
    private DixiaoTaskService dixiaoTaskService;

    @Qualifier("DixiaoResultDao")
    @Autowired
    private IDixiaoResultMapper dixiaoResultMapper;

    @Autowired
    @Qualifier("operateFileDataToGreenPlum")
    private OperateFileDataToGreenPlum operateFileDataToGreenPlum;

    @Autowired
    @Qualifier("logReminderService")
    private LogReminderService logReminderService;

    @Override
    public void execute(String filePath) {
        File file = null;
        DixiaoTaskDomain taskDomain = null;
        final String userName = filePath.substring(filePath.lastIndexOf("/")+1);
        try {
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getServerHost(), recommendationConfigBean.getServerUser(), EncryptUtil.encryption(recommendationConfigBean.getServerPassword(), "market"), recommendationConfigBean.getServerPort(), recommendationConfigBean.getServerConnectType());
            String targetFileName = systemConfigBean.getDixiaoFileLocalPath() + "@model.dixiao_list";
            LOG.info(filePath+" dixiao user downloadFile begin:");
            if (fileOperateService.downloadFile(remoteServerDomain, filePath, targetFileName)) {
                file = new File(targetFileName);
                LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");

                while (iterator.hasNext()) {
                    String line = iterator.next();
                    String[] taskColumn = line.split(columnSeparator);
                    if (taskColumn != null && taskColumn.length == 24 ) {
                        String saleid = taskColumn[0].trim();
                        String sale_bo_id = taskColumn[1].trim();
                        List<DixiaoTaskDomain> list = dixiaoTaskService.queryDixiaoTaskByConfig(saleid, sale_bo_id, DixiaoTaskStatusEnum.TASK_INIT.getValue());
                        if (list == null || list.size()==0){
                            LOG.error("Can't find task of this file,file name is "+filePath);
                            logReminderService.insertJXHReminder("18914764596,18951871658", filePath);
                            return;
                        }
                        taskDomain = list.get(0);
                        dixiaoResultMapper.updateTaskFilename(taskDomain.getTaskid(), userName);
                        break;//该逻辑前提是一个用户文件的saleid和boid都必须一样
                    } else {
                        LOG.error("this line:" + line + " has error, the column length is:" + taskColumn.length + " ignore");
                    }
                }

                //批量导入数据
                iterator.close();
                iterator = FileUtils.lineIterator(file, "UTF-8");
                File greenPlumFile = new File(file.getParent() + File.separator + targetFileName.substring(targetFileName.indexOf('@') + 1));
                String added = "|"+taskDomain.getTaskid();
                String column = "sale_id,sale_boid_id,aim_sub_id,user_id,cust_id,phone,name,sex,area_code,sys_name,main_prod_name,main_prod_code,net_time,pin_prod_id,pin_charge_id,pin_level,lll_arpu,ll_arpu,l_arpu,a_arpu,a_volume,a_voice,is_roam,terminal,taskid";
                operateFileDataToGreenPlum.dataRefreshPlus(iterator, greenPlumFile, 6, added, column);
                File newFile = new File(file.getParent() + File.separator + userName);
                file.renameTo(newFile);
            }
        } catch (Exception e) {
            LOG.error("Get dixiao user file Task Error. ", e);
            return;
        }
        finally
        {
            LOG.info(filePath+" dixiao user downloadFile end");
            if (file != null)
            {
                FileUtils.deleteQuietly(file);
            }
        }

        final long taskid = taskDomain.getTaskid();
        //对根据地市进行切分
        final List<DixiaoResultDomain> resultList = new ArrayList<DixiaoResultDomain>();
        StringBuffer querysql = new StringBuffer();
        querysql.append("select count(*) as matchno,area_code,pin_level,taskid from model.dixiao_list where taskid =");
        querysql.append(taskid).append(" group by area_code,pin_level,taskid");


        greenPlumOperateService.query(querysql.toString(),new RowCallbackHandler()
        {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                DixiaoResultDomain resultDomain = new DixiaoResultDomain();
                resultDomain.setTaskid(taskid);
                resultDomain.setMonthcode(TimeUtil.formatDateToYM(new Date()));
                resultDomain.setRankid(resultSet.getString("pin_level"));
                resultDomain.setArea(resultSet.getString("area_code"));
                resultDomain.setMatchno(resultSet.getLong("matchno"));
                resultDomain.setIsonline(0);//文件入库后所有档位先放到线下处理
                resultDomain.setStatus(0);
                resultDomain.setIsnewest(1);
                resultDomain.setFtpflag(0);
                resultList.add(resultDomain);
            }
        }, 0);

        //统计数据更新
        if (resultList.size()>0) {
            dixiaoResultMapper.insertDixiaoResult(resultList);
        }
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("taskid", taskid);
        paras.put("status", DixiaoTaskStatusEnum.TASK_IMPORT.getValue());
        dixiaoResultMapper.modifyTaskStatus(paras);
        LOG.info(filePath + " dixiao user file process successfully!");
    }
}
