package com.axon.market.web.controller.istatistics;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.istatistics.AnalysisFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bry08ant on 2016/9/19.
 */
@Controller("insightAnalysis")
public class InsightAnalysisController
{
    @Autowired
    @Qualifier("analysisFlowService")
    private AnalysisFlowService analysisFlowService;

    @RequestMapping(value = "queryAnalysisFlowRateByUDate.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryAnalysisFlowRateByUDate(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_FLOW_ANALYSIS.getValue()});

        Map<String, Object> result = new HashMap<String, Object>();
        String yearMonth = String.valueOf(paras.get("yearMonth"));
        List<Map<String,String>> analysisFlowRateList = analysisFlowService.queryAnalysisFlowRate(yearMonth);
        Map<String,String[]> analysisFlowGrow = analysisFlowService.queryAnalysisFlowGrow(yearMonth);
        result.put("flowData",analysisFlowRateList);
        result.put("flowContrast",analysisFlowGrow);
        return result;
    }

    @RequestMapping(value = "queryAnalysisFlowByUDate.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,String>> queryAnalysisFlowByUDate(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_FLOW_ANALYSIS.getValue()});

        String yearMonth = String.valueOf(paras.get("yearMonth"));
        return analysisFlowService.queryAnalysisFlow(yearMonth);
    }

    @RequestMapping(value = "queryTerminalPackageData.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryTerminalPackageData(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_TERMINAL_PACKAGE_COMPARISON.getValue()});
        Map<String, Object> result = new HashMap<String, Object>();
        String yearMonth = String.valueOf(paras.get("yearMonth"));
        Map<String, Object> proportionOfCityUser = analysisFlowService.queryProportionOfCityUser(yearMonth);
        Map<String, Object> proportionOfPackage = analysisFlowService.queryProportionOfPackage(yearMonth);
        result.put("cityUser",proportionOfCityUser);
        result.put("terminalPackage",proportionOfPackage);
        return result;
    }

//
//    @RequestMapping(value = "queryTerminalBrandData.view", method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String,Object> queryTerminalBrandData(@RequestBody Map<String, Object> paras,HttpSession session)
//    {
//        UserDomain userDomain = UserUtils.getLoginUser(session);
////        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_TERMINAL_BRAND_ANALYSIS.getValue()});
//        Map<String, Object> result = new HashMap<String, Object>();
//        int curPage =(Integer) (paras.get("curPage"));
//        int countsPerPage = (Integer) (paras.get("countsPerPage"));
//        Map<String, Object> option = (Map<String, Object>) paras.get("option");
//        Integer itemCounts = analysisFlowService.queryTerminalBrandCounts(option);
//        List<Map<String,String>> terminalBrandList = analysisFlowService.queryTerminalBrandData((curPage - 1) * countsPerPage, countsPerPage, option);
//        result.put("itemCounts", itemCounts);
//        result.put("items", terminalBrandList);
//        return result;
//    }
//
//    @RequestMapping(value = "queryTerminalModelData.view", method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String,Object> queryTerminalModelData(@RequestBody Map<String, Object> paras,HttpSession session)
//    {
//        UserDomain userDomain = UserUtils.getLoginUser(session);
////        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_TERMINAL_MODEL_ANALYSIS.getValue()});
//        Map<String, Object> result = new HashMap<String, Object>();
//        int curPage =(Integer) (paras.get("curPage"));
//        int countsPerPage = (Integer) (paras.get("countsPerPage"));
//        Map<String, Object> option = (Map<String, Object>) paras.get("option");
//        Integer itemCounts = analysisFlowService.queryTerminalModelCounts(option);
//        List<Map<String,String>> terminalModelList = analysisFlowService.queryTerminalModelData((curPage - 1) * countsPerPage, countsPerPage, option);
//        result.put("itemCounts", itemCounts);
//        result.put("items", terminalModelList);
//        return result;
//    }
//
//    @RequestMapping(value = "queryTerminalSystemData.view", method = RequestMethod.POST)
//    @ResponseBody
//    public Map<String,Object> queryTerminalSystemData(@RequestBody Map<String, Object> paras,HttpSession session)
//    {
//        UserDomain userDomain = UserUtils.getLoginUser(session);
////        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_TERMINAL_SYSTEM_ANALYSIS.getValue()});
//        Map<String, Object> result = new HashMap<String, Object>();
//        int curPage =(Integer) (paras.get("curPage"));
//        int countsPerPage = (Integer) (paras.get("countsPerPage"));
//        Map<String, Object> option = (Map<String, Object>) paras.get("option");
//        Integer itemCounts = analysisFlowService.queryTerminalSystemCounts(option);
//        List<Map<String,String>> terminalSystemList = analysisFlowService.queryTerminalSystemData((curPage - 1) * countsPerPage, countsPerPage, option);
//        for (Map<String,String> map : terminalSystemList){
//            String net ="";
//            String mobile_type=map.get("net");
//            String[][] _mobile = {{ "CDMA", "1" }, { "GSM", "2" }, { "TD-CDMA", "4" }, { "CDMA-2000", "8" }, { "WCDMA", "16" }, { "TD-LTE", "32" }, {"FD-LTE", "64" }};
//            for (int i = 0; i < _mobile.length; i++)
//            {
//                if (mobile_type == "")
//                {
//                    net = "未知,";
//                }
//                else if ((Integer.parseInt(mobile_type) & Integer.parseInt(_mobile[i][1])) != 0)
//                {
//                    net += _mobile[i][0] + ",";
//                }
//            }
//            net=net.substring(0,net.length()-1)+"手机";
//            map.put("net",net);
//        }
//        result.put("itemCounts", itemCounts);
//        result.put("items", terminalSystemList);
//        return result;
//    }
//
    @RequestMapping(value = "queryStockUserDaily.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryStockUserDaily(@RequestBody Map<String, Object> paras,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_STOCK_USER_DAILY.getValue()});

        String tableName = String.valueOf(paras.get("tableName"));
        return analysisFlowService.queryStockUserDaily(tableName);
    }

    @RequestMapping(value = "queryMonthlyReservedData.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,String>> queryMonthlyReservedData(@RequestBody Map<String, Object> paras,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_MONTHLY_RESERVED.getValue()});

        String year = String.valueOf(paras.get("year"));
        String month = String.valueOf(paras.get("month"));
        String yearMonth = month.length()<2? year+"0"+month : year+month;
        return analysisFlowService.queryMonthlyReservedData(yearMonth);
    }

    @RequestMapping(value = "queryBaseMonthlyReservedData.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryBaseMonthlyReservedData(@RequestBody Map<String, Object> paras,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_BASE_MONTHLY_RESERVED.getValue()});

        Map<String,Object> result = new HashMap<String,Object>();
        String year = String.valueOf(paras.get("year"));
        String month = String.valueOf(paras.get("month"));
        String city = String.valueOf(paras.get("city"));
        String yearMonth = month.length()<2? year+"0"+month : year+month;
        List<Map<String,Object>> resultData = analysisFlowService.queryBaseMonthlyReservedDataResult(yearMonth, city);
        List<Map<String,Object>> processData = analysisFlowService.queryBaseMonthlyReservedDataProcess(yearMonth, city);
        result.put("result",resultData);
        result.put("process",processData);
        return result;
    }

    @RequestMapping(value = "queryStockUserIncomeData.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,String>> queryStockUserIncomeData(HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_STOCK_USER_INCOME.getValue()});

        return analysisFlowService.queryStockUserIncomeData();
    }

    @RequestMapping(value = "queryBehaviorPreferences.view", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,String>> queryBehaviorPreferences(@RequestBody Map<String, Object> paras,HttpSession session)
    {
        String yearMonth = String.valueOf(paras.get("yearMonth"));
        return analysisFlowService.queryBehaviorPreferences(yearMonth);
    }

    @RequestMapping(value = "downloadAnalysisDaily.view", method = RequestMethod.POST)
    public void downloadSegment(HttpServletRequest request, HttpServletResponse response, String dailyType) throws Exception
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_STOCK_USER_DAILY.getValue()});
        analysisFlowService.downloadAnalysisDaily(response, dailyType);
    }

    @RequestMapping(value = "downloadMonthlyReserved.view", method = RequestMethod.POST)
   public void downloadMonthlyReserved(HttpServletRequest request, HttpServletResponse response, String yearMonth) throws Exception
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_MONTHLY_RESERVED.getValue()});
        analysisFlowService.downloadMonthlyReserved(response, yearMonth);
    }

    @RequestMapping(value = "downloadBaseMonthlyReserved.view", method = RequestMethod.POST)
    public void downloadBaseMonthlyReserved(HttpServletRequest request, HttpServletResponse response, String yearMonth,String cityId,String cityName) throws Exception
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_BASE_MONTHLY_RESERVED.getValue()});
        analysisFlowService.downloadBaseMonthlyReserved(response, yearMonth, cityId);
    }

    @RequestMapping(value = "downloadIncomeSituation.view", method = RequestMethod.POST)
    public void downloadIncomeSituation(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        UserDomain userDomain = UserUtils.getLoginUser(request.getSession());
//        UserUtils.assertUserPermissions(userDomain, new String[]{UserConstants.UserPermission.PERMISSION_STOCK_USER_INCOME.getValue()});
        analysisFlowService.downloadIncomeSituation(response);
    }
}
