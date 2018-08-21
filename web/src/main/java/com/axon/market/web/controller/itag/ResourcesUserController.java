package com.axon.market.web.controller.itag;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.itag.ResourcesUserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.itag.ResourcesUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/8/28.
 */
@Controller
public class ResourcesUserController
{
    @Autowired
    @Qualifier("resourcesUserService")
    private ResourcesUserService resourcesUserService;

    @RequestMapping(value = "queryResourceUserModelByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryResourceUserModelByPage(@RequestParam Map<String, Object> params,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        int curPageIndex = Integer.valueOf(String.valueOf(params.get("start")));
        int pageSize = Integer.valueOf(String.valueOf(params.get("length")));

        int count = resourcesUserService.queryResourcesUsersCount(userDomain);
        List<ResourcesUserDomain> resourcesUserList = resourcesUserService.queryResourcesUsersByPage(curPageIndex, pageSize, userDomain);
        return new Table(resourcesUserList, count);
    }

    @RequestMapping(value = "queryResourceModelById.view", method = RequestMethod.POST)
    @ResponseBody
    public ResourcesUserDomain queryResourceModelById(@RequestBody Map<String, Object> params,HttpSession session)
    {
        String resourceId = String.valueOf(params.get("id"));
        return resourcesUserService.queryResourceModelById(Integer.valueOf(resourceId));
    }

    @RequestMapping(value = "createResourcesUserModel.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult createResourcesUserModel(HttpServletRequest request)
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
        return resourcesUserService.createResourcesUserModel(request, userDomain);
    }

    @RequestMapping(value = "deleteResourceModel.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteResourceModel(@RequestBody Map<String, Object> params,HttpServletRequest request)
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
        String resourceId = String.valueOf(params.get("id"));
        return resourcesUserService.deleteResourceModel(Integer.valueOf(resourceId), userDomain);
    }

    @RequestMapping(value = "editResourceModel.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult editResourceModel(@RequestBody ResourcesUserDomain resourcesUserDomain,HttpServletRequest request)
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
        resourcesUserDomain.setUpdateUser(userDomain.getId());
        return resourcesUserService.editResourceModel(resourcesUserDomain);
    }

    @RequestMapping(value = "downloadResourceModel.view", method = RequestMethod.POST)
    public void downloadResourceModel(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
        Integer resourceId = Integer.parseInt(request.getParameter("id"));
        resourcesUserService.downloadResourceModel(response, userDomain, resourceId);
    }
}
