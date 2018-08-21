package com.axon.market.web.controller.istatistics;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.istatistics.TDCMCityReportService;
import com.axon.market.core.service.istatistics.TDCMProductReportService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/6/28.
 */

@Controller("TDCMProductReportController")
public class TDCMProductReportController
{

    @Autowired
    @Qualifier("TDCMProductReportService")
    TDCMProductReportService tdcmProductReportService;

    private  static  Logger LOG = Logger.getLogger(TDCMProductReportController.class.getName());

    /**
     * 查询B2I产品报表
     * @param param
     * @return
     */
    @RequestMapping(value = "queryB2IProductReport.view",method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String,Object>> queryB2IProductReport(@RequestParam Map<String,Object> param){
        String month = String.valueOf(param.get("monthId"));
        String productType = String.valueOf(param.get("productType"));
        if(StringUtils.isEmpty(month) || StringUtils.isEmpty(productType)){
            return new Table<>();
        }
        int count = tdcmProductReportService.queryB2IProductReporCounts(param);
        List<Map<String,Object>> dataList = tdcmProductReportService.queryB2IProductReport(param);
        return  new Table<>(dataList,count);
    }

    @RequestMapping(value = "downloadB2IProductReport.view",method = RequestMethod.POST)
    public void downloadB2IProductReport(@RequestParam Map<String,Object> param,HttpServletRequest request,HttpServletResponse response){
        try
        {
            tdcmProductReportService.downloadB2IProductReport(param,request,response);
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("下载b2i产品质态分析报表失败：",e);
        }
    }

}
