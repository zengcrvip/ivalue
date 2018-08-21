package com.axon.market.web.controller.ishop;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.ishop.ShopWeeklyService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by hale on 2017/5/8.
 */
@Controller("shopWeeklyController")
public class ShopWeeklyController
{
    @Autowired
    @Qualifier("shopWeeklyService")
    private ShopWeeklyService shopWeeklyService;

    Logger LOG = Logger.getLogger(ShopWeeklyController.class.getName());

    /**
     * 查询周报
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopWeeklyByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryShopWeeklyByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        List<Map<String, Object>> shopDailyList = shopWeeklyService.queryShopWeeklyByPage(paras);
        Integer itemCounts = shopDailyList==null?0:shopDailyList.size();
        return new Table(shopDailyList, itemCounts);
    }

    /**
     * 查询周报合计
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopWeeklyTotal.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryShopWeeklyTotal(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = shopWeeklyService.queryShopWeeklyTotal(paras);
        return result;
    }

    /**
     * 导出周报报表
     * @param params
     * @param request
     * @param response
     * @param session
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("exportShopWeekly.view")
    public void exportShopWeekly(@RequestParam Map<String, Object> params, HttpServletRequest request,
                                 HttpServletResponse response, HttpSession session){
        String type = String.valueOf(params.get("type"));
        String areaName = String.valueOf(params.get("areaName"));
        String menuName = "0".equals(type) ? "地市周报" : "营业厅周报";
        String dateTime = String.valueOf(params.get("dateTime"));
        String dateName = ("".equals(dateTime) || "null".equals(dateTime)) ? "全部" : dateTime;
        String fileName = menuName + "-" + dateName + "-" + areaName + "-" + System.currentTimeMillis();
        List<Map<String, Object>> listPage = shopWeeklyService.queryShopWeeklyByPage(params);
        Map<String, Object> listTotal = shopWeeklyService.queryShopWeeklyTotal(params);
        if (null != listPage && null != listTotal){
            HSSFWorkbook workbook = shopWeeklyService.getShopWeeklyExcelData(listPage, listTotal, menuName);
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
