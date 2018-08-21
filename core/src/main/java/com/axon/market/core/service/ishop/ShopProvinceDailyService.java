package com.axon.market.core.service.ishop;

import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by dtt on 2017/7/10.
 * 炒店2.0省份日报Service类
 */
@Component("shopProvinceDailyService")
public class ShopProvinceDailyService {
    //ShopBlackService - 炒店黑名单服务类
    private static final Logger LOG = Logger.getLogger(ShopBlackService.class.getName());
    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    String queryShopProvinceDailySql = "select \t\n" +
            "\tcity_name as cityName,\n" +
            "\ttotal_base_num as totalBaseNum,\n" +
            "\teffect_base_num as effectBaseNum,\n" +
            "\tprovince_task_num as provinceTaskNum,\n" +
            "\tcity_task_num as cityTaskNum,\n" +
            "\tbase_task_num as baseTaskNum,\n" +
            "\texecute_task_num as executeTaskNum,\n" +
            "\tCASE WHEN(province_task_num + city_task_num + base_task_num) <> 0 THEN round( execute_task_num * 100/(province_task_num + city_task_num + base_task_num),2) || '%' ELSE 0.00 || '%' END as executeTaskRate,\n" +
            "\tbusiness_type_num as businessTypeNum,\n" +
            "\tcity_target_user as cityTargetUser,\n" +
            "\tappoint_user as appointUser,\n" +
            "\tcity_resident_user as cityResidentUser,\n" +
            "\tbase_cover_user as baseCoverUser,\n" +
            "\tCASE WHEN base_cover_user <> 0 THEN round(city_resident_user*100/base_cover_user,2) || '%' ELSE 0.00 || '%' END as residentRate,\n" +
            "\tsend_user as sendUser,\n" +
            "\tCASE WHEN send_user <> 0 THEN round(recv_succ_user*100/send_user,2) || '%' ELSE 0.00 || '%' END as smsRate \n" +
            "from DATATABLE \n" +
            "where timest = DATETIME \n" +
            "order by city_code ";

    /**
     * 查询省份日报统计信息
     * 创建人：dongtt
     * 创建时间：2017-07-12
     *
     * @param dateTime
     * @return
     */
    public List<Map<String, Object>> queryShopProvinceDaily(String dateTime) {
        //region 设置查询日期
//        if (StringUtils.isEmpty(dateTime)) {
//            dateTime = getYesterday();
//        }
//        else {
//            String[] dateTimeArray = dateTime.split("-");
//            if (dateTimeArray.length == 3) {
//                dateTime = dateTimeArray[0] + dateTimeArray[1] + dateTimeArray[2];
//            } else {
//                dateTime = getYesterday();
//            }
//        }
        //endregion
        String sql1 = queryShopProvinceDailySql.replace("DATATABLE", "umid.rpt_city_push_result_day").replace("DATETIME", dateTime);
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            dataList = greenPlumOperateService.query(sql1);
        } catch (Exception e) {
            LOG.error("查询省份日报信息失败", e);
            return dataList;
        }
        return dataList;
    }


    /**
     * @param dataList
     * @return
     */
    public HSSFWorkbook getShopProvinceDailyExcelData(List<Map<String, Object>> dataList, String fileName) {
        String[] fields = new String[]{"cityname","totalbasenum","effectbasenum","provincetasknum","citytasknum","basetasknum","executetasknum","executetaskrate","businesstypenum","citytargetuser","appointuser","cityresidentuser","basecoveruser","residentrate","senduser","smsrate"};

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
        HSSFSheet sheet = workbook.createSheet(fileName);
        //创建单元格
        HSSFRow row = sheet.createRow(0);
        HSSFCell c0 = row.createCell(0);
        c0.setCellValue(new HSSFRichTextString("地市"));
        c0.setCellStyle(style);
        HSSFCell c5 = row.createCell(1);
        c5.setCellValue(new HSSFRichTextString("门店情况"));
        c5.setCellStyle(style);
        HSSFCell c1 = row.createCell(3);
        c1.setCellValue(new HSSFRichTextString("任务执行情况"));
        c1.setCellStyle(style);
        HSSFCell c2 = row.createCell(9);
        c2.setCellValue(new HSSFRichTextString("目标用户群"));
        c2.setCellStyle(style);
        HSSFCell c3 = row.createCell(14);
        c3.setCellValue(new HSSFRichTextString("引流人群"));
        c3.setCellStyle(style);


        HSSFRow row2 = sheet.createRow(1);
        HSSFCell c15 = row2.createCell(1);
        c15.setCellValue(new HSSFRichTextString("渠道总数"));
        c15.setCellStyle(style);
        HSSFCell c16 = row2.createCell(2);
        c16.setCellValue(new HSSFRichTextString("上线营业厅总数"));
        c16.setCellStyle(style);

        HSSFCell c17 = row2.createCell(3);
        c17.setCellValue(new HSSFRichTextString("省级任务总数"));
        c17.setCellStyle(style);
        HSSFCell c18 = row2.createCell(4);
        c18.setCellValue(new HSSFRichTextString("地市自建任务总数"));
        c18.setCellStyle(style);
        HSSFCell c19 = row2.createCell(5);
        c19.setCellValue(new HSSFRichTextString("营业厅任务总数"));
        c19.setCellStyle(style);
        HSSFCell c20 = row2.createCell(6);
        c20.setCellValue(new HSSFRichTextString("执行的任务总数"));
        c20.setCellStyle(style);
        HSSFCell c21 = row2.createCell(7);
        c21.setCellValue(new HSSFRichTextString("炒店任务人工执行率"));
        c21.setCellStyle(style);
        HSSFCell c22 = row2.createCell(8);
        c22.setCellValue(new HSSFRichTextString("业务类型数量"));
        c22.setCellStyle(style);

        HSSFCell c23 = row2.createCell(9);
        c23.setCellValue(new HSSFRichTextString("地市实际目标人群"));
        c23.setCellStyle(style);
        HSSFCell c24 = row2.createCell(10);
        c24.setCellValue(new HSSFRichTextString("指定导入人群"));
        c24.setCellStyle(style);
        HSSFCell c25 = row2.createCell(11);
        c25.setCellValue(new HSSFRichTextString("地市目标常驻人群"));
        c25.setCellStyle(style);
        HSSFCell c26 = row2.createCell(12);
        c26.setCellValue(new HSSFRichTextString("本厅常驻覆盖人群"));
        c26.setCellStyle(style);
        HSSFCell c27 = row2.createCell(13);
        c27.setCellValue(new HSSFRichTextString("常驻覆盖率"));
        c27.setCellStyle(style);

        HSSFCell c28 = row2.createCell(14);
        c28.setCellValue(new HSSFRichTextString("引流人数"));
        c28.setCellStyle(style);
        HSSFCell c29 = row2.createCell(15);
        c29.setCellValue(new HSSFRichTextString("短信到达率"));
        c29.setCellStyle(style);


        // ----------------数据行--------------------------------------------
        if (null != dataList && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> rowData = dataList.get(i);
                HSSFRow temprow = sheet.createRow(i + 2);

                for (int j = 0; j < fields.length; j++) {
                    String value = "";
                    if (null != rowData.get(fields[j])) {
                        value = "" + rowData.get(fields[j]);
                    }
                    temprow.createCell(j).setCellValue(new HSSFRichTextString(value));
                }
            }
        }

        Region region1 = new Region(0, (short) 0, 1, (short) 0);
        setRegionStyle(sheet,region1,style);
        Region region6 = new Region(0, (short) 1, 0, (short) 2);
        setRegionStyle(sheet,region6,style);
        Region region2 = new Region(0, (short) 3, 0, (short) 8);
        setRegionStyle(sheet,region2,style);
        Region region3 = new Region(0, (short) 9, 0, (short) 13);
        setRegionStyle(sheet,region3,style);
        Region region4 = new Region(0, (short) 14, 0, (short) 15);
        setRegionStyle(sheet,region4,style);

        sheet.addMergedRegion(region1);
        sheet.addMergedRegion(region6);
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);

        return workbook;
    }

    /**
     * 设置合并单元格边框样式
     * @param sheet
     * @param region
     * @param cs
     */
    @SuppressWarnings("deprecation")
    public static void setRegionStyle(HSSFSheet sheet, Region region, HSSFCellStyle cs) {
        for (int i = region.getRowFrom(); i <= region.getRowTo(); i++) {
            HSSFRow row = HSSFCellUtil.getRow(i, sheet);
            for (int j = region.getColumnFrom(); j <= region.getColumnTo(); j++) {
                HSSFCell cell = HSSFCellUtil.getCell(row, (short) j);
                cell.setCellStyle(cs);
            }
        }
    }
}
