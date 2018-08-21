package com.axon.market.core.service.istatistics;

import com.axon.market.common.bean.Table;
import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import com.axon.market.common.util.excel.ExportUtils;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/6/26.
 */
@Service("TDCMCityReportService")
public class TDCMCityReportService
{
    private static Logger LOG = Logger.getLogger(TDCMCityReportService.class.getName());

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    String[] fields = new String[]{"cityname","cz_user_num","cz_user_rate","arpu","mou","dou","rate_4g","rate_4g_1st","stop_rate","cash_1st_stop_rate","churn_rate","cash_1st_churn_rate"};

    /**
     * 查询质态分析地市报表
     *
     * @param param month 为数据账期  格式：201705
     *              city 为地市名称  格式：南京   ps:地市条件不带‘市’字
     * @return
     */
    public Table<Map<String, Object>> queryB2ICityReport(Map<String, Object> param)
    {
        String month = String.valueOf(param.get("monthId"));
        String city = String.valueOf(param.get("cityName"));
        if (StringUtils.isEmpty(month) || StringUtils.isEmpty(city))
        {
            return new Table<>();
        }
        final String sql = "select  case  when city_name = '合计' then 10 else 1 end as dsc,city_name as cityName,round(cast(arpu as numeric),2) as arpu,round(cast(mou as numeric),2) as mou,round(cast(dou as numeric),2) as dou,round(cast(rate_4g*100 as numeric),2) || '%' as rate_4g,round(cast(stop_rate*100 as numeric),2) || '%' as stop_rate,round(cast(cash_1st_stop_rate*100 as numeric),2) || '%' as cash_1st_stop_rate,round(cast(churn_rate*100 as numeric),2) || '%' as churn_rate,round(cast(cash_1st_churn_rate*100 as numeric),2) || '%' as cash_1st_churn_rate,month_id as monthId from uapp.tdcm_city_report where 1=1 ";
        final String sql2 = "SELECT CASE WHEN city_name = '合计' THEN 10 ELSE 1 END AS dsc, city_name AS cityName,round(cast(cz_user_num AS NUMERIC), 2) AS cz_user_num,\n" +
                " round(cast(cz_user_rate * 100 AS NUMERIC),2) || '%' AS cz_user_rate,\n" +
                " round(cast(arpu AS NUMERIC), 2) AS arpu,\n" +
                " round(cast(mou AS NUMERIC), 2) AS mou,\n" +
                " round(cast(dou AS NUMERIC), 2) AS dou,\n" +
                " round(cast(rate_4g * 100 AS NUMERIC),2) || '%' AS rate_4g,\n" +
                " round(cast(cash_1st_rate_4g * 100 AS NUMERIC),2) || '%' AS rate_4g_1st,\n" +
                " round(cast(stop_rate * 100 AS NUMERIC),2) || '%' AS stop_rate,\n" +
                " round(cast(cash_1st_stop_rate * 100 AS NUMERIC),2) || '%' AS cash_1st_stop_rate,\n" +
                " round(cast(churn_rate * 100 AS NUMERIC),2) || '%' AS churn_rate,\n" +
                " round(cast(cash_1st_churn_rate * 100 AS NUMERIC),2) || '%' AS cash_1st_churn_rate,\n" +
                " month_id AS monthId FROM uapp.tdcm_city_report where 1=1 ";
        String citySql = "";
        if (!"合计".equals(city))
        {
            citySql = " and city_name = \'" + city+"\' ";
        }
        String finalSql = sql2 + citySql + " and month_id = "+month+" order by dsc";
        List<Map<String, Object>> dataList;
        int count;
        try
        {
            dataList = greenPlumOperateService.query(finalSql);
            count = dataList.size();
        }
        catch (Exception e)
        {
            LOG.error("查询B2I地市报表异常：", e);
            return new Table<>();
        }
        return new Table<Map<String, Object>>(dataList, count);
    }

    /**
     * 下载B2I地市质态分析报表
     * @param param
     * @param request
     * @param response
     * @param session
     * @throws UnsupportedEncodingException
     */
    public void downloadB2ICityReport(Map<String,Object> param,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws UnsupportedEncodingException
    {
        String city= URLDecoder.decode(String.valueOf(param.get("cityName")), "UTF-8");
        String month = String.valueOf(param.get("monthId"));
        param.put("cityName",city);
        Table<Map<String,Object>> table =  queryB2ICityReport(param);
        List<Map<String,Object>> dataList  = table.data;
        if("合计".equals(city)){
           city = "全省";
        }
        String fileName = month + city+"B2I地市质态分析报表";
        List<ExcelRowEntity> excelDataList = getExcelData(dataList);
        ExportUtils.getInstance().exportData(fileName, excelDataList, request, response);
    }


    /**
     * 私有方法，生成EXECl导出对象
     *
     * @param dataList 源数据
     * @return
     */
    private List<ExcelRowEntity> getExcelData(List<Map<String, Object>> dataList)
    {
        List<ExcelRowEntity> result = new ArrayList<ExcelRowEntity>();
        // 表头处理-----------------------------------------------
        ExcelRowEntity header = new ExcelRowEntity();
        result.add(header);
        header.setRowType(2);
        List<ExcelCellEntity> cellEntityList1 = new ArrayList<ExcelCellEntity>();
        header.setCellEntityList(cellEntityList1);
        cellEntityList1.add(new ExcelCellEntity(1, 1, "地市"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "出账用户数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "出账用户占比"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "ARPU"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "MOU"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "DOU"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "4G登网率"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "4G首充登网率"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "停机率"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "已首充停机率"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "离网率"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "已首充离网率"));

        // ----------------数据行--------------------------------------------
        if (null != dataList && dataList.size() > 0)
        {
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> rowData = dataList.get(i);
                ExcelRowEntity excelRow = new ExcelRowEntity();
                List<ExcelCellEntity> cellEntityList = new ArrayList<ExcelCellEntity>();
                excelRow.setCellEntityList(cellEntityList);
                excelRow.setRowType(-1);
                for (int j = 0; j < fields.length; j++)
                {
                    String value = "";
                    if (null != rowData.get(fields[j]))
                    {
                        value = "" + rowData.get(fields[j]);
                    }
                    cellEntityList.add(new ExcelCellEntity(1, 1, value));
                }
                result.add(excelRow);
            }
        }

        return result;
    }

}
