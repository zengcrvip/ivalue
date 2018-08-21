package com.axon.market.web.controller.ikeeper;

import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.constant.ikeeper.KeeperAppResponseCodeEnum;
import com.axon.market.common.domain.ikeeper.TaskDomain;
import com.axon.market.common.domain.ikeeper.TaskShowDomain;
import com.axon.market.common.domain.ikeeper.TaskTodoDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.core.service.icommon.PhonePlusService;
import com.axon.market.core.service.ikeeper.KeeperTaskAppService;
import com.axon.market.core.service.ikeeper.KeeperWelfareService;
import com.axon.market.core.service.isystem.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by yuanfei on 2017/8/15.
 */
@Controller
public class TaskAppController
{
    @Autowired
    @Qualifier("keeperTaskAppService")
    private KeeperTaskAppService keeperTaskAppService;

    @Autowired
    @Qualifier("keeperWelfareService")
    private KeeperWelfareService keeperWelfareService;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("phonePlusService")
    private PhonePlusService phonePlusService;

    @RequestMapping(value = "queryMyKeeperTask.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryMyKeeperTask(@RequestBody Map<String, String> param)
    {
        ResultVo result = new ResultVo();
        String token = param.get("token");
        result.setResultObj(keeperTaskAppService.queryMyKeeperTask(token));
        return result;
    }

    @RequestMapping(value = "queryTaskInstDetailsByStateAndType.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryTaskInstDetailsByStateAndType(@RequestBody Map<String, String> param)
    {
        ResultVo result = new ResultVo();
        String token = param.get("token");
        String state = param.get("state");
        String typeId = param.get("typeId");
        if (StringUtils.isEmpty(state) || StringUtils.isEmpty(typeId))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue();
        }
        result.setResultObj(keeperTaskAppService.queryTaskInstDetailsByStateAndType(state, Integer.valueOf(typeId), token));
        return result;
    }

    @RequestMapping(value = "maintainCustomerSmsReminder.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo maintainCustomerSmsReminder(@RequestBody Map<String, String> param)
    {
        Boolean autograph = StringUtils.isNotEmpty(param.get("autograph")) && param.get("autograph") == "Y"? true : false;
        String token = param.get("token");
        String detailId = param.get("detailId");
        if (StringUtils.isEmpty(detailId))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue();
        }
        return keeperTaskAppService.maintainCustomerSmsReminder(autograph,token, Integer.valueOf(detailId));
    }

    @RequestMapping(value = "maintainCustomerCallReminder.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo maintainCustomerCallReminder(@RequestBody Map<String, String> param)
    {
        String token = param.get("token");
        String detailId = param.get("detailId");
        if (StringUtils.isEmpty(detailId))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue();
        }
        return keeperTaskAppService.maintainCustomerCallReminder(token, Integer.valueOf(detailId));
    }

    @RequestMapping(value = "feedbackCallReminderResult.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo feedbackCallReminderResult(@RequestBody Map<String, String> param)
    {
        String detailId = param.get("detailId");
        String serialId = param.get("serialId");
        String result = param.get("result");
        String businessTendency = param.get("businessTendency");
        String taskInstResultId = param.get("taskInstResultId");

        if ("0".equals(businessTendency))
        {
            return new ResultVo("0000","暂不处理");
        }

        if (StringUtils.isEmpty(detailId) || StringUtils.isEmpty(detailId) || StringUtils.isEmpty(result) || StringUtils.isEmpty(taskInstResultId))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue();
        }
        return keeperTaskAppService.feedbackCallReminderResult(Integer.valueOf(detailId), serialId, result, businessTendency, Integer.valueOf(taskInstResultId));
    }

    @RequestMapping(value = "queryWelfareProductsByWelfareId.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryWelfareProductsByWelfareId(@RequestBody Map<String, String> param)
    {
        String welfareId = param.get("welfareId");
        if (StringUtils.isEmpty(welfareId))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue();
        }
        return keeperTaskAppService.queryWelfareProductsByWelfareId(Integer.valueOf(welfareId));
    }

    @RequestMapping(value = "queryCustomerDetail.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryCustomerDetail(@RequestBody Map<String, String> param)
    {
        String detailId = param.get("detailId");
        if (StringUtils.isEmpty(detailId))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue();
        }
        return keeperTaskAppService.queryCustomerDetail(Integer.valueOf(detailId));
    }

    @RequestMapping(value = "queryInterfaceManUnderSameOrg.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryInterfaceManUnderSameOrg(@RequestBody Map<String, String> param)
    {
        String token = param.get("token");
        return keeperTaskAppService.queryInterfaceManUnderSameOrg(token);
    }

    @RequestMapping(value = "forwardingTask.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo forwardingTask(@RequestBody Map<String, String> param)
    {
        String token = param.get("token");
        String detailId = param.get("detailId");
        if (StringUtils.isEmpty(detailId))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue();
        }
        return keeperTaskAppService.forwardingTask(token, Integer.valueOf(detailId));
    }

    @RequestMapping(value = "queryTaskSmsTemplates.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryTaskSmsTemplates(@RequestBody Map<String, String> param)
    {
        String token = param.get("token");
        String typeId = param.get("typeId");
        String state = param.get("state");
        if (StringUtils.isEmpty(typeId) || StringUtils.isEmpty(state))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue();
        }
        return keeperTaskAppService.queryTaskSmsTemplates(token, Integer.valueOf(typeId), Integer.valueOf(state));
    }

    @RequestMapping(value = "maintainCustomerBatchSmsReminder.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo maintainCustomerBatchSmsReminder(@RequestBody Map<String, String> param)
    {
        String token = param.get("token");
        String typeId = param.get("typeId");
        String state = param.get("state");
        // 格式为：{"任务id1":"是否使用签名(0:否，1：是)","任务id1":"是否使用签名"}
        String taskSmsDetails = param.get("taskSmsDetails");
        if (StringUtils.isEmpty(typeId) || StringUtils.isEmpty(state)  || StringUtils.isEmpty(taskSmsDetails))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue();
        }

        return keeperTaskAppService.maintainCustomerBatchSmsReminder(token, Integer.valueOf(typeId), Integer.valueOf(state), taskSmsDetails);
    }

    @RequestMapping(value = "giveCustWelfare.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo giveCustWelfare(@RequestBody Map<String, String> paras)
    {
        String welfareId = paras.get("welfareId");
        String telephone = paras.get("telephone");
        String token = paras.get("token");
        if (StringUtils.isEmpty(welfareId) || StringUtils.isEmpty(telephone))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue("未提供赠送的福利或手机号");
        }
        UserDomain userDomain = userService.queryUserByToken(token);
        return keeperWelfareService.giveCustWelfare(welfareId, userDomain.getId(), telephone, false, false);
    }

    @RequestMapping(value = "giveCustWelfareSmsReminder.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo giveCustWelfareSmsReminder(@RequestBody Map<String, String> paras) {
        String telephone = (String)paras.get("telephone");
        Boolean autograph = paras.get("autograph").equals("Y")?true:false;
        String content = (String)paras.get("content");
        String token = paras.get("token");
        UserDomain userDomain = userService.queryUserByToken(token);

        keeperWelfareService.reminder(telephone,autograph,userDomain.getId(),content);
        return new ResultVo();
    }

    @RequestMapping(value = "giveCustWelfareVoiceReminder.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo giveCustWelfareVoiceReminder(@RequestBody Map<String, String> paras) {
        String telephone = (String)paras.get("telephone");
        String token = paras.get("token");
        UserDomain userDomain = userService.queryUserByToken(token);
        ResultVo resultVo = new ResultVo();
        try {
            phonePlusService.initCallToPhonePlus(userDomain.getTelephone(),telephone,userDomain.getTelephone());
        } catch (Exception e) {
            resultVo.setResultCode("-1");
            resultVo.setResultMsg("调用话+接口失败");
            return resultVo;
        }
        return resultVo;
    }

    @RequestMapping(value = "queryTodoTaskCount.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryTodoTaskCount(@RequestBody Map<String, String> paras) {
        String token = paras.get("token") == null?"":paras.get("token");
        if (token.equals("")){
            return new ResultVo("-1", "系统出错，请联系系统管理员");
        }
        UserDomain userDomain = userService.queryUserByToken(token);
        return keeperTaskAppService.queryTodoTaskCount(userDomain.getId());
    }

    @RequestMapping(value = "queryTodoTaskByDate.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryTodoTaskByDate(@RequestBody Map<String, String> paras) {
        String createdate = paras.get("createdate") == null?"":paras.get("createdate");
        String token = paras.get("token") == null?"":paras.get("token");
        if (token.equals("")){
            return new ResultVo("-1", "系统出错，请联系系统管理员");
        }
        UserDomain userDomain = userService.queryUserByToken(token);
        return keeperTaskAppService.queryTodoTaskByDate(createdate, userDomain.getId());
    }

    @RequestMapping(value = "insertTodoTask.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo insertTodoTask(@RequestBody Map<String, String> paras) {
        String token = paras.get("token") == null?"":paras.get("token");
        if (token.equals("")){
            return new ResultVo("-1","系统用户获取不到，请联系系统管理员!");
        }
        String content = paras.get("content") == null?"":paras.get("content");
        String createdate = paras.get("createdate") == null?"":paras.get("createdate");
        UserDomain userDomain = userService.queryUserByToken(token);
        TaskTodoDomain taskdomain = new TaskTodoDomain();
        taskdomain.setUserid(userDomain.getId());
        taskdomain.setUsername(userDomain.getName());
        taskdomain.setTaskcontent(content);
        taskdomain.setCreatedate(createdate);

        return keeperTaskAppService.insertTodoTask(taskdomain);
    }

    @RequestMapping(value = "deleteTodoTask.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo deleteTodoTask(@RequestBody Map<String, String> paras) {
        Integer taskid = Integer.valueOf(paras.get("taskid"));
        return keeperTaskAppService.deleteTodoTask(taskid);
    }

    @RequestMapping(value = "setTodoTaskStatus.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo setTodoTaskStatus(@RequestBody Map<String, String> paras) {
        Integer taskid = Integer.valueOf(paras.get("taskid"));
        return keeperTaskAppService.setTodoTaskStatus(taskid);
    }

    @RequestMapping(value = "queryUnreadTaskCount.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryUnreadTaskCount(@RequestBody Map<String, String> paras) {
        String token = paras.get("token") == null?"":paras.get("token");
        if (token.equals("")){
            return new ResultVo("-1","系统用户获取不到，请联系系统管理员!");
        }
        UserDomain userDomain = userService.queryUserByToken(token);
        return keeperTaskAppService.queryUnreadTaskCount(userDomain.getId());
    }

    @RequestMapping(value = "queryCashflow.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryCashflow(@RequestBody Map<String, String> paras) {
        String token = paras.get("token") == null?"":paras.get("token");
        if (token.equals("")){
            return new ResultVo("-1","系统用户获取不到，请联系系统管理员!");
        }
        UserDomain userDomain = userService.queryUserByToken(token);
        return keeperTaskAppService.queryCashflow(userDomain.getId());
    }

    /**
     * 掌柜首页实时任务提醒
     * @param param
     * @return
     */
    @RequestMapping(value = "queryRealTimeReminder.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryRealTimeReminder(@RequestBody Map<String, String> param)
    {
        String token = param.get("token") == null?"":param.get("token");
        if (token.equals("")){
            return new ResultVo("-1","系统用户获取不到，请联系系统管理员!");
        }
        return keeperTaskAppService.queryRealTimeReminder(token);
    }
}
