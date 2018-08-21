package com.axon.market.web.controller.isystem;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.isystem.ClientOrderRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/5/16.
 */
@Controller("clientOrderRecordController")
public class ClientOrderRecordController
{
    @Autowired
    @Qualifier("clientOrderRecordService")
    ClientOrderRecordService clientOrderRecordService;

    @RequestMapping(value = "queryClientOrderRecord.view",method = RequestMethod.POST)
    @ResponseBody
    public Table queryClientOrderRecord(@RequestParam("dateTime") String dateTime,@RequestParam(value = "phone",required = false) String phone,@RequestParam("length") Integer limit,@RequestParam("start") Integer offset){
//        phone = StringUtils.trimWhitespace(phone);
        int count = clientOrderRecordService.queryClientOrderCount(phone,dateTime);
        List<Map<String, Object>> list = clientOrderRecordService.queryClientOrderRecord(phone,dateTime,limit,offset);
        return new Table(list,count);
    }

}
