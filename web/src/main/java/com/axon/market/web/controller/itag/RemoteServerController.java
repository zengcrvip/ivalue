package com.axon.market.web.controller.itag;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.itag.RemoteServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/4.
 */
@Controller("remoteServerController")
public class RemoteServerController
{
    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @RequestMapping(value = "queryRemoteServersByPage.view")
    @ResponseBody
    public Table queryRemoteServersByPage(@RequestParam Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer start = Integer.parseInt(param.get("start"));
        Integer length = Integer.parseInt(param.get("length"));
        String serverName = param.get("nameSearch");
        return remoteServerService.queryRemoteServersByPage(serverName,start, length);
    }

    @RequestMapping(value = "addOrEditRemoteServer.view")
    @ResponseBody
    public Operation addOrEditRemoteServer(@RequestBody RemoteServerDomain remoteServerDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return remoteServerService.addOrEditRemoteServer(remoteServerDomain, userDomain);
    }

    @RequestMapping(value = "deleteRemoteServer.view")
    @ResponseBody
    public Operation deleteRemoteServer(@RequestBody Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer serverId = Integer.parseInt(param.get("id"));
        return remoteServerService.deleteRemoteServer(serverId, userDomain.getId());
    }

    @RequestMapping(value = "queryAllRemoteServerNames.view")
    @ResponseBody
    public List<Map<String, String>> queryAllRemoteServerNames(HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return remoteServerService.queryAllRemoteServerIdAndNames();
    }

    @RequestMapping(value = "testConnection.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation testConnection(@RequestBody RemoteServerDomain remoteServerDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return remoteServerService.testConnection(remoteServerDomain);
    }
}
