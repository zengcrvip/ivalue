package com.axon.market.web.controller.iscene;

import com.axon.market.common.bean.Operation;
import com.axon.market.core.service.iscene.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by DELL on 2016/12/19.
 */
@Controller("commandController")
public class CommandController
{
    @Autowired
    @Qualifier("commandService")
    private CommandService commandService;


    /**
     * 控制中心命令提交
     * @param param
     * @return
     */
    @RequestMapping(value = "getCommandContent.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getCommand(@RequestBody Map<String, String> param)
    {
        String command = param.get("content");
        return commandService.sendCommand(command);
    }
}
