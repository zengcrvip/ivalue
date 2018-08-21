package com.axon.market.web.controller.istatistics;

import com.axon.market.core.service.istatistics.TrafficOrderService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wangtt on 2017/6/19.
 */
@Controller("trafficOrderController")
public class TrafficOrderController
{
    @Autowired
    @Qualifier("trafficOrderService")
    TrafficOrderService trafficOrderService;

    private  static final Logger LOG = Logger.getLogger(TrafficOrderController.class.getName());

    /**
     * 查询流量包订购日报统计信息
     * @param param
     * @return
     */
    @RequestMapping(value = "queryDailyTrafficOrder.view",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryTrafficOrder(@RequestBody Map<String,Object> param){
        String startTime = String.valueOf(param.get("startTime"));
        String endTime = String.valueOf(param.get("endTime"));
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)){
            return map;
        }
        map.put("items",trafficOrderService.queryTrafficOrder(startTime,endTime));
        return map;
    }

    /**
     * 查询流量包月报统计信息
     * @param param
     * @return
     */
    @RequestMapping(value = "queryMonthlyTrafficOrder.view",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryMonthlyTrafficOrder(@RequestBody Map<String,Object> param){
        String startTime = String.valueOf(param.get("startTime"));
        String endTime = String.valueOf(param.get("endTime"));
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)){
            return map;
        }
        map.put("items",trafficOrderService.queryMonthlyTrafficOrder(startTime, endTime));
        return map;
    }

    /**
     * 查询流量包全省统计信息
     * @param param
     * @return
     */
    @RequestMapping(value = "queryProvincialTrafficOrder.view",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryProvincialTrafficOrder(@RequestBody Map<String,Object> param){
        int curPage = (Integer) (param.get("curPage"));
        int countsPerPage = (Integer) (param.get("countsPerPage"));
        String startTime = String.valueOf(param.get("startTime"));
        String endTime = String.valueOf(param.get("endTime"));
        Map<String,Object> map = new HashMap<String,Object>();
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)){
            return map;
        }
        int itemCounts = trafficOrderService.queryProvincialTrafficOrderCounts(startTime,endTime);
        List<Map<String, Object>> list = trafficOrderService.queryProvincialTrafficOrder(startTime, endTime, curPage, countsPerPage);
        map.put("items",list);
        map.put("itemCounts",itemCounts);
        return map;
    }


    /**
     * 下载
     * @param params
     * @param request
     * @param response
     * @param session
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "downloadTrafficOrder.view",method = RequestMethod.POST)
    public void downloadTrafficOrder(@RequestParam Map<String, Object> params, HttpServletRequest request,
                                     HttpServletResponse response, HttpSession session){
        List<Map<String,Object>> list = null;
        String startTime = String.valueOf(params.get("startTime"));
        String endTime = String.valueOf(params.get("endTime"));
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)){
            return;
        }
        String fileName = "default",menuName = "sheet1";
        int type = Integer.parseInt(String.valueOf(params.get("reportType")));
        if(type == 1){
            fileName = startTime + "至" + endTime +"流量包订购日报统计报表";
            menuName = "流量包订购日报";
            list = trafficOrderService.queryTrafficOrder(startTime, endTime);
        }else if(type == 2){
            fileName = startTime + "至" + endTime +"流量包订购月报统计报表";
            menuName = "流量包订购月报";
            list = trafficOrderService.queryMonthlyTrafficOrder(startTime, endTime);
        }else if(type == 3){
            fileName = startTime + "至" + endTime +"全省流量包订购统计报表";
            menuName = "全省流量包订购";
            list = trafficOrderService.queryProvincialTrafficOrder(startTime,endTime,null,null);
        }
        if(list != null){
            HSSFWorkbook workbook = trafficOrderService.getTrafficOrderExcelData(list,menuName,type);
            OutputStream out = null;
            try
            {
                out = response.getOutputStream();
                response.setContentType("application/vnd.ms-excel");
                fileName = URLEncoder.encode(fileName, "UTF-8");
                response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
                workbook.write(out);
            }
            catch (Exception e)
            {
                LOG.error("导出数据异常：" + e.getMessage());
            }
            finally
            {
                if (out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch (IOException e)
                    {
                        LOG.error("导出数据异常：" + e.getMessage());
                    }
                }
            }
        }


    }
}
