package com.axon.market.web.controller.isystem;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.TestPhoneNumberDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.isystem.TestPhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/8.
 */
@Controller("testPhoneNumberController")
public class TestPhoneNumberController
{
    @Autowired
    @Qualifier("testPhoneNumberService")
    private TestPhoneNumberService testPhoneNumberService;

    @RequestMapping(value = "queryTestPhoneNumbersByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryTestPhoneNumbersByPage(@RequestParam Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer start = Integer.parseInt(param.get("start"));
        Integer length = Integer.parseInt(param.get("length"));
        return testPhoneNumberService.queryTestPhoneNumbersByPage(start, length);
    }

    @RequestMapping(value = "addOrEditTestPhoneNumber.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditTestPhoneNumber(@RequestBody TestPhoneNumberDomain testPhoneNumberDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return testPhoneNumberService.addOrEditTestPhoneNumber(testPhoneNumberDomain, userDomain);
    }

    @RequestMapping(value = "deleteTestPhoneNumber.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteTestPhoneNumber(@RequestBody TestPhoneNumberDomain testPhoneNumberDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return testPhoneNumberService.deleteTestPhoneNumber(testPhoneNumberDomain.getId(), userDomain.getId());
    }
}
