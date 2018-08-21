//package com.axon.market.web.controller.ikeeper;
//
//import com.axon.market.common.bean.ResultVo;
//import com.axon.market.common.domain.imodel.ModelDomain;
//import com.axon.market.common.domain.isystem.UserDomain;
//import com.axon.market.common.util.JsonUtil;
//import com.axon.market.common.util.UserUtils;
//import com.axon.market.core.service.icommon.AreaService;
//import com.axon.market.core.service.ikeeper.KeeperMgrService;
//import com.axon.market.core.service.imodel.ModelService;
//import com.axon.market.core.service.ikeeper.KeeperProductService;
//import com.axon.market.core.service.isystem.UserService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by yuanfei on 2017/4/24.
// */
//@Controller
//public class KepperForAppController
//{
//    @Autowired
//    @Qualifier("areaService")
//    private AreaService areaService;
//
//    @Autowired
//    @Qualifier("userService")
//    private UserService userService;
//
//    @Autowired
//    @Qualifier("keeperMgrService")
//    private KeeperMgrService keeperMgrService;
//
//    @Autowired
//    @Qualifier("modelService")
//    private ModelService modelService;
//
//    @Autowired
//    @Qualifier("shopKeeperProductService")
//    private KeeperProductService shopKeeperProductService;
//
//
//    /**
//     * @param param
//     * @return
//     */
//    @RequestMapping(value = "fetchAreaOverview.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public ResultVo fetchAreaOverview(@RequestBody Map<String, String> param)
//    {
//        ResultVo result = new ResultVo();
//        String token = param.get("token");
//        UserDomain userDomain = userService.queryUserByToken(token);
//        List<Map<String, Object>> list = areaService.queryAreaByToken(userDomain);
//        result.setResultObj(list);
//        return result;
//    }
//
//    /**
//     * @param param
//     * @return
//     */
//    @RequestMapping(value = "fetchChannelOverview.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public ResultVo fetchChannelOverview(@RequestBody Map<String, String> param)
//    {
//        ResultVo result = new ResultVo();
//        String token = param.get("token");
//        UserDomain userDomain = userService.queryUserByToken(token);
//        List<Map<String, Object>> list = areaService.queryChannelByToken(userDomain);
//        result.setResultObj(list);
//        return result;
//    }
//
//    /**
//     * @param param
//     * @return
//     */
//    @RequestMapping(value = "fetchActivityOverview.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public ResultVo fetchActivityOverview(@RequestBody Map<String, String> param)
//    {
//        ResultVo result = new ResultVo();
//        String token = param.get("token");
//
//        List<Map<String, Object>> list = keeperMgrService.fetchActivities();
//        result.setResultObj(list);
//        return result;
//    }
//
//    /**
//     * @param param
//     * @param request
//     * @return
//     */
//    @RequestMapping(value = "fetchFeeOverview.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public ResultVo fetchFeeOverview(@RequestBody Map<String, String> param, HttpServletRequest request)
//    {
//        ResultVo result = new ResultVo();
//        String token = param.get("token");
//        String startDate = param.get("startDate");
//        String endDate = param.get("endDate");
//        String orgType = param.get("orgType");
//        String orgCode = param.get("orgCode");
//        String activityId = param.get("activityId");
//
//        UserDomain userDomain = userService.queryUserByToken(token);
//        Map<String, Object> list = keeperMgrService.fetchFee(userDomain, startDate, endDate, orgType, orgCode, activityId);
//        result.setResultObj(list);
//        return result;
//    }
//
//    /**
//     * @param param
//     * @return
//     */
//    @RequestMapping(value = "fetchAreaRank.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public ResultVo fetchAreaRank(@RequestBody Map<String, String> param)
//    {
//        ResultVo result = new ResultVo();
//        String token = param.get("token");
//        String startDate = param.get("startDate");
//        String endDate = param.get("endDate");
//
//        //查询条件
//        String areaCode = param.get("areaCode");
//        String activityId = param.get("activityId");
//
//        UserDomain userDomain = userService.queryUserByToken(token);
//        if (StringUtils.isNotEmpty(userDomain.getBusinessHallIds()))
//        {
//            result.setResultCode("3333");
//            result.setResultMsg("权限不足");
//            return result;
//        }
//        Map<String, Object> list = keeperMgrService.fetchAreaRank(userDomain, startDate, endDate, areaCode, activityId);
//        result.setResultObj(list);
//        return result;
//    }
//
//    /**
//     * @param param
//     * @return
//     */
//    @RequestMapping(value = "fetchChannelRank.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public ResultVo fetchChannelRank(@RequestBody Map<String, String> param)
//    {
//        ResultVo result = new ResultVo();
//        String token = param.get("token");
//        String startDate = param.get("startDate");
//        String endDate = param.get("endDate");
//
//        //查询条件
//        String areaCode = param.get("areaCode");
//        String channelCode = param.get("channelCode");
//        String activityId = param.get("activityId");
//
//        UserDomain userDomain = userService.queryUserByToken(token);
//        Map<String, Object> list = keeperMgrService.fetchChannelRank(userDomain, startDate, endDate, areaCode, channelCode, activityId);
//        result.setResultObj(list);
//        return result;
//    }
//
//    /**
//     * 登录用户信息查询
//     *
//     * @param param
//     * @return
//     */
//    @RequestMapping(value = "fetchUserOverview.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public ResultVo fetchUserOverview(@RequestBody Map<String, String> param)
//    {
//        ResultVo result = new ResultVo();
//        String token = param.get("token");
//        UserDomain userDomain = userService.queryUserByToken(token);
//        result.setResultObj(userDomain);
//        return result;
//    }
//
//    @RequestMapping(value = "queryMatchUserCountByRule", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public int queryMatchUserCountByRule(@RequestBody Map<String, Object> param) throws JsonProcessingException
//    {
//        String rules = JsonUtil.objectToString(param.get("rules"));
//        return keeperMgrService.queryMatchUserCountByRule(rules);
//    }
//
//    @RequestMapping(value = "editOrAddModelByKeeper.view", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultVo editOrAddModelByKeeper(@RequestBody ModelDomain modelDomain, HttpSession session)
//    {
//        UserDomain userDomain = UserUtils.getLoginUser(session);
//        return modelService.editOrAddModelByKeeper(modelDomain, userDomain);
//    }
//
//    /**
//     * 查询可赠送的福利产品
//     *
//     *
//     * @return
//     */
//    @RequestMapping(value = "queryAllProductOfShopKeeper.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    @ResponseBody
//    public ResultVo queryAllProductOfShopKeeper()
//    {
//        ResultVo result = new ResultVo();
//        result.setResultObj(shopKeeperProductService.queryProduct(null,null,"N"));
//        return result;
//    }
//}
