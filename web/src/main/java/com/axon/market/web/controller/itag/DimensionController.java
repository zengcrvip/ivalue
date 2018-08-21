package com.axon.market.web.controller.itag;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.icommon.IdAndNameDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.itag.DimensionDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.itag.DimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/4.
 */
@Controller("dimensionController")
public class DimensionController
{
    @Autowired
    @Qualifier("dimensionService")
    private DimensionService dimensionService;

    @RequestMapping(value = "queryDimensionsByPage.view")
    @ResponseBody
    public Table queryDimensionsByPage(@RequestParam Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer start = Integer.parseInt(param.get("start"));
        Integer length = Integer.parseInt(param.get("length"));
        return dimensionService.queryDimensionsByPage(start, length);
    }

    @RequestMapping(value = "addOrEditDimension.view")
    @ResponseBody
    public Operation addOrEditDimension(@RequestBody DimensionDomain dimensionDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return dimensionService.addOrEditDimension(dimensionDomain);
    }

    @RequestMapping(value = "deleteDimension.view")
    @ResponseBody
    public Operation deleteDimension(@RequestBody DimensionDomain dimensionDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return dimensionService.deleteDimension(dimensionDomain.getId());
    }

    @RequestMapping(value = "queryAllDimensionIdAndNames.view")
    @ResponseBody
    public List<IdAndNameDomain> queryAllDimensionIdAndNames(HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return dimensionService.queryAllDimensionIdAndNames();
    }

    @RequestMapping(value = "queryDimensionValueById.view")
    @ResponseBody
    public Table queryDimensionValueById(@RequestParam Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer id = Integer.parseInt(param.get("id"));
        return dimensionService.queryDimensionValueById(id);
    }

    @RequestMapping(value = "queryAllDimensions.view")
    @ResponseBody
    public Map<String,Object> queryAllDimensions(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        return dimensionService.queryAllDimensions();
    }
}
