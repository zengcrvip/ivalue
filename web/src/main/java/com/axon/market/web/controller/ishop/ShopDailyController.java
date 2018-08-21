package com.axon.market.web.controller.ishop;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.ishop.ShopDailyService;
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
 * Created by hale on 2017/4/25.
 */
@Controller("shopDailyController")
public class ShopDailyController
{

    private static final Logger LOG = Logger.getLogger(ShopDailyController.class);

    @Autowired
    @Qualifier("shopDailyService")
    private ShopDailyService shopDailyService;

    @RequestMapping(value = "queryShopDailyByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryShopDailyByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        List<Map<String, Object>> shopDailyList = shopDailyService.queryShopDailyByPage(paras);
        Integer itemCounts = shopDailyList.size();
        return new Table(shopDailyList, itemCounts);
    }

    @RequestMapping(value = "queryShopDailyTotal.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryShopDailyTotal(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = shopDailyService.queryShopDailyTotal(paras);
        return result;
    }

    /**
     * 地市周报 导出excel
     *
     * @param params
     * @param request
     * @param response
     * @param session
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = " exportShopDaily.view", method = RequestMethod.POST)
    public void exportShopDaily(@RequestParam Map<String, Object> params, HttpServletRequest request,
                                HttpServletResponse response, HttpSession session)
    {
        String type = String.valueOf(params.get("type"));
        String areaName = String.valueOf(params.get("areaName"));

        String menuName = "0".equals(type) ? "地市日报" : "营业厅日报";
        String dateTime = String.valueOf(params.get("dateTime"));
        String dateName = ("".equals(dateTime) || "null".equals(dateTime)) ? "全部" : dateTime;
        String fileName = menuName + "-" + dateName + "-" + areaName + "-" + System.currentTimeMillis();

        List<Map<String, Object>> listPage = shopDailyService.queryShopDailyByPage(params);
        Map<String, Object> listTotal = shopDailyService.queryShopDailyTotal(params);

        if (null != listPage && null != listTotal)
        {
            HSSFWorkbook workbook = shopDailyService.getShopDailyExcelData(listPage, listTotal, menuName);
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
