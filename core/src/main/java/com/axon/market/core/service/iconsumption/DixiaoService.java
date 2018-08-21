package com.axon.market.core.service.iconsumption;

import com.axon.market.common.bean.*;
import com.axon.market.common.constant.iconsumption.*;
import com.axon.market.common.domain.iconsumption.DixiaoCodeDomain;
import com.axon.market.common.domain.iconsumption.DixiaoListDomain;
import com.axon.market.common.domain.iconsumption.DixiaoResultDomain;
import com.axon.market.common.domain.iconsumption.DixiaoTaskDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.excel.export.ExportConfigFactory;
import com.axon.market.common.excel.export.FileExportor;
import com.axon.market.common.excel.export.domain.common.ExportConfig;
import com.axon.market.common.excel.export.domain.common.ExportResult;
import com.axon.market.common.excel.export.exception.FileExportException;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.dao.mapper.iconsumption.IDixiaoResultMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;


/**
 * Created by zhuwen on 2017/7/24.
 */
@Component("dixiaoService")
public class DixiaoService {
    private static final Logger LOG = Logger.getLogger(DixiaoService.class.getName());

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Qualifier("DixiaoResultDao")
    @Autowired
    private IDixiaoResultMapper dixiaoResultMapper;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    private static Map<String, String> areaMap = new HashMap<String, String>() {
        {
            put("0025", "南京市");
            put("0510", "无锡市");
            put("0511", "镇江市");
            put("0512", "苏州市");
            put("0513", "南通市");
            put("0514", "扬州市");
            put("0515", "盐城市");
            put("0516", "徐州市");
            put("0517", "淮安市");
            put("0518", "连云港市");
            put("0519", "常州市");
            put("0523", "泰州市");
            put("0527", "宿迁市");
        }
    };

    public static DixiaoService getInstance() {
        return (DixiaoService) SpringUtil.getSingletonBean("dixiaoService");
    }

    /**
     * 低消任务提醒
     *
     * @param userDomain
     * @return
     */
    public ServiceResult reminder(UserDomain userDomain) {
        ServiceResult result = new ServiceResult();
        String message = null;
        String telephone = null;
        if (userDomain.getAreaCode() == 99999) {
            //省级
            message = MessageFormat.format("{0}", smsConfigBean.getDixiaoRemindertoCitySmsContent());
            telephone = smsConfigBean.getDixiaoRemindertoCityContact();
        } else {
            //非省级
            DecimalFormat df = new DecimalFormat("0000");
            message = MessageFormat.format(smsConfigBean.getDixiaoRemindertoProvinceSmsContent(), areaMap.get(df.format(userDomain.getAreaCode())));
            telephone = smsConfigBean.getDixiaoRemindertoProvinceContact();
        }

        if (message.trim().equals("")) {
            result.setRetValue(-1);
            result.setDesc("低消提醒短信内容为空，请联系系统管理员核实!");
        } else if (telephone.trim().equals("")) {
            result.setRetValue(-1);
            result.setDesc("低消提醒短信联系方式为空，请联系系统管理员核实!");
        } else {
            String[] telephonelist = telephone.split(",");
            for (String tel:telephonelist){
                sendSmsService.sendReminderNoticeSms(telephone, message);
                LOG.info("send msg successfully!telephone is:" + tel + "message is " + message);
            }
        }
        return result;
    }

    /**
     * 低消任务线下分配
     *
     * @param paras
     * @return
     */
    public ServiceResult allocateDixiaoOffline(Map<String, Object> paras, UserDomain userDomain) {
        ServiceResult result = new ServiceResult();

        if (userDomain.getAreaCode() == 99999) {
            result.setDesc("操作员权限问题，请联系系统管理员!");
            result.setRetValue(-1);
            return result;
        }

        if (paras.get("taskid") == null) {
            result.setDesc("系统异常，请联系系统管理员!");
            result.setRetValue(-1);
            return result;
        }

        //判断状态
        DixiaoTaskDomain domain = null;
        domain = dixiaoResultMapper.queryOneDixiaoTask((Integer)paras.get("taskid"));
        if (domain==null){
            result.setDesc("系统异常，请联系系统管理员!");
            result.setRetValue(-1);
            return result;
        }
        //市级线下分配只有在状态为3才允许
        if (domain.getStatus() != DixiaoTaskStatusEnum.TASK_CITY_CHOOSE.getValue()){
            result.setDesc("档位已经锁定，无法再进行分配，请联系系统管理员!");
            result.setRetValue(-1);
            return result;
        }

        //补齐4位
        DecimalFormat df = new DecimalFormat("0000");
        paras.put("area", df.format(userDomain.getAreaCode()));

        paras.put("isonline", 0);
        String addedstr = paras.get("addedlist") == null ? "" : (String) (paras.get("addedlist"));
        String deletedstr = paras.get("deletedlist") == null ? "" : (String) (paras.get("deletedlist"));

        //新增分配
        if (!"".equals(addedstr)) {
            dixiaoResultMapper.updateToAllocated(paras);
        }

        //删除分配
        if (!"".equals(deletedstr)) {
            dixiaoResultMapper.updateToUnallocated(paras);
        }

        //更新推送方式
        if (paras.get("method") != null) {
            dixiaoResultMapper.updateTaskMethod(paras);
        }

        return result;
    }

    /**
     * 低消任务线下分配
     *
     * @param paras
     * @return
     */
    public ServiceResult allocateDixiaoOfflineforProvince(Map<String, Object> paras) {
        ServiceResult result = new ServiceResult();

        if (paras.get("taskid") == null) {
            result.setDesc("系统异常，请联系系统管理员!");
            result.setRetValue(-1);
            return result;
        }

        paras.put("isonline", 0);
        String addedstr = paras.get("addedlist") == null ? "" : (String) (paras.get("addedlist"));
        String deletedstr = paras.get("deletedlist") == null ? "" : (String) (paras.get("deletedlist"));

        //新增分配
        if (!"".equals(addedstr)) {
            dixiaoResultMapper.updateToAllocated(paras);
        }

        //更新推送方式
        if (paras.get("methodmap") != null) {
            StringBuffer areaDownloadList = new StringBuffer();
            StringBuffer areaVoiceList = new StringBuffer();
            areaDownloadList.append("");
            areaVoiceList.append("");
            Map<String, Integer> methodMap = (Map<String, Integer>) paras.get("methodmap");

            for (String area : methodMap.keySet()) {
                Integer method = Integer.valueOf(methodMap.get(area));
                if (method == 1) {
                    areaDownloadList.append(area);
                    areaDownloadList.append(",");
                } else if (method == 0) {
                    areaVoiceList.append(area);
                    areaVoiceList.append(",");
                }
            }
            if (areaDownloadList.toString().endsWith(",")){
                areaDownloadList.deleteCharAt(areaDownloadList.lastIndexOf(","));
            }
            if (areaVoiceList.toString().endsWith(",")){
                areaVoiceList.deleteCharAt(areaVoiceList.lastIndexOf(","));
            }

            //根据地市更新推送方式
            if (!areaDownloadList.toString().equals("")) {
                paras.put("area", areaDownloadList.toString());
                paras.put("method", 1);
                dixiaoResultMapper.updateTaskMethod(paras);
            }
            if (!areaVoiceList.toString().equals("")) {
                paras.put("area", areaVoiceList.toString());
                paras.put("method", 0);
                dixiaoResultMapper.updateTaskMethod(paras);
            }
        }

        //删除分配
        if (!"".equals(deletedstr)) {
            dixiaoResultMapper.updateToUnallocated(paras);
        }

        return result;
    }

    /**
     * 低消任务线上分配
     *
     * @param paras
     * @return
     */
    public ServiceResult allocateDixiaoOnline(Map<String, Object> paras) {
        if (paras.get("taskid") == null) {
            return new ServiceResult(-1, "系统异常，请联系系统管理员!");
        }

        paras.put("isonline", 1);
        String addedstr = paras.get("addedlist") == null ? "" : (String) (paras.get("addedlist"));
        String deletedstr = paras.get("deletedlist") == null ? "" : (String) (paras.get("deletedlist"));

        //如果没有分配，直接返回
        if ("".equals(addedstr) && "".equals(deletedstr)) {
            return new ServiceResult(-1, "没有分配，请确认!");
        }

        //新增分配
        if (!"".equals(addedstr)) {
            //线上分配必须带团队编码
            if (paras.get("partnercode") == null || paras.get("partnercode").equals("")) {
                return new ServiceResult(-1, "参数不对，请联系系统管理员!");
            } else {
                if (dixiaoResultMapper.queryOnlineFtpTotal((Integer) paras.get("taskid"), addedstr)>0){
                    return new ServiceResult(-1, "分配的档位中存在已经推送成功的，请重新分配!");
                }
                dixiaoResultMapper.updateToAllocated(paras);
            }
        }

        //删除分配
        if (!"".equals(deletedstr)) {
            if (dixiaoResultMapper.queryOnlineFtpTotal((Integer) paras.get("taskid"), deletedstr)>0){
                return new ServiceResult(-1, "取消分配的档位中存在已经推送成功的，请重新分配!");
            }
            dixiaoResultMapper.updateToUnallocated(paras);
        }

        return new ServiceResult();
    }

    /**
     * 任务更新
     *
     * @param paras
     * @return
     */
    public ServiceResult modifyTaskStatus(Map<String, Object> paras) {
        ServiceResult result = new ServiceResult();

        if (paras.get("taskid") == null) {
            result.setRetValue(-1);
            result.setDesc("参数异常，请联系系统管理员!");
            return result;
        }

        dixiaoResultMapper.modifyTaskStatus(paras);
        return result;
    }

    /**
     * 合作伙伴分配
     *
     * @param paras
     * @return
     */
    public ServiceResult allocatePartner(Map<String, Object> paras) {
        ServiceResult result = new ServiceResult();

        if (paras.get("deletedlist") != null && !paras.get("deletedlist").equals("")) {
            DixiaoResultDomain domain = dixiaoResultMapper.queryResultByPartner(paras);
            if (domain != null ){
                result.setRetValue(-1);
                result.setDesc("待取消的团队中已经选中了档位，对应团队编码为 "+domain.getPartnercode());
                return result;
            }
            dixiaoResultMapper.deletePartner(paras);
        }

        if (paras.get("addedlist") != null && !paras.get("addedlist").equals("")) {
            List<DixiaoCodeDomain> dataList = new ArrayList<DixiaoCodeDomain>();
            String[] partnerCode = ((String) (paras.get("addedlist"))).split(",");
            for (String str : partnerCode) {
                DixiaoCodeDomain dixiaoCode = new DixiaoCodeDomain();
                dixiaoCode.setCode(str);
                dataList.add(dixiaoCode);
            }
            dixiaoResultMapper.insertPartnerAllocate(dataList);
        }
        return result;
    }

    /**
     * 更新ftp风雷标志位
     *
     * @param paras
     * @return
     */
    public ServiceResult ModifyNotifyFtp(Map<String, Object> paras) {
        ServiceResult result = new ServiceResult();

        if (paras.get("taskid") == null) {
            result.setRetValue(-1);
            result.setDesc("系统参数不对，请联系系统管理员!");
            return result;
        }

        paras.put("notify_ftp", 1);
        dixiaoResultMapper.modifyNotifyFtp(paras);
        return result;
    }

    public List<DixiaoCodeDomain> queryBusinessCodeByPage(Map<String, Object> paras) {
        final List<DixiaoCodeDomain> list = new ArrayList<>();

        String querySql = getBusinessSql(paras, 0);
        greenPlumOperateService.query(querySql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                DixiaoCodeDomain domain = new DixiaoCodeDomain();
                domain.setCode(resultSet.getString("business_hall_code"));
                domain.setName(resultSet.getString("business_name"));
                domain.setCreatetime(resultSet.getDate("createtime"));
                list.add(domain);
            }
        }, 0);
        return list;
    }

    public int queryBusinessCodeTotal(Map<String, Object> paras) {
        String querySql = getBusinessSql(paras, 1);
        return greenPlumOperateService.queryRecordCount(querySql);
    }

    public String getBusinessSql(Map<String, Object> paras, int type) {
        StringBuffer querySql = new StringBuffer();
        boolean where = false;
        if (type == 0) {
            querySql.append("select business_hall_code,business_name,createtime from model.business_code_from_voiceplus ");
        } else {
            querySql.append("select count(*) from model.business_code_from_voiceplus ");
        }
        if (paras.get("code") != null && !"".equals(paras.get("code"))) {
            querySql.append("where business_hall_code='").append(paras.get("code")).append("' ");
            where = true;
        }
        if (paras.get("name") != null && !"".equals(paras.get("name"))) {
            if (!where) {
                querySql.append("where ");
            } else {
                querySql.append("and ");
            }
            querySql.append(" business_name like '%").append(paras.get("name")).append("%'  ");
        }

        if (type == 0) {
            querySql.append("order by business_name ");
            if (paras.get("length") != null && paras.get("start") != null) {
                querySql.append("limit ").append(paras.get("length")).append(" offset ").append(paras.get("start"));
            }
        }

        return querySql.toString();
    }

    public int queryDixiaoListTotal(Map<String, Object> paras) {
        String querySql = totalSql(paras);
        return greenPlumOperateService.queryRecordCount(querySql);
    }

    public List<DixiaoListDomain> queryDixiaoListByPage(Map<String, Object> paras) {
        final List<DixiaoListDomain> list = new ArrayList<DixiaoListDomain>();
        String detailsql = detailSql(paras);

        greenPlumOperateService.query(detailsql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                DixiaoListDomain listDomain = new DixiaoListDomain();
                listDomain.setSaleid(resultSet.getString("sale_id"));
                listDomain.setBoid(resultSet.getString("sale_boid_id"));
                listDomain.setAimsubid(resultSet.getString("aim_sub_id"));
                listDomain.setUserid(resultSet.getString("user_id"));
                listDomain.setCustid(resultSet.getString("cust_id"));
                listDomain.setPhone(axonEncrypt.decryptWithoutCountrycode(resultSet.getString("phone")));
                listDomain.setName(resultSet.getString("name"));
                listDomain.setSex(resultSet.getString("sex"));
                listDomain.setArea(resultSet.getString("area_code"));
                listDomain.setNetType(resultSet.getString("sys_name"));
                listDomain.setMainProductName(resultSet.getString("main_prod_name"));
                listDomain.setMainProductCode(resultSet.getString("main_prod_code"));
                listDomain.setNettime(resultSet.getString("net_time"));
                listDomain.setProductId(resultSet.getString("pin_prod_id"));
                listDomain.setChargeId(resultSet.getString("pin_charge_id"));
                listDomain.setRankID(resultSet.getString("pin_level"));
                listDomain.setFeeLast1(resultSet.getString("lll_arpu"));
                listDomain.setFeeLast2(resultSet.getString("ll_arpu"));
                listDomain.setFeeLast3(resultSet.getString("l_arpu"));
                listDomain.setFeeAvg(resultSet.getString("a_arpu"));
                listDomain.setGprsLast3(resultSet.getString("a_volume"));
                listDomain.setCallLast3(resultSet.getString("a_voice"));
                listDomain.setIsfeiman(resultSet.getString("is_roam"));
                listDomain.setTerminal(resultSet.getString("terminal"));
                listDomain.setRankType(resultSet.getString("level_type"));
                list.add(listDomain);
            }
        }, 0);

        return list;
    }

    public String totalSql(Map<String, Object> paras) {
        StringBuffer querySql = new StringBuffer();
        querySql.append("select count(*) from model.dixiao_list ");
        querySql.append("where 1=1 ");

        if (paras.get("id") != null && !"".equals(paras.get("id"))) {
            querySql.append("and mapid = ").append(paras.get("id")).append(" ");
        }
        if (paras.get("rankid") != null && !"".equals(paras.get("rankid"))) {
            querySql.append("and pin_level = '").append(paras.get("rankid")).append("' ");
        }
        if (paras.get("phone") != null && !"".equals(paras.get("phone"))) {
            querySql.append("and phone = '").append(paras.get("phone")).append("' ");
        }
        if (paras.get("area") != null && !"".equals(paras.get("area")) && !"99999".equals(paras.get("area"))) {
            querySql.append("and area_code = '").append(paras.get("area")).append("' ");
        }
        return querySql.toString();
    }

    public String detailSql(Map<String, Object> paras) {
        StringBuffer querySql = new StringBuffer();
        querySql.append("select sale_id, sale_boid_id, aim_sub_id, user_id, cust_id, phone, name,sex, area_code, sys_name, main_prod_name, main_prod_code, net_time, pin_prod_id, pin_charge_id, pin_level, lll_arpu, ll_arpu, l_arpu, a_arpu, a_volume, a_voice, is_roam, terminal, level_type from model.dixiao_list ");
        querySql.append("where 1=1 ");

        if (paras.get("id") != null && !"".equals(paras.get("id"))) {
            querySql.append("and mapid = ").append(paras.get("id")).append(" ");
        }
        if (paras.get("rankid") != null && !"".equals(paras.get("rankid"))) {
            querySql.append("and pin_level = '").append(paras.get("rankid")).append("' ");
        }
        if (paras.get("phone") != null && !"".equals(paras.get("phone"))) {
            querySql.append("and phone = '").append(paras.get("phone")).append("' ");
        }
        if (paras.get("area") != null && !"".equals(paras.get("area")) && !"99999".equals(paras.get("area"))) {
            querySql.append("and area_code = '").append(paras.get("area")).append("' ");
        }
        querySql.append("limit ").append(paras.get("length")).append(" offset ").append(paras.get("start"));

        return querySql.toString();
    }


    /**
     * 低消任务配置 导出excel
     *
     * @param paras
     * @param request
     * @param response
     */
    public void exportDixiao(Map<String, Object> paras, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        parasMap.put("taskid", paras.get("taskid") == null ? "" : paras.get("taskid"));
        parasMap.put("rankid", paras.get("rankid") == null ? "" : paras.get("rankid"));
        parasMap.put("area", paras.get("area") == null ? "" : paras.get("area"));
        parasMap.put("mapid", paras.get("id") == null ? "" : paras.get("id"));
        parasMap.put("ranktype", paras.get("ranktype") == null ? "" : paras.get("ranktype"));
        parasMap.put("sysname", paras.get("sysname") == null ? "" : paras.get("sysname"));


        //拼接sql
        String downloadsql = downloadSql(parasMap);
        List<DixiaoListDomain> fileList = new ArrayList<DixiaoListDomain>();
        greenPlumOperateService.query(downloadsql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                DixiaoListDomain listDomain = new DixiaoListDomain();
                listDomain.setSaleid(resultSet.getString("sale_id"));
                listDomain.setBoid(resultSet.getString("sale_boid_id"));
                listDomain.setAimsubid(resultSet.getString("aim_sub_id"));
                listDomain.setUserid(resultSet.getString("user_id"));
                listDomain.setCustid(resultSet.getString("cust_id"));
                listDomain.setPhone(axonEncrypt.decryptWithoutCountrycode(resultSet.getString("phone")));
                listDomain.setName(resultSet.getString("name"));
                listDomain.setSex(resultSet.getString("sex"));
                listDomain.setArea(resultSet.getString("area_code"));
                listDomain.setNetType(resultSet.getString("sys_name"));
                listDomain.setMainProductName(resultSet.getString("main_prod_name"));
                listDomain.setMainProductCode(resultSet.getString("main_prod_code"));
                listDomain.setNettime(resultSet.getString("net_time"));
                listDomain.setProductId(resultSet.getString("pin_prod_id"));
                listDomain.setChargeId(resultSet.getString("pin_charge_id"));
                listDomain.setRankID(resultSet.getString("pin_level"));
                listDomain.setFeeLast1(resultSet.getString("lll_arpu"));
                listDomain.setFeeLast2(resultSet.getString("ll_arpu"));
                listDomain.setFeeLast3(resultSet.getString("l_arpu"));
                listDomain.setFeeAvg(resultSet.getString("a_arpu"));
                listDomain.setGprsLast3(resultSet.getString("a_volume"));
                listDomain.setCallLast3(resultSet.getString("a_voice"));
                listDomain.setIsfeiman(resultSet.getString("is_roam"));
                listDomain.setTerminal(resultSet.getString("terminal"));
                listDomain.setRankType(resultSet.getString("level_type"));
                fileList.add(listDomain);
            }
        }, 0);

        String ranktypename = parasMap.get("ranktype").equals("0") ? "流量" : "语音";
        //parasMap.get("rankid") + "-" +
        String fileName = "低销用户分配" + "-" + parasMap.get("sysname") + "-" + areaMap.get("" + parasMap.get("area")) + "-" + ranktypename + "-" + TimeUtil.formatDateToYMDHMS(new Date());

        try{
            ExportConfig exportConfig = ExportConfigFactory.getExportConfig(DixiaoService.class.getClassLoader().getResourceAsStream("dixiaotemplate.xml"));
            ExportResult exportResult = FileExportor.getExportResult(exportConfig, fileList);
            response.setContentType("application/vnd.ms-excel");
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            OutputStream outputStream = response.getOutputStream();
            exportResult.export(outputStream);
        }catch(FileExportException e){
            LOG.error("export dixiao excel file failed!");
        }catch(FileNotFoundException e){
            LOG.error("export dixiao excel file failed!");
        }catch(IOException e){
            LOG.error("export dixiao excel file failed!");
        }
    }
/*    public void exportDixiao(Map<String, Object> paras, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        parasMap.put("taskid", paras.get("taskid") == null ? "" : paras.get("taskid"));
        parasMap.put("rankid", paras.get("rankid") == null ? "" : paras.get("rankid"));
        parasMap.put("area", paras.get("area") == null ? "" : paras.get("area"));
        parasMap.put("mapid", paras.get("id") == null ? "" : paras.get("id"));
        parasMap.put("ranktype", paras.get("ranktype") == null ? "" : paras.get("ranktype"));
        parasMap.put("sysname", paras.get("sysname") == null ? "" : paras.get("sysname"));


        //拼接sql
        String downloadsql = downloadSql(parasMap);
        List<DixiaoListDomain> fileList = new ArrayList<DixiaoListDomain>();
        greenPlumOperateService.query(downloadsql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                DixiaoListDomain listDomain = new DixiaoListDomain();
                listDomain.setSaleid(resultSet.getString("sale_id"));
                listDomain.setBoid(resultSet.getString("sale_boid_id"));
                listDomain.setAimsubid(resultSet.getString("aim_sub_id"));
                listDomain.setUserid(resultSet.getString("user_id"));
                listDomain.setCustid(resultSet.getString("cust_id"));
                listDomain.setPhone(axonEncrypt.decryptWithoutCountrycode(resultSet.getString("phone")));
                listDomain.setName(resultSet.getString("name"));
                listDomain.setSex(resultSet.getString("sex"));
                listDomain.setArea(resultSet.getString("area_code"));
                listDomain.setNetType(resultSet.getString("sys_name"));
                listDomain.setMainProductName(resultSet.getString("main_prod_name"));
                listDomain.setMainProductCode(resultSet.getString("main_prod_code"));
                listDomain.setNettime(resultSet.getString("net_time"));
                listDomain.setProductId(resultSet.getString("pin_prod_id"));
                listDomain.setChargeId(resultSet.getString("pin_charge_id"));
                listDomain.setRankID(resultSet.getString("pin_level"));
                listDomain.setFeeLast1(resultSet.getString("lll_arpu"));
                listDomain.setFeeLast2(resultSet.getString("ll_arpu"));
                listDomain.setFeeLast3(resultSet.getString("l_arpu"));
                listDomain.setFeeAvg(resultSet.getString("a_arpu"));
                listDomain.setGprsLast3(resultSet.getString("a_volume"));
                listDomain.setCallLast3(resultSet.getString("a_voice"));
                listDomain.setIsfeiman(resultSet.getString("is_roam"));
                listDomain.setTerminal(resultSet.getString("terminal"));
                listDomain.setRankType(resultSet.getString("level_type"));
                fileList.add(listDomain);
            }
        }, 0);

        String ranktypename = parasMap.get("ranktype").equals("0") ? "流量" : "语音";
        String fileName = "低销用户分配" + "-" + parasMap.get("sysname") + "-" + areaMap.get("" + parasMap.get("area")) + "-" + ranktypename + "-" + parasMap.get("rankid") + "-" + TimeUtil.formatDateToYMDHMS(new Date());
        List<ExcelRowEntity> excelDataList = getdixiaoExcelData(fileList);
        ExportUtils.getInstance().exportData(fileName, excelDataList, request, response);

    }*/

    public String downloadSql(Map<String, Object> parasMap) {
        StringBuffer sql = new StringBuffer();
        sql.append("select sale_id, sale_boid_id, aim_sub_id, user_id, cust_id, phone, name,sex, area_code, sys_name, main_prod_name, main_prod_code, net_time, pin_prod_id, pin_charge_id, pin_level, lll_arpu, ll_arpu, l_arpu, a_arpu, a_volume, a_voice, is_roam, terminal,level_type from model.dixiao_list ");
        sql.append("where 1=1 ");
        if (!"".equals(String.valueOf(parasMap.get("taskid")))) {
            sql.append("and taskid = ").append(String.valueOf(parasMap.get("taskid"))).append(" ");
        }
/*        if (!"".equals(String.valueOf(parasMap.get("mapid")))) {
            sql.append("and mapid = ").append(String.valueOf(parasMap.get("mapid"))).append(" ");
        }*/
        if (!"".equals(String.valueOf(parasMap.get("mapid")))) {
            sql.append("and mapid in (").append(parasMap.get("mapid")).append(") ");
        }

        //C侧是CBSS，B侧带过来的是BSS或者OCS
        if (!"".equals(String.valueOf(parasMap.get("sysname")))) {
            if ("BSS".equals(String.valueOf(parasMap.get("sysname")))) {
                sql.append("and (sys_name = '").append(String.valueOf(parasMap.get("sysname"))).append("' or sys_name = 'OCS') ");
            } else {
                sql.append("and sys_name = '").append(String.valueOf(parasMap.get("sysname"))).append("' ");
            }
        }

        return sql.toString();
    }

    private List<ExcelRowEntity> getdixiaoExcelData(List<DixiaoListDomain> dataList) {
        List<ExcelRowEntity> result = new ArrayList<ExcelRowEntity>();

        // 表头处理-----------------------------------------------
        ExcelRowEntity header = new ExcelRowEntity();
        result.add(header);
        header.setRowType(2);
        List<ExcelCellEntity> cellEntityList1 = new ArrayList<ExcelCellEntity>();
        header.setCellEntityList(cellEntityList1);
        cellEntityList1.add(new ExcelCellEntity(1, 1, "活动编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "波次编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "目标客户群编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "用户编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "客户编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "手机号"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "姓名"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "性别"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "区号"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "系统名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "主产品名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "主产品代码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "入网时间"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "匹配档位的产品ID"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "资费id"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "档位金额"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "上月出账金额"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "上上月出账金额"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "上上上月出账金额"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "近三个月平均消费"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "近三个月消耗总流量"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "近三个月消耗语音"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "是否长市漫"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "终端类型"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "档位类型"));

        // ----------------数据行--------------------------------------------
        if (null != dataList && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                DixiaoListDomain listDomain = dataList.get(i);
                ExcelRowEntity excelRow = new ExcelRowEntity();
                List<ExcelCellEntity> cellEntityList = new ArrayList<ExcelCellEntity>();

                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getSaleid()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getBoid()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getAimsubid()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getUserid()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getCustid()));
                cellEntityList.add(new ExcelCellEntity(1, 1, axonEncrypt.decryptWithoutCountrycode(listDomain.getPhone())));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getName()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getSex()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getArea()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getNetType()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getMainProductName()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getMainProductCode()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getNettime()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getProductId()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getChargeId()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getRankID()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getFeeLast1()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getFeeLast2()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getFeeLast3()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getFeeAvg()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getGprsLast3()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getCallLast3()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getIsfeiman()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getTerminal()));
                cellEntityList.add(new ExcelCellEntity(1, 1, listDomain.getRankType()));

                excelRow.setCellEntityList(cellEntityList);
                excelRow.setRowType(-1);
                result.add(excelRow);
            }
        }
        return result;
    }

    /**
     * 更新档位类型
     * * @param paras
     * *
     *
     * @return
     */
    public ServiceResult updateDixiaoRankType(Map<String, Object> paras) {
        ServiceResult result = new ServiceResult();
        if (paras.get("taskid") == null || paras.get("ranktype") == null) {
            result.setRetValue(-1);
            result.setDesc("系统参数错误，请联系系统管理员!");
            return result;
        }

        long taskid = (Integer) paras.get("taskid");
        String ranktype = (String) paras.get("ranktype");
        dixiaoResultMapper.updateTaskRankType(taskid, ranktype);
        dixiaoResultMapper.updateDixiaoRankType(taskid, ranktype);
        return result;
    }

    /**
     * 获得低消统计信息
     * * @param paras
     * *
     *
     * @return
     */
    public Map<String, Object> queryDixiaoStatistic(Map<String, Object> paras) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> map = null;
        if (paras.get("taskid") == null) {
            return result;
        }
        long taskid = (Integer) paras.get("taskid");

        //线下分配用户
        param.clear();
        param.put("taskid", taskid);
        param.put("isonline", 0);
        param.put("status", 1);
        map = dixiaoResultMapper.querySumResult(param);
        if (map != null && map.get("totalnum") != null) {
            result.put("totalAllocateOffline", map.get("totalnum"));
        }else{
            result.put("totalAllocateOffline", 0);
        }

        //线上待分配用户数
        param.clear();
        param.put("taskid", taskid);
        param.put("isonline", 1);
        param.put("status", 0);
        map = dixiaoResultMapper.querySumResult(param);
        if (map != null && map.get("totalnum") != null) {
            result.put("totalUnallocateOnline", map.get("totalnum"));
        }else{
            result.put("totalUnallocateOnline", 0);
        }

        //线上分配用户数
        param.clear();
        param.put("taskid", taskid);
        param.put("isonline", 1);
        param.put("status", 1);
        map = dixiaoResultMapper.querySumResult(param);
        if (map != null && map.get("totalnum") != null) {
            result.put("totalAllocateOnline", map.get("totalnum"));
        }else{
            result.put("totalAllocateOnline", 0);
        }

        //总用户数
        param.clear();
        param.put("taskid", taskid);
        map = dixiaoResultMapper.querySumResult(param);
        if (map != null && map.get("totalnum") != null) {
            result.put("total", map.get("totalnum"));
        }else{
            result.put("total", 0);
        }

        //具体团队分配个数
        List<Map<String, Object>> list = dixiaoResultMapper.queryPartnerSumResult(taskid);
        result.put("partnerAllocate", list);
        return result;
    }


    /**
     * 获得线上团队统计信息
     * * @param paras
     * *
     *
     * @return
     */
    public List<Map<String, Object>> queryDixiaoParnterStatistic(Map<String, Object> paras) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<String, Object> param = new HashMap<String, Object>();
        if (paras.get("taskid") == null) {
            return result;
        }
        long taskid = Integer.valueOf((String) paras.get("taskid"));//(Integer)paras.get("taskid");
        param.put("taskid", taskid);

/*        if (paras.get("start")!=null && !"".equals(paras.get("start"))){
            param.put("offset",Integer.valueOf((String)paras.get("start")));
        }
        if (paras.get("length")!=null && !"".equals(paras.get("length"))){
            param.put("limit",Integer.valueOf((String)paras.get("length")));
        }*/

        List<Map<String, Object>> list = dixiaoResultMapper.queryPartnerRankIDSumResult(param);
        if (list == null || list.isEmpty()) {
            return result;
        }

        Map<String, Object> compareMap = new HashMap<String, Object>();
        Map<String, Object> saveMap = new HashMap<>();
        StringBuffer ranklist = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            if (compareMap.containsKey((String)map.get("partnercode")+map.get("updatetime"))) {
                //ranklist.append(",").append(map.get("rankid"));
                if (saveMap.get("area").equals(map.get("area"))){
                    ranklist.append("," + map.get("rankid"));
                }else{
                    ranklist.append("," + areaMap.get(map.get("area")) + ":" + map.get("rankid"));
                }
                saveMap.put("ranklist", ranklist.toString());
                //saveMap.put("totalnum", ((BigDecimal) saveMap.get("totalnum")).add((BigDecimal) map.get("totalnum")));
                long total = (long)saveMap.get("totalnum") + (long)map.get("totalnum");
                saveMap.put("totalnum", total);
                saveMap.put("area",map.get("area"));
            } else {
                saveMap = new HashMap<>();
                ranklist = new StringBuffer();
                //ranklist.append(map.get("rankid"));
                //compareMap.put((String) map.get("partnercode"), "");
                compareMap.put((String)map.get("partnercode")+map.get("updatetime"), "");
                saveMap.put("partnercode", map.get("partnercode"));
                saveMap.put("partnername", map.get("partnername"));
                saveMap.put("updatetime", map.get("updatetime"));
                //saveMap.put("totalnum", (BigDecimal) map.get("totalnum"));
                saveMap.put("totalnum", map.get("totalnum"));
                ranklist.append(areaMap.get(map.get("area"))+":"+map.get("rankid"));
                saveMap.put("ranklist", ranklist.toString());
                saveMap.put("area", map.get("area"));

                result.add(saveMap);
            }
        }
        return result;
    }

    /**
     * 获得线上团队统计信息的记录个数
     * * @param paras
     * *
     *
     * @return
     */
    public Integer queryDixiaoParnterStatisticTotal(Map<String, Object> paras) {
        Map<String, Object> param = new HashMap<String, Object>();
        long taskid = Integer.valueOf((String) paras.get("taskid"));//(Integer)paras.get("taskid");
        int count = 0;
        param.put("taskid", taskid);
        List<Map<String, Object>> list = dixiaoResultMapper.queryPartnerRankIDSumResult(param);
        if (list == null || list.isEmpty()) {
            count = 0;
        }else{
            count = list.size();
            list.clear();
        }
        return count;
    }

    /**
     * 获得线上团队统计信息的记录个数
     * * @param paras
     * *
     *
     * @return
     */
    public DixiaoTaskDomain queryDixiaoTaskById(Map<String, Object> paras) {
        long taskid = (Integer)paras.get("taskid");
        return dixiaoResultMapper.queryOneDixiaoTask(taskid);
    }
}