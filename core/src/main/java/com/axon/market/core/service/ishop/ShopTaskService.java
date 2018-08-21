package com.axon.market.core.service.ishop;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.constant.ishop.ShopTaskClassifyEnum;
import com.axon.market.common.domain.iresource.SmsContentDomain;
import com.axon.market.common.domain.iscene.PositionBaseDomain;
import com.axon.market.common.domain.ischeduling.MarketJobDomain;
import com.axon.market.common.domain.ishop.ShopTaskAuditHistoryDomain;
import com.axon.market.common.domain.ishop.ShopTaskChannelDomain;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.*;
import com.axon.market.common.util.excel.ExcelReader;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.core.service.iscene.PositionSynDataService;
import com.axon.market.core.service.isystem.UserService;
import com.axon.market.dao.mapper.iresource.ISmsContentMapper;
import com.axon.market.dao.mapper.ishop.IShopTaskMapper;
import com.axon.market.dao.scheduling.IPositionSynDataMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 炒店任务service
 * Created by zengcr on 2017/2/14.
 */
@Component("shopTaskService")
public class ShopTaskService
{
    private static final Logger LOG = Logger.getLogger(ShopTaskService.class.getName());

    @Qualifier("shopTaskDao")
    @Autowired
    private IShopTaskMapper iShopTaskMapper;

    @Autowired
    @Qualifier("smsContentDao")
    private ISmsContentMapper smsContentDao;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Autowired
    @Qualifier("positionSynDataService")
    private PositionSynDataService positionSynDataService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    @Autowired
    @Qualifier("positionSynDataDao")
    private IPositionSynDataMapper iPositionSynDataMapper;

    public static ShopTaskService getInstance()
    {
        return (ShopTaskService) SpringUtil.getSingletonBean("shopTaskService");
    }

    /**
     * 获取炒店任务总数
     * shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态
     *
     * @return 总数
     */
    public int queryShopTaskTotal(Map<String, Object> paras)
    {
        return iShopTaskMapper.queryShopTaskTotal(paras);
    }

    /**
     * 分页查询炒店任务
     * offset 起始标记位,limit 限制大小,shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态
     *
     * @return
     */
    public List<ShopTaskDomain> queryShopTaskByPage(Map<String, Object> paras)
    {
        return iShopTaskMapper.queryShopTaskByPage(paras);
    }

    /**
     * 根据个性化任务名称，波次编码，客户群编码，营业厅类型查询任务是否存在
     *
     * @param name
     * @param boidId
     * @param segment
     * @param departTypeCode
     * @return
     */
    public ShopTaskDomain queryRecommendationShopTaskByName(String name, String boidId, String segment, String departTypeCode)
    {
        return iShopTaskMapper.queryRecommendationShopTaskByName(name, boidId, segment, departTypeCode);
    }

    /**
     * 根据活动编码和波次编码查询相同的活动不同的波次的炒店任务
     *
     * @param saleId
     * @param boidId
     * @return
     */
    public List<ShopTaskDomain> queryShopTaskBySaleIdAndBoid(String saleId, String boidId)
    {
        return iShopTaskMapper.queryShopTaskBySaleIdAndBoid(saleId, boidId);
    }


    /**
     * 查询待执行的任务总数,非营业厅管理员查询
     * shopTaskId,shopTaskName
     *
     * @return
     */
    public int queryShopTaskExecuteAll(Map<String, Object> parasMap)
    {
        return iShopTaskMapper.queryShopTaskExecuteAll(parasMap);
    }

    /**
     * 分页查询待执行的任务数，非营业厅管理员查询
     * offset 起始标记位,limit 限制大小,shopTaskId 炒店任务ID,shopTaskName 炒店任务名称
     *
     * @return
     */
    public List<ShopTaskDomain> queryShopTaskExecute(Map<String, Object> parasMap)
    {

        return iShopTaskMapper.queryShopTaskExecute(parasMap);
    }

    /**
     * 查询待执行的任务总数，营业厅管理员查询
     * shopTaskId,shopTaskName
     *
     * @return
     */
    public int queryShopTaskExecuteAllForClerk(Map<String, Object> parasMap)
    {
        return iShopTaskMapper.queryShopTaskExecuteAllForClerk(parasMap);
    }

    /**
     * 分页查询待执行的任务数，营业厅管理员查询
     * offset 起始标记位,limit 限制大小,shopTaskId 炒店任务ID,shopTaskName 炒店任务名称
     *
     * @return
     */
    public List<ShopTaskDomain> queryShopTaskExecuteForClerk(Map<String, Object> parasMap)
    {

        return iShopTaskMapper.queryShopTaskExecuteForClerk(parasMap);
    }


    /**
     * 查询营销内容总数
     *
     * @return
     */
    public int querySmsContentsTotal(String searchContent, String mob)
    {
        return smsContentDao.querySmsContentCounts(searchContent, null, mob);
    }

    /**
     * 分页查询营销内容
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<SmsContentDomain> querySmsContentsByPage(String searchContent, String mob, int offset, int limit)
    {
        List<SmsContentDomain> result = smsContentDao.querySmsContentsByPage(searchContent, null, mob, offset, limit);
        for (SmsContentDomain smsContentDomain : result)
        {
            String telePhone = smsContentDomain.getCreateUserTelePhone();
            if (null != telePhone && !"".equals(telePhone))
            {
                if (smsContentDomain.getCreateUserTelePhone().length() >= 11)
                {
                    smsContentDomain.setCreateUserTelePhone(smsContentDomain.getCreateUserTelePhone().substring(0, smsContentDomain.getCreateUserTelePhone().length() - 4) + "****");
                }
            }
        }
        return result;
    }

    /**
     * 插入炒店任务
     *
     * @param taskDomain
     * @return
     */
    @Transactional
    public ServiceResult insertShopTask(ShopTaskDomain taskDomain, UserDomain userDomain) throws Exception
    {
        ServiceResult result = new ServiceResult();
        taskDomain = setDefault(taskDomain);
        //第一步：插入炒店任务
        iShopTaskMapper.insertShopTask(taskDomain);
        //第二步：插入炒店任务对应的炒店
        String baseIds = taskDomain.getBaseIds();
        if (null == baseIds || "".equals(baseIds))
        {
            if (null != taskDomain.getBaseAreaTypes() && !"".equals(taskDomain.getBaseAreaTypes()))
            {
                iShopTaskMapper.insertShopTaskToBaseByArea(taskDomain);
            }
        }
        else
        {
            iShopTaskMapper.insertShopTaskToBaseByIds(taskDomain);
        }
        //第三步：审批通过，插入待执行任务池
        if (ShopTaskStatusEnum.TASK_READY.getValue() == taskDomain.getStatus())
        {
            long currentTime = System.currentTimeMillis();
            long startTime = MarketTimeUtils.formatDate(taskDomain.getStartTime() + " 00:00:00").getTime();
            long endTime = MarketTimeUtils.formatDate(taskDomain.getStopTime() + " 23:59:00").getTime();
            if (currentTime > startTime && currentTime < endTime)
            {
                iShopTaskMapper.insertShopTaskPool(taskDomain);
                //第四步：修改炒店任务的下次营销时间
                Map<String, Object> paras = new HashMap<String, Object>();
                paras.put("shopTaskId", taskDomain.getId());
                iShopTaskMapper.updateShopTaskNextTime(paras);
            }
            //开始时间大于当前时间
            if (currentTime < startTime && currentTime < endTime)
            {
                //修改炒店任务的下次营销时间
                Map<String, Object> paras = new HashMap<String, Object>();
                paras.put("shopTaskId", taskDomain.getId());
                iShopTaskMapper.updateShopTaskNextTimeAsStart(paras);
            }
        }
        else
        {
            //催单
            if (userDomain != null)
            {
                reminderItem(taskDomain.getId(), taskDomain.getTaskName(), userDomain);
            }
        }
        return result;
    }

    /**
     * 根据用户ID校验任务是否重复
     *
     * @param paras
     * @return
     */
    public boolean validateTaskName(Map<String, Object> paras)
    {
        int num = 0;
        try
        {
            num = iShopTaskMapper.queryShopTaskNumByName(paras);
        }
        catch (Exception e)
        {
            num = 0;
            LOG.error("shop task validateTaskName error", e);
        }
        if (num > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 根据主键ID查询炒店任务
     *
     * @param taskId 主键ID
     * @return
     */
    public ShopTaskDomain queryShopTaskById(Integer taskId)
    {
        ShopTaskDomain shopTaskDomain = iShopTaskMapper.queryShopTaskById(taskId);
//        String baseNames = shopTaskDomain.getBaseNames();
//        if (null != baseNames && !"".equals(baseNames))
//        {
//            shopTaskDomain.setBaseNames(baseNames.replaceAll(",", "、"));
//        }
        return shopTaskDomain;
    }

    public String queryShopPhone(Map<String, Object> paras)
    {
        String shopPhone = null;
        try
        {
            shopPhone = iShopTaskMapper.queryShopPhone(paras);
        }
        catch (Exception e)
        {
            shopPhone = null;
            LOG.error("shop task queryShopPhone error", e);
        }
        return shopPhone;
    }

    public String queryShopMsgDesc(Map<String, Object> paras)
    {
        String msgDesc = null;
        try
        {
            msgDesc = iShopTaskMapper.queryShopMsgDesc(paras);
        }
        catch (Exception e)
        {
            msgDesc = null;
            LOG.error("shop task queryShopMsgDesc error", e);
        }
        return msgDesc;
    }

    /**
     * 根据主键ID查询待执行的炒店任务
     *
     * @param taskId 主键ID
     * @return
     */
    public ShopTaskDomain queryShopTaskPoolById(Integer taskId)
    {
        ShopTaskDomain shopTaskDomain = iShopTaskMapper.queryShopTaskPoolById(taskId);
        return shopTaskDomain;
    }

    /**
     * @return
     */
    @Transactional
    public ServiceResult updateShopTask(ShopTaskDomain taskDomain, UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        taskDomain = setDefault(taskDomain);
        try
        {
            iShopTaskMapper.updateShopTask(taskDomain);
            //第二步：插入炒店任务对应的炒店
            iShopTaskMapper.deleteShopTaskToBase(taskDomain);
            String baseIds = taskDomain.getBaseIds();
            if (null == baseIds || "".equals(baseIds))
            {
                iShopTaskMapper.insertShopTaskToBaseByArea(taskDomain);
            }
            else
            {
                iShopTaskMapper.insertShopTaskToBaseByIds(taskDomain);
            }
            //审批通过，插入待执行任务池
            if (ShopTaskStatusEnum.TASK_READY.getValue() == taskDomain.getStatus())
            {
                iShopTaskMapper.updateShopTaskPool(taskDomain);
                //第三步：修改炒店任务的下次营销时间
                Map<String, Object> paras = new HashMap<String, Object>();
                paras.put("shopTaskId", taskDomain.getId());
                iShopTaskMapper.updateShopTaskNextTime(paras);
            }
            else
            {
                //催单
                if (userDomain != null)
                {
                    reminderItem(taskDomain.getId(), taskDomain.getTaskName(), userDomain);
                }
            }
            //第四步，清除审批记录
            iShopTaskMapper.deleteShopTaskAuditHistory(taskDomain);

        }
        catch (Exception e)
        {
            LOG.error("shop task update error", e);
            result.setRetValue(-1);
            result.setDesc("炒店任务修改操作失败");
        }
        return result;
    }

    /**
     * 炒店任务删除
     *
     * @param taskId
     * @return
     */
    public ServiceResult deleteShopTaskById(Integer taskId)
    {
        ServiceResult result = new ServiceResult();
        if (1 != iShopTaskMapper.updateShopTaskById(taskId, ShopTaskStatusEnum.TASK_DELETE.getValue()))
        {
            result.setRetValue(-1);
            result.setDesc("数据库修改操作失败");
        }
        return result;
    }

    /**
     * 查询炒店营销号码
     *
     * @return
     */
    public List<Map<String, String>> querFixedAccessNum(Map<String, Object> parasMap)
    {
        return iShopTaskMapper.querFixedAccessNum(parasMap);
    }

    /**
     * 查询炒店业务类型
     *
     * @return
     */
    public List<Map<String, String>> queryShopBusinessType()
    {
        return iShopTaskMapper.queryShopBusinessType();
    }


    /**
     * 查询需要我审批的营销任务
     *
     * @param paras
     * @return
     */
    public List<Map<String, Object>> queryNeedMeAuditShopTasks(Map<String, String> paras)
    {
        int userId = Integer.parseInt(paras.get("userId"));
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        // 用户记录审核历史中的id
        List<Integer> shopTaskIds = new ArrayList<Integer>();
        //获取炒店任务列表
        List<Map<String, Object>> shopTaskList = iShopTaskMapper.queryAllShopTaskAuditByUser(paras);
        if (CollectionUtils.isEmpty(shopTaskList))
        {
            return result;
        }

        try
        {
            // userAuditMarketJobIndex（key为营销任务id，value为用户第几个审核营销任务）
            Map<Integer, Integer> userAuditShopTaskIndex = calculateUserAuditShopTaskIndex(shopTaskList, shopTaskIds, userId);
            // marketJobAuditInfo 营销任务审核记录
            List<Map<String, Object>> ShopTaskAuditInfo = iShopTaskMapper.queryShopTaskAuditInfo("(" + StringUtils.join(shopTaskIds, ",") + ")");
            // 获取用户要审核的营销任务
            result = calculateUserAuditShopTasks(shopTaskList, ShopTaskAuditInfo, userAuditShopTaskIndex);
            // 根据id排序
            Collections.sort(result, new Comparator<Map<String, Object>>()
            {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2)
                {
                    return (Integer.parseInt(String.valueOf(o2.get("id")))) - Integer.parseInt(String.valueOf(o1.get("id")));
                }
            });
        }
        catch (IOException e)
        {
            LOG.error("Query My Audit Shop Tasks error. ", e);
        }
        return result;
    }

    /**
     * 计算用户是第几个审核炒店任务
     * calculateUserAuditShopTaskIndex（key为炒店任务id，value为用户第几个审核炒店任务）
     *
     * @param shopTaskList
     * @param userId
     * @throws IOException
     */
    private Map<Integer, Integer> calculateUserAuditShopTaskIndex(List<Map<String, Object>> shopTaskList, List<Integer> shopTaskIds, int userId) throws IOException
    {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        // 查询出需要用户审核的营销任务
        Iterator<Map<String, Object>> marketJobIterator = shopTaskList.iterator();
        // 遍历查询出的需要审核的营销任务，把id和审核顺序加入返回map中
        while (marketJobIterator.hasNext())
        {
            Map<String, Object> marketJob = marketJobIterator.next();
            int marketJobId = Integer.parseInt(String.valueOf(marketJob.get("id")));
            String auditUsers = String.valueOf(marketJob.get("marketingAuditUsers"));
            // 获取客户群需要审核的人
            List<Map<String, String>> auditUserList = JsonUtil.stringToObject(auditUsers, new TypeReference<List<Map<String, String>>>()
            {
            });
            Iterator<Map<String, String>> auditUserIterator = auditUserList.iterator();
            while (auditUserIterator.hasNext())
            {
                Map<String, String> auditUser = auditUserIterator.next();
                int auditUserId = Integer.parseInt(auditUser.get("auditUser"));
                if (userId == auditUserId)
                {
                    shopTaskIds.add(marketJobId);
                    // 营销任务id为key，用户审核该营销任务的次序为value
                    result.put(marketJobId, Integer.parseInt(auditUser.get("order")));
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @param marketJobList
     * @param ShopTaskAuditInfo
     * @param userAuditShopTaskIndex
     * @return
     */
    private List<Map<String, Object>> calculateUserAuditShopTasks(List<Map<String, Object>> marketJobList, List<Map<String, Object>> ShopTaskAuditInfo, Map<Integer, Integer> userAuditShopTaskIndex)
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(marketJobList);
        List<Integer> shopTaskIds = new ArrayList<Integer>();
        // 审核历史表中存在的id集合
        List<Integer> shopTaskAuditIds = new ArrayList<Integer>();

        Iterator<Map<String, Object>> shopTaskAuditInfoIterator = ShopTaskAuditInfo.iterator();
        // 遍历获得审核历史表中存在的id集合
        while (shopTaskAuditInfoIterator.hasNext())
        {
            Map<String, Object> shopTaskAuditInfo = shopTaskAuditInfoIterator.next();
            shopTaskAuditIds.add(Integer.parseInt(String.valueOf(shopTaskAuditInfo.get("id"))));
        }

        for (Map.Entry<Integer, Integer> shopTaskEntry : userAuditShopTaskIndex.entrySet())
        {
            int marketJobId = shopTaskEntry.getKey(), order = shopTaskEntry.getValue();
            shopTaskAuditInfoIterator = ShopTaskAuditInfo.iterator();

            Map<String, Object> shopTaskAuditInfo;
            // 判断任务是否是第一次审核
            if (shopTaskAuditIds.contains(marketJobId))
            {
                while (shopTaskAuditInfoIterator.hasNext())
                {
                    shopTaskAuditInfo = shopTaskAuditInfoIterator.next();
                    if (Integer.parseInt(String.valueOf(shopTaskAuditInfo.get("id"))) == marketJobId && (Integer.parseInt(String.valueOf(shopTaskAuditInfo.get("count"))) + 1) == order)
                    {
                        shopTaskIds.add(marketJobId);
                        break;
                    }
                }
            }
            else
            {
                if (order == 1)
                {
                    shopTaskIds.add(marketJobId);
                }
            }
        }

        Iterator<Map<String, Object>> resultIterator = result.iterator();
        // 去除不满足的任务
        while (resultIterator.hasNext())
        {
            Map<String, Object> resultMap = resultIterator.next();
            if (!shopTaskIds.contains(Integer.parseInt(String.valueOf(resultMap.get("id")))))
            {
                resultIterator.remove();
            }
        }

        return result;
    }

    /**
     * 查询炒店任务审核失败的最新一条数据
     *
     * @param paras
     * @return
     */
    public Map<String, Object> queryShopTaskAuditReject(Map<String, Object> paras)
    {
        return iShopTaskMapper.queryShopTaskAuditReject(paras);
    }

    /**
     * 提交炒店营销任务审批结果
     *
     * @param id
     * @param operate
     * @param reason
     * @param userDomain
     * @return
     */
    @Transactional
    public ServiceResult submitShopTaskAudit(int id, String operate, String reason, UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        try
        {
            boolean isApproveAudit = "approve".equals(operate);
            ShopTaskDomain shopTaskDomain = iShopTaskMapper.queryShopTaskById(id);
            ShopTaskAuditHistoryDomain shopTaskAuditHistoryDomain = getShopTaskAuditHistoryDomain(id, userDomain.getId(), operate, reason);
            //审批行为记入历史操作
            int insertSuccess = iShopTaskMapper.insertShopTaskAuditHistory(shopTaskAuditHistoryDomain);
            if (insertSuccess > 0 && !isApproveAudit)
            {
                iShopTaskMapper.updateShopTaskById(id, ShopTaskStatusEnum.TASK_FAIL.getValue());
            }
            else if (insertSuccess > 0 && isApproveAudit)
            {
                Map<String, Object> parasMap = new HashMap<String, Object>();
                parasMap.put("taskId", id);
                parasMap.put("reminder", null);
                List<ShopTaskAuditHistoryDomain> shopTaskAuditHistories = iShopTaskMapper.queryShopTaskAuditHistoryDomain(parasMap);
                String auditUsers = shopTaskAuditHistories.get(0).getAuditUsers();
                List<Map<String, Object>> auditUserList = JsonUtil.stringToObject(auditUsers, new TypeReference<List<Map<String, Object>>>()
                {
                });
                List<Integer> needApproveUsers = queryAllAuditUserId(auditUserList);
                for (ShopTaskAuditHistoryDomain shopTaskAuditHistory : shopTaskAuditHistories)
                {
                    needApproveUsers.remove(shopTaskAuditHistory.getAuditUser());
                }
                if (CollectionUtils.isEmpty(needApproveUsers))
                {
                    iShopTaskMapper.updateShopTaskById(id, ShopTaskStatusEnum.TASK_READY.getValue());
                    //生成代办池任务
                    try
                    {
                        long currentTime = System.currentTimeMillis();
                        long startTime = MarketTimeUtils.formatDate(shopTaskDomain.getStartTime() + " 00:00:00").getTime();
                        long endTime = MarketTimeUtils.formatDate(shopTaskDomain.getStopTime() + " 23:59:00").getTime();

                        if (currentTime > startTime && currentTime < endTime)
                        {
                            iShopTaskMapper.insertShopTaskPool(shopTaskDomain);

/*                            //由于model用户数据每天凌晨6点生成，一般的审批工作在白天，审批完成即使生成任务也无法提醒，因此这部分先暂时注释 add by zhuwen
                            //临时促销任务要往shop_task_execute_history插一条记录
                            if (ShopTaskClassifyEnum.TASK_TEMP_PROMOTION.getValue()==shopTaskDomain.getTaskClassifyId()) {
                                Map<String, String> para = new HashMap<String, String>();
                                para.put("userId", String.valueOf(userDomain.getCreateUserId()));
                                para.put("baseIds", shopTaskDomain.getBaseIds());
                                para.put("id", String.valueOf(shopTaskDomain.getId()));
                                //没有插入数据营销任务会执行不了，必须报错
                                int num = iShopTaskMapper.insertShopTaskForExecute(para);
                                if (num < 1) {
                                    result.setRetValue(-1);
                                    result.setDesc("炒店营销任务审核操作异常");
                                    return result;
                                }
                            }*/
                            //修改炒店任务的下次营销时间
                            Map<String, Object> paras = new HashMap<String, Object>();
                            paras.put("shopTaskId", id);
                            iShopTaskMapper.updateShopTaskNextTime(paras);
                        }
                        //开始时间大于当前时间
                        if (currentTime < startTime && currentTime < endTime)
                        {
                            //修改炒店任务的下次营销时间
                            Map<String, Object> paras = new HashMap<String, Object>();
                            paras.put("shopTaskId", id);
                            iShopTaskMapper.updateShopTaskNextTimeAsStart(paras);
                        }
                    }
                    catch (ParseException e)
                    {
                        LOG.error("shop task audit error", e);
                    }
                }
            }
            else
            {
                result.setRetValue(-1);
                result.setDesc("炒店营销任务审核操作异常");
                return result;
            }

            //给炒店任务创建者发送审批结果短信
            String userPhone = iShopTaskMapper.queryUserPhoneOfCreateShopTask(id);
            if (StringUtils.isNotEmpty(userPhone))
            {
                String message = MessageFormat.format(smsConfigBean.getAuditNoticeSmsContent(), "炒店营销任务[" + shopTaskDomain.getTaskName() + "]", userDomain.getName(), isApproveAudit ? "通过" : "拒绝");
                sendSmsService.sendReminderNoticeSms(userPhone, message);
            }
        }
        catch (Exception e)
        {
            LOG.error("Submit Market Job error. ", e);
            result.setRetValue(-1);
            result.setDesc("炒店营销任务审核操作异常");
        }
        result.setDesc("炒店营销任务审核完成！");
        return result;
    }

    /**
     * 生成炒店任务审批对象
     *
     * @param taskId
     * @param auditUserId
     * @param operate
     * @param reason
     * @return
     */
    private ShopTaskAuditHistoryDomain getShopTaskAuditHistoryDomain(int taskId, int auditUserId, String operate, String reason)
    {
        ShopTaskAuditHistoryDomain domain = new ShopTaskAuditHistoryDomain();
        domain.setTaskId(taskId);
        domain.setAuditUser(auditUserId);
        domain.setAuditResult(operate);
        domain.setRemarks(reason);
        return domain;
    }

    private List<Integer> queryAllAuditUserId(List<Map<String, Object>> auditUserList)
    {
        List<Integer> result = new ArrayList<Integer>();

        for (Map<String, Object> map : auditUserList)
        {
            result.add(Integer.parseInt(String.valueOf(map.get("auditUser"))));
        }
        return result;
    }


    /**
     * 炒店新增修改时默认是配置
     *
     * @param taskDomain
     * @return
     */
    private ShopTaskDomain setDefault(ShopTaskDomain taskDomain)
    {

        taskDomain.setBeginTime("09:00");
        taskDomain.setEndTime("18:00");
        if (null != taskDomain.getMarketUser() && "4".equals(taskDomain.getMarketUser()))
        {
            taskDomain.setSceneType(31);
            taskDomain.setSceneTypeName("精细化平台");
        }
        else
        {
            taskDomain.setSceneType(1);
            taskDomain.setSceneTypeName("互联网部");
        }
        taskDomain.setMonitorInterval(1);
        taskDomain.setChannelId(1);
        taskDomain.setMonitorType(1);
        taskDomain.setTriggerChannelId(0);
        taskDomain.setTaskWeight(99);
        taskDomain.setTaskClassifyId(ShopTaskClassifyEnum.TASK_SHOP.getValue());
        return taskDomain;
    }

    /**
     * 生成执行任务
     *
     * @param paras
     * @return
     */
    public ServiceResult manualShopTask(Map<String, String> paras)
    {
        ServiceResult result = new ServiceResult();
        ShopTaskDomain shopTaskDomain = iShopTaskMapper.queryShopTaskPoolById(Integer.parseInt(paras.get("id")));
        if (6 == shopTaskDomain.getStatus())
        {
            result.setRetValue(-1);
            result.setDesc("当前任务已终止，不能执行");
            return result;
        }
        int num = 0;
        try
        {
            if ("4".equals(paras.get("status")))
            {
                //从暂停恢复
                num = iShopTaskMapper.updateShopTaskExecuteBySystemId(Integer.parseInt(paras.get("id")), Integer.parseInt(paras.get("baseIds")), 30);
            }
            else
            {
                if (iShopTaskMapper.queryShopTaskForExecute(paras) < 1)
                {
                    num = iShopTaskMapper.insertShopTaskForExecute(paras);
                }
            }

        }
        catch (Exception e)
        {
            num = -1;
            LOG.error(e.getMessage());
        }
        if (num < 0)
        {
            result.setRetValue(-1);
            result.setDesc("营销失败，联系管理员！");
        }
        return result;
    }

    /**
     * 任务的暂停操作
     *
     * @param id
     * @return
     */
    public ServiceResult stopShopTask(Integer id, Integer baseId)
    {
        ServiceResult result = new ServiceResult();
//        ShopTaskDomain shopTaskPoolDomain = iShopTaskMapper.queryShopTaskPoolById(id);
        result.setDesc("暂停任务成功");
//        boolean isSendSuccess = true;
//        Integer marketUserType = shopTaskPoolDomain.getMarketUser();

//        try
//        {
//            if (marketUserType == 3)
//            {
//                //常驻+流动用户的单子暂停 TODO
//                result = positionSynDataService.deleteShopTask(shopTaskPoolDomain);
//            }
//            else if (marketUserType == 1)
//            {
//                //常驻单子暂停 TODO
//
//            }
//            else if (marketUserType == 2)
//            {
//                //流动用户单子暂停
//                result = positionSynDataService.deleteShopTask(shopTaskPoolDomain);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            isSendSuccess = false;
//            result.setRetValue(-1);
//            result.setDesc("炒店营销任务暂停操作失败");
//        }

        if (true)
        {
//            iShopTaskMapper.updateShopTaskPoolBySystemId(id, ShopTaskStatusEnum.TASK_PAUSE.getValue());
            iShopTaskMapper.updateShopTaskExecuteBySystemId(id, baseId, 4);
        }
        return result;
    }

    /**
     * 催单炒店任务
     *
     * @param id
     * @return
     */
    public ServiceResult reminderItem(Integer id, String taskName, UserDomain loginDomain)
    {
        ServiceResult serviceResult = new ServiceResult();
        try
        {
            Map<String, Object> paras = new HashMap<String, Object>();
            paras.put("taskId", id);
            paras.put("reminder", true);
            List<ShopTaskAuditHistoryDomain> shopTaskAuditHistories = iShopTaskMapper.queryShopTaskAuditHistoryDomain(paras);
            String auditUsers = loginDomain.getMarketingAuditUsers();
            List<Map<String, Object>> auditUserList = JsonUtil.stringToObject(auditUsers, new TypeReference<List<Map<String, Object>>>()
            {
            });
            List<Integer> needApproveUsers = queryAllAuditUserId(auditUserList);
            for (ShopTaskAuditHistoryDomain shopTaskAuditHistory : shopTaskAuditHistories)
            {
                needApproveUsers.remove(shopTaskAuditHistory.getAuditUser());
            }
            //如果贷审批人为空
            if (CollectionUtils.isEmpty(needApproveUsers))
            {
                serviceResult.setRetValue(-1);
                serviceResult.setDesc("该任务的审批人为空，请找系统管理员核实！");
            }
            else
            {
                Integer userId = needApproveUsers.get(0);
                UserDomain userDomain = userService.queryUserById(userId);
                String telephone = axonEncrypt.decrypt(userDomain.getTelephone());
                if (StringUtils.isNotEmpty(telephone))
                {
                    String message = MessageFormat.format(smsConfigBean.getReminderNoticeSmsContent(), userDomain.getName(), "炒店营销任务[" + taskName + "]");

                    sendSmsService.sendReminderNoticeSms(telephone, message);
                }
            }
        }
        catch (Exception e)
        {
            serviceResult.setRetValue(-1);
            serviceResult.setDesc("炒店任务催单处理失败");
            LOG.error("shop task炒店催单功能处理失败", e);
        }
        return serviceResult;
    }

    /**
     * 终止任务
     *
     * @param id
     * @return
     */
    @Transactional
    public ServiceResult pauseItem(Integer id, String taskName, UserDomain loginDomain)
    {
        ServiceResult serviceResult = new ServiceResult();
        try
        {
            //1从次日起不再生成待办任务
            iShopTaskMapper.updateShopTaskById(id, ShopTaskStatusEnum.TASK_STOP.getValue());
            // 终止现在正在执行的任务
            iShopTaskMapper.updateShopTaskPoolBySystemId(id, ShopTaskStatusEnum.TASK_STOP.getValue());
            iShopTaskMapper.updateShopTaskExecuteBySystemId(id, null, ShopTaskStatusEnum.TASK_STOP.getValue());
            List<Map<String, Object>> pTaskIdList = iShopTaskMapper.getPTaskIdByTaskId(id);
            if (pTaskIdList != null && pTaskIdList.size() > 0)
            {
                positionSynDataService.deleteShopTask(pTaskIdList);
            }
        }
        catch (Exception e)
        {
            serviceResult.setRetValue(-1);
            serviceResult.setDesc("终止任务处理失败");
            LOG.error("shop task终止任务功能处理失败", e);
        }
        return serviceResult;
    }

    /**
     * 文件保存
     *
     * @param fileInfo 源文件
     * @param is       输入流
     * @param type     导入用户文件类别
     * @return
     */
    @Transactional
    public ServiceResult storeFile(Map<String, Object> fileInfo, InputStream is, String type)
    {
        ServiceResult result = new ServiceResult();
        Boolean isExceed = false;
        Boolean isExceedShopUserBlackList = false;
        List<String[]> fileData = null;
        String fileId = "" + fileInfo.get("fileId");

        int rowNo = 0;
        try
        {
            fileData = readXlsFile(is, 0);
            if (fileData.size() == 0)
            {
                return new ServiceResult(-1, "数据行数为0");
            }
            else
            {
                for (String[] row : fileData)
                {
                    if (!row[0].startsWith("1") && !row[0].startsWith("86"))
                    {
                        continue;
                    }

                    if (type == "appointUser" && rowNo >= 500)
                    {
                        isExceed = true;
                        break;
                    }
                    else if (type == "shopUserBlackList" && rowNo >= 5000)
                    {
                        isExceedShopUserBlackList = true;
                        break;
                    }

                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    rowMap.put("fileId", fileId);
                    rowMap.put("rowNo", rowNo++);
                    if (type != "shopUserBlackList")
                    {
                        String encryptPhone = axonEncrypt.encrypt(row[0]);
                        rowMap.put("rowData", encryptPhone);
                    }
                    else
                    {
                        rowMap.put("rowData", row[0]);
                    }
                    rowMap.put("status", "success");
                    rowMap.put("result", "导入完成");
                    if (row.length == 1)
                    {
                        //校验号码
                        if (row[0] == null || "".equals(row[0]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "号码不能为空");
                        }
                        else
                        {
                            if (!isPhoneFormat(row[0]))
                            {
                                rowMap.put("status", "error");
                                rowMap.put("result", "号码格式不规范");
                            }
                        }
                    }
                    else
                    {
                        rowMap.put("status", "error");
                        rowMap.put("result", "不符合模板规定列数");
                    }
                    iShopTaskMapper.insertRow(rowMap);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOG.error("shop task storeFile", e);
            fileInfo.put("status", "error");
            fileInfo.put("result", "存储失败");
            return new ServiceResult(-1, "存储失败");
        }
        fileInfo.put("status", "YBC");
        fileInfo.put("result", "已存储");
        iShopTaskMapper.insertFile(fileInfo);

        if (isExceed)
        {
            return new ServiceResult(0, "存储文件超过500条 只存储500条, 共导入用户:" + (rowNo));
        }
        else if (isExceedShopUserBlackList)
        {
            return new ServiceResult(0, "存储文件超过5000条 只存储5000条, 共导入用户:" + (rowNo));
        }

//        if (rowNo > 1) {
//            rowNo--;
//        }

        return new ServiceResult(0, "存储文件成功, 总号码数:" + rowNo);
    }

    /**
     * 私有方法
     * 读取EXCEL数据
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param is
     * @param startRow
     * @return
     * @throws Exception
     */
    private List<String[]> readXlsFile(InputStream is, int startRow) throws Exception
    {
        List<String[]> list = new ArrayList<String[]>();
        ExcelReader excel = new ExcelReader(is);
        int rowNum = excel.getRowNums() - startRow;
        int count = 0;
        for (int i = startRow; count < rowNum; i++)
        {
            String[] row = excel.readRow(i);
            if (row != null)
            {
                list.add(row);
                count++;
            }
        }
        return list;
    }

    private static boolean isPhoneFormat(String param)
    {
        boolean isPhone = false;
        Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9]\\d{9}$");
        Matcher m = p.matcher(param);
        boolean b = m.matches();
        if (b)
        {
            isPhone = true;
        }
        return isPhone;
    }


    /**
     * 查询炒店任务号码导入数据总数
     *
     * @param fileId 文件ID
     * @return
     */
    public int queryShopTaskPhoneImportTotal(Long fileId)
    {
        return iShopTaskMapper.queryShopTaskPhoneImportTotal(fileId);
    }

    /**
     * 查询炒店任务号码导入数据
     *
     * @param offset 每次查询数量
     * @param limit  起始标记位
     * @param fileId 文件ＩＤ
     * @return
     */
    public List<Map<String, Object>> queryShopTaskPhoneImport(int offset, int limit, Long fileId)
    {
        List<Map<String, Object>> list = iShopTaskMapper.queryShopTaskPhoneImport(offset, limit, fileId);
        for (Map<String, Object> item : list)
        {
            //解密手机号
            String phone = axonEncrypt.decrypt(String.valueOf(item.get("data")));
            if (phone.startsWith("86"))
            {
                phone = phone.substring(2, phone.length());
            }
            item.put("data", phone);
        }
        return list;
    }

    /**
     * 根据文件ID查询文件内容
     *
     * @param fileId
     * @return
     */
    public List<Map<String, Object>> queryShopTaskPhoneByFileId(String fileId)
    {
        List<Map<String, Object>> list = iShopTaskMapper.queryShopTaskPhoneByFileId(fileId);
        for (Map<String, Object> item : list)
        {
            //解密手机号
            String phone = axonEncrypt.decrypt(String.valueOf(item.get("phone")));
            if (phone.startsWith("86"))
            {
                phone = phone.substring(2, phone.length());
            }
            item.put("phone", phone);
        }

        return list;
    }

    /**
     * 临时保存导入指定用户数据
     *
     * @return
     */
    public Map<String, Object> saveAppointUsersImport(Map<String, Object> paras)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Long fileId = (Long) paras.get("fileId");
        int num = iShopTaskMapper.saveAppointUsersImport(fileId);
        if (num < 1)
        {
            result.put("retValue", -1);
            result.put("desc", "保存导入指定用户号码文件异常");
        }
        result.put("retValue", 0);
        result.put("desc", "OK");
        result.put("num", num);
        return result;
    }

    /**
     * 临时保存导入免打扰用户数据
     *
     * @return
     */
    public Map<String, Object> saveBlackUsersImport(Map<String, Object> paras)
    {
        Long fileId = (Long) paras.get("fileId");
        Map<String, Object> result = new HashMap<String, Object>();
        int num = iShopTaskMapper.saveBlackUsersImport(fileId);
        if (num < 1)
        {
            result.put("retValue", -1);
            result.put("desc", "保存导入免打扰用户号码文件异常");
        }
        result.put("retValue", 0);
        result.put("desc", "OK");
        result.put("num", num);
        return result;
    }

    public List<Map<String, Object>> queryHistoryFileById(Map<String, Object> paras)
    {
        return iShopTaskMapper.queryHistoryFileById(paras);
    }

    /**
     * 查询指定用户清单
     *
     * @param fileId
     * @return
     */
    public List<Map<String, Object>> queryAppointPhoneList(Long fileId)
    {
        List<Map<String, Object>> list = iShopTaskMapper.queryAppointPhoneList(fileId);
        return list;
    }

    /**
     * 查询指定用户清单
     *
     * @param fileId
     * @return
     */
    public List<String> queryAppointPhoneListByFileId(Long fileId)
    {
        return iShopTaskMapper.queryAppointPhoneListByFileId(fileId);
    }

    /**
     * 查询免打扰用户清单
     *
     * @param fileId
     * @return
     */
    public List<Map<String, Object>> queryBlackPhoneList(Long fileId)
    {
        List<Map<String, Object>> list = iShopTaskMapper.queryBlackPhoneList(fileId);
        return list;
    }

    public int queryShopPerNum(Map<String, String> paras)
    {
        String tableSql = " model.model_" + paras.get("baseAreaId") + " where base_id = " + paras.get("baseId");
        return greenPlumOperateService.queryTableRecordCount(tableSql);
    }


    /**
     * 根据任务ID查询要执行的门店
     *
     * @param taskId
     * @param userId
     * @return
     */
    public List<Map<String, Object>> getExecuteBaseByTaskId(String taskId, Integer userId)
    {
        List<Map<String, Object>> list = iShopTaskMapper.getExecuteBaseByTaskId(taskId, userId);
        return list;
    }

    /**
     * 自动生成每天的炒店待办任务
     */
    @Transactional
    public void generateShopTaskPool()
    {
        try
        {
            LOG.info("shop task syncTaskPoolEveryDay begin........");
            iShopTaskMapper.generateShopTaskPool();
            iShopTaskMapper.generateShopTaskExecuteHis();
            //修改炒店任务的下次营销时间
            Map<String, Object> paras = new HashMap<String, Object>();
            iShopTaskMapper.updateShopTaskNextTimeSysTem(paras);
            LOG.info("shop task syncTaskPoolEveryDay end........");
        }
        catch (Exception e)
        {
            LOG.error("shop task syncTaskPoolEveryDay error........", e);
        }
    }

    /**
     * 同步炒店配置数据
     */
    public void syncShopInfo()
    {
        //查询当天修改的生效炒店配置数据
        List<PositionBaseDomain> shopList = null;
        try
        {
            LOG.info("sync shop begin...................");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String dateTime = TimeUtil.formatDateToYMDDevide(calendar.getTime()) + " 18:00:00";
            shopList = iShopTaskMapper.queryUpdateShops(dateTime);
            if (shopList.size() == 0)
            {
                LOG.info("sync shop end 一共同步门店数：" + shopList.size());
                return;
            }
            StringBuffer buffer = new StringBuffer();
            for (PositionBaseDomain positionBaseDomain : shopList)
            {
                buffer.append(positionBaseDomain.getBaseId()).append(",");
            }
            String baseIds = buffer.substring(0, buffer.length() - 1);
            iPositionSynDataMapper.deleteConfBaseInfoById(baseIds);
            iPositionSynDataMapper.syncConfBaseInfo(shopList);
            LOG.info("sync shop end 一共同步门店数：" + shopList.size());
        }
        catch (Exception e)
        {
            LOG.error("sync shop error", e);
        }
    }

    /**
     * 任务池里待执行的任务
     *
     * @param date
     * @return
     */
    public List<ShopTaskDomain> queryAllWaitingExecuteShopTask(String date)
    {
        return iShopTaskMapper.queryAllWaitingExecuteShopTask(date);
    }

    /**
     * 每天午忙和晚忙需要执行的任务
     *
     * @param date
     * @return
     */
    public List<ShopTaskDomain> queryExtralExecuteShopTask(String date)
    {
        return iShopTaskMapper.queryExtralExecuteShopTask(date);
    }

    public int updateShopTaskStatus(int taskId, int baseId, int status)
    {
        return iShopTaskMapper.updateShopTaskStatus(taskId, baseId, status);
    }

    public int updateShopTaskExecuteById(Map<String, Object> paras)
    {
        return iShopTaskMapper.updateShopTaskExecuteById(paras);
    }

    public int updateShopTaskExecuteBySystemId(Integer taskId, Integer baseId, Integer status)
    {
        return iShopTaskMapper.updateShopTaskExecuteBySystemId(taskId, baseId, status);
    }

    public List<Map<String, Object>> getPTaskIdByTaskIdAndBaseId(Integer taskId, Integer baseId)
    {
        return iShopTaskMapper.getPTaskIdByTaskIdAndBaseId(taskId, baseId);
    }

    public int updateRecommendationTaskStatus(String time)
    {
        return iShopTaskMapper.updateRecommendationTaskStatus(time);
    }

    /**
     * 根据主键获取炒店已经执行的常驻用户类型任务数
     *
     * @param domain
     * @return
     */
    public int getPerTaskExecuteNumById(ShopTaskDomain domain)
    {
        int num = 0;
        try
        {
            num = iShopTaskMapper.getPerTaskExecuteNumById(domain);
        }
        catch (Exception e)
        {
            num = 0;
            LOG.error("shop task getPerTaskExecuteNumById", e);
        }
        return num;
    }

    /**
     * 新注册的炒店添加历史生效任务
     *
     * @param baseId
     * @return
     */
    public ServiceResult shopAddEffectiveTask(Integer baseId, Integer areaCode)
    {
        ServiceResult result = new ServiceResult();
        LOG.info("shop task shopAddEffectiveTask" + "; baseId=" + baseId + "; areaCode=" + areaCode);
        try
        {
            iShopTaskMapper.addShopAddEffectiveTask(baseId, areaCode);
        }
        catch (Exception e)
        {
            result.setRetValue(-1);
            result.setDesc("新注册的炒店添加实时任务失败！");
            LOG.error("shop task shopAddEffectiveTask 新注册的炒店添加实时任务失败", e);
        }

        return result;
    }

    public List<Map<String, Object>> queryConfSegment()
    {
        return iShopTaskMapper.queryConfSegment();
    }

    /**
     * 导出炒店任务
     *
     * @param paras(shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态)
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> exportShopTask(Map<String, Object> paras)
    {
        return iShopTaskMapper.exportShopTask(paras);
    }

    /**
     * 导出炒店任务审批
     *
     * @param paras(shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态)
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> exportAuditShopTask(Map<String, Object> paras)
    {

        int userId = Integer.parseInt(String.valueOf(paras.get("userId")));
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        // 用户记录审核历史中的id
        List<Integer> shopTaskIds = new ArrayList<Integer>();
        //获取炒店任务列表
        List<Map<String, Object>> shopTaskList = new ArrayList<>();
        try
        {
            shopTaskList = iShopTaskMapper.exportAuditShopTask(paras);
            if (CollectionUtils.isEmpty(shopTaskList))
            {
                return result;
            }
        }
        catch (Exception e)
        {
            return result;
        }


        try
        {
            // userAuditMarketJobIndex（key为营销任务id，value为用户第几个审核营销任务）
            Map<Integer, Integer> userAuditShopTaskIndex = calculateUserAuditShopTaskIndex(shopTaskList, shopTaskIds, userId);
            // marketJobAuditInfo 营销任务审核记录
            List<Map<String, Object>> ShopTaskAuditInfo = iShopTaskMapper.queryShopTaskAuditInfo("(" + StringUtils.join(shopTaskIds, ",") + ")");
            // 获取用户要审核的营销任务
            result = calculateUserAuditShopTasks(shopTaskList, ShopTaskAuditInfo, userAuditShopTaskIndex);
            // 根据id排序
            Collections.sort(result, new Comparator<Map<String, Object>>()
            {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2)
                {
                    return (Integer.parseInt(String.valueOf(o2.get("id")))) - Integer.parseInt(String.valueOf(o1.get("id")));
                }
            });
        }
        catch (IOException e)
        {
            LOG.error("query exportAuditShopTask error. ", e);
        }
        return result;
    }

    /**
     * 插入shop_task_channel
     *
     * @param dataList
     * @return
     */
    public int insertShopTaskChannel(List<ShopTaskChannelDomain> dataList)
    {
        return iShopTaskMapper.insertShopTaskChannel(dataList);
    }

    /**
     * 将精细化传过来的AZ1103活动渠道文件推送到炒店任务中
     */
    @Transactional
    public void pushJXHChannelToshop()
    {
        //1、查询所有待办的渠道
        List<ShopTaskChannelDomain> channelDomains = iShopTaskMapper.queryShopTaskChannel();

        if (channelDomains != null && channelDomains.size() > 0)
        {
            for (ShopTaskChannelDomain channelDomain : channelDomains)
            {
                //2、将渠道更新到炒店
                iShopTaskMapper.pushChannelToshopBase(channelDomain);
                //3、修改状态
                iShopTaskMapper.updateShopTaskChannelStatus(channelDomain);
            }
        }
    }

    /**
     * 查询八天未执行任务base_id及其相关管理员telephone字段
     */
    public List<Map<String,Object>> queryExecuteShopTask(String startTime,String endTime)
    {
        return iShopTaskMapper.queryExecuteShopTask(startTime,endTime);
    }

    /**
     * 更改营业厅单独管理员的状态
     * 营业厅管理员账号冻结短信提醒
     */
    public Integer updateUserStatus (Map<String,String> param)
    {
        String baseId = String.valueOf(param.get("baseId"));
        String telephone = String.valueOf(param.get("telephone"));
        String name = String.valueOf(param.get("name"));

        String message = MessageFormat.format(smsConfigBean.getReminderDisableSmsContent(),name);
        LOG.info("Send message successful : message -> " + message + " , phone -> " + telephone);
        sendSmsService.sendReminderNoticeSms(telephone, message);
        return iShopTaskMapper.updateUserStatus(baseId);
    }

    /**
     * 更改营业厅的状态
     */
    public Integer updateShopStatus (Map<String,String> param)
    {
        String baseId=String.valueOf(param.get("baseId"));
        return iShopTaskMapper.updateShopStatus(baseId);
    }

}
