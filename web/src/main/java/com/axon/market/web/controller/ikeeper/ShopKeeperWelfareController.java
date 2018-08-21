package com.axon.market.web.controller.ikeeper;

import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.ishopKeeper.ShopKeeperWelfareDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.ikeeper.KeeperWelfareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhuwen on 2017/8/21.
 */
@Controller("shopKeeperWelfareController")
public class ShopKeeperWelfareController
{
    @Autowired
    @Qualifier("keeperWelfareService")
    private KeeperWelfareService keeperWelfareService;

    /**
     * 各种条件查询福利列表信息
     *
     * @param paras <br/>
     * @return
     */
    @RequestMapping(value = "queryWelfareOfShopKeeper.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<ShopKeeperWelfareDomain> queryWelfareOfShopKeeper(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer areaId = userDomain.getAreaId();
        Integer welfareId = paras.get("welfareId") == null ? null : Integer.parseInt(String.valueOf(paras.get("welfareId")));
        Integer typeId = paras.get("typeId") == null ? null : Integer.parseInt(String.valueOf(paras.get("typeId")));
        String welfareName = SearchConditionUtil.optimizeCondition(String.valueOf(paras.get("welfareName"))).trim();
        Integer limit = paras.get("length") == null ? null : Integer.valueOf(String.valueOf(paras.get("length")));
        Integer offset = paras.get("start") == null ? null : Integer.valueOf(String.valueOf(paras.get("start")));
        String netType = paras.get("netType") == null ? null :(String)paras.get("netType");

        return keeperWelfareService.queryWelfare(welfareId, welfareName, typeId, limit, offset, areaId, netType);
    }

    /**
     * 根据福利id查询福利信息
     *
     * @param paras <br/>
     * @return
     */
    @RequestMapping(value = "queryWelfareById.view", method = RequestMethod.POST)
    @ResponseBody
    public ShopKeeperWelfareDomain queryWelfareById(@RequestBody Map<String, Object> paras)
    {
        Integer welfareId = Integer.parseInt(String.valueOf(paras.get("welfareId")));
        return keeperWelfareService.queryWelfareById(welfareId);
    }

    /**
     * 新增福利信息
     *
     * @param shopKeeperWelfareDomain
     * @return
     */
    @RequestMapping(value = "addWelfareOfShopKeeper.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult addWelfareOfShopKeeper(@RequestBody ShopKeeperWelfareDomain shopKeeperWelfareDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        shopKeeperWelfareDomain.setAreaId(userDomain.getAreaId());
        shopKeeperWelfareDomain.setCreateUserId(userDomain.getId());
        return keeperWelfareService.addWelfare(shopKeeperWelfareDomain);
    }

    /**
     * 编辑福利信息
     *
     * @param shopKeeperWelfareDomain
     * @return
     */
    @RequestMapping(value = "updateWelfareOfShopKeeper.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateWelfareOfShopKeeper(@RequestBody ShopKeeperWelfareDomain shopKeeperWelfareDomain)
    {
        return keeperWelfareService.updateWelfare(shopKeeperWelfareDomain);
    }

    /**
     * 删除福利信息
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "deleteWelfareOfShopKeeper.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteWelfareOfShopKeeper(@RequestBody Map<String, Object> paras)
    {
        Integer welfareId = Integer.parseInt(String.valueOf(paras.get("welfareId")));
        return keeperWelfareService.deleteWelfare(welfareId);
    }

    /**
     * 赠送福利信息
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "giveCustWelfare.view", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo giveCustWelfare(@RequestBody Map<String, Object> paras)
    {
        if (paras.get("welfareId") == null)
        {
            return new ResultVo("-1", "未提供赠送的福利");
        }
        String welfareId = (String) paras.get("welfareId");
        Integer userId = Integer.valueOf((String) paras.get("userId"));
        String telephone = (String) paras.get("telephone");
        Boolean autograph = paras.get("autograph").equals("Y") ? true : false;

        return keeperWelfareService.giveCustWelfare(welfareId, userId, telephone, autograph, true);
    }

    @RequestMapping(value = "queryKeeperPhonelist.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, Object>> queryKeeperPhonelist(@RequestParam Map<String, Object> paras)
    {
        Integer welfareId = Integer.valueOf((String) paras.get("welfareId"));
        Integer userId = Integer.valueOf((String) paras.get("userId"));
        return keeperWelfareService.queryKeeperPhonelist(welfareId, userId);
    }


    /**
     * 查询可赠送的福利
     *
     * @return
     */
    @RequestMapping(value = "queryAvailWelfare.app", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResultVo queryAvailWelfare()
    {
        ResultVo result = new ResultVo();
        List<Map<String, Object>> list = keeperWelfareService.queryWelfareForApp();
        result.setResultObj(list);
        return result;
    }
}
