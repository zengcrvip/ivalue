package com.axon.market.web.controller.isystem;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.RoleDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.isystem.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */
@Controller
public class RoleController
{
    @Autowired
    @Qualifier("roleService")
    private RoleService roleService;

    @RequestMapping(value = "queryRolesByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<RoleDomain> queryUsersByPage(@RequestParam Map<String, Object> params, HttpSession session)
    {
        int curPageIndex = Integer.valueOf((String) params.get("start"));
        int pageSize = Integer.valueOf((String) params.get("length"));
        String name = (String) params.get("name");

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("name", SearchConditionUtil.optimizeCondition(name));

        int count = roleService.queryRolesCount(condition);
        List<RoleDomain> uses = roleService.queryRolesByPage(curPageIndex, pageSize, "create_time", condition);
        return new Table(uses, count);
    }

    @RequestMapping(value = "queryAllRole.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> queryAllRole(@RequestParam Map<String, Object> params, HttpSession session)
    {
        return roleService.queryAllRole();
    }

    @RequestMapping(value = "queryAllPermissions.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> queryAllPermissions(@RequestBody Map<String, Object> params)
    {
        String type = String.valueOf(params.get("type"));
        return roleService.queryAllPermissions(type);
    }

    @RequestMapping(value = "createRoleInfo.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult createRoleInfo(@RequestBody RoleDomain role, HttpSession session)
    {
        role.setCreateUser(UserUtils.getLoginUser(session).getId());
        return roleService.insertRole(role);
    }

    @RequestMapping(value = "updateRoleInfo.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateRoleInfo(@RequestBody RoleDomain role, HttpSession session)
    {
        role.setUpdateUser(UserUtils.getLoginUser(session).getId());
        return roleService.updateRole(role);
    }

    @RequestMapping(value = "deleteRole.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteRole(@RequestBody Map<String, Object> param, HttpSession session)
    {
        String id = String.valueOf(param.get("id"));
        return roleService.deleteRole(Integer.valueOf(id));
    }

}
