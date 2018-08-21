package com.axon.market.web.controller.ishop;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.domain.iresource.SmsContentDomain;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.domain.ishop.ShopTemporaryTaskDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import com.axon.market.common.util.excel.ExportUtils;
import com.axon.market.core.service.icommon.AreaService;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.core.service.ishop.ShopTemporaryTaskService;
import com.axon.market.dao.mapper.ishop.IShopTemporaryTaskMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 炒店任务管理Controller
 * Created by zengcr on 2017/2/14.
 */
@Controller("shopTaskController")
public class ShopTaskController
{
    /**
     * 导出EXECL数据字段
     */
    private static String[] fields = new String[]{"phone"};
    private static final Logger LOG = Logger.getLogger(ShopTaskController.class.getName());

    @Qualifier("shopTaskService")
    @Autowired
    private ShopTaskService shopTaskService;

    @Qualifier("shopTemporaryTaskService")
    @Autowired
    private ShopTemporaryTaskService shopTempTaskService;

    /**
     * 炒店任务分页展示
     *
     * @param paras   炒店ID，炒店名称，状态
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopTaskByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<ShopTaskDomain> queryShopTaskByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        parasMap.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        parasMap.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());

        Map<String, Object> result = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String) paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //炒店任务ID
        String shopTaskId = (String) (paras.get("shopTaskId"));
        //炒店任务名称
        String shopTaskName = SearchConditionUtil.optimizeCondition((String) (paras.get("shopTaskName"))).trim();
        //炒店任务状态
        String taskStatus = (String) (paras.get("shopTaskStatus"));
        //营业厅编码
        String shopTaskBaseCode = SearchConditionUtil.optimizeCondition((String) (paras.get("shopTaskBaseCode"))).trim();
        //营业厅名称
        String shopTaskBaseName = SearchConditionUtil.optimizeCondition((String) (paras.get("shopTaskBaseName"))).trim();
        // 当前时间
        String dateTime = SearchConditionUtil.optimizeCondition((String) (paras.get("dateTime"))).trim();

        Integer itemCounts = 0;

        parasMap.put("shopTaskId", shopTaskId);
        parasMap.put("shopTaskName", shopTaskName);
        parasMap.put("taskStatus", taskStatus);
        parasMap.put("shopTaskBaseCode", shopTaskBaseCode);
        parasMap.put("shopTaskBaseName", shopTaskBaseName);
        parasMap.put("dateTime", dateTime);
        parasMap.put("offset", curPageIndex);
        parasMap.put("limit", pageSize);
        List<ShopTaskDomain> shopTaskList = null;
        itemCounts = shopTaskService.queryShopTaskTotal(parasMap);
        shopTaskList = shopTaskService.queryShopTaskByPage(parasMap);

        result.put("itemCounts", itemCounts);
        result.put("items", shopTaskList);
        return new Table(shopTaskList, itemCounts);
    }

    /**
     * 炒店任务池分页展示
     *
     * @param paras   炒店ID，炒店名称
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopTaskExecute.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<ShopTaskDomain> queryShopTaskExecute(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        parasMap.put("userId", userDomain.getId());
        parasMap.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        parasMap.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());

        Map<String, Object> result = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf(String.valueOf(paras.get("start")));
        int pageSize = Integer.valueOf(String.valueOf(paras.get("length")));
        //炒店任务ID
        String shopTaskId = String.valueOf(paras.get("shopTaskId"));
        //炒店任务名称
        String shopTaskName = SearchConditionUtil.optimizeCondition((String) (paras.get("shopTaskName")));
        // 营业厅编码
        String shopTaskBaseCode = String.valueOf(paras.get("shopTaskBaseCode"));
        // 来源
        Integer taskType = Integer.valueOf(String.valueOf(paras.get("taskType")));
        // 业务类型
        String businessId = String.valueOf(paras.get("businessId"));
        Integer itemCounts = 0;

        parasMap.put("shopTaskId", shopTaskId);
        parasMap.put("shopTaskName", shopTaskName);
        parasMap.put("shopTaskBaseCode", shopTaskBaseCode);
        parasMap.put("taskType", taskType);
        parasMap.put("businessId", businessId);
        parasMap.put("offset", curPageIndex);
        parasMap.put("limit", pageSize);
        List<ShopTaskDomain> shopTaskList = null;
        if ("".equals(parasMap.get("businessCodes").toString()))
        {
            //非营业厅营业员
            itemCounts = shopTaskService.queryShopTaskExecuteAll(parasMap);
            shopTaskList = shopTaskService.queryShopTaskExecute(parasMap);
        }
        else
        {
            //营业厅营业员
            itemCounts = shopTaskService.queryShopTaskExecuteAllForClerk(parasMap);
            shopTaskList = shopTaskService.queryShopTaskExecuteForClerk(parasMap);
        }
        result.put("itemCounts", itemCounts);
        result.put("items", shopTaskList);
        return new Table(shopTaskList, itemCounts);
    }

    /**
     * 查询营销内容
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryContentByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryContentByPage(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int curPage = (Integer) (paras.get("curPage"));
        int countsPerPage = (Integer) (paras.get("countsPerPage"));
        String qryContentInfo = (String) (paras.get("qryContentInfo"));
        String qryKeyInfo = (String) (paras.get("qryKeyInfo"));
        Integer itemCounts = 0;

        List<SmsContentDomain> contentList = null;
        itemCounts = shopTaskService.querySmsContentsTotal(qryContentInfo, qryKeyInfo);
        contentList = shopTaskService.querySmsContentsByPage(qryContentInfo, qryKeyInfo, (curPage - 1) * countsPerPage, countsPerPage);

        result.put("itemCounts", itemCounts);
        result.put("items", contentList);
        return result;
    }

    /**
     * 根据登录用户和任务名称查询该用户是否创建过相同的任务
     *
     * @param paras
     * @param session
     * @return true：是，false：否
     */
    @RequestMapping(value = "validateTaskName", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> validateTaskName(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        parasMap.put("taskName", paras.get("taskName"));
        parasMap.put("userId", userDomain.getId());
        boolean isExists = shopTaskService.validateTaskName(parasMap);
        parasMap.put("isExists", isExists);
        return parasMap;

    }

    /**
     * 新建炒店任务
     *
     * @param shopTaskDomain
     * @param session
     * @return
     */
    @RequestMapping(value = "createShopTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult createShopTask(@RequestBody ShopTaskDomain shopTaskDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        shopTaskDomain.setCreateUser(String.valueOf(userDomain.getId()));
        Integer areaCode = userDomain.getAreaCode();
        String businessHallIds = userDomain.getBusinessHallIds();
        if (99999 == areaCode)
        {
            shopTaskDomain.setTaskType(1); //省级任务
        }
        else if (null == businessHallIds || "".equals(businessHallIds))
        {
            shopTaskDomain.setTaskType(2); //地市级任务
        }
        else
        {
            shopTaskDomain.setTaskType(3); //营业厅级任务
        }
        //判断该用户是否需要审批，如果不需要审批，直接设置状态为审批通过
        if (StringUtils.isEmpty(userDomain.getMarketingAuditUsers()))
        {
            shopTaskDomain.setStatus(ShopTaskStatusEnum.TASK_READY.getValue());
        }
        else
        {
            shopTaskDomain.setStatus(ShopTaskStatusEnum.TASK_AUDITING.getValue());
        }

        ServiceResult result = new ServiceResult();
        try {
            result = shopTaskService.insertShopTask(shopTaskDomain, userDomain);
        } catch (Exception e) {
            LOG.error("shop task insert error", e);
            result.setRetValue(-1);
            result.setDesc("炒店任务新增操作失败");
        }
        return  result;
    }

    /**
     * 根据ID查询炒店任务
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopTaskById.view")
    @ResponseBody
    public Map<String, Object> queryShopTaskById(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Integer taskId = Integer.valueOf(String.valueOf(paras.get("id")));
        String taskClassifyId = String.valueOf(paras.get("taskClassifyId"));
        if (taskClassifyId != null && ("2".equals(taskClassifyId) || "3".equals(taskClassifyId) || "4".equals(taskClassifyId)))
        {
            ShopTemporaryTaskDomain shopTaskDomain = null;
            shopTaskDomain = shopTempTaskService.queryShopTempTaskById(taskId);
            result.put("shopTaskDomain", shopTaskDomain);
        }
        else
        {
            ShopTaskDomain shopTaskDomain = shopTaskService.queryShopTaskById(taskId);
            result.put("shopTaskDomain", shopTaskDomain);
        }

        return result;
    }

    @RequestMapping(value = "queryShopMsgDesc.view")
    @ResponseBody
    public Map<String, Object> queryShopMsgDesc(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> parasMap = new HashMap<String, Object>();
        parasMap.put("baseId", paras.get("baseId"));
        String shopPhone = shopTaskService.queryShopMsgDesc(parasMap);
        result.put("shopPhone", shopPhone);
        return result;
    }

    @RequestMapping(value = "queryShopPhone.view")
    @ResponseBody
    public Map<String, Object> queryShopPhone(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> parasMap = new HashMap<String, Object>();
        parasMap.put("baseId", paras.get("baseId"));
        String shopPhone = shopTaskService.queryShopPhone(parasMap);
        result.put("shopPhone", shopPhone);
        return result;
    }

    /**
     * 炒店任务修改
     *
     * @param taskDomain
     * @param session
     * @return
     */
    @RequestMapping(value = "updateShopTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateShopTask(@RequestBody ShopTaskDomain taskDomain, HttpSession session)
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);
        if (StringUtils.isBlank(loginUser.getMarketingAuditUsers()))
        {
            taskDomain.setStatus(ShopTaskStatusEnum.TASK_READY.getValue());
        }
        else
        {
            taskDomain.setStatus(ShopTaskStatusEnum.TASK_AUDITING.getValue());
        }
        return shopTaskService.updateShopTask(taskDomain, loginUser);
    }

    /**
     * 炒店任务删除
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "deleteShopTaskById.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteShopTaskById(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Integer taskId = Integer.valueOf(String.valueOf(paras.get("id")));
        return shopTaskService.deleteShopTaskById(taskId);
    }

    /**
     * 查询营销任务接入号
     *
     * @return
     */
    @RequestMapping(value = "querFixedAccessNum.view")
    @ResponseBody
    public List<Map<String, String>> querFixedAccessNum(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        parasMap.put("areaCode", paras.get("areaCode"));
        parasMap.put("type", paras.get("actionType"));
        List<Map<String, String>> result = shopTaskService.querFixedAccessNum(parasMap);
        return result;
    }

    /**
     * 查询炒店对应的业务类型
     *
     * @return
     */
    @RequestMapping(value = "queryShopBusinessType.view")
    @ResponseBody
    public List<Map<String, String>> queryShopBusinessType(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        List<Map<String, String>> result = shopTaskService.queryShopBusinessType();
        return result;
    }


    @RequestMapping(value = "queryNeedMeAuditShopTasks.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> queryNeedMeAuditShopTasks(@RequestParam Map<String, Object> params, HttpSession session)
    {
        int userId = UserUtils.getLoginUser(session).getId();
        // 炒店任务名称
        String shopTaskName = SearchConditionUtil.optimizeCondition((String) (params.get("shopTaskName"))).trim();
        // 营业厅编码
        String shopTaskBaseCode = SearchConditionUtil.optimizeCondition((String) (params.get("shopTaskBaseCode"))).trim();
        // 营业厅名称
        String shopTaskBaseName = SearchConditionUtil.optimizeCondition((String) (params.get("shopTaskBaseName"))).trim();
        // 当前时间
        String dateTime = SearchConditionUtil.optimizeCondition((String) (params.get("dateTime"))).trim();
        //炒店任务类型
        String taskClassifyId = !"".equals(String.valueOf(params.get("taskClassifyId"))) ? String.valueOf(params.get("taskClassifyId")) : "";

        Map<String, String> paras = new HashMap<String, String>();
        paras.put("userId", String.valueOf(userId));
        paras.put("shopTaskName", shopTaskName);
        paras.put("shopTaskBaseCode", shopTaskBaseCode);
        paras.put("shopTaskBaseName", shopTaskBaseName);
        paras.put("dateTime", dateTime);
        paras.put("taskClassifyId", taskClassifyId);
        int curPageIndex = Integer.valueOf((String) params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));

        List<Map<String, Object>> modelDomainList = shopTaskService.queryNeedMeAuditShopTasks(paras);
        int itemCounts = modelDomainList.size();
        int startIndex = curPageIndex;
        int endIndex = startIndex + pageSize > itemCounts ? itemCounts : startIndex + pageSize;
        return new Table(modelDomainList.subList(startIndex, endIndex), itemCounts);
    }

    /**
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopTaskAuditReject.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryShopTaskAuditReject(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return shopTaskService.queryShopTaskAuditReject(paras);
    }

    @RequestMapping(value = "auditShopTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult auditShopTask(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        //营销任务id
        int id = Integer.parseInt(String.valueOf(paras.get("id")));
        //审核结果
        String operate = "against";//默认不通过
        if (Integer.parseInt((String) paras.get("operate")) == 0)
        {
            operate = "approve";//通过
        }
        //原因
        String reason = (String) (paras.get("reason"));
        return shopTaskService.submitShopTaskAudit(id, operate, reason, UserUtils.getLoginUser(session));
    }

    /**
     * 手动执行炒店任务
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "manualShopTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult manualShopTask(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String id = String.valueOf(paras.get("id"));
        String baseIds = String.valueOf(paras.get("baseIds"));
        String status = String.valueOf(paras.get("status"));
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("id", id);
        paraMap.put("baseIds", baseIds);
        paraMap.put("status", status);
        paraMap.put("userId", String.valueOf(userDomain.getId()));
        return shopTaskService.manualShopTask(paraMap);
    }

    /**
     * 营销任务执行的门店
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "getExecuteBaseByTaskId.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> getExecuteBaseByTaskId(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        String id = String.valueOf(paras.get("id"));
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> bases = null;
        bases = shopTaskService.getExecuteBaseByTaskId(id, userDomain.getId());
        return new Table(bases, null);
    }

    /**
     * 暂停执行炒店任务
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "stopShopTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult stopShopTask(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String id = String.valueOf(paras.get("id"));
        String baseId = String.valueOf(paras.get("baseId"));
        return shopTaskService.stopShopTask(Integer.valueOf(id), Integer.valueOf(baseId));
    }

    /**
     * 催单炒店任务
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "reminderItem.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult reminderItem(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String id = String.valueOf(paras.get("id"));
        String taskName = (String) paras.get("taskName");
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return shopTaskService.reminderItem(Integer.valueOf(id), taskName, userDomain);
    }

    /**
     * 终止任务
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "pauseItem.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult pauseItem(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String id = String.valueOf(paras.get("id"));
        String taskName = (String) paras.get("taskName");
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return shopTaskService.pauseItem(Integer.valueOf(id), taskName, userDomain);
    }

    /**
     * 批量导入指定用户文件
     *
     * @param request
     * @return
     */
    @RequestMapping("importAppointFile.view")
    @ResponseBody
    public Map<String, Object> importAppointFile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile file = multipartRequest.getFile("file");
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Map<String, Object> fileInfo = new HashMap<String, Object>();
        Long fileId = new Date().getTime();
        fileInfo.put("fileId", fileId);
        fileInfo.put("fileName", file.getOriginalFilename());
        fileInfo.put("fileSize", file.getSize());
        fileInfo.put("taskType", "炒店任务指定用户文件导入");
        fileInfo.put("createUser", userDomain == null ? "admin" : userDomain.getId());
        fileInfo.put("createDate", new Date());
        fileInfo.put("targetTable", "shoptask_appointUsers");
        ServiceResult returnResult = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            returnResult = shopTaskService.storeFile(fileInfo, file.getInputStream(), "appointUser");
            result.put("retValue", returnResult.getRetValue());
            result.put("desc", returnResult.getDesc());
            result.put("fileId", fileId);
        }
        catch (IOException e)
        {
            LOG.error("shop task error importAppointFile", e);
            result.put("retValue", "-1");
            result.put("desc", "文件导入失败");
        }
        return result;
    }

    /**
     * 批量导入免打扰用户文件
     *
     * @param request
     * @return
     */
    @RequestMapping("importBlackFile.view")
    @ResponseBody
    public Map<String, Object> importBlackFile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile file = multipartRequest.getFile("file");
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Map<String, Object> fileInfo = new HashMap<String, Object>();
        Long fileId = new Date().getTime();
        fileInfo.put("fileId", fileId);
        fileInfo.put("fileName", file.getOriginalFilename());
        fileInfo.put("fileSize", file.getSize());
        fileInfo.put("taskType", "炒店任务免打扰用户文件导入");
        fileInfo.put("createUser", userDomain == null ? "admin" : userDomain.getId());
        fileInfo.put("createDate", new Date());
        fileInfo.put("targetTable", "shoptask_blackUsers");
        ServiceResult returnResult = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            returnResult = shopTaskService.storeFile(fileInfo, file.getInputStream(), "blackUser");
            result.put("retValue", returnResult.getRetValue());
            result.put("desc", returnResult.getDesc());
            result.put("fileId", fileId);
        }
        catch (IOException e)
        {
            LOG.error("shop task error importBlackFile", e);
            result.put("retValue", "-1");
            result.put("desc", "导入文件失败");
        }
        return result;
    }

    /**
     * 批量导入用户号码列表展示
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopTaskPhoneImport.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryShopTaskPhoneImport(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int curPage = (Integer) (paras.get("curPage"));
        int countsPerPage = (Integer) (paras.get("countsPerPage"));
        Long fileId = (Long) paras.get("fileId");
        Integer itemCounts = 0;

        List<Map<String, Object>> shopTaskPhoneList = null;
        itemCounts = shopTaskService.queryShopTaskPhoneImportTotal(fileId);
        shopTaskPhoneList = shopTaskService.queryShopTaskPhoneImport((curPage - 1) * countsPerPage, countsPerPage, fileId);
        result.put("itemCounts", itemCounts);
        result.put("items", shopTaskPhoneList);
        return result;
    }

    /**
     * 临时保存导入指定用户数据
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "saveAppointUsersImport.view")
    @ResponseBody
    public Map<String, Object> saveAppointUsersImport(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return shopTaskService.saveAppointUsersImport(paras);
    }

    /**
     * 临时保存导入免打扰用户数据
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "saveBlackUsersImport.view")
    @ResponseBody
    public Map<String, Object> saveBlackUsersImport(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return shopTaskService.saveBlackUsersImport(paras);
    }

    /**
     * 根据员工工号查询历史导入文件
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryHistoryFileById")
    @ResponseBody
    public Map<String, Object> queryHistoryFileById(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        paras.put("userId", userDomain.getId());
        List<Map<String, Object>> list = shopTaskService.queryHistoryFileById(paras);
        result.put("data", list);
        return result;
    }

    /**
     * 查询炒店常驻用户数
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopPerNum.view")
    @ResponseBody
    public Map<String, Object> queryShopPerNum(@RequestBody Map<String, String> paras, HttpSession session)
    {
        Integer num = 0;
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            num = shopTaskService.queryShopPerNum(paras);
            result.put("retValue", "0");
            result.put("desc", "OK");
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
            result.put("retValue", "-1");
            result.put("desc", e.getMessage());
        }
        result.put("num", num);
        return result;
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getShopTaskNumFileDown.view", method = RequestMethod.POST)
    public void getShopTaskNumFileDown(@RequestParam Map<String, Object> param, HttpServletRequest request,
                                       HttpServletResponse response, HttpSession session)
    {
        String fileId = (String) param.get("fileId");
        String fileName = (String) param.get("fileName");
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        datas = shopTaskService.queryShopTaskPhoneByFileId(fileId);
        String tableName = fileName.split("\\.")[0];
        if (tableName.contains(" "))
        {
            tableName = tableName.split(" ")[0];
        }
        List<ExcelRowEntity> excelDataList = getExcelData(datas);
        ExportUtils.getInstance().exportData(tableName, excelDataList, request, response);
    }

    /**
     * 炒店任务配置 导出excel
     *
     * @param paras
     * @param request
     * @param response
     * @param session
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "exportShopTask.view", method = RequestMethod.POST)
    public void exportShopTask(@RequestParam Map<String, Object> paras, HttpServletRequest request,
                               HttpServletResponse response, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        parasMap.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        parasMap.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());

        //炒店任务ID
        String shopTaskId = (String) (paras.get("shopTaskId"));
        //炒店任务名称
        String shopTaskName = SearchConditionUtil.optimizeCondition((String) (paras.get("shopTaskName"))).trim();
        //炒店任务状态
        String taskStatus = (String) (paras.get("shopTaskStatus"));
        //营业厅编码
        String shopTaskBaseCode = SearchConditionUtil.optimizeCondition((String) (paras.get("shopTaskBaseCode"))).trim();
        //营业厅名称
        String shopTaskBaseName = SearchConditionUtil.optimizeCondition((String) (paras.get("shopTaskBaseName"))).trim();
        // 当前时间
        String dateTime = SearchConditionUtil.optimizeCondition((String) (paras.get("dateTime"))).trim();

        parasMap.put("shopTaskId", shopTaskId);
        parasMap.put("shopTaskName", shopTaskName);
        parasMap.put("taskStatus", taskStatus);
        parasMap.put("shopTaskBaseCode", shopTaskBaseCode);
        parasMap.put("shopTaskBaseName", shopTaskBaseName);
        parasMap.put("dateTime", dateTime);

        String dateName = "".equals(dateTime) ? "全部" : dateTime;
        List<Map<String, Object>> shopTaskList = shopTaskService.exportShopTask(parasMap);
        String fileName = "炒店任务配置" + "-" + dateName + "-" + System.currentTimeMillis();
        List<ExcelRowEntity> excelDataList = getShopTaskExcelData(shopTaskList);
        ExportUtils.getInstance().exportData(fileName, excelDataList, request, response);
    }

    /**
     * 炒店任务审批 导出excel
     *
     * @param params
     * @param request
     * @param response
     * @param session
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = " exportAuditShop.view", method = RequestMethod.POST)
    public void exportAuditShop(@RequestParam Map<String, Object> params, HttpServletRequest request,
                                HttpServletResponse response, HttpSession session)
    {
        int userId = UserUtils.getLoginUser(session).getId();
        // 炒店任务名称
        String shopTaskName = SearchConditionUtil.optimizeCondition((String) (params.get("shopTaskName"))).trim();
        // 营业厅编码
        String shopTaskBaseCode = SearchConditionUtil.optimizeCondition((String) (params.get("shopTaskBaseCode"))).trim();
        // 营业厅名称
        String shopTaskBaseName = SearchConditionUtil.optimizeCondition((String) (params.get("shopTaskBaseName"))).trim();
        // 当前时间
        String dateTime = SearchConditionUtil.optimizeCondition((String) (params.get("dateTime"))).trim();

        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("userId", String.valueOf(userId));
        paras.put("shopTaskName", shopTaskName);
        paras.put("shopTaskBaseCode", shopTaskBaseCode);
        paras.put("shopTaskBaseName", shopTaskBaseName);
        paras.put("dateTime", dateTime);

        String dateName = "".equals(dateTime) ? "全部" : dateTime;
        List<Map<String, Object>> modelDomainList = shopTaskService.exportAuditShopTask(paras);
        String fileName = "炒店任务审批" + "-" + dateName + "-" + System.currentTimeMillis();
        List<ExcelRowEntity> excelDataList = getShopTaskExcelData(modelDomainList);
        ExportUtils.getInstance().exportData(fileName, excelDataList, request, response);
    }

    /**
     * 私有方法，生成execl表格对象
     *
     * @param dataList 源数据
     * @return
     */
    private List<ExcelRowEntity> getExcelData(List<Map<String, Object>> dataList)
    {
        List<ExcelRowEntity> result = new ArrayList<ExcelRowEntity>();

        // 表头处理-----------------------------------------------
        ExcelRowEntity header = new ExcelRowEntity();
        result.add(header);
        header.setRowType(2);
        List<ExcelCellEntity> cellEntityList1 = new ArrayList<ExcelCellEntity>();
        header.setCellEntityList(cellEntityList1);
        cellEntityList1.add(new ExcelCellEntity(1, 1, "号码"));

        // ----------------数据行--------------------------------------------
        if (null != dataList && dataList.size() > 0)
        {
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> rowData = dataList.get(i);
                ExcelRowEntity excelRow = new ExcelRowEntity();
                List<ExcelCellEntity> cellEntityList = new ArrayList<ExcelCellEntity>();
                excelRow.setCellEntityList(cellEntityList);
                excelRow.setRowType(-1);
                for (int j = 0; j < fields.length; j++)
                {
                    String value = "";
                    if (null != rowData.get(fields[j]))
                    {
                        value = "" + rowData.get(fields[j]);
                    }
                    cellEntityList.add(new ExcelCellEntity(1, 1, value));
                }
                result.add(excelRow);
            }
        }

        return result;
    }

    private List<ExcelRowEntity> getShopTaskExcelData(List<Map<String, Object>> dataList)
    {
        String[] shopTaskFields = new String[]{"taskName", "taskType", "businessName", "startTime", "stopTime", "createTimeStr", "marketUser", "status", "shopBaseName", "baseCode", "smsContent"};
        List<ExcelRowEntity> result = new ArrayList<ExcelRowEntity>();

        // 表头处理-----------------------------------------------
        ExcelRowEntity header = new ExcelRowEntity();
        result.add(header);
        header.setRowType(2);
        List<ExcelCellEntity> cellEntityList1 = new ArrayList<ExcelCellEntity>();
        header.setCellEntityList(cellEntityList1);
        cellEntityList1.add(new ExcelCellEntity(1, 1, "任务名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "来源"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "业务类型"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "开始时间"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "结束时间"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "配置时间"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "目标用户"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "状态"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "营业厅名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "营业厅编码"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "短信内容"));

        // ----------------数据行--------------------------------------------
        if (null != dataList && dataList.size() > 0)
        {
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> rowData = dataList.get(i);
                ExcelRowEntity excelRow = new ExcelRowEntity();
                List<ExcelCellEntity> cellEntityList = new ArrayList<ExcelCellEntity>();
                excelRow.setCellEntityList(cellEntityList);
                excelRow.setRowType(-1);
                for (int j = 0; j < shopTaskFields.length; j++)
                {
                    String value = "";
                    if (null != rowData.get(shopTaskFields[j]))
                    {
                        value = "" + rowData.get(shopTaskFields[j]);
                    }
                    cellEntityList.add(new ExcelCellEntity(1, 1, value));
                }
                result.add(excelRow);
            }
        }

        return result;
    }
}
