package com.axon.market.web.controller.iflow;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.domain.iflow.OldCustomerDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.icommon.FileUploadService;
import com.axon.market.core.service.iflow.OldCustomerService;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
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
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by wangtt on 2017/7/24.
 */

@Controller("olderPreferentialController")
public class OldCustomerController
{

    private static Logger LOG = Logger.getLogger(OldCustomerController.class.getName());

    @Autowired
    @Qualifier("oldCustomerService")
    OldCustomerService oldCustomerService;

    @Autowired
    @Qualifier("fileUploadService")
    private FileUploadService fileUploadService;


    //最多导入营业厅数量
    final int MaxConfBaseInfoNum = 1000;

    /**
     * 导入指定用户(停用)
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("importOldCustomerAppointFile.view")
    @ResponseBody
    public Map<String,Object> importAppointFile(HttpServletRequest request, HttpServletResponse response, HttpSession session){
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
        fileInfo.put("taskType", "老客营销指定用户文件导入");
        fileInfo.put("createUser", userDomain == null ? "admin" : userDomain.getId());
        fileInfo.put("createDate", new Date());
        fileInfo.put("targetTable", "old_customer_appointUsers");
        ServiceResult returnResult = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            returnResult = oldCustomerService.storeFile(fileInfo, file.getInputStream(), "appointUser");
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
     * 导入指定用户（新）
     * @return
     */
    @RequestMapping("importTheOldCustomerAppointFile.view")
    @ResponseBody
    public Map<String,Object> importTheAppointFile(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Map<String,Object> result = new HashMap<String,Object>();
        Map<String, Object> fileInfo = new HashMap<String, Object>();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Long fileId = new Date().getTime();
        try
        {
            LOG.info("");
            File tempFile = fileUploadService.fileUpload(request, "oldCustomerAppointUsers"+fileId);
            if(!tempFile.exists() || !tempFile.isFile()){
                result.put("retValue", -1);
                result.put("desc", "文件不存在");
                return result;
            }
            if(tempFile.length() == 0){
                result.put("retValue", -1);
                result.put("desc", "文件为空");
                tempFile.delete();
                return result;
            }
            // 文件超过10M则不允许上传
            if(tempFile.length() >= 10485760){
                result.put("retValue", -1);
                result.put("desc", "文件过大");
                tempFile.delete();
                return result;
            }
            fileInfo.put("fileId", fileId);
            fileInfo.put("fileName", "oldCustomerAppointUsers"+fileId);
            fileInfo.put("fileSize", tempFile.length());
            fileInfo.put("taskType", "老客营销指定用户文件导入");
            fileInfo.put("createUser", userDomain == null ? "admin" : userDomain.getId());
            fileInfo.put("createDate", new Date());
            fileInfo.put("targetTable", "old_customer_appointUsers");
            //文件读取批量入库
            ServiceResult serviceResult = oldCustomerService.importFile(fileInfo, tempFile);
            result.put("retValue", serviceResult.getRetValue());
            result.put("desc",serviceResult.getDesc());
            result.put("fileId", fileId);
        }
        catch (Exception e)
        {
            LOG.error("importTheOldCustomerAppointFile error",e);
            result.put("retValue", "-1");
            result.put("desc", "文件导入失败");
        }
        return result;
    }




    /**
     * 导入免打扰用户文件
     *
     * @param request
     * @return
     */
    @RequestMapping("importOldCustomerBlackFile.view")
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
        fileInfo.put("taskType", "老客营销免打扰用户文件导入");
        fileInfo.put("createUser", userDomain == null ? "admin" : userDomain.getId());
        fileInfo.put("createDate", new Date());
        fileInfo.put("targetTable", "old_customer_blackUsers");
        ServiceResult returnResult = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            returnResult = oldCustomerService.storeFile(fileInfo, file.getInputStream(), "blackUser");
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
     * 导入线下营业厅
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping("importAppointBaseInfo.view")
    @ResponseBody
    public Map<String,Object> importBaseInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session){
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
        fileInfo.put("taskType", "老客营销线下营业厅文件导入");
        fileInfo.put("createUser", userDomain == null ? "admin" : userDomain.getId());
        fileInfo.put("createDate", new Date());
        fileInfo.put("targetTable", "preferential_task_2_base");
        ServiceResult returnResult = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            returnResult = oldCustomerService.storeConfBaseInfoFile(fileInfo, file.getInputStream(), MaxConfBaseInfoNum);
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
     * 临时保存导入指定用户数据
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "saveOldCustomerAppointUsers.view")
    @ResponseBody
    public Map<String, Object> saveAppointUsersImport(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return oldCustomerService.saveAppointUsersImport(paras);
    }

    /**
     * 临时保存导入免打扰用户数据
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "saveOldCustomerBlackUsersImport.view")
    @ResponseBody
    public Map<String, Object> saveBlackUsersImport(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return oldCustomerService.saveBlackUsersImport(paras);
    }


    /**
     * 临时保存老客营销指定营业厅
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping("saveOldCustomerBaseInfoImport.view")
    @ResponseBody
    public Map<String, Object> saveBaseInfoImport(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return oldCustomerService.saveBaseInfoImport(paras);
    }

    /**
     * 新增老客优惠活动任务
     * @param oldCustomerDomain
     * @param session
     * @return
     */
    @RequestMapping(value = "createOldCustomerTask.view",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult createOldCustomerPreferentialActivity(@RequestBody OldCustomerDomain oldCustomerDomain,HttpSession session){
        ServiceResult result = new ServiceResult();
        UserDomain user = UserUtils.getLoginUser(session);
        oldCustomerDomain.setCreateUserId(user.getId());
        //查询老用户任务审批人
        Map<String,Object> map = oldCustomerService.queryAuditStr(user.getId());
        if(map == null || map.get("userId") == null){
            return new ServiceResult(-1,"抱歉，你没有老用户专享的操作权限");
        }
        /**
         *  sms:自建任务 jxhsms:精细化群发任务
         */
//        oldCustomerDomain.setTaskSource("sms");
        //根据创建者是否有审核人判断是否需要审核
        //扩展：从old_customer_user中取
        if (StringUtils.isEmpty(String.valueOf(map.get("oldCustomerAuditUsers")))){
            oldCustomerDomain.setStatus(ShopTaskStatusEnum.TASK_READY.getValue());
        }else{
            oldCustomerDomain.setStatus(ShopTaskStatusEnum.TASK_AUDITING.getValue());
        }
        try
        {
            result  =  oldCustomerService.insertOldCustomerPreferentialActivity(oldCustomerDomain, user);
        }
        catch (Exception e)
        {
            LOG.error("old customer task insert error", e);
            result.setRetValue(-1);
            result.setDesc("老用户营销活动新增操作失败");
        }
        return result;
    }

    /**
     * 查询所有炒店类型的接口
     * @return
     */
    @RequestMapping(value = "queryLocationType.view",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> queryLocationType(){
        return oldCustomerService.queryLocationType();
    }

    /**
     * 分页查询老用户优惠活动
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "queryOldCustomerByPage.view",method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String,Object>> queryOldCustomerByPage(@RequestParam Map<String,Object> param,HttpSession session){
        UserDomain userDomain = UserUtils.getLoginUser(session);
        param.put("userAreaId", userDomain.getAreaId());
        return oldCustomerService.queryOldCustomerByPage(param);
    }


    /**
     * 分页查询所有老用户优惠活动
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "queryAllOldCustomerByPage.view",method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String,Object>> queryAllOldCustomerByPage(@RequestParam Map<String,Object> param,HttpSession session){
        UserDomain userDomain = UserUtils.getLoginUser(session);
        param.put("userAreaId", userDomain.getAreaId());
        return oldCustomerService.queryAllOldCustomerByPage(param);
    }





    /**
     * 查询老用户优惠活动细节
     * @param param
     * @return
     */
    @RequestMapping("previewOldCustomer.view")
    @ResponseBody
    public Map<String,Object> previewOldCustomer(@RequestBody Map<String,Object> param){
        String taskId = String.valueOf(param.get("id"));
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(taskId)){
            map.put("retValue", -1);
            map.put("desc","任务信息异常，请联系管理员");
            return map;
        }
        OldCustomerDomain oldCustomerDomain = oldCustomerService.previewOldCustomer(Integer.parseInt(taskId));
        if(oldCustomerDomain == null){
            map.put("retValue", -1);
            map.put("desc","任务已经失效，请刷新页面");
            return map;
        }else{
            map.put("domain",oldCustomerDomain);
            map.put("retValue", 0);
            map.put("desc","success");
            return map;
        }
    }

    /**
     * 更新老用户优惠活动明细
     * @param oldCustomerDomain
     * @param session
     * @return
     */
    @RequestMapping(value = "updateOldCustomer.view",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateOldCustomer(@RequestBody OldCustomerDomain oldCustomerDomain,HttpSession session){
        UserDomain loginUser = UserUtils.getLoginUser(session);
        Map<String,Object> map = oldCustomerService.queryAuditStr(loginUser.getId());
        if (org.apache.commons.lang.StringUtils.isEmpty(String.valueOf(map.get("oldCustomerAuditUsers")))){
            oldCustomerDomain.setStatus(ShopTaskStatusEnum.TASK_READY.getValue());
        }else{
            oldCustomerDomain.setStatus(ShopTaskStatusEnum.TASK_AUDITING.getValue());
        }
        try
        {
            return oldCustomerService.updateOldCustomer(oldCustomerDomain,loginUser.getId());
        }
        catch (Exception e)
        {
            LOG.error("updateOldCustomer error",e);
            return new ServiceResult(-1,"更新失败");
        }
    }


    /**
     * 查询需要我审核的老用户优惠活动
     * @param param
     * @param session
     * @return
     */
    @RequestMapping("queryNeedMeAuditOldCustomer.view")
    @ResponseBody
    public Table<Map<String,Object>> queryNeedMeAuditOldCustomer(@RequestParam Map<String,Object> param,HttpSession session){
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        int userId = UserUtils.getLoginUser(session).getId();
        String taskName =  SearchConditionUtil.optimizeCondition(String.valueOf(param.get("taskName")));
        Integer limit = Integer.parseInt(String.valueOf(param.get("length")));
        Integer offset = Integer.parseInt(String.valueOf(param.get("start")));
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("taskName",taskName);
        paraMap.put("userId",userId);
        try
        {
            resultList =  oldCustomerService.queryNeedMeAuditOldCustomer(paraMap);
            int count = resultList.size();
            int startIndex = offset;
            int endIndex = startIndex + limit > count ? count : startIndex + limit;
            return new Table<Map<String, Object>>(resultList.subList(startIndex,endIndex),count);
        }
        catch (Exception e)
        {
            LOG.error("queryNeedMeAuditOldCustomer error ",e);
            return new Table<>();
        }

    }
    /**
     * 审核老用户优惠活动
     * 0：通过 ； 1：不通过
     * @param param
     * @return
     */
    @RequestMapping(value = "auditOldCustomer.view",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult auditOldCustomerTask(@RequestBody Map<String,Object> param,HttpSession session){
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer taskId = Integer.parseInt(String.valueOf(param.get("id")));
        boolean isDel = oldCustomerService.queryTaskIsDel(taskId);
        if(isDel){
            return new ServiceResult(-1,"该任务已经被删除");
        }
        return oldCustomerService.auditOldCustomerTask(param,userDomain);
    }

    /**
     * 删除任务
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "deleteOldCustomer.view",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteOldCustomer(@RequestBody Map<String,Object> param,HttpSession session){
        Integer userId = UserUtils.getLoginUser(session).getId();
        Integer taskId = (Integer)param.get("id");
        return oldCustomerService.deleteOldCustomer(taskId, userId);
    }

    /**
     * 任务点击执行
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "executeOldCustomerTask.view",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult executeOldCustomerTask(@RequestBody Map<String,Object> param,HttpSession session){
        Integer userId = UserUtils.getLoginUser(session).getId();
        Integer taskId = (Integer)param.get("id");
        return oldCustomerService.executeOldCustomerTask(taskId, userId);
    }


    /**
     * 查询审核失败原因
     * @param param
     * @return
     */
    @RequestMapping("getOldCustomerTaskAuditReason.view")
    @ResponseBody
    public Map<String,Object> getOldCustomerTaskAuditReason(@RequestBody Map<String,Object> param){
        int taskId = Integer.parseInt(String.valueOf(param.get("taskId")));
       String str = oldCustomerService.getOldCustomerTaskAuditReason(taskId);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("reason",str);
        return  map;
    }

    /**
     * 终止老用户优惠活动任务
     * @param param
     * @param session
     * @return
     */
    @RequestMapping("terminateOldCustomerTask.view")
    @ResponseBody
    public ServiceResult terminateOldCustomerTask(@RequestBody Map<String,String> param,HttpSession session){
        int taskId =  Integer.parseInt(String.valueOf(param.get("id")));
        UserDomain userDomain = UserUtils.getLoginUser(session);
        int userId = userDomain.getId();
        return oldCustomerService.terminateOldCustomerTask(taskId,userId);
    }

    /**
     * 前端页面鉴权
     * @param param
     * @return
     */
    @RequestMapping("queryOldCustomerAudit.view")
    @ResponseBody
    public ServiceResult queryOldCustomerAudit(@RequestBody Map<String,Object> param,HttpSession session){
        ServiceResult result = new ServiceResult();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer userId = userDomain.getId();
        Map<String,Object> map = oldCustomerService.queryAuditStr(userId);
        if(map == null || map.get("userId") == null){
            result.setDesc("抱歉您暂时没有老用户专享专题的操作权限，请联系管理员");
            result.setRetValue(-1);
        }
        return result;
    }


























}
