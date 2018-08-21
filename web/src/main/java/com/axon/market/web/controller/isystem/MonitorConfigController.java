package com.axon.market.web.controller.isystem;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.isystem.MonitorConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by xuan on 2017/4/17.
 */
@Controller("monitorConfigController")
public class MonitorConfigController
{
    @Autowired
    @Qualifier("monitorConfigService")
    private MonitorConfigService monitorConfigService;


    /**
     * 列表查询
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "queryMonitorConfig.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryMonitorConfig(@RequestParam Map<String, String> param, HttpSession session)
    {
        return monitorConfigService.queryMonitorConfig(param);
    }

    /**
     * 新增/修改
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditMonitor.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditMonitor(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Operation operation = monitorConfigService.addOrEditMonitor(paras, userDomain);
        return operation;
    }


    /**
     * 上传文件入库
     *
     * @param fileName//文件名
     * @param request
     * @return
     */
    @RequestMapping(value = "addEmailOrPhone.view" , method =  RequestMethod.POST)
    @ResponseBody
    public Operation addEmailOrPhone(@RequestParam(value = "name", required = false) String fileName,@RequestParam(value = "type", required = false) String type,
                                 HttpServletRequest request)
    {
        boolean isSuccess = false;
        String filePath = request.getSession().getServletContext().getRealPath(fileName);
        if(!StringUtils.isEmpty(filePath)){
            isSuccess=true;
            //isSuccess = monitorConfigService.batchUpload(filePath,type);
        }
        return new Operation(isSuccess, filePath);
    }

    /**
     * 删除监控配置
     *
     * @param paras id 监控配置id
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "deleteMonitor.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteMonitor(@RequestBody Map<String, Object> paras)
    {
        Operation operation = monitorConfigService.deleteMonitor(paras);
        return operation;
    }
    /**
     * 文件上传
     * @param fileName//js处理后的文件名
     * @param orgFileName//原文件名
     * @param request
     * @return
     */
    @RequestMapping("uploadUrlFileMonitor.view")
    @ResponseBody
    public Operation uploadUrlFileMonitor(@RequestParam(value = "fname",required = false) String fileName,
                                   @RequestParam(value = "originalFile",required = false) String orgFileName,
                                   HttpServletRequest request)
    {
        if(StringUtils.isEmpty(fileName) || StringUtils.isEmpty(orgFileName)){
            return new Operation(false,"文件传输异常");
        }
        return monitorConfigService.uploadUrlFile(fileName, orgFileName, request);
    }

    /**
     * 查询导入的email和phone的数量
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryEmailOrPhone.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation queryEmailOrPhone(@RequestBody Map<String, Object> paras)
    {
        Operation operation = monitorConfigService.queryEmailOrPhone(paras);
        return operation;
    }

    /**
     * 查询导入的email数量
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryEmail.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation queryEmail(@RequestBody Map<String, Object> paras)
    {
        Operation operation = monitorConfigService.queryEmail(paras);
        return operation;
    }

    /**
     * 查询导入phone的数量
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryPhone.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation queryPhone(@RequestBody Map<String, Object> paras)
    {
        Operation operation = monitorConfigService.queryPhone(paras);
        return operation;
    }

    /**
     * 根据id查询监控配置
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryMonitorById.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryMonitorById(@RequestBody Map<String, Object> paras)
    {
        return monitorConfigService.queryMonitorById(paras);
    }

    /**
     * 列表查询邮箱
     * @param param
     * @return
     */
    @RequestMapping(value = "queryEmailList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryEmailList(@RequestParam Map<String, String> param)
    {
        return monitorConfigService.queryEmailList(param);
    }

    /**
     * 列表查询号码
     * @param param
     * @return
     */
    @RequestMapping(value = "queryPhoneList.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryPhoneList(@RequestParam Map<String, String> param)
    {
        return monitorConfigService.queryPhoneList(param);
    }

    /**
     * 删除邮箱
     *
     * @param paras id 邮箱id
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "deleteEmail.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteEmail(@RequestBody Map<String, Object> paras)
    {
        Operation operation = monitorConfigService.deleteEmail(paras);
        return operation;
    }

    /**
     * 删除号码
     *
     * @param paras id 号码id
     * @return Operation 增删改统一返回对象
     */
    @RequestMapping(value = "deletePhone.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deletePhone(@RequestBody Map<String, Object> paras)
    {
        Operation operation = monitorConfigService.deletePhone(paras);
        return operation;
    }

    /**
     * 新增/修改 email
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "addOrEditEmail.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditEmail(@RequestBody Map<String, Object> paras)
    {
        Operation operation = monitorConfigService.addOrEditEmail(paras);
        return operation;
    }
    /**
     * 新增/修改 email
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "addOrEditPhone.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditPhone(@RequestBody Map<String, Object> paras)
    {
        Operation operation = monitorConfigService.addOrEditPhone(paras);
        return operation;
    }

}
