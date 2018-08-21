package com.axon.market.web.controller.ikeeper;

import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.icommon.AreaService;
import com.axon.market.core.service.ikeeper.KeeperMgrService;
import com.axon.market.core.service.isystem.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/4/24.
 */
@Controller
public class KepperForPcController
{
    @Autowired
    @Qualifier("areaService")
    private AreaService areaService;

    @Autowired
    @Qualifier("keeperMgrService")
    private KeeperMgrService keeperMgrService;

    @RequestMapping(value = "fetchAreaOverview.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> fetchAreaOverview(@RequestBody Map<String, String> param,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return areaService.queryAreaByToken(userDomain);
    }

    @RequestMapping(value = "fetchChannelOverview.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> fetchChannelOverview(@RequestBody Map<String, String> param,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return areaService.queryChannelByToken(userDomain);
    }

    @RequestMapping(value = "fetchActivityOverview.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> fetchActivityOverview(@RequestBody Map<String, String> param)
    {
        //String token = param.get("token");
        return keeperMgrService.fetchActivities();
    }

    @RequestMapping(value = "fetchFeeOverview.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> fetchFeeOverview(@RequestBody Map<String, String> param,HttpSession session)
    {
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String orgType = param.get("orgType");
        String orgCode = param.get("orgCode");
        String activityId = param.get("activityId");

        UserDomain userDomain =UserUtils.getLoginUser(session);
        return keeperMgrService.fetchFee(userDomain,startDate,endDate,orgType,orgCode,activityId);
    }

    @RequestMapping(value = "fetchAreaRank.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> fetchAreaRank(@RequestBody Map<String, String> param,HttpSession session)
    {
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        //查询条件
        String areaCode = param.get("areaCode");
        String activityId = param.get("activityId");

        UserDomain userDomain =UserUtils.getLoginUser(session);
        return keeperMgrService.fetchAreaRank(userDomain, startDate, endDate,areaCode,activityId);
    }

    @RequestMapping(value = "fetchChannelRank.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> fetchChannelRank(@RequestBody Map<String, String> param,HttpSession session)
    {
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        //查询条件
        String areaCode = param.get("areaCode");
        String channelCode = param.get("channelCode");
        String activityId = param.get("activityId");

        UserDomain userDomain =UserUtils.getLoginUser(session);
        return keeperMgrService.fetchChannelRank(userDomain, startDate, endDate, areaCode,channelCode,activityId);
    }
}
