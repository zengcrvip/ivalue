package com.axon.market.core.service.ishop;


import com.axon.market.common.bean.Table;
import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import com.axon.market.common.util.excel.ExportUtils;
import com.axon.market.dao.mapper.ishop.IJXHTaskStatisticsMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/6/15.
 */
@Service("JXHTaskStatisticsService")
public class JXHTaskStatisticsService
{
    Logger LOG = Logger.getLogger(JXHTaskStatisticsService.class.getName());

    @Autowired
    @Qualifier("JXHTaskStatisticsDao")
    IJXHTaskStatisticsMapper JXHTaskStatisticsDao;

    private static String[] fields = new String[]{"date","taskName","marketType","businessType","targetUser","marketContent","smsSend","smsRecv","smsRate"};

    /**
     * 查询精细化任务统计数据
     * @param param
     * @return
     */
    public Table<Map<String,Object>> queryJXHTaskStatistics(Map<String,String> param){
        Table<Map<String,Object>> table;
        try
        {
            List<Map<String,Object>> dataList = JXHTaskStatisticsDao.queryJXHTaskStatistics(param);
            int count = JXHTaskStatisticsDao.queryJXHTaskCount(param);
            table = new Table(dataList,count);
        }
        catch (Exception e)
        {
            LOG.error("查询精细化任务统计异常:",e);
            return new Table<Map<String, Object>>();
        }
        return table;
    }

    /**
     * 下载精细化任务统计报表
     * @param param
     * @param request
     * @param response
     * @param session
     */
    public void downloadJXHTaskStatistics(Map<String,String> param,HttpServletRequest request,HttpServletResponse response,HttpSession session)
    {
        List<Map<String,Object>> dataList ;
        try
        {
            dataList = JXHTaskStatisticsDao.queryJXHTaskStatistics(param);
            String tableName = param.get("startTime") + "至" + param.get("endTime") + "营销任务统计报表";
            List<Map<String,Object>> newDataList = new ArrayList<Map<String,Object>>();
            for(Map<String,Object> data:dataList){
                String mType= String.valueOf(data.get("marketType"));
                int bType = Integer.parseInt(String.valueOf(data.get("businessType")));
                String targetUser = String.valueOf(data.get("targetUser"));
                switch (mType){
                    case "sms" : data.put("marketType","自建群发任务");break;
                    case "scenesms" : data.put("marketType","自建场景任务");break;
                    case "jxhsms" : data.put("marketType","精细化群发任务");break;
                    case "jxhscene" : data.put("marketType","精细化场景任务");break;
                    default: data.put("marketType","");break;
                }
                switch (bType){
                    case 1 : data.put("businessType","互联网综合业务");break;
                    case 2 : data.put("businessType","内容营销");break;
                    case 3 : data.put("businessType","流量经营");break;
                    case 4 : data.put("businessType","APP场景营销");break;
                    default: data.put("businessType","");break;
                }
                if(targetUser.length() == 0){
                    data.put("targetUser","全网监控");
                }
                newDataList.add(data);
            }
            List<ExcelRowEntity> excelDataList = getExcelData(newDataList);
            ExportUtils.getInstance().exportData(tableName, excelDataList, request, response);
        }
        catch (Exception e)
        {
            LOG.error("下载营销任务统计报表异常",e);
        }


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
        cellEntityList1.add(new ExcelCellEntity(1, 1, "任务名称"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "来源"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "业务类型"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "目标用户"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "短信内容"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "短信发送人数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "短信到达人数"));
        cellEntityList1.add(new ExcelCellEntity(1, 1, "短信到达率"));

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
