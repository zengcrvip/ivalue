package com.axon.market.core.service.ischeduling;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.constant.isystem.ModelStatusEnum;
import com.axon.market.common.domain.ischeduling.MarketJobAuditHistoryDomain;
import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.common.domain.ischeduling.MarketingTasksDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.*;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.core.service.imodel.ModelService;
import com.axon.market.core.service.iscene.ScenceSmsSynDataService;
import com.axon.market.core.service.isystem.UserService;
import com.axon.market.dao.mapper.ischeduling.IMarketingTasksMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by yuanfei on 2017/6/6.
 */
@Service("marketingTasksService")
public class MarketingTasksService
{
    private static final Logger LOG = Logger.getLogger(MarketingTasksService.class.getName());

    @Autowired
    @Qualifier("marketingTasksDao")
    private IMarketingTasksMapper marketingTasksDao;

    @Autowired
    @Qualifier("marketingTaskPoolService")
    private MarketingTaskPoolService marketingTaskPoolService;

    @Autowired
    @Qualifier("marketJobAuditHistoryService")
    private MarketJobAuditHistoryService marketJobAuditHistoryService;

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("scenceSmsSynDataService")
    private ScenceSmsSynDataService scenceSmsSynDataService;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("modelService")
    private ModelService modelService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    public static MarketingTasksService getInstance()
    {
        return (MarketingTasksService) SpringUtil.getSingletonBean("marketingTasksService");
    }

    /**
     * @see IMarketingTasksMapper#queryTasksCount(Map)
     * @param condition
     * @return
     */
    public int queryTasksCount(Map<String,Object> condition)
    {
        return  marketingTasksDao.queryTasksCount(condition);
    }

    /**
     * @see IMarketingTasksMapper#queryTasksByPage(long, long, Map)
     * @param offset
     * @param maxRecord
     * @param condition
     * @return
     */
    public List<MarketingTasksDomain> queryTasksByPage(long offset, long maxRecord, Map<String,Object> condition)
    {
        return marketingTasksDao.queryTasksByPage(offset, maxRecord, condition);
    }

    /**
     * @see IMarketingTasksMapper#insertMarketingTask(MarketingTasksDomain)
     * @return
     */
    @Transactional
    public ServiceResult insertMarketingTask(MarketingTasksDomain taskDomain,UserDomain userDomain) throws Exception
    {
        ServiceResult result = new ServiceResult();

        if (1 != marketingTasksDao.insertMarketingTask(taskDomain))
        {
            result.setRetValue(-1);
            result.setDesc("新增任务操作失败");
        }
        if (ShopTaskStatusEnum.TASK_READY.getValue().equals(taskDomain.getStatus()))
        {
            // 不需要审批的立即生成任务到任务池
            initMarketingTaskToPool(taskDomain);

            // 如果任务是分批执行任务，需要进行营销模型数据拆分
            ThreadPoolUtil.submit(new Runnable() {
                @Override
                public void run() {
                    if (taskDomain.getIsBoidSale() == 1)
                    {
                        createGroupMarketingTableData(taskDomain);
                    }
                    else
                    {
                        updateMarketingPoolTargetNum(taskDomain);
                    }
                }
            });
        }else{
            //催单
            if (userDomain != null)
            {
                reminderItem(taskDomain.getId(), taskDomain.getName(), userDomain);
            }
        }

        return result;
    }

    /**
     * @see IMarketingTasksMapper#updateMarketingTask(MarketingTasksDomain)
     * @param tasksDomain
     * @return
     */
    @Transactional
    public ServiceResult updateMarketingTask(MarketingTasksDomain tasksDomain ,  UserDomain loginUser)
    {
        ServiceResult result = new ServiceResult();
        if (1 != marketingTasksDao.updateMarketingTask(tasksDomain))
        {
            result.setRetValue(-1);
            result.setDesc("修改任务操作失败");
        }
        if (!ShopTaskStatusEnum.TASK_READY.getValue().equals(tasksDomain.getStatus())){
            //催单
            if (loginUser != null)
            {
                reminderItem(loginUser.getId(), loginUser.getName(), loginUser);
            }
        }
        //清除审批记录
        marketingTasksDao.deleteMarketingTaskAuditHis(tasksDomain.getId());

        return result;
    }

    /**
     * @see IMarketingTasksMapper#deleteMarketingTask(Integer)
     * @param taskId
     * @return
     */
    public ServiceResult deleteMarketingTask(String taskId)
    {
        ServiceResult result = new ServiceResult();
        if (StringUtils.isEmpty(taskId))
        {
            result.setRetValue(-1);
            result.setDesc("任务id为空，无法删除！");
            return result;
        }

        if (1 != marketingTasksDao.deleteMarketingTask(Integer.valueOf(taskId)))
        {
            result.setRetValue(-1);
            result.setDesc("删除任务操作失败");
        }
        return result;
    }

    /**
     *
     * @param taskId
     * @return
     */
    public MarketingTasksDomain queryMarketingTaskById(Integer taskId)
    {
        return marketingTasksDao.queryMarketingTaskById(taskId);
    }

    /**
     *
     * @param testNumbers
     * @param content
     * @return
     */
    public ServiceResult sendMarketingTaskTestSms(String testNumbers, String content, String contentId)
    {
        String[] testNumberArray = testNumbers.split(",");
        sendSmsService.sendMarketJobTestSms(Arrays.asList(testNumberArray), content, contentId);
        return new ServiceResult();
    }

    /**
     * 营销任务的暂停操作
     * @param id
     * @return
     */
    @Transactional
    public ServiceResult stopMarketingTask(Integer id)
    {
        ServiceResult result = new ServiceResult();
        MarketingTasksDomain marketJobDomain = marketingTasksDao.queryMarketingTaskById(id);
        Integer status = marketJobDomain.getStatus();
        if (status.equals(ShopTaskStatusEnum.TASK_STOP.getValue())) {
            result.setRetValue(-1);
            result.setDesc("营销任务已终止，不能重复操作！");
        }
        else if (status.equals(ShopTaskStatusEnum.TASK_READY.getValue()))
        {
            Integer willStatus = ShopTaskStatusEnum.TASK_STOP.getValue();
            //修改数据库状态
            if (1 != marketingTasksDao.updateMarketingTaskStatus(id, willStatus))
            {
                result.setRetValue(-1);
                result.setDesc("数据库终止操作失败");
                return result;
            }
            // 修改营销任务对应的任务池任务状态为终止状态
            marketingTaskPoolService.updateMarketingPoolTaskStatus(id, willStatus);

            // TODO 具体如何终止任务池任务发送的情况？
            result.setDesc("营销任务终止成功");
        }
        else
        {
            result.setRetValue(-1);
            result.setDesc("失效或未生效的任务无需终止！");
        }

        return result;
    }

    /**
     * @see IMarketingTasksMapper#batchUpdateMarketingTasksNextMarketTime()
     */
    public int batchUpdateMarketingTasksNextMarketTime()
    {
        return marketingTasksDao.batchUpdateMarketingTasksNextMarketTime();
    }

    /**
     * 审批营销任务
     * @param id
     * @param operate
     * @param reason
     * @param userDomain
     * @return
     */
    public ServiceResult submitMarketingTaskAudit(int id, String operate, String reason, UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        try
        {
            boolean isApproveAudit = "approve".equals(operate);
            MarketingTasksDomain taskDomain = marketingTasksDao.queryMarketingTaskById(id);
            MarketJobAuditHistoryDomain marketJobAuditHistoryDomain = getMarketJobAuditHistoryDomain(id, userDomain.getId(), operate, reason);
            //审批行为记入历史操作
            int insertSuccess = marketJobAuditHistoryService.insertMarketJobAuditHistory(marketJobAuditHistoryDomain);
            if (insertSuccess > 0 && !isApproveAudit)
            {
                updateMarketingTaskStatus(id, ShopTaskStatusEnum.TASK_FAIL.getValue());
            }
            else if (insertSuccess > 0 && isApproveAudit)
            {
                List<MarketJobAuditHistoryDomain> marketJobAuditHistories = marketJobAuditHistoryService.queryMarketJobAuditHistoryDomain(id);

                String auditUsers = marketJobAuditHistories.get(0).getAuditUsers();
                List<Map<String, Object>> auditUserList = JsonUtil.stringToObject(auditUsers, new TypeReference<List<Map<String, Object>>>()
                {
                });
                List<Integer> needApproveUsers = queryAllAuditUserId(auditUserList);
                for (MarketJobAuditHistoryDomain marketJobAuditHistory : marketJobAuditHistories)
                {
                    needApproveUsers.remove(marketJobAuditHistory.getAuditUser());
                }
                if (CollectionUtils.isEmpty(needApproveUsers))
                {
                    updateMarketingTaskStatus(id, ShopTaskStatusEnum.TASK_READY.getValue());
                    // 营销任务审批通过的时间有效期内则立即生成任务到任务池并计算下一次营销时间
                    taskDomain.setStatus(ShopTaskStatusEnum.TASK_READY.getValue());
                    initMarketingTaskToPool(taskDomain);

                    // 如果任务是分批执行任务，需要进行营销模型数据拆分
                    if (taskDomain.getIsBoidSale() == 1)
                    {
                        ThreadPoolUtil.submit(new Runnable() {
                            @Override
                            public void run() {
                                createGroupMarketingTableData(taskDomain);
                            }
                        });
                    }
                }
            }
            else
            {
                result.setRetValue(-1);
                result.setDesc("营销任务审核操作异常");
                return result;
            }

            String userPhone = marketingTasksDao.queryUserPhoneOfCreateTaskById(id);
            if (StringUtils.isNotEmpty(userPhone))
            {
                String message = MessageFormat.format(smsConfigBean.getAuditNoticeSmsContent(), "营销任务【" + taskDomain.getName() + "】", userDomain.getName(), isApproveAudit ? "通过" : "拒绝");
                sendSmsService.sendAuditNoticeSms(userPhone, message);
            }
        }
        catch (Exception e)
        {
            LOG.error("Submit Market Job error. ", e);
            result.setRetValue(-1);
            result.setDesc("营销任务审核操作异常");
            return result;
        }
        result.setDesc("营销任务审核完成！");
        return result;
    }

    /**
     * 查询需要我审批的营销任务
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryNeedMeAuditMarketingTasks(int userId)
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        // 用户记录审核历史中的id
        List<Integer> marketJobIds = new ArrayList<Integer>();
        //获取营销任务列表
        List<Map<String, Object>> marketJobList = marketingTasksDao.queryAllMarketJobsAuditByUser(userId);
        if (CollectionUtils.isEmpty(marketJobList))
        {
            return result;
        }

        try
        {
            // userAuditMarketJobIndex（key为营销任务id，value为用户第几个审核营销任务）
            Map<Integer, Integer> userAuditMarketJobIndex = calculateUserAuditMarketJobIndex(marketJobList, marketJobIds, userId);
            // marketJobAuditInfo 营销任务审核记录
            List<Map<String, Object>> marketJobAuditInfo = marketingTasksDao.queryMarketingTasksAuditInfo("(" + StringUtils.join(marketJobIds, ",") + ")");
            // 获取用户要审核的营销任务
            result = calculateUserAuditMarketJobs(marketJobList, marketJobAuditInfo, userAuditMarketJobIndex);
            // 根据id排序
            Collections.sort(result, new Comparator<Map<String, Object>>()
            {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2)
                {
                    return Integer.parseInt(String.valueOf(o1.get("id"))) - (Integer.parseInt(String.valueOf(o2.get("id"))));
                }
            });
        }
        catch (IOException e)
        {
            LOG.error("Query My Audit Market Jobs error. ", e);
        }

        return result;
    }

    /**
     * 校验营销任务名称重名
     * @param id
     * @param name
     * @return
     */
    public ServiceResult checkMarketingTaskName(String id,String name)
    {
        ServiceResult result = new ServiceResult();
        Integer taskId = StringUtils.isNotEmpty(id)?Integer.valueOf(id):null;
        if (marketingTasksDao.checkMarketingTaskName(taskId,name) > 0)
        {
            result.setDesc("任务名称【"+name+"】已存在！");
            result.setRetValue(-1);
        }
        return result;
    }

    /**
     * 查询营销任务的审批进度
     * @param taskId
     * @return
     */
    public List<MarketJobAuditHistoryDomain> queryMarketingTaskAuditProgress(Integer taskId,Integer currentUserId)
    {
        List<MarketJobAuditHistoryDomain> result = new ArrayList<MarketJobAuditHistoryDomain>();
        try
        {
            // 获取已经审核的人
            List<MarketJobAuditHistoryDomain> marketJobAuditHistories = marketJobAuditHistoryService.queryMarketJobAuditProgress(taskId);
            // 获取所有需要审核的人
            List<Map<String, Object>> marketJobAllAuditUser = marketJobAuditHistoryService.queryAllMarketJobAuditUser(currentUserId);

            List<Integer> marketJobAuditedUsers = getMarketJobAuditedUsers(marketJobAuditHistories);
            // 获取排序后的审核用户id
            List<Integer> marketJobIdsOrderList = getMarketJobAuditUserOrder(String.valueOf(marketJobAllAuditUser.get(0).get("marketingAuditUsers")));

            for (Integer userId : marketJobIdsOrderList)
            {
                if (marketJobAuditedUsers.contains(userId))
                {
                    for (MarketJobAuditHistoryDomain marketJobAuditHistoryDomain : marketJobAuditHistories)
                    {
                        if (userId.equals(marketJobAuditHistoryDomain.getAuditUser()))
                        {
                            result.add(marketJobAuditHistoryDomain);
                            break;
                        }
                    }
                }
                else
                {
                    for (Map<String, Object> map : marketJobAllAuditUser)
                    {
                        Integer id = Integer.parseInt(String.valueOf(map.get("id")));
                        if (userId.equals(id))
                        {
                            MarketJobAuditHistoryDomain marketJobAuditHistoryDomain = new MarketJobAuditHistoryDomain();
                            marketJobAuditHistoryDomain.setAuditUser(id);
                            marketJobAuditHistoryDomain.setAuditUserName(String.valueOf(map.get("name")));
                            result.add(marketJobAuditHistoryDomain);
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Query Market Job Audit Progress error. ", e);
        }

        return result;
    }

    /**
     *
     * @param marketJobAuditHistories
     * @return
     */
    private List<Integer> getMarketJobAuditedUsers(List<MarketJobAuditHistoryDomain> marketJobAuditHistories)
    {
        List<Integer> result = new ArrayList<Integer>();

        for (MarketJobAuditHistoryDomain marketJobAuditHistoryDomain : marketJobAuditHistories)
        {
            result.add(marketJobAuditHistoryDomain.getAuditUser());
        }

        return result;
    }

    /**
     *
     * @param marketJobAuditUsers
     * @return
     * @throws IOException
     */
    private List<Integer> getMarketJobAuditUserOrder(String marketJobAuditUsers) throws IOException
    {
        List<Integer> result = new ArrayList<Integer>();

        List<Map<String, Object>> auditUsers = JsonUtil.stringToObject(marketJobAuditUsers, new TypeReference<List<Map<String, Object>>>()
        {
        });

        for (Map<String, Object> map : auditUsers)
        {
            result.add(Integer.parseInt(String.valueOf(map.get("auditUser"))));
        }

        return result;
    }

    /**
     * 立即生成任务到任务池
     * @param taskDomain
     * @throws ParseException
     */
    private void initMarketingTaskToPool(MarketingTasksDomain taskDomain) throws ParseException
    {
        // 如果是任务不需要审批直接判断是否需要进入任务池，并设定下次营销时间
        long startTime = TimeUtil.formatDate(taskDomain.getStartTime() + " 00:00:00").getTime();
        long endTime = TimeUtil.formatDate(taskDomain.getStopTime() + " 23:59:59").getTime();
        long currentTime = System.currentTimeMillis();

        //判断是否在有效期呢
        if (startTime <= currentTime && currentTime <= endTime)
        {
            // 在有效期内，当天审批通过的任务需要进入任务池
            marketingTaskPoolService.insertMarketingTaskToPool(taskDomain);
        }

        //修改下次营销的执行时间
        marketingTasksDao.updateMarketingTaskNextMarketTime(taskDomain.getId());
    }

    /**
     * 计算用户是第几个审核营销任务
     * userAuditMarketJobIndex（key为营销任务id，value为用户第几个审核营销任务）
     *
     * @param marketJobList
     * @param userId
     * @throws IOException
     */
    private Map<Integer, Integer> calculateUserAuditMarketJobIndex(List<Map<String, Object>> marketJobList, List<Integer> marketJobIds, int userId) throws IOException
    {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        // 查询出需要用户审核的营销任务
        Iterator<Map<String, Object>> marketJobIterator = marketJobList.iterator();
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
                    marketJobIds.add(marketJobId);
                    // 营销任务id为key，用户审核该营销任务的次序为value
                    result.put(marketJobId, Integer.parseInt(auditUser.get("order")));
                    break;
                }
            }
        }
        return result;
    }

    private MarketJobAuditHistoryDomain getMarketJobAuditHistoryDomain(int marketJobId, int auditUserId, String operate, String reason)
    {
        MarketJobAuditHistoryDomain domain = new MarketJobAuditHistoryDomain();
        domain.setMarketJobId(marketJobId);
        domain.setAuditUser(auditUserId);
        domain.setAuditResult(operate);
        domain.setRemarks(reason);
        return domain;
    }

    /**
     * 更新营销任务的状态
     *
     * @param taskId
     */
    private void updateMarketingTaskStatus(int taskId, Integer status)
    {
        marketingTasksDao.updateMarketingTaskStatus(taskId, status);
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
     * @param marketJobList
     * @param marketJobAuditInfoList
     * @param userAuditMarketJobIndex
     * @return
     */
    private List<Map<String, Object>> calculateUserAuditMarketJobs(List<Map<String, Object>> marketJobList, List<Map<String, Object>> marketJobAuditInfoList, Map<Integer, Integer> userAuditMarketJobIndex)
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(marketJobList);
        List<Integer> marketJobIds = new ArrayList<Integer>();
        // 审核历史表中存在的id集合
        List<Integer> marketJobAuditIds = new ArrayList<Integer>();

        Iterator<Map<String, Object>> marketJobAuditInfoIterator = marketJobAuditInfoList.iterator();
        // 遍历获得审核历史表中存在的id集合
        while (marketJobAuditInfoIterator.hasNext())
        {
            Map<String, Object> marketJobAuditInfo = marketJobAuditInfoIterator.next();
            marketJobAuditIds.add(Integer.parseInt(String.valueOf(marketJobAuditInfo.get("id"))));
        }

        for (Map.Entry<Integer, Integer> marketJobEntry : userAuditMarketJobIndex.entrySet())
        {
            int marketJobId = marketJobEntry.getKey(), order = marketJobEntry.getValue();
            marketJobAuditInfoIterator = marketJobAuditInfoList.iterator();

            Map<String, Object> marketJobAuditInfo;
            // 判断客户群是否是第一次审核
            if (marketJobAuditIds.contains(marketJobId))
            {
                while (marketJobAuditInfoIterator.hasNext())
                {
                    marketJobAuditInfo = marketJobAuditInfoIterator.next();
                    if (Integer.parseInt(String.valueOf(marketJobAuditInfo.get("id"))) == marketJobId && (Integer.parseInt(String.valueOf(marketJobAuditInfo.get("count"))) + 1) == order)
                    {
                        marketJobIds.add(marketJobId);
                        break;
                    }
                }
            }
            else
            {
                if (order == 1)
                {
                    marketJobIds.add(marketJobId);
                }
            }
        }

        Iterator<Map<String, Object>> resultIterator = result.iterator();
        // 去除不满足的营销任务
        while (resultIterator.hasNext())
        {
            Map<String, Object> resultMap = resultIterator.next();
            if (!marketJobIds.contains(Integer.parseInt(String.valueOf(resultMap.get("id")))))
            {
                resultIterator.remove();
            }
        }

        return result;
    }

    /**
     * 查询所有当天待执行的任务
     * @param date
     * @return
     */
    public List<MarketingPoolTaskDomain> queryAllWaitingExecuteMarketTask(String date)
    {
       return marketingTasksDao.queryAllWaitingExecuteMarketTask(date);
    }

    /**
     * 根据任务ID修改任务状态
     * @param taskId
     * @param status
     * @return
     */
    public int updateShopTaskExecuteBySystemId(Integer taskId, Integer status)
    {
        return marketingTasksDao.updateMarketTaskExecuteBySystemId(taskId, status);
    }

    /**
     * 根据任务ID修改任务的执行次数及状态
     * @param paras
     * @return
     */
    public int updateMarketTaskExecuteById(Map<String,Object> paras)
    {
        return marketingTasksDao.updateMarketTaskExecuteById(paras);
    }

    /**
     * 根据任务ID修改任务的状态
     * @param taskId
     * @param status
     * @return
     */
    public int updateMarketingTaskStatus(Integer taskId, Integer status)
    {
        return marketingTasksDao.updateMarketingTaskStatus(taskId, status);
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
            List<MarketJobAuditHistoryDomain> marketJobAuditHistories = marketJobAuditHistoryService.queryMarketJobAuditHistoryDomain(id);
            String auditUsers = loginDomain.getMarketingAuditUsers();
            List<Map<String, Object>> auditUserList = JsonUtil.stringToObject(auditUsers, new TypeReference<List<Map<String, Object>>>()
            {
            });
            List<Integer> needApproveUsers = queryAllAuditUserId(auditUserList);
            for (MarketJobAuditHistoryDomain marketTaskAuditHistory : marketJobAuditHistories)
            {
                needApproveUsers.remove(marketTaskAuditHistory.getAuditUser());
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
                    String message = MessageFormat.format(smsConfigBean.getReminderNoticeSmsContent(), userDomain.getName(), "营销任务[" + taskName + "]");

                    sendSmsService.sendAuditNoticeSms(telephone, message);
                }
            }
        }
        catch (Exception e)
        {
            serviceResult.setRetValue(-1);
            serviceResult.setDesc("营销任务催单处理失败");
            LOG.error("营销任务催单功能处理失败", e);
        }
        return serviceResult;
    }

    /**
     * 创建分组任务表，并将对应的模型数据按营销日期存入该表中
     *
     * @param taskDomain
     * @throws ParseException
     */
    private void createGroupMarketingTableData(MarketingTasksDomain taskDomain)
    {
        try
        {
            // 1、根据拆分信息获取分组日期
            List<String> dateList = new ArrayList<String>();
            Calendar calendar = Calendar.getInstance();
            Date startDate = TimeUtil.formatDateToYMDDevide(taskDomain.getStartTime());
            Date endDate = TimeUtil.formatDateToYMDDevide(taskDomain.getStopTime());
            Date currentDate = TimeUtil.formatDateToYMDDevide(TimeUtil.formatDateToYMDDevide(new Date()));
            Integer interval = taskDomain.getSendInterval();

            // 如果审批通过时间超过有效期，任务废弃，没必要再创建对应的任务分组表了
            if (currentDate.getTime() > endDate.getTime())
            {
                LOG.info("Task: " + taskDomain.getName() + " is invalid ......");
                return;
            }

            // 如果开始时间 > 当前时间，即执行时间还没有到，那么从开始时间计算分组，否则从当前时间
            if (startDate.getTime() >= currentDate.getTime())
            {
                calendar.setTime(startDate);
            }
            else
            {
                calendar.setTime(currentDate);
            }

            // 获取分组日期
            while(calendar.getTime().getTime() <= endDate.getTime())
            {
                dateList.add(TimeUtil.formatDateToYMD(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, interval);
            }

            // 如果无法完成拆分，即拆表开始时间和有效期结束时间差小于营销时间间隔，则表示不再进行拆表以及后续操作
            if (CollectionUtils.isEmpty(dateList))
            {
                LOG.info("Can`t Split,Cause: split_startTime is "+ TimeUtil.formatDateToYMDDevide(new Date()) +"and Task_EndTime is "+taskDomain.getStopTime() +", it`s interval less than "+interval);
                return;
            }

            // 2、先创建GP中对应的待分组的任务表
            String taskTableName = "task.task_"+taskDomain.getId();
            if (!greenPlumOperateService.isExistsTable(taskTableName))
            {
                if (!greenPlumOperateService.createGroupingTaskTable(taskTableName))
                {
                    LOG.error("Create Model Table Error");
                    return;
                }
            }

            // 3、获取所有任务中的模型信息，拼接查询GP对应模型数据的基础sql
            String modelIds = taskDomain.getMarketSegmentIds();
            String[] modelIdArray = modelIds.split(",");

            String baseSql = "select distinct phone,area_id from " + greenPlumOperateService.getModelDataTableName(modelIdArray[0]);
            for (int i = 1; i < modelIdArray.length; i++)
            {
                String modelTable = greenPlumOperateService.getModelDataTableName(modelIdArray[i]);
                baseSql += " union select * from " + modelTable;
            }

            //4、根据不同的sendDate生成对应的数据查询SQL
            //查询这批数据的数目
            int totalCount = greenPlumOperateService.queryRecordCount("select count(0) from (" + baseSql + ") as modelTable");
            //获取每批次的发送用户数
            int groupCount = (int)Math.ceil((float)totalCount/dateList.size());

            LOG.info("Task Table:" + taskTableName + "[source from model "+StringUtils.join(modelIdArray,"|")+"] contain totalCount:" + totalCount + "[No-duplication-Result],group: " + dateList.size() + ",dateGroup:" + dateList + ",oneGroupItemCount: <=" + groupCount);

            String dateSql = "";
            for (int i = 0; i< dateList.size(); i++)
            {
                dateSql += (i ==0 ? "": " union ") + "(select modelTable"+i+".*, " + dateList.get(i) + " from (" + baseSql + ") as modelTable"+i+" order by phone limit " + groupCount + " offset " + groupCount * i+")";
            }

            // 5、执行入库操作
            dateSql = "insert into " + taskTableName + " select * from (" + dateSql + ") as modelTable";
            int resultCount = greenPlumOperateService.update(dateSql);
            LOG.info("insert into "+ taskTableName + " Success,Total insert :" + resultCount);

            //更新任务池任务的目标用户数
            if (startDate.getTime() <= currentDate.getTime())
            {
                marketingTaskPoolService.updateMarketingPoolTaskTargetCount(taskDomain.getId(), groupCount);
            }
        }
        catch (Exception e)
        {
            LOG.error("Split Table Error",e);
            return;
        }
    }

    /**
     *
     * @return
     */
    public List<Map<String,Object>> queryMarketingUserDistribution()
    {
        return marketingTasksDao.queryMarketingUserDistribution();
    }

    /**
     *
     * @param userId
     * @return
     */
    public List<String> queryMyTodoTaskColumn(Integer userId)
    {
        return userService.queryUserCanOperateTaskGX(userId);
    }

    /**
     *
     * @param type
     * @param userDomain
     * @return
     */
    public Table queryMyTodoTask (String type,UserDomain userDomain)
    {
        Integer userId = userDomain.getId();
        switch (type)
        {
            case "1": return queryNeedMeAuditModelsGX(userId);//查询需要我审批的模型
            case "2": return queryNeedMeAuditMarketingTasksGX(userId);//查询需要我审批的营销任务
            case "3": return queryMarketingTaskPoolByPageGX(userId);
            case "4": return queryTodaySegmentsByPageGX(userDomain);
        }
        return new Table();
    }

    /**
     * 查询需要我审批的模型
     * @param userId
     * @return
     */
    private Table queryNeedMeAuditModelsGX(Integer userId)
    {
        int startIndex = 0;
        int pageSize = 10;
        List<Map<String, Object>> modelDomainList = modelService.queryNeedMeAuditModels(userId);
        int itemCounts = modelDomainList.size();
        int endIndex = startIndex + pageSize > itemCounts ? itemCounts : startIndex + pageSize;
        return new Table(modelDomainList.subList(startIndex, endIndex),itemCounts);
    }

    /**
     * 查询需要我审批的营销任务
     * @param userId
     * @return
     */
    private Table queryNeedMeAuditMarketingTasksGX(Integer userId)
    {
        int startIndex = 0;
        int pageSize = 10;
        List<Map<String, Object>> modelDomainList = queryNeedMeAuditMarketingTasks(userId);
        int itemCounts = modelDomainList.size();
        int endIndex = startIndex + pageSize > itemCounts ? itemCounts : startIndex + pageSize;
        return new Table(modelDomainList.subList(startIndex, endIndex),itemCounts);
    }

    /**
     * 查询当天我能操作的赢下任务
     * @param userId
     * @return
     */
    private Table queryMarketingTaskPoolByPageGX(Integer userId)
    {
        int curPageIndex = 0;
        int pageSize = 10;

        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("name", null);
        condition.put("status",ShopTaskStatusEnum.TASK_READY.getValue());
        condition.put("marketType", null);
        condition.put("businessType", null);

        int count = marketingTaskPoolService.queryMarketingPoolTasksCount(condition);
        List<MarketingPoolTaskDomain> tasks = marketingTaskPoolService.queryMarketingPoolTasksByPage(curPageIndex, pageSize, condition);

        return new Table(tasks,count);
    }

    /**
     * 查询今日能看到的刷新的标签
     * @param userDomain
     * @return
     */
    private Table queryTodaySegmentsByPageGX(UserDomain userDomain)
    {
        Integer start = 0;
        Integer length = 10;
        String status = ModelStatusEnum.READY.getValue() + "," + ModelStatusEnum.REFRESHING.getValue();

        return modelService.queryModelsByPage(start, length, status, userDomain, null, null, null, null,true);
    }

    /**
     * 更新当前入任务池的营销任务数
     * @param taskDomain
     */
    private void updateMarketingPoolTargetNum(MarketingTasksDomain taskDomain)
    {
        String sql = "";
        String modelIds = taskDomain.getMarketSegmentIds();
        String[] modelIdArray = modelIds.split(",");
        // 生成客户群数据sql(踢重)
        for (int i=0; i<modelIdArray.length; i++)
        {
            sql += (i==0 ? "" : " union") + " select * from " + greenPlumOperateService.getModelDataTableName(modelIdArray[i]);
        }
        sql = "select count(0) from (" + sql + ") as modelTable";

        int totalCount = greenPlumOperateService.queryRecordCount(sql);
        marketingTaskPoolService.updateMarketingPoolTaskTargetCount(taskDomain.getId(), totalCount);
    }
}
