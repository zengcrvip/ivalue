package com.axon.market.web.controller.ishop;

import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.ishop.MarketRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/16.
 */
@Controller("marketRecordController")
public class MarketRecordController
{

    @Qualifier("marketRecordService")
    @Autowired
    private MarketRecordService marketRecordService;

    @RequestMapping(value = "queryMarketRecordByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryMarketRecordByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        paras.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        paras.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());
        paras.put("phone", SearchConditionUtil.optimizeCondition(String.valueOf(paras.get("phone"))));
        paras.put("baseCode", SearchConditionUtil.optimizeCondition(String.valueOf(paras.get("baseCode"))));
        return marketRecordService.queryMarketRecordByPage(paras);
    }
}
