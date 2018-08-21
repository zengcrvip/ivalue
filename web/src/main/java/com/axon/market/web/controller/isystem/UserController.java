package com.axon.market.web.controller.isystem;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.bean.VerificationCodeBean;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.common.util.VerifyCodeUtil;
import com.axon.market.core.service.iscene.PositionSceneService;
import com.axon.market.core.service.isystem.UserService;
import com.axon.market.web.constants.UserConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/1/3.
 */
@Controller
public class UserController
{
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    private static VerificationCodeBean verificationCodeBean = VerificationCodeBean.getInstance();

    @Qualifier("positionSceneService")
    @Autowired
    private PositionSceneService positionSceneService;


    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @RequestMapping(value = "loginByPhone.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loginByPhone(@RequestBody Map<String, Object> paras, HttpSession session,HttpServletRequest request) throws IOException
    {
        Map<String, Object> returnResult = new HashMap<String, Object>();
        String userPhone = (String) paras.get("phone");
        String verificationCode = (String) paras.get("code");
        String vCode = (String) paras.get("vCode");

        String code = (String)request.getSession(true).getAttribute("randomString");

        //判断验证码是否正确（如果使用万能验证码则验证通过即通过）
        if (!((verificationCodeBean.getOpenUniversal() && verificationCodeBean.getCode().toUpperCase().equals(vCode.toUpperCase())) || code.toUpperCase().equals(vCode.toUpperCase())))
        {
            returnResult.put("retValue", -1);
            returnResult.put("desc", "图片校验码错误");
            return returnResult;
        }

        LOG.info("user login by phone:"+userPhone);
        ServiceResult result = userService.loginByPhone(userPhone, verificationCode, session,request);
        returnResult.put("retValue", result.getRetValue());
        returnResult.put("desc", result.getDesc());
        if (0 == result.getRetValue())
        {
            returnResult.put("url", "index.view");
        }
        return returnResult;
    }

    @RequestMapping(value = "loginByName.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loginByName(@RequestBody Map<String, Object> paras, HttpSession session,HttpServletRequest request) throws IOException
    {
        Map<String, Object> returnResult = new HashMap<String, Object>();
        String userPhone = (String) paras.get("user");
        String userPwd = (String) paras.get("password");
        String vCode = (String) paras.get("vCode");

        String code = (String)request.getSession(true).getAttribute("randomString");

        if (StringUtils.isEmpty(code)) {
            returnResult.put("retValue", -1);
            returnResult.put("desc", "图片校验码加载失败");
            return returnResult;
        }

        //判断验证码是否正确（如果使用万能验证码则验证通过即通过）
        if (!((verificationCodeBean.getOpenUniversal() && verificationCodeBean.getCode().toUpperCase().equals(vCode.toUpperCase())) || code.toUpperCase().equals(vCode.toUpperCase())))
        {
            returnResult.put("retValue", -1);
            returnResult.put("desc", "图片校验码错误");
            return returnResult;
        }
        ServiceResult result = userService.loginByName(userPhone, userPwd, session);
        returnResult.put("retValue", result.getRetValue());
        returnResult.put("desc", result.getDesc());
        if (0 == result.getRetValue())
        {
            returnResult.put("url", "index.view");
        }
        return returnResult;
    }

    @RequestMapping(value = "sendVerificationCode", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult sendVerificationCode(@RequestBody Map<String, Object> paras, HttpSession session,HttpServletRequest request)
    {
        String userPhone = (String) paras.get("phone");
        //发送验证码
        return userService.sendVerificationCode(userPhone,request,"PC");
    }

    @RequestMapping(value = "loginOut.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loginOut(HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        session.invalidate();
        String url = session.getServletContext().getInitParameter("reLoginUrl");
        //防止浏览器使用网站缓存
        String randomStr = "?time=" + System.currentTimeMillis();
        result.put("url", url + randomStr);
        return result;
    }

    @RequestMapping(value = "queryUsersByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<UserDomain> queryUsersByPage(@RequestParam Map<String, Object> params, HttpSession session) throws IOException
    {
        int curPageIndex = Integer.valueOf((String)params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));
        String name = (String)params.get("name");
        String telephone = (String)params.get("telephone");
        String areaId = (String)params.get("areaId");
        String roleId = (String)params.get("roleId");
        UserDomain loginUser = UserUtils.getLoginUser(session);
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("name", SearchConditionUtil.optimizeCondition(name));
        condition.put("telephone", SearchConditionUtil.optimizeCondition(telephone));
        condition.put("areaId", StringUtils.isNotEmpty(areaId) && !"-1".equals(areaId)?areaId:null);
        condition.put("roleId", StringUtils.isNotEmpty(roleId) && !"-1".equals(roleId) ? roleId : null);

        int count = userService.queryUsersCount(loginUser, condition);
        List<UserDomain> uses =  userService.queryUsersByPage(curPageIndex, pageSize,loginUser,condition);
        return new Table(uses,count);
    }

    @RequestMapping(value = "queryPersonalBaseInfo.view")
    @ResponseBody
    public UserDomain queryPersonalBaseInfo(HttpSession session) throws IOException
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);
        return userService.queryPersonalBaseInfo(loginUser.getId());
    }

    @RequestMapping(value = "queryAuditUsers.view", method = RequestMethod.POST)
    @ResponseBody
    public String queryAuditUsers(@RequestBody Map<String, Object> params, HttpSession session) throws JsonProcessingException
    {
        String auditType = String.valueOf(params.get("auditType"));
        String areaId = String.valueOf(params.get("areaId"));
        UserDomain loginUser = UserUtils.getLoginUser(session);
        Integer selectAreaId = StringUtils.isNotEmpty(areaId)?Integer.valueOf(areaId):loginUser.getAreaId();
        Integer beHandleUser = null;
        if (null != params.get("userId"))
        {
            beHandleUser = Integer.valueOf(String.valueOf(params.get("userId")));
        }

        return userService.queryAuditUsers(auditType, selectAreaId, beHandleUser);
    }

    @RequestMapping(value = "queryCurrentUserInfo.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryCurrentUserInfo(@RequestBody Map<String, Object> params, HttpSession session)
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);

        Map<String,Object> result = new HashMap<String,Object>();
        result.put("loginUser", loginUser);
        result.put("dataPermission",session.getAttribute(UserConstants.SESSION_D_PERMISSION));
        return result;
    }

    @RequestMapping(value = "queryCurrentUserInfoById.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryCurrentUserInfoById(@RequestBody Map<String, Object> params, HttpSession session)
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);
       UserDomain userDomain = userService.queryUserById(loginUser.getId());
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("loginUser", userDomain);
        return result;
    }

    @RequestMapping(value = "createUser.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult createUser(@RequestBody UserDomain userDomin, HttpSession session)
    {
        userDomin.setCreateUserId(UserUtils.getLoginUser(session).getId());
        return userService.createUser(userDomin);
    }

    @RequestMapping(value = "updateUser.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateUser(@RequestBody UserDomain userDomin, HttpSession session)
    {
        return userService.updateUser(userDomin);
    }

    @RequestMapping(value = "startStopUser.view")
    @ResponseBody
    public ServiceResult startStopUser(@RequestBody Map<String, Object> paras, HttpSession session) throws IOException
    {
        Integer userId = Integer.valueOf(String.valueOf(paras.get("userId")));
        return userService.startStopUser(userId);
    }

    @RequestMapping(value = "checkChangeAuditUser.view")
    @ResponseBody
    public ServiceResult checkChangeAuditUser(@RequestBody Map<String, Object> paras, HttpSession session) throws Exception
    {
        ServiceResult result = new ServiceResult();
        String userId = String.valueOf(paras.get("USER_ID"));
        String auditType = String.valueOf(paras.get("AUDIT_TYPE"));
        if (StringUtils.isEmpty(userId))
        {
            result.setRetValue(-1);
            result.setDesc("系统异常");
            return result;
        }
       /* String auditNames = userService.checkChangeAuditUser(Integer.valueOf(userId), auditType);
        result.setRetValue(StringUtils.isEmpty(auditNames) ? 0 : 1);
        result.setDesc(auditNames);*/
        return  userService.checkChangeAuditUser(Integer.valueOf(userId), auditType);
    }

    @RequestMapping(value = "queryBusinessHallByCondition.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryBusinessHallByCondition(@RequestParam Map<String, Object> params, HttpSession session)
    {
        int curPageIndex = Integer.valueOf((String)params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));
        String name = (String)params.get("name");
        String areaCode = (String)params.get("areaCode");
        String editUserId = null != params.get("editUserId")? (String)params.get("editUserId") : "";

        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("baseName",name);
        condition.put("offset",curPageIndex);
        condition.put("limit",pageSize);
        condition.put("baseAreaId",areaCode);
        condition.put("editUserId",editUserId);
        condition.put("editUserType",StringUtils.isNotEmpty(editUserId)?"edit":"create");
        condition.put("baseTypeId", params.get("baseTypeId"));
        condition.put("status", 10);
        int itemCounts = positionSceneService.queryBasesTotal(condition);
        List<Map<String,String>> baseList = positionSceneService.queryBasesByPage(condition);
        return new Table(baseList,itemCounts);
    }

    @RequestMapping(value = "queryUnderMeBusinessHallsByCondition.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,List<Map<String,Object>>> queryUnderMeBusinessHallsByCondition(@RequestBody Map<String, Object> params, HttpSession session)
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);
        String targetUserId = String.valueOf(params.get("targetUserId"));
        String businessHallType = String.valueOf(params.get("businessHallType"));
        String selectedBusinessHalls = params.get("selectedBusinessHalls") != null? String.valueOf(params.get("selectedBusinessHalls")): null;
        return userService.queryUnderMeBusinessHallsByCondition(loginUser,Integer.valueOf(targetUserId),Integer.valueOf(businessHallType),selectedBusinessHalls);
    }

    @RequestMapping(value = "updatePersonalBaseInfo.view")
    @ResponseBody
    public ServiceResult updatePersonalBaseInfo(@RequestBody Map<String, Object> paras, HttpSession session) throws IOException
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);

        ServiceResult result = new ServiceResult();
        Object oldPassword = paras.get("oldPwd");
        if (oldPassword == null || StringUtils.isEmpty(String.valueOf(oldPassword)))
        {
            result.setRetValue(-1);
            result.setDesc("登录密码不能为空！");
            return result;
        }

        String oldPwd = (String) paras.get("oldPwd");
        String password = (String) paras.get("password");
/*        String email = (String) paras.get("email");
        String telephone = (String) paras.get("telephone");
        loginUser.setEmail(email);
        loginUser.setTelephone(telephone);*/
        loginUser.setPassword(password);
        return userService.updatePersonalBaseInfo(loginUser, oldPwd);
    }

    @RequestMapping(value = "checkChangeUserArea.view")
    @ResponseBody
    public ServiceResult checkChangeUserArea(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String userId = String.valueOf(paras.get("USER_ID"));
        return userService.checkChangeUserArea(Integer.valueOf(userId));
    }

    @RequestMapping(value = "queryAllMyCreatedSubUsers.view")
    @ResponseBody
    public List<Map<String,Object>> queryAllMyCreatedSubUsers(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String userId = String.valueOf(paras.get("userId"));

        return userService.queryAllMyCreatedSubUsers(Integer.valueOf(userId), userDomain.getAreaId());
    }

    @RequestMapping(value = "batchUpdateUsersAuditUser.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult batchUpdateUsersAuditUser(@RequestBody Map<String, Object> params, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String subAdmin = String.valueOf(params.get("subAdmin"));
        String businessHallIds = String.valueOf(params.get("businessHallIds"));
        return userService.batchUpdateUsersAuditUser(Integer.valueOf(subAdmin), businessHallIds,userDomain);
    }
}