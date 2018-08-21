package com.axon.market.web.controller.ikeeper;

import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.ikeeper.KeeperAppResponseCodeEnum;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.ikeeper.KeeperUserService;
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
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/4/20.
 */
@Controller
public class UserAppController
{
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("keeperUserService")
    private KeeperUserService keeperUserService;

    @RequestMapping(value = "loginForApp.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo loginForApp(@RequestBody Map<String, String> param,HttpServletRequest request)
    {
        String phone = param.get("phone");
        String verificationCode = param.get("validationCode");
        return keeperUserService.loginApp(phone,verificationCode,request);
    }

    @RequestMapping(value = "validationCodeService.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo validationCodeService(@RequestBody Map<String, String> param,HttpServletRequest request)
    {
        ResultVo result = new ResultVo();
        String phone = param.get("phone");
        ServiceResult serviceResult = userService.sendVerificationCode(phone, request,"app");
        result.setResultCode(serviceResult.getRetValue() == 0?"0000":"4444");
        result.setResultMsg(serviceResult.getDesc());
        return result;
    }

    @RequestMapping(value = "fetchKeeperUserInfo.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo fetchKeeperUserInfo(@RequestBody Map<String, String> param,HttpServletRequest request)
    {
        ResultVo result = new ResultVo();
        String token = param.get("token");
        result.setResultObj(keeperUserService.queryKeeperUserByToken(token));
        return result;
    }

    @RequestMapping(value = "queryMyOrg.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryMyOrg(@RequestBody Map<String, String> param)
    {
        String token = param.get("token");
        return keeperUserService.queryMyOrg(token);
    }


    @RequestMapping(value = "queryMySmsSignature.app", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo queryMySmsSignature(@RequestBody Map<String, String> params)
    {
        String token = params.get("token");
        return keeperUserService.queryMySmsSignature(token);
    }

    @RequestMapping(value = "editKeeperUserSmsSignature.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo editKeeperUserSmsSignature(@RequestBody Map<String, String> param)
    {
        String token = param.get("token");
        String newSmsSignature = param.get("newSmsSignature");
        if (StringUtils.isEmpty(newSmsSignature))
        {
            return KeeperAppResponseCodeEnum.PARAM_LOSE.getValue("未设置新的短信签名");
        }
        return keeperUserService.updateKeeperUserSmsSignature(token, newSmsSignature);
    }


}
