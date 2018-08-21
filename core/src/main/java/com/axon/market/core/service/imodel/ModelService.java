package com.axon.market.core.service.imodel;

import com.axon.market.common.bean.*;
import com.axon.market.common.constant.icommon.CategoryTypeEnum;
import com.axon.market.common.constant.isystem.MarketSystemElementCreateEnum;
import com.axon.market.common.constant.isystem.ModelStatusEnum;
import com.axon.market.common.domain.icommon.CategoryDomain;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.domain.isystem.ModelAuditHistoryDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.*;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.CategoryService;
import com.axon.market.core.service.icommon.FileUploadService;
import com.axon.market.core.service.icommon.RefreshDataService;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.core.service.isystem.ModelAuditHistoryService;
import com.axon.market.core.service.isystem.RoleService;
import com.axon.market.core.service.isystem.UserService;
import com.axon.market.dao.mapper.imodel.IModelMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by chenyu on 2016/1/4.
 */
@Component("modelService")
public class ModelService
{
    private static final Logger LOG = Logger.getLogger(ModelService.class.getName());

    @Autowired
    @Qualifier("modelDao")
    private IModelMapper modelDao;

    @Autowired
    @Qualifier("categoryService")
    private CategoryService categoryService;

    @Autowired
    @Qualifier("roleService")
    private RoleService roleService;

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Autowired
    @Qualifier("modelAuditHistoryService")
    private ModelAuditHistoryService modelAuditHistoryService;

    @Autowired
    @Qualifier("fileUploadService")
    private FileUploadService fileUploadService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("modelDataOperateService")
    private ModelDataOperateService modelDataOperateService;

    @Autowired
    @Qualifier("refreshDataService")
    private RefreshDataService refreshDataService;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    /**
     * @return
     */
    public static ModelService getInstance()
    {
        return (ModelService) SpringUtil.getSingletonBean("modelService");
    }

    /**
     * @return
     */
    public List<ModelDomain> queryAllModels()
    {
        return modelDao.queryAllModels();
    }

    public List<ModelDomain> queryAllRuleModelsBySystem()
    {
        return modelDao.queryAllRuleModelsBySystem();
    }

    /**
     * @param offset
     * @param limit
     * @param nameSearch
     * @param createTypeSearch
     * @param userNameSearch
     * @param catalogSearch
     * @return
     */
    public Table queryModelsByPage(Integer offset, Integer limit,String status,UserDomain userDomain, String nameSearch, String createTypeSearch, String userNameSearch, String catalogSearch,Boolean qryToday)
    {
        try
        {
            //查询分页数据
            Integer count = modelDao.queryAllModelCounts(status,userDomain,nameSearch, createTypeSearch, userNameSearch, catalogSearch,qryToday);
            //查询数据
            List<ModelDomain> list = modelDao.queryModelsByPage(offset, limit, status,userDomain,nameSearch, createTypeSearch, userNameSearch, catalogSearch,qryToday);
            setSpecifiedRoleNames(list);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Models Error. ", e);
            return new Table();
        }
    }

    /**
     * @param id
     * @return
     */
    public ModelDomain queryModelById(Integer id)
    {
        return modelDao.queryModelById(id);
    }

    /**
     * 获取指定角色名称
     *
     * @param modelDomainList
     */
    private void setSpecifiedRoleNames(List<ModelDomain> modelDomainList)
    {
        if (CollectionUtils.isNotEmpty(modelDomainList))
        {
            List<Map<String, Object>> roleIdAndName = roleService.queryAllRole();

            Map<Integer, String> roleIdNameMap = new HashMap<Integer, String>();

            for (Map<String, Object> role : roleIdAndName)
            {
                roleIdNameMap.put(Integer.parseInt(String.valueOf(role.get("id"))), String.valueOf(role.get("name")));
            }

            for (ModelDomain modelDomain : modelDomainList)
            {
                String specifiedRoleIds = modelDomain.getSpecifiedRoleIds();
                if (StringUtils.isNotEmpty(specifiedRoleIds))
                {
                    List<String> roleNames = new ArrayList<String>();
                    String[] specifiedUserIdArray = specifiedRoleIds.split(",");
                    for (String roleId : specifiedUserIdArray)
                    {
                        roleNames.add(roleIdNameMap.get(Integer.parseInt(roleId)));
                    }
                    modelDomain.setSpecifiedRoleNames(StringUtils.join(roleNames, ","));
                }
            }
        }
    }

    /**
     * @param request
     * @param modelDomain
     * @param userDomain
     * @return
     */
    @Transactional
    public Operation addOrEditModel(HttpServletRequest request, ModelDomain modelDomain, UserDomain userDomain)
    {
        // TODO 判断数据库重复
        Boolean result = false;
        String message = "系统异常";

        try
        {
            if (modelDomain == null)
            {
                modelDomain = generateModelDomainByRequest(request);
            }

            if (isModelExisted(modelDomain))
            {
                return new Operation(false, "模型名称重复");
            }

            if (StringUtils.isEmpty(userDomain.getSegmentAuditUsers()))
            {
                modelDomain.setStatus(ModelStatusEnum.READY.getValue());
            }
            else
            {
                modelDomain.setStatus(ModelStatusEnum.AUDITING.getValue());
            }

            //新增
            if (modelDomain.getId() == null)
            {
                modelDomain.setCreateUser(userDomain.getId());
                modelDomain.setCreateTime(TimeUtil.formatDate(new Date()));
                result = modelDao.createModel(modelDomain) == 1;
                message = result ? "新增模型成功" : "新增模型失败";
            }
            else
            {
                modelDomain.setLastUpdateUser(userDomain.getId());
                modelDomain.setLastUpdateTime(TimeUtil.formatDate(new Date()));
                result = modelDao.updateModel(modelDomain) == 1;
                message = result ? "更新模型成功" : "更新模型失败";
                //删除审核历史
                modelAuditHistoryService.deleteModelAuditHistory(modelDomain.getId());
            }

            if (MarketSystemElementCreateEnum.LOCAL_IMPORT.getValue().equals(modelDomain.getCreateType()))
            {
                String fileName = modelDomain.getId() + "@" + greenPlumOperateService.getModelDataTableName(modelDomain);
                final File file = fileUploadService.fileUpload(request, fileName);
                if (file != null)
                {
                    ThreadPoolUtil.submit(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            refreshDataService.refreshLocalImportModelData(file);
                        }
                    });
                }
            }
            else if (MarketSystemElementCreateEnum.RULE.getValue().equals(modelDomain.getCreateType()))
            {
                final ModelDomain newModelDomain = modelDomain;
                final List<RuleNode> nodes = JsonUtil.stringToObject(newModelDomain.getRule(), new TypeReference<List<RuleNode>>()
                {
                });
                ThreadPoolUtil.submit(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        refreshDataService.refreshRuleModelData(newModelDomain, nodes);
                    }
                });
            }

            // 无需审批且配置了短信提醒的需要发送短信提醒
            Boolean isNeedAudit = StringUtils.isNotEmpty(userDomain.getSegmentAuditUsers());
            if (!isNeedAudit && result && 1 == modelDomain.getIsNeedSendNotifySms())
            {
                List<String> phones = userService.queryPhonesByUserRoleIds(modelDomain.getSpecifiedRoleIds());
                String sendMessage = MessageFormat.format(smsConfigBean.getSegmentModifyNoticeSmsContent(), modelDomain.getName());
                sendSmsService.sendSmsOfModelNotice(phones, sendMessage);
            }
        }
        catch (Exception e)
        {
            LOG.error("Add Or Edit Model Error. ", e);
        }

        return new Operation(result, message);
    }

    public ResultVo editOrAddModelByKeeper(ModelDomain modelDomain, UserDomain userDomain)
    {
        ResultVo result = new ResultVo();
        Boolean flag = false;
        String message = "系统异常";

        if (modelDomain.getId() == null)
        {
            modelDomain.setCreateUser(userDomain.getId());
            modelDomain.setCreateTime(TimeUtil.formatDate(new Date()));
            flag = modelDao.createModel(modelDomain) == 1;
            message = flag ? "新增模型成功" : "新增模型失败";
        }
        else
        {
            modelDomain.setLastUpdateUser(userDomain.getId());
            modelDomain.setLastUpdateTime(TimeUtil.formatDate(new Date()));
            flag = modelDao.updateModel(modelDomain) == 1;
            message = flag ? "更新模型成功" : "更新模型失败";
            //删除审核历史
            modelAuditHistoryService.deleteModelAuditHistory(modelDomain.getId());
        }

        final ModelDomain newModelDomain = modelDomain;
//        final List<RuleNode> nodes = JsonUtil.stringToObject(newModelDomain.getRule(), new TypeReference<List<RuleNode>>()
//        {
//        });
        ThreadPoolUtil.submit(new Runnable()
        {
            @Override
            public void run()
            {
                refreshDataService.refreshKeeperRuleModelData(newModelDomain, modelDomain.getRule());
            }
        });

        return result;
    }

    /**
     * @param id
     * @param userId
     * @return
     */
    public Operation deleteModel(Integer id, Integer userId)
    {
        try
        {
            Boolean result = modelDao.deleteModel(id, userId, TimeUtil.formatDate(new Date())) == 1;
            String message = result ? "删除模型成功" : "删除模型失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Delete Model Error. ", e);
            return new Operation();
        }
    }

    /**
     * @param modelId
     * @param count
     * @return
     */
    public Integer updateModelRefreshInfo(Integer modelId, Integer count, String refreshTime)
    {
        return modelDao.updateModelRefreshInfo(modelId, count, refreshTime);
    }

    /**
     * @param modelId
     * @return
     */
    public String queryModelRefreshTimeByModelId(int modelId)
    {
        return modelDao.queryModelRefreshTimeByModelId(modelId);
    }

    /**
     * 目录查询可见的模型
     *
     * @param userDomain
     * @return
     */
    public List<CategoryDomain> queryAllModelsUnderCatalog(UserDomain userDomain)
    {
        List<CategoryDomain> categoryList = categoryService.queryAllCategory(CategoryTypeEnum.CT_MODEL.getValue());
        //如果目录不存在，则不需要再进行查询
        if (CollectionUtils.isNotEmpty(categoryList))
        {
            List<ModelDomain> modelList = modelDao.queryAllModelsByUser(userDomain, false);
            for (ModelDomain modelDomain : modelList)
            {
                CategoryDomain categoryDomain = new CategoryDomain();
                categoryDomain.setId(modelDomain.getId());
                categoryDomain.setName(modelDomain.getName());
                categoryDomain.setpId(Integer.valueOf(modelDomain.getCatalogId()));
                categoryDomain.setIsParent(false);
                categoryDomain.setElement(modelDomain);
                categoryList.add(categoryDomain);
            }
        }

        return categoryList;
    }

    /**
     * 查询需要我审批的模型
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryNeedMeAuditModels(Integer userId)
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        // 用户记录审核历史中的id
        List<Integer> modelIds = new ArrayList<Integer>();

        List<Map<String, Object>> modelList = modelDao.queryAllAuditModelsByUser(userId);
        if (CollectionUtils.isEmpty(modelList))
        {
            return result;
        }

        try
        {
            // userAuditSegmentIndex（key为客户群id，value为用户第几个审核客户群）
            Map<Integer, Integer> userAuditModelIndex = calculateUserAuditModelIndex(modelList, modelIds, userId);
            // segmentAuditInfo 客户群审核记录
            List<Map<String, Object>> modelAuditList = modelDao.queryModelAuditInfo("(" + StringUtils.join(modelIds, ",") + ")");
            // 获取用户要审核的客户群
            result = calculateUserAuditModels(modelList, modelAuditList, userAuditModelIndex);
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
            LOG.error("Query My Audit Model Error. ", e);
        }

        return result;
    }

    //提交模型审核
    public ServiceResult submitModelAudit(int id, String operate, String reason, UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        try
        {
            boolean isApproveAudit = "approve".equals(operate);
            ModelDomain modelDomain = modelDao.queryAuditingModelById(id);//当前审核中的对象
            ModelAuditHistoryDomain domain = getModelAuditHistoryDomain(id, userDomain.getId(), operate, reason);
            int insertSuccess = modelAuditHistoryService.insertModelAuditHistory(domain);
            if (insertSuccess > 0 && !isApproveAudit)
            {
                setSegmentStatus(id, ModelStatusEnum.AUDIT_REJECT.getValue());
            }
            else if (insertSuccess > 0 && isApproveAudit)
            {
                List<ModelAuditHistoryDomain> modelAuditHistoryDomains = modelAuditHistoryService.queryModelAuditHistoryDomain(id);

                String auditUsers = modelAuditHistoryDomains.get(0).getAuditUsers();
                List<Map<String, Object>> auditUserList = JsonUtil.stringToObject(auditUsers, new TypeReference<List<Map<String, Object>>>()
                {
                });
                List<Integer> needApproveUsers = queryAllAuditUserId(auditUserList);
                for (ModelAuditHistoryDomain segmentAuditHistoryDomain : modelAuditHistoryDomains)
                {
                    needApproveUsers.remove(segmentAuditHistoryDomain.getAuditUser());
                }
                if (CollectionUtils.isEmpty(needApproveUsers))
                {
                    setSegmentStatus(id, ModelStatusEnum.READY.getValue());

                    // 对审批通过的模型进行进行短信提醒
                    if (modelDomain.getIsNeedSendNotifySms() != null && 1 == modelDomain.getIsNeedSendNotifySms())
                    {
                        List<String> phones = userService.queryPhonesByUserRoleIds(modelDomain.getSpecifiedRoleIds());
                        String sendMessage = MessageFormat.format(smsConfigBean.getSegmentModifyNoticeSmsContent(), modelDomain.getName());
                        sendSmsService.sendSmsOfModelNotice(phones, sendMessage);
                    }
                }
            }
            else
            {
                result.setRetValue(-1);
                result.setDesc("客户群审核操作异常");
                return result;
            }

            /*
            String userPhone = modelDao.queryUserPhoneOfCreateModelByModelId(id);
            if (StringUtils.isNotEmpty(userPhone))
            {
                String message = MessageFormat.format(smsConfigBean.getAuditNoticeSmsContent(), "客户群【" + segmentDomain.getName() + "】", userDomain.getName(), isApproveAudit ? "通过" : "拒绝");
                sendSmsService.sendAuditNoticeSms(userPhone, message);
            }
            */
        }
        catch (Exception e)
        {
            LOG.error("Submit Segment Audit error. ", e);
            result.setRetValue(-1);
            result.setDesc("客户群审核操作异常");
            return result;
        }
        result.setDesc("模型审核完成！");
        return result;
    }

    public ServiceResult checkDuplicationOfModelName(String id,String name)
    {
        ServiceResult result = new ServiceResult();
        Integer modelId = StringUtils.isNotEmpty(id)?Integer.valueOf(id):null;
        ModelDomain modelDomain = modelDao.queryModelByName(name);
        if (null != modelDomain && !modelDomain.getId().equals(modelId))
        {
            result.setRetValue(-1);
            result.setDesc("模型名【"+name+"】已存在");
        }
        return result;
    }

    private int setSegmentStatus(int id, Integer status)
    {
        return modelDao.setModelStatus(id, status);
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

    private ModelAuditHistoryDomain getModelAuditHistoryDomain(int modelId, int auditUserId, String operate, String reason)
    {
        ModelAuditHistoryDomain domain = new ModelAuditHistoryDomain();
        domain.setModelId(modelId);
        domain.setAuditUser(auditUserId);
        domain.setAuditResult(operate);
        domain.setRemarks(reason);
        return domain;
    }

    /**
     * 计算用户是第几个审核客户群
     * userAuditSegmentIndex（key为客户群id，value为用户第几个审核客户群）
     *
     * @param modelList
     * @param userId
     * @throws IOException
     */
    private Map<Integer, Integer> calculateUserAuditModelIndex(List<Map<String, Object>> modelList, List<Integer> modelIds, int userId) throws IOException
    {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        // 查询出需要用户审核的客户群
        Iterator<Map<String, Object>> modelIterator = modelList.iterator();
        // 遍历查询出的需要审核的客户群，把id和审核顺序加入返回map中
        while (modelIterator.hasNext())
        {
            Map<String, Object> segment = modelIterator.next();
            int modelId = Integer.parseInt(String.valueOf(segment.get("id")));
            String auditUsers = String.valueOf(segment.get("modelAuditUsers"));
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
                    modelIds.add(modelId);
                    // 客户群id为key，用户审核该客户群的次序为value
                    result.put(modelId, Integer.parseInt(auditUser.get("order")));
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @param modelList
     * @param modelAuditList
     * @param userAuditModelIndex
     * @return
     */
    private List<Map<String, Object>> calculateUserAuditModels(List<Map<String, Object>> modelList, List<Map<String, Object>> modelAuditList, Map<Integer, Integer> userAuditModelIndex)
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(modelList);
        List<Integer> modelIds = new ArrayList<Integer>();
        // 审核历史表中存在的id集合
        List<Integer> modelAuditIds = new ArrayList<Integer>();

        Iterator<Map<String, Object>> modelAuditInfoIterator = modelAuditList.iterator();
        // 遍历获得审核历史表中存在的id集合
        while (modelAuditInfoIterator.hasNext())
        {
            Map<String, Object> modelAuditInfo = modelAuditInfoIterator.next();
            modelAuditIds.add(Integer.parseInt(String.valueOf(modelAuditInfo.get("id"))));
        }

        for (Map.Entry<Integer, Integer> modelEntry : userAuditModelIndex.entrySet())
        {
            int segmentId = modelEntry.getKey(), order = modelEntry.getValue();
            modelAuditInfoIterator = modelAuditList.iterator();

            Map<String, Object> modelAuditInfo;
            // 判断客户群是否是第一次审核
            if (modelAuditIds.contains(segmentId))
            {
                while (modelAuditInfoIterator.hasNext())
                {
                    modelAuditInfo = modelAuditInfoIterator.next();
                    if (Integer.parseInt(String.valueOf(modelAuditInfo.get("id"))) == segmentId && (Integer.parseInt(String.valueOf(modelAuditInfo.get("count"))) + 1) == order)
                    {
                        modelIds.add(segmentId);
                        break;
                    }
                }
            }
            else
            {
                if (order == 1)
                {
                    modelIds.add(segmentId);
                }
            }
        }

        Iterator<Map<String, Object>> resultIterator = result.iterator();
        // 去除不满足的客户群
        while (resultIterator.hasNext())
        {
            Map<String, Object> resultMap = resultIterator.next();
            if (!modelIds.contains(Integer.parseInt(String.valueOf(resultMap.get("id")))))
            {
                resultIterator.remove();
            }
        }

        return result;
    }

    /**
     * @param response
     * @param userDomain
     * @param modelId
     * @throws IOException
     */
    public void downloadModel(HttpServletResponse response, UserDomain userDomain, Integer modelId) throws IOException
    {
        ModelDomain modelDomain = modelDao.queryModelById(modelId);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        String fileName = modelDomain.getName() + "_" + MarketTimeUtils.formatDateToYMD(new Date());
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".csv\"");
        writeDataToStream(response, userDomain.getAreaCode(), userDomain.getId(), modelDomain);
    }

    /**
     *
     * @param modelId
     * @param currentUserId
     * @return
     */
    public List<ModelAuditHistoryDomain> queryModelAuditProgress(int modelId, int currentUserId)
    {
        List<ModelAuditHistoryDomain> result = new ArrayList<ModelAuditHistoryDomain>();
        try
        {
            // 获取已经审核的人
            List<ModelAuditHistoryDomain> modelAuditHistories = modelAuditHistoryService.queryModelAuditProgress(modelId);
            // 获取所有需要审核的人
            List<Map<String, Object>> modelAllAuditUser = modelAuditHistoryService.queryAllModelAuditUser(currentUserId);

            List<Integer> modelAuditedUsers = getSegmentAuditedUsers(modelAuditHistories);
            // 获取排序后的审核用户id
            List<Integer> modelIdsOrderList = getSegmentAuditUserOrder(String.valueOf(modelAllAuditUser.get(0).get("segmentAuditUsers")));

            for (Integer userId : modelIdsOrderList)
            {
                if (modelAuditedUsers.contains(userId))
                {
                    for (ModelAuditHistoryDomain modelAuditHistoryDomain : modelAuditHistories)
                    {
                        if (userId.equals(modelAuditHistoryDomain.getAuditUser()))
                        {
                            result.add(modelAuditHistoryDomain);
                            break;
                        }
                    }
                }
                else
                {
                    for (Map<String, Object> map : modelAllAuditUser)
                    {
                        Integer id = Integer.parseInt(String.valueOf(map.get("id")));
                        if (userId.equals(id))
                        {
                            ModelAuditHistoryDomain modelAuditHistoryDomain = new ModelAuditHistoryDomain();
                            modelAuditHistoryDomain.setAuditUser(id);
                            modelAuditHistoryDomain.setAuditUserName(String.valueOf(map.get("name")));
                            result.add(modelAuditHistoryDomain);
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Query Model Audit Progress error. ", e);
        }

        return result;
    }

    /**
     * @param modelAuditUsers
     * @return
     * @throws IOException
     */
    private List<Integer> getSegmentAuditUserOrder(String modelAuditUsers) throws IOException
    {
        List<Integer> result = new ArrayList<Integer>();

        List<Map<String, Object>> auditUsers = JsonUtil.stringToObject(modelAuditUsers, new TypeReference<List<Map<String, Object>>>()
        {
        });

        for (Map<String, Object> map : auditUsers)
        {
            result.add(Integer.parseInt(String.valueOf(map.get("auditUser"))));
        }

        return result;
    }

    /**
     * @param modelAuditHistories
     * @return
     */
    private List<Integer> getSegmentAuditedUsers(List<ModelAuditHistoryDomain> modelAuditHistories)
    {
        List<Integer> result = new ArrayList<Integer>();

        for (ModelAuditHistoryDomain modelAuditHistoryDomain : modelAuditHistories)
        {
            result.add(modelAuditHistoryDomain.getAuditUser());
        }

        return result;
    }

    /**
     * @param response
     * @param areaCode
     * @param modelDomain
     * @throws IOException
     */
    private Long writeDataToStream(HttpServletResponse response, final Integer areaCode, Integer userId, final ModelDomain modelDomain) throws IOException
    {
        final OutputStream os = response.getOutputStream();
        final Long[] count = {0L};
        try
        {
            Integer batchSize = 50000;
            final String[] querySql = modelDataOperateService.createQuerySegmentSql(modelDomain, areaCode, userId);

            LOG.info("specialQuerySql:" + querySql[0]);

            os.write(querySql[1].getBytes("GBK"));

            greenPlumOperateService.query(querySql[0], new RowCallbackHandler()
            {
                @Override
                public void processRow(ResultSet resultSet) throws SQLException
                {
                    String phone = AxonEncryptUtil.getInstance().decrypt(resultSet.getString("phone"));
                    phone = phone.startsWith("86") ? phone.substring(2) : phone;
                    String downColumnValue = phone + "\t," + resultSet.getString("value") + "\r\n";
                    try
                    {
                        os.write(downColumnValue.getBytes("GBK"));
                        count[0]++;
                    }
                    catch (IOException e)
                    {
                        LOG.error("", e);
                    }
                }
            }, batchSize);
        }
        catch (Exception e)
        {
            LOG.error("writeDataToStream error", e);
        }
        finally
        {
            if (os != null)
            {
                os.close();
            }
        }
        return count[0];
    }

    /**
     * @param request
     * @return
     */
    private ModelDomain generateModelDomainByRequest(HttpServletRequest request)
    {
        ModelDomain modelDomain = new ModelDomain();
        Integer segmentId = StringUtils.isEmpty(request.getParameter("id")) ? null : Integer.valueOf(request.getParameter("id"));
        String segmentName = request.getParameter("name");
        String catalogId = request.getParameter("catalogId");
        String catalogName = request.getParameter("catalogName");
        String remarks = request.getParameter("remarks");
        String specifiedRoleIds = request.getParameter("specifiedRoleIds");
        Integer isNeedSendNotifySms = Integer.valueOf(String.valueOf(request.getParameter("isNeedSendNotifySms")));

        modelDomain.setId(segmentId);
        if (StringUtils.isNotEmpty(segmentName))
        {
            modelDomain.setName(segmentName);
        }
        if (StringUtils.isNotEmpty(catalogId))
        {
            modelDomain.setCatalogId(catalogId);
        }
        if (StringUtils.isNotEmpty(catalogName))
        {
            modelDomain.setCatalogName(catalogName);
        }
        if (StringUtils.isNotEmpty(remarks))
        {
            modelDomain.setRemarks(remarks);
        }
        if (StringUtils.isNotEmpty(specifiedRoleIds))
        {
            modelDomain.setSpecifiedRoleIds(specifiedRoleIds);
        }
        modelDomain.setIsNeedSendNotifySms(isNeedSendNotifySms);
        modelDomain.setCreateType(MarketSystemElementCreateEnum.LOCAL_IMPORT.getValue());
        return modelDomain;
    }

    /**
     * @param modelDomain
     * @return
     */
    public Boolean isModelExisted(ModelDomain modelDomain)
    {
        ModelDomain oldModelDomain = modelDao.queryModelByName(modelDomain.getName());
        if (oldModelDomain == null)
        {
            return false;
        }
        return !oldModelDomain.getId().equals(modelDomain.getId());
    }

    /**
     * 计算营销任务中的模型人数
     * @param modelIds
     * @return
     */
    public int calcModelsUserCount(String modelIds)
    {
        return modelDao.calcModelsUserCount(modelIds);
    }

    /**
     * 查询我能查看的所有模型
     * @param modelId
     * @param loginUser
     * @return
     */
    public List<Map<String,Object>> queryModelsUnderMe(Integer modelId, UserDomain loginUser)
    {
        return modelDao.queryModelsUnderMe(modelId, loginUser);
    }
}
