package com.axon.market.web.controller.ikeeper;

import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.core.service.icommon.PhonePlusService;
import com.axon.market.core.service.ikeeper.KeeperWelfareService;
import com.axon.market.core.service.isystem.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Duzm on 2017/8/14.
 */
@Controller
public class TaskForAppController {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("phonePlusService")
    private PhonePlusService phonePlusService;

    @Autowired
    @Qualifier("keeperWelfareService")
    private KeeperWelfareService keeperWelfareService;


    @RequestMapping(value = "initCall2PhonePlus.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo initCall2PhonePlus(@RequestBody Map<String, String> param) {
        String token = param.get("token");
        Integer taskId = Integer.parseInt(param.get("taskId")); 
        Long detailId = Long.parseLong(param.get("detailId"));
        String calledNumber = param.get("calledNumber");
        UserDomain userDomain = userService.queryUserByToken(token);
        return new ResultVo();
    }

    @RequestMapping(value = "giveCustProduct.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo giveCustProduct(@RequestBody Map<String, String> paras) {
        if (paras.get("productId") == null){
            return new ResultVo("-1", "未提供赠送的产品");
        }
        Integer productId = Integer.valueOf((String) paras.get("productId"));
        String productName = (String)paras.get("productName");
        String telephone = (String)paras.get("telephone");
        String token = paras.get("token");
        UserDomain userDomain = userService.queryUserByToken(token);
        return new ResultVo();
    }
}
