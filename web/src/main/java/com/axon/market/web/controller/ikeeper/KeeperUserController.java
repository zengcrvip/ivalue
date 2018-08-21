package com.axon.market.web.controller.ikeeper;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.ikeeper.KeeperUserDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.ikeeper.KeeperUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/8/10.
 */
@Controller
public class KeeperUserController
{
    @Autowired
    @Qualifier("keeperUserService")
    private KeeperUserService keeperUserService;

    @RequestMapping(value = "queryKeeperUsersByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryKeeperUsersByPage(@RequestParam Map<String, Object> params,HttpSession session)
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);
        int curPageIndex = Integer.valueOf(String.valueOf(params.get("start")));
        int pageSize = Integer.valueOf(String.valueOf(params.get("length")));
        String phone = (params.get("phone") != null && StringUtils.isNotEmpty(String.valueOf(params.get("phone"))))?String.valueOf(params.get("phone")):null;

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("phone", SearchConditionUtil.optimizeCondition(phone));

        int count = keeperUserService.queryKeeperUsersCount(loginUser.getId(),loginUser.getAreaCode(),condition);
        List<KeeperUserDomain> keeperUserList = keeperUserService.queryKeeperUsersByPage(curPageIndex, pageSize, loginUser.getId(), loginUser.getAreaCode(),condition);
        return new Table(keeperUserList, count);
    }

    @RequestMapping(value = "queryKeeperUserDetail.view", method = RequestMethod.POST)
    @ResponseBody
    public KeeperUserDomain queryKeeperUserDetail(@RequestBody Map<String, Object> params,HttpSession session)
    {
        String userId = String.valueOf(params.get("userId"));
        return keeperUserService.queryKeeperUserDetail(Integer.valueOf(userId));
    }

    @RequestMapping(value = "createKeeperUser.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult createKeeperUser(@RequestBody KeeperUserDomain keeperUserDomain,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return keeperUserService.createKeeperUser(keeperUserDomain, userDomain);
    }

    @RequestMapping(value = "updateKeeperUser.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateKeeperUser(@RequestBody KeeperUserDomain keeperUserDomain,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        keeperUserDomain.setUpdateUser(userDomain.getId());
        return keeperUserService.updateKeeperUser(keeperUserDomain);
    }

    @RequestMapping(value = "deleteKeeperUser.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteKeeperUser(@RequestBody Map<String, Object> params,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String userId = String.valueOf(params.get("userId"));
        return keeperUserService.deleteKeeperUser(Integer.valueOf(userId),userDomain.getId());
    }

    @RequestMapping(value = "queryUsersForKeeperUser.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> queryUsersForKeeperUser(@RequestBody Map<String, Object> params,HttpSession session)
    {
        String areaId = String.valueOf(params.get("areaId"));
        return keeperUserService.queryUsersForKeeperUser(Integer.valueOf(areaId));
    }

    @RequestMapping(value = "querySmsSignatureAuditUsers.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> querySmsSignatureAuditUsers(@RequestBody Map<String, Object> params,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String areaId = String.valueOf(params.get("areaId"));
        if (StringUtils.isEmpty(areaId))
        {
            return new ArrayList<Map<String,Object>>();
        }
        return keeperUserService.queryKeeperAuditUsers(userDomain, "smsSignature", Integer.valueOf(areaId));
    }

    @RequestMapping(value = "queryKeeperTaskAudits.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> queryKeeperTaskAudits(@RequestBody Map<String, Object> params,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return keeperUserService.queryKeeperAuditUsers(userDomain, "task", userDomain.getAreaId());
    }

    @RequestMapping(value = "queryAuditingSmsSignature.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryAuditingSmsSignature(@RequestParam Map<String, Object> params,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        int curPageIndex = Integer.valueOf(String.valueOf(params.get("start")));
        int pageSize = Integer.valueOf(String.valueOf(params.get("length")));

        int count = keeperUserService.queryAuditingSmsSignatureCount(userDomain.getId());
        List<Map<String,Object>> auditingSmsSignatureList = keeperUserService.queryAuditingSmsSignatureByPage(curPageIndex, pageSize, userDomain.getId());
        return new Table(auditingSmsSignatureList, count);
    }

    @RequestMapping(value = "auditSmsSignature.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult auditSmsSignature(@RequestBody Map<String, Object> params,HttpSession session)
    {
        String auditSmsSignatureId = String.valueOf(params.get("auditId"));
        String auditDesc = String.valueOf(params.get("auditDesc"));
        String auditResult = String.valueOf(params.get("auditResult"));
        if (StringUtils.isEmpty(auditSmsSignatureId) || StringUtils.isEmpty(auditResult) || StringUtils.isEmpty(auditResult))
        {
            return new ServiceResult(-1,"入参有误");
        }
        return keeperUserService.auditSmsSignature(Integer.valueOf(auditSmsSignatureId), auditDesc, auditResult);
    }
}
