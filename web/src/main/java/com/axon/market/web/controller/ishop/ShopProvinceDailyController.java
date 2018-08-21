package com.axon.market.web.controller.ishop;

import com.axon.market.common.bean.Table;
import com.axon.market.core.service.ishop.ShopProvinceDailyService;
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
 * Created by dtt on 2017/7/10.
 * 炒店2.0省份日报controller
 */
@Controller("shopProvinceDailyController")
public class ShopProvinceDailyController
{
    private static final Logger LOG = Logger.getLogger(ShopProvinceDailyController.class);

    @Autowired
    @Qualifier("shopProvinceDailyService")
    private ShopProvinceDailyService shopProvinceDailyService;



    /**
     * 查询省份日报
     * 创建人：dongtt
     * 创建时间：2017-07-12
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryShopProvinceDaily.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryShopProvinceDaily(@RequestBody Map<String, Object> paras)
    {
        String dateTime = String.valueOf(paras.get("dateTime"));
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isEmpty(dateTime))
        {
            return map;
        }
        map.put("items", shopProvinceDailyService.queryShopProvinceDaily(dateTime));
        return map;
    }

    /**
     * 下载省份日报报表
     * @param dateTime
     * @param request
     * @param response
     * @param session
     */
    @RequestMapping(value = "exportShopProvinceDaily.view", method = RequestMethod.POST)
    public void exportShopProvinceDaily(@RequestParam("dateTime") String dateTime, HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        if(StringUtils.isEmpty(dateTime)){
            LOG.error("download ShopProvinceDaily error:dateTime is empty");
            dateTime = "";
        }
        String fileName = dateTime + "省分日报";
        //查出结果集
        List<Map<String, Object>> dataList  = shopProvinceDailyService.queryShopProvinceDaily(dateTime);
        if(dataList != null){
            HSSFWorkbook hssfWorkbook = shopProvinceDailyService.getShopProvinceDailyExcelData(dataList,fileName);
            OutputStream out = null;
            try
            {
                out = response.getOutputStream();
                response.setContentType("application/vnd.ms-excel");
                fileName = URLEncoder.encode(fileName, "UTF-8");
                response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
                hssfWorkbook.write(out);
            }
            catch (Exception e)
            {
                LOG.error("导出省份日报数据异常：" + e.getMessage());
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
                        LOG.error("导出省份日报数据异常：" + e.getMessage());
                    }
                }
            }
        }
    }


}
