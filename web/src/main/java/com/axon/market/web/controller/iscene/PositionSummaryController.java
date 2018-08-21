package com.axon.market.web.controller.iscene;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iscene.PositionSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地市任务汇总统计
 * Created by zengcr on 2016/12/13.
 */
@Controller("positionSummaryController")
public class PositionSummaryController
{
    @Autowired
    @Qualifier("positionSummaryService")
    private PositionSummaryService positionSummaryService;

    /**
     * 查询地市任务汇总统计
     * @param paras 时间，日报或年报的标识
     * @param session
     * @return
     */
    @RequestMapping(value = "queryPositionSummary.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryPositionSummary(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String,Object>> positionSummary = null;

        UserDomain userDomain = UserUtils.getLoginUser(session);
        paras.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        paras.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());

        positionSummary = positionSummaryService.queryPositionSummary(paras);
        result.put("items", positionSummary);
        return result;
    }
}
