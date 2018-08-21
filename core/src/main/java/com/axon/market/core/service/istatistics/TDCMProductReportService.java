package com.axon.market.core.service.istatistics;

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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by wangtt on 2017/6/28.
 */
@Service("TDCMProductReportService")
public class TDCMProductReportService
{
    private static Logger LOG = Logger.getLogger(TDCMProductReportService.class.getName());

    String[] fields = new String[]{"item","productName","cz_user_num","cz_user_rate","arpu","mou","dou","rate_4g","rate_4g_1st","stop_rate","cash_1st_stop_rate","churn_rate","cash_1st_churn_rate"};

    final String sql  = " SELECT CASE WHEN item = '合计' THEN 100 when item = 'T项目' then  1 when item = 'D项目' then  2 when item = 'C项目' then  3 when item = 'M项目' then 4 ELSE 50 end as dsc,item ,product_name as productName, cz_user_num , round(cast(cz_user_rate*100 as numeric),2) || '%' as cz_user_rate,round(cast(arpu as numeric),2) as arpu,  round(cast(mou as numeric),2) as mou, round(cast(dou as numeric),2) as dou, round(cast(rate_4g*100 as numeric),2) || '%' as rate_4g,round(cast(cash_1st_rate_4g * 100 AS NUMERIC),2) || '%' AS rate_4g_1st,round(cast(stop_rate*100 as numeric),2) || '%' as  stop_rate,  round(cast(cash_1st_stop_rate*100 as numeric),2) || '%' as cash_1st_stop_rate, round(cast(churn_rate*100 as numeric),2) || '%' as churn_rate, round(cast(cash_1st_churn_rate*100 as numeric),2) || '%' as cash_1st_churn_rate, month_id  as monthId FROM uapp.tdcm_product_report where 1=1 ";

    final String countSql = "select count(*) from uapp.tdcm_product_report where 1=1";

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;


    /**
     * 查询B2I产品质态分析表
     * @param param
     * @return
     */
    public List<Map<String,Object>> queryB2IProductReport(Map<String,Object> param){
        String month = String.valueOf(param.get("monthId"));
        String productType = String.valueOf(param.get("productType"));
        String limit = String.valueOf(param.get("length"));
        String offset = String.valueOf(param.get("start"));
        String queryProduct = "";
        if (!"合计".equals(productType))
        {
            queryProduct = " and item = \'" + productType+"\' ";
        }
        String queryMonth = " and month_id = " + month;
        String page = " limit "+limit+" offset "+offset;
        if(StringUtils.isEmpty(limit) || StringUtils.isEmpty(offset)){
            page = "";
        }
        String finalSql = sql + queryProduct+queryMonth+" order by dsc "+page;
        List<Map<String,Object>> dataList;
        try
        {
            dataList = greenPlumOperateService.query(finalSql);
        }
        catch (Exception e)
        {
            LOG.error("查询B2I产品质态分析表error:",e);
            return new ArrayList<>();
        }
        return  dataList;
    }

    /**
     * 查询B2I产品质态分析表分页
     * @param param
     * @return
     */
    public int queryB2IProductReporCounts(Map<String,Object> param){
        String month = String.valueOf(param.get("monthId"));
        String productType = String.valueOf(param.get("productType"));
        String queryProduct = "";
        if (!"合计".equals(productType))
        {
            queryProduct = " and item = \'" + productType+"\' ";
        }
        String queryMonth = " and month_id = " + month;
        String finalSql = countSql + queryMonth + queryProduct;
        int count;
        try
        {
            count = greenPlumOperateService.queryRecordCount(finalSql);
        }
        catch (Exception e)
        {
            LOG.error("查询B2I产品质态分析表error:");
            return 0;
        }
        return count;
    }


    public void downloadB2IProductReport(Map<String,Object> param,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
        String product= URLDecoder.decode(String.valueOf(param.get("productType")), "UTF-8");
        String month = String.valueOf(param.get("monthId"));
        param.put("productType", product);
        List<Map<String,Object>> dataList = queryB2IProductReport(param);
        String fileName;
        if("合计".equals(product)){
            fileName = month+"B2I产品质态分析报表-全部";
        }else{
            fileName = month+"B2I产品质态分析报表-"+product;
        }
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
        cellEntityList1.add(new ExcelCellEntity(1, 1, "类型"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "产品名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "出账用户"));
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
