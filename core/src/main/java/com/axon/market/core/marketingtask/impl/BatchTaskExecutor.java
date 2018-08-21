package com.axon.market.core.marketingtask.impl;

import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.cache.IRedisAction;
import com.axon.market.common.cache.RedisCache;
import com.axon.market.common.domain.icommon.market.JumpLinkDomain;
import com.axon.market.common.domain.icommon.market.PTaskDomain;
import com.axon.market.common.domain.icommon.market.PdrDomain;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.domain.iresource.SmsContentDomain;
import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.common.util.*;
import com.axon.market.core.marketingtask.MarketTaskExecutor;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.SmsMarketService;
import com.axon.market.core.service.imodel.ModelService;
import com.axon.market.core.service.iresource.SmsContentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 短信push批量任务执行器
 * Created by zengcr on 2017/6/13.
 */
@Service("batchTaskExecutor")
public class BatchTaskExecutor implements MarketTaskExecutor
{
    private static final Logger LOG = Logger.getLogger(BatchTaskExecutor.class.getName());

    @Autowired
    @Qualifier("modelService")
    private ModelService modelService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Autowired
    @Qualifier("smsMarketService")
    private SmsMarketService smsMarketService;

    @Autowired
    @Qualifier("smsContentService")
    private SmsContentService smsContentService;

    @Autowired
    @Qualifier("redisCache")
    private RedisCache redisCache;

    private HttpUtil httpUtil = HttpUtil.getInstance();

    private SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();

    @Override
    public String execute(MarketingPoolTaskDomain poolTaskDomain)
    {
        Date date = new Date();
        LOG.info("Sms Schedule : " + poolTaskDomain.getId() + " " + date.toString());

        Integer count = 0, contentId = poolTaskDomain.getMarketContentId();
        int repeatStrategy = poolTaskDomain.getRepeatStrategy();
        Boolean isMessageContainLink = false;

        if (null != contentId)
        {
            SmsContentDomain smsContentDomain = smsContentService.querySmsContentById(contentId);
            String smsUrl = smsContentDomain.getUrl();
            isMessageContainLink = StringUtils.isNotEmpty(smsUrl);
        }
        else
        {
            contentId = 0;
        }

        String smsMessage = poolTaskDomain.getMarketContent();
        Long sendTime = date.getTime() / 1000;
        Integer marketUserCountLimit = poolTaskDomain.getMarketUserCountLimit() == null ? Integer.MAX_VALUE : poolTaskDomain.getMarketUserCountLimit();
        String accessNumber = poolTaskDomain.getAccessNumber();
        // 生成pTask实体
        PTaskDomain pTaskDomain = generatePTaskDomain(poolTaskDomain.getName(), contentId, sendTime, poolTaskDomain.getCreateUserName(), accessNumber, count, smsMessage);
        Integer createResult = smsMarketService.createPTask(pTaskDomain);
        if (createResult != null)
        {
           /* String testPhones = marketJobDomain.getTestPhones();
            if (StringUtils.isNotEmpty(testPhones))
            {
                String[] testPhonesList = testPhones.split(",");
                List<PdrDomain> pdrDomains = generatePdrDomains(testPhonesList, smsUrl, smsMessage, pTaskDomain.getId(), sendTime, isMessageContainLink);
                count += smsMarketService.createPdr(pdrDomains);
            }*/

            if ("1".equals(poolTaskDomain.getIsBoidSale()))
            {
                String tableName = "task.task_" + poolTaskDomain.getId();
                //判断分组任务表是否已经创建,如果没有创建则通知后续操作不做处理
                if (greenPlumOperateService.isExistsTable(tableName))
                {
                    return "WAIT";
                }
               // List<String> conditions = createConditions(poolTaskDomain.getAreaCodes(), tableName);
                count += insertSmsContents(createSelectDataFromTaskSql(tableName, smsMessage, pTaskDomain.getId(), sendTime, null), 10000, marketUserCountLimit, repeatStrategy, isMessageContainLink);
            }
            else
            {
                String segmentIds = poolTaskDomain.getMarketSegmentIds();
                if (StringUtils.isNotEmpty(segmentIds))
                {
                    String[] segmentIdList = segmentIds.split(",");
                    for (String segmentId : segmentIdList)
                    {
                        ModelDomain modelDomain = modelService.queryModelById(Integer.parseInt(segmentId));
                        String tableName = greenPlumOperateService.getModelDataTableName(modelDomain);
                        List<String> conditions = createConditions(poolTaskDomain.getAreaCodes(), tableName);
                        count += insertSmsContents(createSelectDataFromSegmentSql(tableName, smsMessage, pTaskDomain.getId(), sendTime, conditions), 10000, marketUserCountLimit, repeatStrategy, isMessageContainLink);
                    }
                }
            }

            smsMarketService.updatePTask(pTaskDomain.getId(), count);
            notifySmsTaskServerService(String.valueOf(pTaskDomain.getId()), accessNumber);
            return count + "-" + pTaskDomain.getId();
        }
        return null;
    }

    /**
     * @param marketAreaIds
     * @param tableName
     * @return
     */
    private List<String> createConditions(String marketAreaIds, String tableName)
    {
        List<String> result = new ArrayList<String>();
        if (StringUtils.isNotEmpty(marketAreaIds))
        {
            String[] areas = marketAreaIds.split(",");
            for (String area : areas)
            {
                //String value = marketingTaskService.queryMarketAreaValuesById(Integer.parseInt(area));
                if (area.contains("-"))
                {
                    StringBuffer sqlCondition = new StringBuffer();
                    String[] condition = area.split("-");
                    sqlCondition.append("(" + tableName + ".area_id=" + condition[0] + " and " + tableName + ".user_type=" + condition[1] + ")");
                    result.add(sqlCondition.toString());
                }
            }
        }
        return result;
    }

    private String createSelectDataFromTaskSql(String tableName, String smsMessage, int taskId, long sendTime, List<String> conditions)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select ").append(tableName).append(".phone,");
        sql.append("'' as sms_url,");
        sql.append("'").append(smsMessage).append("'").append(" as sms_message,");
        sql.append(taskId).append(" as task_id,");
        sql.append(sendTime).append(" as send_time,");
        sql.append("0 as status from ");
        sql.append(tableName);
        // sql.append(" left join ").append(greenPlumServerBean.getGpCurrentSchemaName()).append(".market_black_phone on ")
        //        .append(greenPlumServerBean.getGpCurrentSchemaName()).append(".market_black_phone.phone = ").append(tableName).append(".phone");
        sql.append("where 1 = 1 ");
        if (CollectionUtils.isNotEmpty(conditions))
        {
            sql.append("and " +StringUtils.join(conditions, " or "));
            // sql.append(" and ");
        }
        sql.append("and ").append(tableName).append(".send_date = ").append(TimeUtil.formatDateToYMD(new Date()));
        // sql.append(greenPlumServerBean.getGpCurrentSchemaName()).append(".market_black_phone.phone is null");
        return sql.toString();
    }

    /**
     * @param tableName
     * @param smsMessage
     * @param taskId
     * @param sendTime
     * @param conditions
     * @return
     */
    private String createSelectDataFromSegmentSql(String tableName, String smsMessage, int taskId, long sendTime, List<String> conditions)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("select ").append(tableName).append(".phone,");
        sql.append("'' as sms_url,");
        sql.append("'").append(smsMessage).append("'").append(" as sms_message,");
        sql.append(taskId).append(" as task_id,");
        sql.append(sendTime).append(" as send_time,");
        sql.append("0 as status from ");
        sql.append(tableName);
        // sql.append(" left join ").append(greenPlumServerBean.getGpCurrentSchemaName()).append(".market_black_phone on ")
        //        .append(greenPlumServerBean.getGpCurrentSchemaName()).append(".market_black_phone.phone = ").append(tableName).append(".phone");
        if (CollectionUtils.isNotEmpty(conditions))
        {
            sql.append(" where ");
            sql.append(StringUtils.join(conditions, " or "));
            // sql.append(" and ");
        }
        // sql.append(greenPlumServerBean.getGpCurrentSchemaName()).append(".market_black_phone.phone is null");
        return sql.toString();
    }

    /**
     * @param taskName
     * @param sendTime
     * @param accessNumber
     * @param count
     * @param marketContent
     * @return
     */
    private PTaskDomain generatePTaskDomain(String taskName, Integer contentId, long sendTime,String createUserName,  String accessNumber, int count, String marketContent)
    {
        PTaskDomain pTaskDomain = new PTaskDomain();
        pTaskDomain.setTaskTitle(taskName);
        pTaskDomain.setIfExecute(0);
        pTaskDomain.setcId(contentId);
        pTaskDomain.setExeTime(sendTime);
        pTaskDomain.setSpNum(Long.parseLong(accessNumber));
        pTaskDomain.setPushType(1);
        pTaskDomain.setMarketSubmitter(createUserName); //任务池任务的执行者就是任务配置的创建者
        pTaskDomain.setMarketUserTargetNumbers(count);
        pTaskDomain.setMarketContent(marketContent);
        return pTaskDomain;
    }

    /**
     * @param sql
     * @param fetchSize
     * @return
     */
    private int insertSmsContents(String sql, final int fetchSize, final int marketUserCountLimit, int repeatStrategy, final boolean isMessageContainLink)
    {

        final Integer count[] = {new Integer(0)};
        final List<PdrDomain> pdrDomains = new ArrayList<PdrDomain>();
        final List<Map<String, String>> requestList = new ArrayList<Map<String, String>>();
        final List<JumpLinkDomain> jumpLinkList = new ArrayList<JumpLinkDomain>();

        greenPlumOperateService.query(sql, new RowCallbackHandler() {
            Map<String, String> map = new HashMap<String, String>();

            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                if (count[0] >= marketUserCountLimit) {
                    return;
                }
                map.clear();
                PdrDomain pdrDomain = new PdrDomain();

                String phone = resultSet.getString("phone");
                String smsMessage = resultSet.getString("sms_message");
                String smsUrl = resultSet.getString("sms_url");
                Integer taskId = resultSet.getInt("task_id");
                Long sendTime = resultSet.getLong("send_time");

                pdrDomain.setMob(Long.parseLong(axonEncrypt.encrypt(phone)));
                // 兼容老版本，URL转短链放在message中，url字段存空
                pdrDomain.setSmsUrl("");
                pdrDomain.setTaskId(taskId);
                pdrDomain.setSendTime(sendTime);

                if (isMessageContainLink) {
                    map.clear();
                    String shortLink = DisposeLinkUtil.getShortLink();
                    map.put("Short", shortLink);
                    map.put("Long", smsUrl);
                    map.put("phone", axonEncrypt.decrypt(phone));
                    map.put("province", systemConfigBean.getProvince());
                    map.put("taskid", String.valueOf(taskId));
                    requestList.add(map);

                    // 构成短链存储数据库bean
                    JumpLinkDomain jumpLinkDomain = new JumpLinkDomain();
                    jumpLinkDomain.setuId(shortLink);
                    jumpLinkDomain.setLongLink(smsUrl);
                    jumpLinkDomain.setMob(Long.parseLong(axonEncrypt.encrypt(phone)));
                    jumpLinkDomain.setProvince(systemConfigBean.getProvince());
                    jumpLinkDomain.setTaskId(taskId);
                    jumpLinkList.add(jumpLinkDomain);

                    pdrDomain.setSmsContent(MessageFormat.format(smsMessage, systemConfigBean.getShortLinkPrefix() + shortLink));
                } else {
                    pdrDomain.setSmsContent(smsMessage);
                }

                pdrDomains.add(pdrDomain);

                // 不足一次扫描量的这里不处理，后面方法处理
                if (pdrDomains.size() >= fetchSize) {
                    count[0] = insertContentToPdr(pdrDomains, marketUserCountLimit, count[0], repeatStrategy, requestList, jumpLinkList);
                }
            }
        }, fetchSize);

        //处理1w的零头
        count[0] = insertContentToPdr(pdrDomains,marketUserCountLimit,count[0],repeatStrategy,requestList, jumpLinkList);

        return count[0];
    }

    /**
     * 需要发送的短信内容入库
     * @param pdrDomains
     * @param marketUserCountLimit
     * @param count
     * @return
     */
    private int insertContentToPdr(List<PdrDomain> pdrDomains, int marketUserCountLimit, int count,int repeatStrategy, List<Map<String, String>> requestList, List<JumpLinkDomain> jumpLinkList)
    {
        eliminateDuplication(pdrDomains, repeatStrategy);

        if (pdrDomains.size() > 0)
        {
            if (count + pdrDomains.size() > marketUserCountLimit)
            {
                int more = count + pdrDomains.size() - marketUserCountLimit;
                count += smsMarketService.createPdr(pdrDomains.subList(0, pdrDomains.size() - more));
                jumpLinksOperate(requestList, jumpLinkList);
            }
            else
            {
                count += smsMarketService.createPdr(pdrDomains);
                jumpLinksOperate(requestList, jumpLinkList);
            }

            pdrDomains.clear();
            requestList.clear();
            jumpLinkList.clear();
        }
        return count;
    }

    /**
     * 存储长链、短链信息
     * @param requestList
     * @param jumpLinkList
     */
    private void jumpLinksOperate(List<Map<String, String>> requestList, List<JumpLinkDomain> jumpLinkList)
    {
        try
        {
            if (CollectionUtils.isNotEmpty(jumpLinkList))
            {
                Map<String, String> param = new HashMap<String, String>();
                String jumpLinks = "{\"links\":" + JsonUtil.objectToString(requestList) + "}";
                param.put("passWord", "axon@2016!");
                param.put("jumpLinks", jumpLinks);
                httpUtil.sendHttpPost(systemConfigBean.getJumpLinkUrl(), param);

                smsMarketService.createJumpLink(jumpLinkList);
            }
        }
        catch (JsonProcessingException e)
        {
            LOG.error("", e);
        }
    }

    /**
     * 剔重
     *
     * @param pdrDomains
     * @return
     */
    private void eliminateDuplication(final List<PdrDomain> pdrDomains, final int repeatStrategy)
    {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date now = new Date();
        List<PdrDomain> needRemovePdrDomains = new ArrayList<PdrDomain>();

        redisCache.doAction(new IRedisAction()
        {
            @Override
            public boolean action(Jedis jedis)
            {
                for (int i = pdrDomains.size() - 1; i >= 0; i--)
                {
                    PdrDomain pdrDomain = pdrDomains.get(i);
                    String phone = String.valueOf(pdrDomain.getMob());
                    if (!jedis.exists(phone))
                    {
                        jedis.set(phone, simpleDateFormat.format(now));
                    }
                    else
                    {
                        try
                        {
                            String lastDate = jedis.get(phone);
                            if (calculateDate(simpleDateFormat.parse(lastDate), now) < repeatStrategy)
                            {
                                needRemovePdrDomains.add(pdrDomains.get(i));
                            }
                            else
                            {
                                jedis.set(phone, simpleDateFormat.format(now));
                            }
                        }
                        catch (Exception e)
                        {
                            needRemovePdrDomains.add(pdrDomains.get(i));
                        }
                    }
                }
                return true;
            }

            private int calculateDate(Date lastDate, Date nowDate) throws ParseException
            {
                Calendar lastCalendar = new GregorianCalendar();
                lastCalendar.setTime(lastDate);
                Calendar nowCalendar = new GregorianCalendar();
                nowCalendar.setTime(nowDate);
                return nowCalendar.get(Calendar.DAY_OF_YEAR) - lastCalendar.get(Calendar.DAY_OF_YEAR);
            }
        });
        // 移除重复的或者没达到踢重策略允许期的号码
        pdrDomains.removeAll(needRemovePdrDomains);
    }

    /**
     * @param taskId
     * @param accessNumber
     */
    private void notifySmsTaskServerService(String taskId, String accessNumber)
    {
        try
        {
            Map<String, String> request = new HashMap<String, String>();
            request.put("taskid", taskId);
            request.put("spnum", accessNumber);
            request.put("action", "3");
            String result = httpUtil.sendHttpPost(smsConfigBean.getTaskUrl(), JsonUtil.objectToString(request));
            LOG.info("Notify Sms Task result : " + result);
        }
        catch (JsonProcessingException e)
        {
            LOG.error("Notify Sms Task Server Service Error. ", e);
        }
    }
}
