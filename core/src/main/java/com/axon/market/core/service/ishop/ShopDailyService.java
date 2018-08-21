package com.axon.market.core.service.ishop;

import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hale on 2017/4/25.
 */
@Component("shopDailyService")
public class ShopDailyService
{
    private static final Logger LOG = Logger.getLogger(ShopBlackService.class.getName());

    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    /**
     * @param paras
     * @return
     */
    public List<Map<String, Object>> queryShopDailyByPage(Map<String, Object> paras)
    {
        String dateTime = String.valueOf(paras.get("dateTime"));
        Integer locationType = Integer.valueOf(String.valueOf(paras.get("locationType")));
        String baseId = String.valueOf(paras.get("baseId"));
        String areaCode = String.valueOf(paras.get("areaCode"));

        if (StringUtils.isEmpty(dateTime) && !"null".equals(dateTime))
        {
            dateTime = getYesterday();
        }
        else
        {
            String[] dateTimeArray = dateTime.split("-");
            if (dateTimeArray.length == 3)
            {
                dateTime = dateTimeArray[0] + dateTimeArray[1] + dateTimeArray[2];
            }
            else
            {
                dateTime = getYesterday();
            }
        }

        String locationTypeSql = "";
        if (locationType != -1 && !"null".equals(locationType))
        {
            locationTypeSql = " and r.base_type_id=" + locationType + "";
        }

        String baseIdSql = "";
        if (!StringUtils.isEmpty(baseId) && !"null".equals(baseId))
        {
            baseIdSql = " and r.baseid=" + baseId + "";
        }

        String areaCodeSql = "";
        if (!"".equals(areaCode) && !"null".equals(areaCode) && !"99999".equals(areaCode))
        {
            areaCodeSql = "and r.city_code = " + areaCode + "";
        }

        try
        {
            String sql = "select r.city_name as cityName,r.base_type_id as baseType,r.base_type_desc as baseTypeName,r.base_name as baseName,CASE WHEN (r.STATUS=1) THEN '是' else '否'end as STATUS,r.province_task_num as provinceTaskNum,r.city_task_num as cityTaskNum,r.base_task_num as baseTaskNum,r.execute_task_num as executeTaskNum,CASE WHEN (r.province_task_num+r.city_task_num+r.base_task_num)  <> 0 THEN round(r.execute_task_num*100/(r.province_task_num+r.city_task_num+r.base_task_num),2) || '%' ELSE 0.00 || '%' END as executeTaskRate,r.business_type_num as businessTypeNum,r.city_target_user as cityTargetUser,r.appoint_user as appointUser,r.city_resident_user as cityResidentUser,r.base_cover_user as baseCoverUser,CASE WHEN r.base_cover_user <> 0 THEN round(r.city_resident_user*100/r.base_cover_user,2)|| '%' ELSE 0.00 || '%' END  as residentRate,CASE WHEN r.send_user <> 0 THEN round(r.recv_succ_user*100/r.send_user,2)|| '%' ELSE 0.00 || '%' END as smsRate ,r.send_user as sendUser from umid.rpt_base_push_result_day as r where r.timest=" + dateTime + " " + areaCodeSql + " " + baseIdSql + " " + locationTypeSql + " order by r.base_type_id desc";
            return greenPlumOperateService.query(sql);
        }
        catch (Exception e)
        {
            LOG.error("queryShopDailyByPage error." + e);
            return new ArrayList<Map<String,Object>>();
        }
    }

    /**
     * @param paras
     * @return
     */
    public Map<String, Object> queryShopDailyTotal(Map<String, Object> paras)
    {
        String dateTime = String.valueOf(paras.get("dateTime"));
        Integer locationType = Integer.valueOf(String.valueOf(paras.get("locationType")));
        String baseId = String.valueOf(paras.get("baseId"));
        String areaCode = String.valueOf(paras.get("areaCode"));

        if (StringUtils.isEmpty(dateTime) && !"null".equals(dateTime))
        {
            dateTime = getYesterday();
        }
        else
        {
            String[] dateTimeArray = dateTime.split("-");
            if (dateTimeArray.length == 3)
            {
                dateTime = dateTimeArray[0] + dateTimeArray[1] + dateTimeArray[2];
            }
            else
            {
                dateTime = getYesterday();
            }
        }

        try
        {
            Map<String, Object> result = new HashMap<String, Object>();
            if (locationType == -1)
            {
                Map<String, Object> map = getTotalData(dateTime, areaCode, baseId, locationType);
                Map<String, Object> hpMap = getTotalData(dateTime, areaCode, baseId, 7);
                Map<String, Object> zyMap = getTotalData(dateTime, areaCode, baseId, 1);
                result.put("allTotal", map);
                result.put("hpTotal", hpMap);
                result.put("zyTotal", zyMap);
                return result;
            }
            else if (locationType == 1)
            {
                Map<String, Object> zyMap = getTotalData(dateTime, areaCode, baseId, locationType);
                result.put("zyTotal", zyMap);
                return result;
            }
            else if (locationType == 7)
            {
                Map<String, Object> hpMap = getTotalData(dateTime, areaCode, baseId, locationType);
                result.put("hpTotal", hpMap);
                return result;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LOG.error("queryShopDailyByPage error." + e);
            return null;
        }
    }

    /**
     * @param dataList
     * @return
     */
    public HSSFWorkbook getShopDailyExcelData(List<Map<String, Object>> dataList, Map<String, Object> dataTotalList, String menuName)
    {
        String[] fields = new String[]{"basetypename","cityname", "basename", "status", "provincetasknum", "citytasknum", "basetasknum", "executetasknum", "executetaskrate", "businesstypenum", "citytargetuser", "appointuser", "cityresidentuser", "basecoveruser", "residentrate", "senduser", "smsrate"};

        // 创建workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);    //填充的背景颜色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);     //左边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);    //右边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);    //顶边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //顶边框
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10); // 字体
        style.setFont(font);

        // 创建sheet页
        HSSFSheet sheet = workbook.createSheet(menuName);
        //创建单元格
        HSSFRow row = sheet.createRow(0);
        HSSFCell c0 = row.createCell(0);
        c0.setCellValue(new HSSFRichTextString("营业厅类型"));
        c0.setCellStyle(style);
        HSSFCell c5 = row.createCell(1);
        c5.setCellValue(new HSSFRichTextString("地市名称"));
        c5.setCellStyle(style);
        HSSFCell c1 = row.createCell(2);
        c1.setCellValue(new HSSFRichTextString("门店情况"));
        c1.setCellStyle(style);
        HSSFCell c2 = row.createCell(4);
        c2.setCellValue(new HSSFRichTextString("任务执行情况"));
        c2.setCellStyle(style);
        HSSFCell c3 = row.createCell(10);
        c3.setCellValue(new HSSFRichTextString("目标用户群"));
        c3.setCellStyle(style);
        HSSFCell c4 = row.createCell(15);
        c4.setCellValue(new HSSFRichTextString("引流人群"));
        c4.setCellStyle(style);

        HSSFRow row2 = sheet.createRow(1);
        HSSFCell c15 = row2.createCell(2);
        c15.setCellValue(new HSSFRichTextString("名称"));
        c15.setCellStyle(style);
        HSSFCell c16 = row2.createCell(3);
        c16.setCellValue(new HSSFRichTextString("上线状态"));
        c16.setCellStyle(style);

        HSSFCell c17 = row2.createCell(4);
        c17.setCellValue(new HSSFRichTextString("省级任务总数"));
        c17.setCellStyle(style);
        HSSFCell c18 = row2.createCell(5);
        c18.setCellValue(new HSSFRichTextString("地市任务总数"));
        c18.setCellStyle(style);
        HSSFCell c19 = row2.createCell(6);
        c19.setCellValue(new HSSFRichTextString("营业厅任务总数"));
        c19.setCellStyle(style);
        HSSFCell c20 = row2.createCell(7);
        c20.setCellValue(new HSSFRichTextString("执行的任务总数"));
        c20.setCellStyle(style);
        HSSFCell c21 = row2.createCell(8);
        c21.setCellValue(new HSSFRichTextString("任务执行率"));
        c21.setCellStyle(style);
        HSSFCell c22 = row2.createCell(9);
        c22.setCellValue(new HSSFRichTextString("业务类型数量"));
        c22.setCellStyle(style);

        HSSFCell c23 = row2.createCell(10);
        c23.setCellValue(new HSSFRichTextString("地市实际目标人群"));
        c23.setCellStyle(style);
        HSSFCell c24 = row2.createCell(11);
        c24.setCellValue(new HSSFRichTextString("指定导入人群"));
        c24.setCellStyle(style);
        HSSFCell c25 = row2.createCell(12);
        c25.setCellValue(new HSSFRichTextString("地市目标常驻人群"));
        c25.setCellStyle(style);
        HSSFCell c26 = row2.createCell(13);
        c26.setCellValue(new HSSFRichTextString("本厅常驻覆盖人群"));
        c26.setCellStyle(style);
        HSSFCell c27 = row2.createCell(14);
        c27.setCellValue(new HSSFRichTextString("常驻覆盖率"));
        c27.setCellStyle(style);

        HSSFCell c28 = row2.createCell(15);
        c28.setCellValue(new HSSFRichTextString("引流人数"));
        c28.setCellStyle(style);
        HSSFCell c29 = row2.createCell(16);
        c29.setCellValue(new HSSFRichTextString("短信到达率"));
        c29.setCellStyle(style);


        // ----------------数据行--------------------------------------------
        if (null != dataList && dataList.size() > 0)
        {
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> rowData = dataList.get(i);
                HSSFRow temprow = sheet.createRow(i + 2);

                for (int j = 0; j < fields.length; j++)
                {
                    String value = "";
                    if (null != rowData.get(fields[j]))
                    {
                        value = "" + rowData.get(fields[j]);
                    }
                    temprow.createCell(j).setCellValue(new HSSFRichTextString(value));
                }
            }
        }

        if (null != dataTotalList)
        {
            Map<String, Object> allTotal = (Map<String, Object>) dataTotalList.get("allTotal");
            Map<String, Object> hpTotal = (Map<String, Object>) dataTotalList.get("hpTotal");
            Map<String, Object> zyTotal = (Map<String, Object>) dataTotalList.get("zyTotal");
            Integer length = dataList.size()+2;

            if (null != hpTotal)
            {
                setExcelCellValue(hpTotal, length++, "黄埔厅合计", sheet);
            }
            if (null != zyTotal)
            {
                setExcelCellValue(zyTotal, length++, "自营厅合计", sheet);
            }
            if (null != allTotal)
            {
                setExcelCellValue(allTotal, length++, "总合计", sheet);
            }
        }

        Region region1 = new Region(0, (short) 0, 1, (short) 0);
        Region region6 = new Region(0, (short) 1, 1, (short) 1);
        Region region2 = new Region(0, (short) 2, 0, (short) 3);
        Region region3 = new Region(0, (short) 4, 0, (short) 9);
        Region region4 = new Region(0, (short) 10, 0, (short) 14);
        Region region5 = new Region(0, (short) 15, 0, (short) 16);
        sheet.addMergedRegion(region1);
        sheet.addMergedRegion(region6);
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);
        sheet.addMergedRegion(region5);
        return workbook;
    }

    /**
     * @param dateTime
     * @param locationType
     * @return
     */
    private Map<String, Object> getTotalData(String dateTime, String areaCode, String baseId, Integer locationType)
    {
        String locationTypeSql = "";
        String businessTypeLocationTypeSql = "";
        if (locationType != -1 && !"null".equals(locationType))
        {
            locationTypeSql = " and r.base_type_id=" + locationType + "";
            businessTypeLocationTypeSql = "AND c.location_type_id = " + locationType + "";
        }

        String areaCodeSql = "";
        String areaCodeSql2 = "";
        if (!"".equals(areaCode) && !"99999".equals(areaCode))
        {
            areaCodeSql = "and c.city_code = " + areaCode + "";
            areaCodeSql2 = "and r.city_code = " + areaCode + "";
        }

        String baseIdSql = "";
        String baseIdSql2 = "";
        if (!StringUtils.isEmpty(baseId) && !"null".equals(baseId))
        {
            baseIdSql = "and c.base_id = " + areaCode + "";
            baseIdSql2 = " and r.baseid=" + baseId + "";
        }

        String sql = "SELECT COALESCE(sum(c. STATUS),0) AS baseNameTotal,COALESCE(sum(c. STATUS),0) AS statusTotal,COALESCE(sum(c.provinceTaskNum),0) AS provinceTaskNumTotal,COALESCE(sum(c.cityTaskNum),0) AS cityTaskNumTotal,COALESCE(sum(c.baseTaskNum),0) AS baseTaskNumTotal,COALESCE(sum(c.executeTaskNum),0) AS executeTaskNumTotal,CASE WHEN (sum(c.provinceTaskNum) + sum(c.cityTaskNum) + sum(c.baseTaskNum)) <> 0 THEN round(sum(c.executeTaskNum) * 100 / (sum(c.provinceTaskNum) + sum(c.cityTaskNum) + sum(c.baseTaskNum)),2) || '%' ELSE 0.00 || '%' END AS executeTaskTotalRate,(SELECT count(DISTINCT b.business_type) execute_taskbus_num FROM dware.push_shop_task_execute_history a INNER JOIN dware.push_shop_task_pool b ON a.task_id = b.id AND b.status IN (2, 4, 6) AND b.date = " + dateTime + " INNER JOIN dware.push_conf_baseinfo c ON a.base_id = c.base_id AND c.timest = " + dateTime + " AND a.date = " + dateTime + " WHERE 1=1 " + areaCodeSql + " " + baseIdSql + " " + businessTypeLocationTypeSql + ") AS businessTypeNumTotal, COALESCE(max(c.cityTargetUser),0) AS cityTargetUserTotal,COALESCE(sum(c.appointUser),0) AS appointUserTotal,COALESCE(sum(c.cityResidentUser),0) AS cityResidentUserTotal,COALESCE(sum(c.baseCoverUser),0) AS baseCoverUserTotal,CASE WHEN (sum(c.baseCoverUser)) <> 0 THEN round(sum(c.cityResidentUser) * 100 / sum(c.baseCoverUser),2) || '%' ELSE 0.00 || '%' END AS residentTotalRate,CASE WHEN (sum(c.sendUserNum)) <> 0 THEN round(sum(c.recvSuccUserNum) * 100 / sum(c.sendUserNum),2) || '%' ELSE 0.00 || '%' END AS smsTotalRate, COALESCE(sum(c.sendUserNum),0) AS sendUserTotalNum  FROM (SELECT r.base_type_desc AS baseTypeName,r.base_name AS baseName,r. STATUS AS STATUS,r.province_task_num AS provinceTaskNum,r.city_task_num AS cityTaskNum,r.base_task_num AS baseTaskNum,r.execute_task_num AS executeTaskNum,CASE WHEN (r.province_task_num + r.city_task_num + r.base_task_num) <> 0 THEN round(r.execute_task_num * 100 / (r.province_task_num + r.city_task_num + r.base_task_num),2) || '%' ELSE 0.00 || '%' END AS executeTaskRate,r.business_type_num AS businessTypeNum,r.city_target_user AS cityTargetUser,r.appoint_user AS appointUser,r.city_resident_user AS cityResidentUser,r.base_cover_user AS baseCoverUser,CASE WHEN r.base_cover_user <> 0 THEN round(r.city_resident_user * 100 / r.base_cover_user,2) || '%' ELSE 0.00 || '%' END AS residentRate,CASE WHEN r.send_user <> 0 THEN round(r.recv_succ_user * 100 / r.send_user,2) || '%' ELSE 0.00 || '%' END AS smsRate,r.send_user AS sendUserNum,r.recv_succ_user AS recvSuccUserNum FROM umid.rpt_base_push_result_day AS r WHERE r.timest = " + dateTime + " " + areaCodeSql2 + " " + baseIdSql2 + " " + locationTypeSql + " ORDER BY r.base_type_id desc) AS c";

        return greenPlumOperateService.queryForMap(sql);
    }

    /**
     * @return
     */
    private String getYesterday()
    {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
    }

    /**
     * @param list
     * @param length
     * @param cellName
     * @param sheet
     */
    private void setExcelCellValue(Map<String, Object> list, Integer length, String cellName, HSSFSheet sheet)
    {
        String[] totalFields = new String[]{"basenametotal","basenametotal","statustotal", "provincetasknumtotal", "citytasknumtotal", "basetasknumtotal", "executetasknumtotal", "executetasktotalrate", "businesstypenumtotal", "citytargetusertotal", "appointusertotal", "cityresidentusertotal", "basecoverusertotal", "residenttotalrate", "sendusertotalnum", "smstotalrate"};

        HSSFRow row = sheet.createRow(length);
        row.createCell(0).setCellValue(new HSSFRichTextString(cellName));

        for (int i = 0; i < list.size(); i++)
        {
            for (int j = 0; j < totalFields.length; j++)
            {
                String value = "";
                if (null != list.get(totalFields[j]))
                {
                    value = "" + list.get(totalFields[j]);
                }
                row.createCell(j + 1).setCellValue(new HSSFRichTextString(value));
            }
        }
    }
}
