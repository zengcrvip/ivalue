package com.axon.market.web.controller.istatistics;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.istatistics.MarketAnalysisService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/7/25.
 */
@Controller("MarketAnalysisController")
public class MarketAnalysisController {
    @Autowired
    @Qualifier("MarketAnalysisService")
    private MarketAnalysisService MarketAnalysisService;


    private static Logger LOG = Logger.getLogger(MarketAnalysisController.class.getName());


    //region  老方法注释
//    @RequestMapping(value = "queryMarketAssessmentByPage.view",method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String,Object> queryMarketAssessmentByPage(@RequestBody Map<String, Object> paras, HttpSession session) throws ParseException
//    {
//        Map<String, Object> result = new HashMap<String, Object>();
//        String sortCol = String.valueOf(paras.get("sortCol"));
//        int curPage = (Integer)(paras.get("curPage"));
//        int countsPerPage = (Integer)(paras.get("countsPerPage"));
//        Map<String, Object> option = (Map<String, Object>) paras.get("option");
//        Integer itemCounts = MarketAnalysisService.queryAllMarketAnalysisCounts(option);
//        List<Map<String,String>> marketAnalysisResult = MarketAnalysisService.queryAllMarketAnalysisByPage((curPage - 1) * countsPerPage, countsPerPage, sortCol, option);
//        result.put("itemCounts",itemCounts);
//        result.put("items",marketAnalysisResult);
//        return result;
//    }


    //    @RequestMapping(value = "queryMarketUserDetailsByPage.view")
//    @ResponseBody
//    public Map<String,Object> queryMarketUserDetailsByPage(@RequestBody Map<String, Object> paras, HttpSession session)
//    {
//        Map<String, Object> result = new HashMap<String, Object>();
//        String sortCol = String.valueOf(paras.get("sortCol"));
//        int curPage = (Integer)(paras.get("curPage"));
//        int countsPerPage = (Integer)(paras.get("countsPerPage"));
//        String taskId = (String) paras.get("taskId");
//        Integer itemCounts = MarketAnalysisService.queryAllMarketUserDetailsCounts(taskId);
//        List<Map<String,String>> marketAnalysisResult = MarketAnalysisService.queryMarketUserDetailsByPage((curPage - 1) * countsPerPage, countsPerPage, sortCol, taskId);
//        result.put("itemCounts",itemCounts);
//        result.put("items",marketAnalysisResult);
//        return result;
//    }

    //endregion

    /**
     * 修改人：dongtt
     * 修改时间：2017-07-31
     * 分页获取营销评估页面数据
     * @param paras
     * @param session
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "queryMarketAssessmentByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, String>> queryMarketAssessmentByPage(@RequestParam Map<String, Object> paras, HttpSession session) {
        int offset = Integer.parseInt(paras.get("start").toString());
        int limit = Integer.parseInt(paras.get("length").toString());
        Integer itemCounts = null;
        List<Map<String, String>> marketAnalysisResult = null;
        try {
            itemCounts = MarketAnalysisService.queryAllMarketAnalysisCounts(paras);
            marketAnalysisResult = MarketAnalysisService.queryAllMarketAnalysisByPage(offset, limit, paras);
        } catch (ParseException e) {
            LOG.error("queryMarketAssessmentByPage error", e);
            return new Table<Map<String, String>>();
        }
        return new Table<Map<String, String>>(marketAnalysisResult, itemCounts);
    }


    /**
     * 修改人：dongtt
     * 修改时间：2017-08-01
     * 分页获取营销任务对应的具体用户信息
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryMarketUserDetailsByPage.view",method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String, String>> queryMarketUserDetailsByPage(@RequestParam Map<String, Object> paras, HttpSession session) {
        int offset = Integer.parseInt(String.valueOf(paras.get("start")));
        int limit = Integer.parseInt(String.valueOf(paras.get("length")));
        String taskId = String.valueOf(paras.get("taskId"));
        Integer itemCounts = null;
        List<Map<String, String>> marketAnalysisResult = null;
        try {
            itemCounts = MarketAnalysisService.queryAllMarketUserDetailsCounts(taskId);
            marketAnalysisResult = MarketAnalysisService.queryMarketUserDetailsByPage(offset, limit, taskId);
        } catch (Exception e) {
            LOG.error("queryMarketUserDetailsByPage error", e);
            e.printStackTrace();
        }
        return new Table<Map<String, String>>(marketAnalysisResult, itemCounts);
    }
}
