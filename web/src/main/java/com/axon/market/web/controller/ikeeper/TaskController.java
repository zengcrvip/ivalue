package com.axon.market.web.controller.ikeeper;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.ikeeper.TaskShowDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;

import com.axon.market.core.service.icommon.FileUploadService;
import com.axon.market.core.service.ikeeper.KeeperTaskService;

import org.apache.commons.lang.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

/**
 * Created by wtt on 2017/8/7.
 */
@Controller("taskController")
public class TaskController
{
    private static Logger LOG = Logger.getLogger(TaskController.class.getName());

    @Autowired
    @Qualifier("keeperTaskService")
    private KeeperTaskService keeperTaskService;

    @Autowired
    @Qualifier("fileUploadService")
    private FileUploadService fileUploadService;

    private AxonEncryptUtil axonEncryptUtil = AxonEncryptUtil.getInstance();


    /**
     * 查询掌柜业务类型
     *
     * @return
     */
    @RequestMapping("queryKeeperTaskType.view")
    @ResponseBody
    public List<Map<String, Object>> queryKeeperTaskType()
    {
        return keeperTaskService.queryKeeperTaskType();
    }

    /**
     * 查询掌柜福利类型
     * @return
     */
    @RequestMapping("queryKeeperWelfareType.view")
    @ResponseBody
    public List<Map<String, Object>> queryKeeperWelfareType()
    {
        return keeperTaskService.queryKeeperWelfareType();
    }


    /**
     * 查询掌柜策略类型
     *
     * @param param
     * @return
     */
    @RequestMapping("queryKeeperRuleByTypeId.view")
    @ResponseBody
    public List<Map<String, Object>> queryKeeperRuleByTypeId(@RequestBody Map<String, Object> param)
    {
        String str = String.valueOf(param.get("typeId"));
        if (StringUtils.isEmpty(str))
        {
            return new ArrayList<>();
        }
        try
        {
            Integer typeId = Integer.parseInt(str);
            return keeperTaskService.queryKeeperRuleByTypeId(typeId);

        }
        catch (NumberFormatException e)
        {
            LOG.error("queryKeeperRuleByTypeId", e);
            return new ArrayList<>();
        }
    }

    /**
     * 导入掌柜任务客群
     *
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("importKeeperTaskCustomerFile.view")
    @ResponseBody
    public Map<String, Object> importKeeperTaskCustomerFile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> fileInfo = new HashMap<String, Object>();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        if (userDomain.getAreaCode() == 99999 || userDomain.getKeeperUser() == null || userDomain.getKeeperUser().getIsCanManage() != 1)
        {
            result.put("retValue", -1);
            result.put("desc", "无导入权限");
            return result;
        }
        Long fileId = new Date().getTime();//文件ID
        try
        {
            File tempFile = fileUploadService.fileUpload(request, "KeeperTaskCustomer" + fileId);
            if (!tempFile.exists() || !tempFile.isFile())
            {
                result.put("retValue", -1);
                result.put("desc", "文件不存在");
                return result;
            }
            if (tempFile.length() == 0)
            {
                result.put("retValue", -1);
                result.put("desc", "文件为空");
                tempFile.delete();
                return result;
            }
            // 文件超过10M则不允许上传
            if (tempFile.length() >= 10485760)
            {
                result.put("retValue", -1);
                result.put("desc", "文件过大");
                tempFile.delete();
                return result;
            }
            fileInfo.put("fileId", fileId);
            fileInfo.put("fileName", tempFile.getName());
            fileInfo.put("fileSize", tempFile.length());
            fileInfo.put("taskType", "掌柜任务用户文件导入");
            fileInfo.put("createUser", userDomain == null ? "admin" : userDomain.getId());
            fileInfo.put("createDate", new Date());
            fileInfo.put("targetTable", "keeper_task_customer");
            //文件读取批量入库
            ServiceResult serviceResult = keeperTaskService.importFile(fileInfo, tempFile);
            result.put("retValue", serviceResult.getRetValue());
            result.put("desc", serviceResult.getDesc());
            result.put("fileId", fileId);
        }
        catch (Exception e)
        {
            LOG.error("importKeeperTaskCustomerFile error", e);
            result.put("retValue", "-1");
            result.put("desc", "文件导入失败");
        }
        return result;
    }

    /**
     * 保存掌柜任务客群
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "saveKeeperTaskCustomer.view")
    @ResponseBody
    public Map<String, Object> saveKeeperTaskCustomer(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return keeperTaskService.saveKeeperTaskCustomer(paras);
    }


    /**
     * 创建掌柜任务
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping("createKeeperTask.view")
    @ResponseBody
    public ServiceResult createKeeperTask(@RequestBody Map<String, Object> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        if (userDomain.getAreaCode() == 99999 || userDomain.getKeeperUser() == null || userDomain.getKeeperUser().getIsCanManage() != 1)
        {
            return new ServiceResult(-1, "无操作权限");
        }
        Integer userId = userDomain.getId();
        Map<String, Map<String, Object>> paramMaps = new HashMap<String, Map<String, Object>>();
        Map<String, Object> keeperTaskMap = new HashMap<String, Object>();
        Map<String, Object> keeperChannelMap = new HashMap<String, Object>();
        Map<String, Object> keeperAuditMap = new HashMap<String, Object>();
        Map<String, Object> keeperRuleMap = new HashMap<String, Object>();
        // 任务主体信息参数组装
        keeperTaskMap.put("typeId", param.get("typeId"));
        keeperTaskMap.put("taskName", param.get("taskName"));
        keeperTaskMap.put("effDate", param.get("effDate"));
        keeperTaskMap.put("expDate", param.get("expDate"));
        keeperTaskMap.put("createUserId", userId);
        keeperTaskMap.put("taskAreaCode", userDomain.getAreaCode());
        keeperTaskMap.put("taskOrgIds", param.get("orgIds"));
        keeperTaskMap.put("taskOrgNames", param.get("orgNames"));
        keeperTaskMap.put("comments", param.get("comments"));
        keeperTaskMap.put("keeperCustomerFileId", param.get("filedId"));
        keeperTaskMap.put("welfareProductIds", param.get("welfareProductIds"));
        keeperTaskMap.put("taskId", "");// test
        paramMaps.put("keeperTaskMap", keeperTaskMap);// 存放任务主表参数
        // 任务渠道信息参数组装
//        Integer channelType = Integer.parseInt(String.valueOf(param.get("channelType")));
        keeperChannelMap.put("channelType", param.get("channelType"));
        keeperChannelMap.put("smsContent", param.get("smsContent"));
        keeperChannelMap.put("outbandContent", param.get("outbandContent"));
        keeperChannelMap.put("outbandPhone", param.get("outbandPhone"));
        keeperChannelMap.put("outbandCount", param.get("outbandCount"));
        paramMaps.put("keeperChannelMap", keeperChannelMap);// 存放渠道信息参数
        // 任务审批人信息参数组装
        keeperAuditMap.put("auditUserId", param.get("auditUserId"));
        paramMaps.put("keeperAuditMap", keeperAuditMap);// 存放审批信息参数
        // 任务规则信息组装
        keeperRuleMap.put("remindRuleId", param.get("remindRuleId"));
        String failureRuleId = String.valueOf(param.get("failureRuleId"));
        if (!StringUtils.isEmpty(failureRuleId))
        {
            keeperRuleMap.put("failureRuleId", param.get("failureRuleId"));
        }
        paramMaps.put("keeperRuleMap", keeperRuleMap);
        return keeperTaskService.createKeeperTask(paramMaps);
    }

    /**
     * 更新任务信息
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping("updateKeeperTask.view")
    @ResponseBody
    public ServiceResult updateKeeperTask(@RequestBody Map<String, Object> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        if (userDomain.getAreaCode() == 99999 || userDomain.getKeeperUser() == null || userDomain.getKeeperUser().getIsCanManage() != 1)
        {
            return new ServiceResult(-1, "无操作权限");
        }
        Integer userId = userDomain.getId();
        Map<String, Map<String, Object>> paramMaps = new HashMap<String, Map<String, Object>>();
        Map<String, Object> keeperTaskMap = new HashMap<String, Object>();
        Map<String, Object> keeperChannelMap = new HashMap<String, Object>();
        Map<String, Object> keeperAuditMap = new HashMap<String, Object>();
        Map<String, Object> keeperRuleMap = new HashMap<String, Object>();
        // 任务主体信息参数组装
        keeperTaskMap.put("taskId", param.get("taskId"));
        keeperTaskMap.put("typeId", param.get("typeId"));
        keeperTaskMap.put("taskName", param.get("taskName"));
        keeperTaskMap.put("effDate", param.get("effDate"));
        keeperTaskMap.put("expDate", param.get("expDate"));
        keeperTaskMap.put("updateUserId", userId);
        keeperTaskMap.put("taskAreaCode", userDomain.getAreaCode());
        keeperTaskMap.put("taskOrgIds", param.get("orgIds"));
        keeperTaskMap.put("taskOrgNames", param.get("orgNames"));
        keeperTaskMap.put("comments", param.get("comments"));
        keeperTaskMap.put("keeperCustomerFileId", param.get("filedId"));
        keeperTaskMap.put("welfareProductIds", param.get("welfareProductIds"));
        paramMaps.put("keeperTaskMap", keeperTaskMap);// 存放任务主表参数
        // 任务渠道信息参数组装
        keeperChannelMap.put("channelType", param.get("channelType"));
        keeperChannelMap.put("smsContent", param.get("smsContent"));
        keeperChannelMap.put("outbandContent", param.get("outbandContent"));
        keeperChannelMap.put("outbandPhone", param.get("outbandPhone"));
        keeperChannelMap.put("outbandCount", param.get("outbandCount"));
        paramMaps.put("keeperChannelMap", keeperChannelMap);// 存放渠道信息参数
        // 任务审批人信息参数组装
        keeperAuditMap.put("auditUserId", param.get("auditUserId"));
        paramMaps.put("keeperAuditMap", keeperAuditMap);// 存放审批信息参数
        // 任务规则信息组装
        keeperRuleMap.put("remindRuleId", param.get("remindRuleId"));
        String failureRuleId = String.valueOf(param.get("failureRuleId"));
        if (!StringUtils.isEmpty(failureRuleId))
        {
            keeperRuleMap.put("failureRuleId", param.get("failureRuleId"));
        }
        paramMaps.put("keeperRuleMap", keeperRuleMap);
        return keeperTaskService.updateKeeperTask(paramMaps);
    }


    /**
     * 查询外呼号码
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "queryOutbandPhone.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> queryOutbandPhone(HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        //取到号码并解密
        String userTelephone = axonEncryptUtil.decrypt(userDomain.getTelephone());
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> userPhone = new HashMap<String, Object>();
        userPhone.put("phone", userTelephone.substring(2));
        Map<String, Object> outbandPhone = new HashMap<String, Object>();
        outbandPhone.put("phone", "10017");
        resultList.add(outbandPhone);
        resultList.add(userPhone);
        return resultList;
    }

    /**
     * 分页查询任务
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping("queryKeeperTaskByPage.view")
    @ResponseBody
    public Table<Map<String, Object>> queryKeeperTaskByPage(@RequestParam Map<String, Object> param, HttpSession session)
    {
        String taskNameStr = String.valueOf(param.get("taskName"));
        String taskName = SearchConditionUtil.optimizeCondition(taskNameStr);
        param.put("taskName", taskName);
        return keeperTaskService.queryKeeperTaskByPage(param);
    }

    /**
     * 根据id查询掌柜任务
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping("queryKeeperTaskById.view")
    @ResponseBody
    public TaskShowDomain queryKeeperTaskById(@RequestBody Map<String, Object> param, HttpSession session)
    {
        String str = String.valueOf(param.get("taskId"));
        if (StringUtils.isEmpty(str))
        {
            return new TaskShowDomain();
        }
        return keeperTaskService.queryKeeperTaskById(Integer.parseInt(str));
    }

    /**
     * 删除任务
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "deleteKeeperTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteKeeperTask(@RequestBody Map<String, Object> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        if (userDomain.getAreaCode() == 99999 || userDomain.getKeeperUser() == null || userDomain.getKeeperUser().getIsCanManage() != 1)
        {
            return new ServiceResult(-1, "无操作权限");
        }
        String str = String.valueOf(param.get("taskId"));
        if (StringUtils.isEmpty(str))
        {
            return new ServiceResult(-1, "数据传输异常");
        }
        return keeperTaskService.deleteKeeperTask(Integer.parseInt(str));
    }


    /**
     * 查询任务名是否已经存在
     *
     * @param param
     * @return
     */
    @RequestMapping("queryTaskNameIsExist.view")
    @ResponseBody
    public ServiceResult queryTaskNameIsExist(@RequestBody Map<String, Object> param)
    {
        String taskName = String.valueOf(param.get("taskName"));
        if (StringUtils.isEmpty(taskName))
        {
            return new ServiceResult(-1, "任务名称不能为空");
        }
        return keeperTaskService.queryTaskNameIsExist(taskName);
    }

    /**
     * 查询需要我审核的任务
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping("queryNeedMeAuditKeeperTask.view")
    @ResponseBody
    public Table<Map<String, Object>> queryNeedMeAuditKeeperTask(@RequestParam Map<String, Object> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String offset = String.valueOf(param.get("start"));
        String limit = String.valueOf(param.get("length"));
        String taskNameStr = String.valueOf(param.get("taskName"));
        String taskName = SearchConditionUtil.optimizeCondition(taskNameStr);
        if (StringUtils.isEmpty(limit) || StringUtils.isEmpty(offset))
        {
            return new Table<>();
        }
        return keeperTaskService.queryNeedMeAuditKeeperTask(taskName, userDomain.getId(), Integer.parseInt(limit), Integer.parseInt(offset));
    }

    /**
     * 审核任务
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping("auditKeeperTask.view")
    @ResponseBody
    public ServiceResult auditKeeperTask(@RequestBody Map<String, Object> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return keeperTaskService.auditKeeperTask(param, userDomain.getId());
    }

    /**
     * 查询审核失败原因
     *
     * @param param
     * @return
     */
    @RequestMapping("queryAuditFailureReason.view")
    @ResponseBody
    public Map<String, String> queryAuditFailureReason(@RequestBody Map<String, Object> param)
    {
        return keeperTaskService.queryAuditFailureReason(param);
    }

    /**
     * 终止
     */
    @RequestMapping("terminateKeeperTask.view")
    @ResponseBody
    public ServiceResult terminateKeeperTask(@RequestBody Map<String, Object> param)
    {
        return keeperTaskService.terminateKeeperTask(param);
    }


}
