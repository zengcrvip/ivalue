package com.axon.market.web.controller.isystem;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.isystem.MessageSendAndResvService;
import javafx.scene.control.Tab;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/5/15.
 */
@Controller("messageSendAndRecvController")
public class MessageSendAndRecvController
{
    @Autowired()
    @Qualifier("messageSendAndResvService")
    MessageSendAndResvService msrService;

    @RequestMapping(value = "queryMessageSendAndRecv.view",method = RequestMethod.POST)
    @ResponseBody
    public Table<List<T>> queryMessageSendAndRecv(@RequestParam(value = "phone") String phone,@RequestParam("length") Integer limit,@RequestParam("start") Integer offset){
        int count = msrService.queryMessageCount(phone);
        List<Map<String,Object>> list =  msrService.queryMessageSendAndRecv(phone,limit,offset);
        return new Table(list,count);
    }

}
