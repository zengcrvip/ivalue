package com.axon.market.web.controller.istatistics;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.istatistics.TDCMCityReportService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by wangtt on 2017/6/26.
 */

@Controller("TDCMCityReportController")
public class TDCMCityReportController
{
    @Autowired
    @Qualifier("TDCMCityReportService")
    TDCMCityReportService tdcmCityReportService;

    private  static  Logger LOG = Logger.getLogger(TDCMCityReportController.class.getName());

    /**
     * 查询B2I地市质态分析
     * @param param
     * @return
     */
    @RequestMapping(value = "queryB2ICityReport.view",method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String,Object>> queryB2ICityReport(@RequestParam Map<String,Object> param){
        return tdcmCityReportService.queryB2ICityReport(param);
    }

    @RequestMapping(value = "downloadB2ICityReport.view",method = RequestMethod.POST)
    public void downloadB2ICityReport(@RequestParam Map<String,Object> param,HttpServletRequest request,HttpServletResponse response,HttpSession session){
        try
        {
            tdcmCityReportService.downloadB2ICityReport(param,request,response,session);
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.error("下载B2I地市质态分析报表异常：",e);
        }
    }

}
