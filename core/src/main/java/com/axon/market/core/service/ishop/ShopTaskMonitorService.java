package com.axon.market.core.service.ishop;

import com.axon.market.common.bean.Table;
import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import com.axon.market.common.util.excel.ExportUtils;
import com.axon.market.dao.mapper.ishop.IShopTaskMonitorMapper;
import org.apache.http.HttpRequest;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/6/3.
 */
@Service("shopTaskMonitorService")
public class ShopTaskMonitorService
{
    @Autowired
    @Qualifier("shopTaskMonitorDao")
    IShopTaskMonitorMapper shopTaskMonitorDao;

    Logger LOG = Logger.getLogger(ShopTaskMonitorService.class.getName());

    private static String[] fields = new String[]{"timeUnit", "taskFileNum", "personalTaskNum", "shopOnlineNum", "provincialTaskNum", "cityTaskNum", "shopNum", "taskNum",
            "shouldTaskNum", "actualTaskNum"};

    /**
     * 查询炒店任务监控数据
     * @param startTime
     * @param endTime
     * @param offset
     * @param limit
     * @return
     */
    public Table<Map<String,Object>> queryShopTaskMonitor(String startTime,String endTime,Integer offset,Integer limit)
    {
        Table<Map<String,Object>> table;
        try
        {
            int count = shopTaskMonitorDao.queryShopTaskMonitorCount(startTime, endTime);
            List<Map<String,Object>> list = shopTaskMonitorDao.queryShopTaskMonitor(startTime, endTime, offset, limit);
            table = new Table<>(list,count);
        }
        catch (Exception e)
        {
            LOG.error("查询炒店任务监控数据异常：",e);
            return new Table<>();
        }
        return table;
    }

    /**
     * 下载炒店任务监控数据
     * @param startTime
     * @param endTime
     * @param request
     * @param response
     */
    public void downloadShopTaskMonitor(String startTime,String endTime,HttpServletRequest request,HttpServletResponse response){

        List<Map<String,Object>> dataList = new ArrayList<>();
        try
        {
            dataList = shopTaskMonitorDao.queryShopTaskMonitor(startTime, endTime, null, null);
        }
        catch (Exception e)
        {
            LOG.error("下载炒店任务监控失败：",e);
        }
        String tableName  = startTime + "至" + endTime + "炒店任务监控" + System.currentTimeMillis();
        List<ExcelRowEntity> excelDataList = getExcelData(dataList);
        ExportUtils.getInstance().exportData(tableName, excelDataList, request, response);
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
        cellEntityList1.add(new ExcelCellEntity(1, 1, "日期"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "活动文件"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "个性化任务"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "在线营业厅"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "省级任务"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "地市任务"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "营业厅"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "有效任务"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "应生产任务数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "实际任务数"));
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
