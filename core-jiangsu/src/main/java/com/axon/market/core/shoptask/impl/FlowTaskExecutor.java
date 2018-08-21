package com.axon.market.core.shoptask.impl;

import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.domain.isystem.PositionDataSynDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.MarketTimeUtils;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.iscene.PositionSynDataService;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.core.shoptask.TaskExecutor;
import com.axon.market.core.shoptask.cache.TaskIdCache;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 针对流动拜访用户营销
 * Created by zengcr on 2017/2/20.
 */
@Component("flowTaskExecutor")
public class FlowTaskExecutor implements TaskExecutor
{
    private static final Logger LOG = Logger.getLogger(FlowTaskExecutor.class.getName());

    @Autowired
    @Qualifier("positionSynDataService")
    private PositionSynDataService positionSynDataService;

    @Qualifier("shopTaskService")
    @Autowired
    private ShopTaskService shopTaskService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    private static Map<String,String> filterSegment = new HashMap<String,String>()
    {
        {
            put("25", "2");
            put("510", "3");
            put("511", "4");
            put("512", "5");
            put("513", "6");
            put("514", "7");
            put("515", "8");
            put("516", "9");
            put("517", "10");
            put("518", "11");
            put("519", "12");
            put("523", "13");
            put("527", "14");
            put("99999", "15");
        }
    };

    @Override
    public String execute(ShopTaskDomain domain)
    {
        //构造对象
        PositionDataSynDomain positionDataSynDomain =  objectExchange(domain);
        List<Map<String,Object>> pTaskIdList = shopTaskService.getPTaskIdByTaskIdAndBaseId(domain.getId(),domain.getBaseId());
        //修改
        if(pTaskIdList != null && pTaskIdList.size() > 0){
            String pTaskId = ""+pTaskIdList.get(0).get("lastTaskId");
            if(pTaskId != null && !"".equals(pTaskId)){
                positionDataSynDomain.setMessageType(3);
                positionDataSynDomain.setTaskId(Long.parseLong("" + pTaskId));
                positionSynDataService.updatePTask(positionDataSynDomain);
                return 0 + "-" + positionDataSynDomain.getTaskId();
            }
        }
        //新增
        //生成监视用户黑名单客户群用户查询SQL
        StringBuffer blackSql = new StringBuffer();
        String dateStr = MarketTimeUtils.formatDateToYMDHMS(new Date());
        String blackSegmentIds = domain.getBlackUsers();
        if(StringUtils.isNotEmpty(blackSegmentIds)){
            String blackTableName = "pdc_temp.heimingdan_"+ dateStr + TaskIdCache.getTaskId();
            //将号码插入目标库
            insertPhoneContents(blackSegmentIds,domain,blackTableName);
            positionDataSynDomain.setUserFilterTypeBlack(1);
            positionDataSynDomain.setUserFilterListBlack(blackTableName.split("\\.")[1]);
        }
         positionSynDataService.insertPTask(positionDataSynDomain);
        return 0 + "-" + positionDataSynDomain.getTaskId();
    }

    /**
     * 对象转换装载
     * @param shopTaskDomain
     * @return
     */
    private PositionDataSynDomain objectExchange(ShopTaskDomain shopTaskDomain){
        PositionDataSynDomain positionDataSynDomain = new PositionDataSynDomain();
        positionDataSynDomain.setTaskName(shopTaskDomain.getTaskName() + ":" + shopTaskDomain.getBaseName());
        positionDataSynDomain.setTaskStartTm(TimeUtil.formatDateToYMDDevide(new Date()) + " 00:00:00");
        positionDataSynDomain.setTaskEndTm(TimeUtil.formatDateToYMDDevide(new Date()) + " 23:59:58");
        positionDataSynDomain.setMonitorStartTm(getTimeSeconds(shopTaskDomain.getBeginTime()));
        positionDataSynDomain.setMonitorEndTm(getTimeSeconds(shopTaskDomain.getEndTime()));
        positionDataSynDomain.setMonitoredBsId(shopTaskDomain.getBaseId());
        String smsUrl = shopTaskDomain.getMarketUrl();
        String smsMessage = shopTaskDomain.getMarketContentText();
        if (StringUtils.isNotEmpty(smsUrl))
        {
            smsMessage += smsUrl;
        }
        //短信签名
        if(StringUtils.isNotEmpty(shopTaskDomain.getMessageAutograph()))
        {
            smsMessage += shopTaskDomain.getMessageAutograph();
        }
        positionDataSynDomain.setMarketContent(smsMessage);
        positionDataSynDomain.setMarketUrl(shopTaskDomain.getMarketUrl());
        positionDataSynDomain.setMarketInterval(shopTaskDomain.getSendInterval() * 24 * 3600);
        positionDataSynDomain.setAreano(shopTaskDomain.getBaseAreaId());
        positionDataSynDomain.setSpNum(shopTaskDomain.getAccessNumber());
//        positionDataSynDomain.setUserFilterBlackSegment(positionSynDataService.getConfSegmentId(positionSceneDomain.getBlackAreas()));
//        positionDataSynDomain.setUserFilterWhiteSegment(positionSynDataService.getConfSegmentId(positionSceneDomain.getWhiteAreas()));
        positionDataSynDomain.setCounty(shopTaskDomain.getMonitorArea());
        positionDataSynDomain.setUsergroupId(shopTaskDomain.getSceneType());
        positionDataSynDomain.setUsergroupName(shopTaskDomain.getSceneTypeName());
        positionDataSynDomain.setTriggerLimit((shopTaskDomain.getMarketLimit() == null || shopTaskDomain.getMarketLimit() > 5000) ? 5000 : shopTaskDomain.getMarketLimit());
        positionDataSynDomain.setWeights(shopTaskDomain.getTaskWeight());
        positionDataSynDomain.setChannelId(shopTaskDomain.getChannelId());
        positionDataSynDomain.setLocationTypeId(shopTaskDomain.getBaseAreaType());
//        positionDataSynDomain.setUserFilterWhiteSegment(filterSegment.get(String.valueOf(shopTaskDomain.getBaseAreaId())));
        positionDataSynDomain.setTriggerChannelId(shopTaskDomain.getTriggerChannelId());
        positionDataSynDomain.setValidityDateTriggerType(shopTaskDomain.getMonitorInterval());
//        positionDataSynDomain.setSmsReportStatus(shopTaskDomain.getIsSendReport());
        positionDataSynDomain.setSmsReportStatus(0);
//        positionDataSynDomain.setSmsReportPhone(shopTaskDomain.getReportPhone() == null ? 0:shopTaskDomain.getReportPhone());
        positionDataSynDomain.setSmsReportPhone(0L);
        positionDataSynDomain.setUserFilterTypeWhite(0);
        positionDataSynDomain.setManruRange(shopTaskDomain.getManruRange());
        return positionDataSynDomain;
    }

    /**
     * 将时间转换为毫秒，
     * @param time 时间，格式为09:00
     * @return
     */
    private int getTimeSeconds(String time){
        String[] args = time.split(":");
        int result =  Integer.parseInt(args[0])*3600+Integer.parseInt(args[1])*60;
        return result;
    }

    private int insertPhoneContents(String blackSegmentIds,ShopTaskDomain domain , String INSERT_TABLE_NAME)
    {
        //创建目标表
        positionSynDataService.createPhoneTable(INSERT_TABLE_NAME);
        List<String> lines = new ArrayList<String>();
        //同步号码 1、将常驻用户作为黑名单  //TODO
//        Integer baseAreaId = domain.getBaseAreaId(), baseId = domain.getBaseId();
//        if (baseAreaId != null && baseId != null)
//        {
//            String fullTableName = "model.model_" + baseAreaId;
//            lines =  getPhoneList(createSelectDataFromModelSql(fullTableName,baseId));
//        }

        //同步号码 2、同步导入的黑名单
        List<Map<String,Object>> phoneList = shopTaskService.queryBlackPhoneList(Long.parseLong(blackSegmentIds));
        for(Map<String,Object> map: phoneList){
            String phone = String.valueOf(map.get("phone"));
            lines.add(AxonEncryptUtil.getInstance().encrypt(phone));
        }
        positionSynDataService.syncPhone(INSERT_TABLE_NAME, lines);
        return phoneList.size();
    }

    /**
     * @param tableName
     * @param baseId
     * @return
     */
    private String createSelectDataFromModelSql(String tableName, int baseId)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select ").append(tableName).append(".phone,");
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" where ");
        sql.append(tableName).append(".base_id = '").append(baseId).append("'");
        return sql.toString();
    }

    private List<String> getPhoneList(String sql)
    {
        final Integer count[] = {new Integer(0)};
        final List<String> phoneList = new ArrayList<String>();
        greenPlumOperateService.query(sql, new RowCallbackHandler()
        {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException
            {
                String phone = resultSet.getString("phone");
                phoneList.add(AxonEncryptUtil.getInstance().decrypt(phone));
            }
        }, 10000);
        return phoneList;
    }
}
