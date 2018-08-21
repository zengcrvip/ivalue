package com.axon.market.web.controller.icommon;

import com.axon.market.common.domain.icommon.AreaDomain;
import com.axon.market.common.domain.icommon.NodeDomain;
import com.axon.market.core.service.icommon.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/1/17.
 */
@Controller
public class AreaController
{
    @Autowired
    @Qualifier("areaService")
    private AreaService areaService;

    @RequestMapping(value = "queryUserAreas.view", method = RequestMethod.POST)
    @ResponseBody
    public List<NodeDomain> queryUserAreas(@RequestBody Map<String,Object> params, HttpSession session)
    {
        return areaService.queryUserAreas();
    }

    @RequestMapping(value = "queryAllAreas.view", method = RequestMethod.POST)
    @ResponseBody
    public List<AreaDomain> queryAllAreas(@RequestBody Map<String,Object> params, HttpSession session)
    {
        return areaService.queryAllAreas();
    }

    @RequestMapping(value = "queryUserAreasCode.view", method = RequestMethod.POST)
    @ResponseBody
    public List<NodeDomain> queryUserAreasCode(@RequestBody Map<String,Object> params, HttpSession session)
    {
        return areaService.queryUserAreasCode();
    }
}
