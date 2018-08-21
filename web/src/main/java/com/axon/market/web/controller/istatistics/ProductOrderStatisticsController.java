package com.axon.market.web.controller.istatistics;

import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.axon.market.core.service.istatistics.ProductOrderStatisticsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/7/24.
 */
@Controller("productOrderStatisticsController")
public class ProductOrderStatisticsController
{
    @Autowired
    @Qualifier("productOrderStatisticsService")
    private ProductOrderStatisticsService productOrderStatisticsService;

    /**
     * 查询4G首登网相关数据
     *@param(startTime,endTime)
     *
     * @return
     */
    @RequestMapping(value = "queryProductOrderStatisticsByPage.view",method = RequestMethod.POST)
    @ResponseBody
    public Table queryProductOrderStatisticsByPage(@RequestParam Map<String, Object> paras, HttpSession session,
                                                   @RequestParam("start") Integer offset,
                                                   @RequestParam("length") Integer limit)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        // UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_MARKET_PRODUCT_MANAGEMENT_INDEX.getValue()});
//        Map<String, Object> result = new HashMap<String, Object>();
//        int curPage = 0;//(Integer) (paras.get("curPage"));
//        int countsPerPage = 10;//(Integer) (paras.get("countsPerPage"));
        String startT = paras.get("startTime") == null?null:String.valueOf(paras.get("startTime"));
        String endT = paras.get("endTime") == null?null:String.valueOf(paras.get("endTime"));
        String city = paras.get("areaValue")!=null?String.valueOf(paras.get("areaValue")):"-1";
        String startTime="";
        String endTime="";

        if (StringUtils.isNotEmpty(startT))
        {
            startTime=startT.replace("-","").substring(0,8);
        }
        if (StringUtils.isNotEmpty(endT))
        {
            endTime=endT.replace("-","").substring(0,8);
        }

        List<Map<String,Object>> marketProductDomainList = productOrderStatisticsService.queryProductOrderStatisticsByPage(startTime, endTime, city, offset, limit);
        Integer itemCounts = productOrderStatisticsService.queryProductOrderStatisticsCounts(startTime, endTime, city);
        return new Table(marketProductDomainList,itemCounts);
//        result.put("itemCounts", itemCounts);
//        result.put("items", marketProductDomainList);
//        return result;
    }

    @RequestMapping(value = "queryMarketAreas.view", method = RequestMethod.POST)
    @ResponseBody
    public String queryMarketAreas(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String areaIds = String.valueOf(paras.get("AREA_IDS"));
        return productOrderStatisticsService.queryMarketAreas(areaIds);
    }

}
