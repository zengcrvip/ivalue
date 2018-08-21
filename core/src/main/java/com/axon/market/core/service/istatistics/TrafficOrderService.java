package com.axon.market.core.service.istatistics;

import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtt on 2017/6/19.
 */
@Service("trafficOrderService")
public class TrafficOrderService
{
    private  static final Logger LOG = Logger.getLogger(TrafficOrderService.class.getName());

    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();


    String queryTrafficOrderSql = "select a.city_name as cityName,b.* from uaide.conf_city a join (select \t\n" +
            "\tcity_code,\n" +
            "\tsum(cbss_5yuan_ordre_cnt) as cbss_5yuan,\n" +
            "\tsum(cbss_10yuan_ordre_cnt) as cbss_10yuan,\n" +
            "\tsum(cbss_domestic_order_cnt_1) as cbss_domestic_1,\n" +
            "\tsum(cbss_province_order_cnt_1) as cbss_province_1,\n" +
            "\tsum(cbss_xianshi_order_cnt) as cbss_xianshi,\n" +
            "\tsum(cbss_jiayouo_order_cnt) as cbss_jiayou,\n" +
            "\tsum(cbss_domestic_order_cnt_2) as cbss_domestic_2,\n" +
            "\tsum(cbss_province_order_cnt_2) as cbss_province_2,\n" +
            "\tsum(cbss_dingxiang_order_cnt) as cbss_dingxiang,\n" +
            "\tsum(cbss_international_order_cnt) as cbss_international,\n" +
            "\tsum(bss_5yuan_ordre_cnt) as bss_5yuan,\n" +
            "\tsum(bss_10yuan_ordre_cnt) as bss_10yuan,\n" +
            "\tsum(bss_domestic_order_cnt_1) as bss_domestic_1,\n" +
            "\tsum(bss_province_order_cnt_1) as bss_province_1,\n" +
            "\tsum(bss_xianshi_order_cnt) as bss_xianshi,\n" +
            "\tsum(bss_diejia_order_cnt) as bss_diejia,\n" +
            "\tsum(bss_domestic_order_cnt_2) as bss_domestic_2,\n" +
            "\tsum(bss_province_order_cnt_2) as bss_province_2,\n" +
            "\tsum(bss_dingxiang_order_cnt) as bss_dingxiang,\n" +
            "\tsum(bss_international_order_cnt) as bss_international,\n" +
            "\tsum(total_order_cnt) as total\n" +
            "from DATATABLE \n" +
            "where timest between STARTTIME and ENDTIME \n" +
            "group by city_code) b  on  cast(a.city_code as numeric) = b.city_code\n" +
            "order by b.city_code desc";


    /**
     * 查询流量包订购日报统计信息
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> queryTrafficOrder(String startTime, String endTime)
    {
        String sql1 = queryTrafficOrderSql.replace("DATATABLE","umid.rpt_order_product_day").replace("STARTTIME",startTime).replace("ENDTIME",endTime);
        List<Map<String, Object>> dataList = new ArrayList<>();
        try
        {
            dataList = greenPlumOperateService.query(sql1);
        }
        catch (Exception e){
            LOG.error("查询流量包日报订购信息失败",e);
            return dataList;
        }
        return dataList;
    }

    /**
     * 查询流量包订购月报统计信息
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> queryMonthlyTrafficOrder(String startTime, String endTime){
        String sql = queryTrafficOrderSql.replace("DATATABLE","umid.rpt_order_product_mon").replace("STARTTIME",startTime).replace("ENDTIME",endTime);
        List<Map<String, Object>> dataList = new ArrayList<>();
        try
        {
            dataList = greenPlumOperateService.query(sql);
        }
        catch (Exception e){
            LOG.error("查询流量包月报订购信息失败",e);
            return dataList;
        }
        return dataList;
    }

    public int queryProvincialTrafficOrderCounts(String startTime, String endTime){
        String sql = "select count(*) from umid.rpt_order_product_day where timest between "+startTime+" and "+endTime+" and city_code=99999;";
        int count = 0;
        try
        {
            count = greenPlumOperateService.queryRecordCount(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return count;
    }



    /**
     * 查询流量包订购全省统计信息
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> queryProvincialTrafficOrder(String startTime, String endTime,Integer curPage,Integer countsPerPage){
        String sql = "SELECT to_date(timest,'yyyymmdd') as time, \n" +
                "\tcbss_5yuan_ordre_cnt as cbss_5yuan, \n" +
                "\tcbss_10yuan_ordre_cnt as cbss_10yuan, \n" +
                "\tcbss_domestic_order_cnt_1 as cbss_domestic_1, \n" +
                "\tcbss_province_order_cnt_1 as cbss_province_1, \n" +
                "\tcbss_xianshi_order_cnt as cbss_xianshi, \n" +
                "\tcbss_jiayouo_order_cnt as cbss_jiayou, \n" +
                "\tcbss_domestic_order_cnt_2 as cbss_domestic_2, \n" +
                "\tcbss_province_order_cnt_2 as cbss_province_2, \n" +
                "\tcbss_dingxiang_order_cnt as cbss_dingxiang, \n" +
                "\tcbss_international_order_cnt as cbss_international, \n" +
                "\tbss_5yuan_ordre_cnt as bss_5yuan, \n" +
                "\tbss_10yuan_ordre_cnt as bss_10yuan, \n" +
                "\tbss_domestic_order_cnt_1 as bss_domestic_1, \n" +
                "\tbss_province_order_cnt_1 as bss_province_1, \n" +
                "\tbss_xianshi_order_cnt as bss_xianshi, \n" +
                "\tbss_diejia_order_cnt as bss_diejia, \n" +
                "\tbss_domestic_order_cnt_2 as bss_domestic_2, \n" +
                "\tbss_province_order_cnt_2 as bss_province_2, \n" +
                "\tbss_dingxiang_order_cnt as bss_dingxiang, \n" +
                "\tbss_international_order_cnt as bss_international, \n" +
                "\ttotal_order_cnt as total\n" +
                "\t  FROM umid.rpt_order_product_day where timest between "+startTime+" and "+endTime+" and city_code=99999 order by timest desc";

        //分页条件
        if(curPage != null && countsPerPage != null){
            int offset = (curPage - 1) * countsPerPage;
            int limit = countsPerPage;
            sql += " offset "+offset+" limit "+limit;
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        try
        {
            dataList = greenPlumOperateService.query(sql);
        }
        catch (Exception e){
            LOG.error("查询流量包全省订购信息失败",e);
            return dataList;
        }
        return dataList;
    }


    /**
     * 下载报表
     * @param dataList
     * @param menuName
     * @param type
     * @return
     */
    public HSSFWorkbook getTrafficOrderExcelData(List<Map<String, Object>> dataList,String menuName,int type){
        String[] fields = new String[]{"cityname","cbss_5yuan","cbss_10yuan","cbss_domestic_1","cbss_province_1","cbss_xianshi","cbss_jiayou","cbss_domestic_2","cbss_province_2","cbss_dingxiang","cbss_international","bss_5yuan","bss_10yuan","bss_domestic_1","bss_province_1","bss_xianshi","bss_diejia","bss_domestic_2","bss_province_2","bss_dingxiang","bss_international","total"};
        if(type == 3){
            //替换第一个字段
            fields[0] = "time";
        }
        // 创建workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);    //填充的背景颜色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);     //左边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);    //右边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);    //顶边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //底边框
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
        //region 创建单元格
        //第一行
        HSSFRow row = sheet.createRow(0);
        HSSFCell c0 = row.createCell(0);
        if(type == 1){
            c0.setCellValue(new HSSFRichTextString("流量包订购量统计日报"));
        }else if(type == 2){
            c0.setCellValue(new HSSFRichTextString("流量包订购量统计月报"));
        }else if(type == 3){
            c0.setCellValue(new HSSFRichTextString("全省流量包订购量统计报表"));
        }
        c0.setCellStyle(style);
        //第二行
        HSSFRow row1 = sheet.createRow(1);
        HSSFCell c1 = row1.createCell(0);
        if(type == 3){
            c1.setCellValue(new HSSFRichTextString("日期"));
        }else{
            c1.setCellValue(new HSSFRichTextString("地市"));
        }
        c1.setCellStyle(style);
        HSSFCell c2 = row1.createCell(1);
        c2.setCellValue(new HSSFRichTextString("CBSS流量包"));
        c2.setCellStyle(style);
        HSSFCell c3 = row1.createCell(11);
        c3.setCellValue(new HSSFRichTextString("BSS流量包"));
        c3.setCellStyle(style);
        HSSFCell c16 = row1.createCell(21);
        c16.setCellValue(new HSSFRichTextString("合计"));
        c16.setCellStyle(style);
        //第三行
        HSSFRow row2 = sheet.createRow(2);
        HSSFCell c4 = row2.createCell(1);
        c4.setCellValue(new HSSFRichTextString("日包"));
        c4.setCellStyle(style);
        HSSFCell c5 = row2.createCell(3);
        c5.setCellValue(new HSSFRichTextString("月包"));
        c5.setCellStyle(style);
        HSSFCell c6 = row2.createCell(6);
        c6.setCellValue(new HSSFRichTextString("加油包"));
        c6.setCellStyle(style);
        HSSFCell c7 = row2.createCell(7);
        c7.setCellValue(new HSSFRichTextString("半年包"));
        c7.setCellStyle(style);
        HSSFCell c8 = row2.createCell(9);
        c8.setCellValue(new HSSFRichTextString("定向包"));
        c8.setCellStyle(style);
        HSSFCell c9 = row2.createCell(10);
        c9.setCellValue(new HSSFRichTextString("国际包"));
        c9.setCellStyle(style);
        HSSFCell c10 = row2.createCell(11);
        c10.setCellValue(new HSSFRichTextString("日包"));
        c10.setCellStyle(style);
        HSSFCell c11 = row2.createCell(13);
        c11.setCellValue(new HSSFRichTextString("月包"));
        c11.setCellStyle(style);
        HSSFCell c12 = row2.createCell(16);
        c12.setCellValue(new HSSFRichTextString("叠加包"));
        c12.setCellStyle(style);
        HSSFCell c13 = row2.createCell(17);
        c13.setCellValue(new HSSFRichTextString("半年包"));
        c13.setCellStyle(style);
        HSSFCell c14 = row2.createCell(19);
        c14.setCellValue(new HSSFRichTextString("定向包"));
        c14.setCellStyle(style);
        HSSFCell c15 = row2.createCell(20);
        c15.setCellValue(new HSSFRichTextString("国际包"));
        c15.setCellStyle(style);

        //第四行
        HSSFRow row3 = sheet.createRow(3);
        HSSFCell c17 = row3.createCell(1);
        c17.setCellValue(new HSSFRichTextString("5元日包"));
        c17.setCellStyle(style);
        HSSFCell c18 = row3.createCell(2);
        c18.setCellValue(new HSSFRichTextString("10元日包"));
        c18.setCellStyle(style);
        HSSFCell c19 = row3.createCell(3);
        c19.setCellValue(new HSSFRichTextString("国内包"));
        c19.setCellStyle(style);
        HSSFCell c20 = row3.createCell(4);
        c20.setCellValue(new HSSFRichTextString("省内包"));
        c20.setCellStyle(style);
        HSSFCell c21 = row3.createCell(5);
        c21.setCellValue(new HSSFRichTextString("闲时包"));
        c21.setCellStyle(style);
        HSSFCell c22 = row3.createCell(7);
        c22.setCellValue(new HSSFRichTextString("国内包"));
        c22.setCellStyle(style);
        HSSFCell c23 = row3.createCell(8);
        c23.setCellValue(new HSSFRichTextString("省内包"));
        c23.setCellStyle(style);
        HSSFCell c24 = row3.createCell(11);
        c24.setCellValue(new HSSFRichTextString("5元日包"));
        c24.setCellStyle(style);
        HSSFCell c25 = row3.createCell(12);
        c25.setCellValue(new HSSFRichTextString("10元日包"));
        c25.setCellStyle(style);
        HSSFCell c26 = row3.createCell(13);
        c26.setCellValue(new HSSFRichTextString("国内包"));
        c26.setCellStyle(style);
        HSSFCell c27 = row3.createCell(14);
        c27.setCellValue(new HSSFRichTextString("省内包"));
        c27.setCellStyle(style);
        HSSFCell c28 = row3.createCell(15);
        c28.setCellValue(new HSSFRichTextString("闲时包"));
        c28.setCellStyle(style);
        HSSFCell c29 = row3.createCell(17);
        c29.setCellValue(new HSSFRichTextString("国内包"));
        c29.setCellStyle(style);
        HSSFCell c30 = row3.createCell(18);
        c30.setCellValue(new HSSFRichTextString("省内包"));
        c30.setCellStyle(style);
        //endregion

        //数据行
        if (null != dataList && dataList.size() > 0)
        {
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> rowData = dataList.get(i);
                HSSFRow temprow = sheet.createRow(i + 4);

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
        //合并单元格
        //第一行
        Region region1 = new Region(0, (short) 0, 0, (short) 21);
        setRegionStyle(sheet,region1,style);
        //第二行
        Region region2 = new Region(1, (short) 0, 3, (short) 0);
        setRegionStyle(sheet,region2,style);
        Region region3 = new Region(1, (short) 1, 1, (short) 10);
        setRegionStyle(sheet,region3,style);
        Region region4 = new Region(1, (short) 11, 1, (short) 20);
        setRegionStyle(sheet,region4,style);
        Region region5 = new Region(1, (short) 21, 3, (short) 21);
        setRegionStyle(sheet,region5,style);
        //第三行
        Region region6 = new Region(2, (short) 1, 2, (short) 2);
        setRegionStyle(sheet,region6,style);
        Region region7 = new Region(2, (short) 3, 2, (short) 5);
        setRegionStyle(sheet,region7,style);
        Region region8 = new Region(2, (short) 6, 3, (short) 6);
        setRegionStyle(sheet,region8,style);
        Region region9 = new Region(2, (short) 7, 2, (short) 8);
        setRegionStyle(sheet,region9,style);
        Region region10 = new Region(2, (short) 9, 3, (short) 9);
        setRegionStyle(sheet,region10,style);
        Region region11 = new Region(2, (short) 10, 3, (short) 10);
        setRegionStyle(sheet,region11,style);
        Region region12 = new Region(2, (short) 11, 2, (short) 12);
        setRegionStyle(sheet,region12,style);
        Region region13 = new Region(2, (short) 13, 2, (short) 15);
        setRegionStyle(sheet,region13,style);
        Region region14 = new Region(2, (short) 16, 3, (short) 16);
        setRegionStyle(sheet,region14,style);
        Region region15 = new Region(2, (short) 17, 2, (short) 18);
        setRegionStyle(sheet,region15,style);
        Region region16 = new Region(2, (short) 19, 3, (short) 19);
        setRegionStyle(sheet,region16,style);
        Region region17 = new Region(2, (short) 20, 3, (short) 20);
        setRegionStyle(sheet,region17,style);
        sheet.addMergedRegion(region1);
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);
        sheet.addMergedRegion(region5);
        sheet.addMergedRegion(region6);
        sheet.addMergedRegion(region7);
        sheet.addMergedRegion(region8);
        sheet.addMergedRegion(region9);
        sheet.addMergedRegion(region10);
        sheet.addMergedRegion(region11);
        sheet.addMergedRegion(region12);
        sheet.addMergedRegion(region13);
        sheet.addMergedRegion(region14);
        sheet.addMergedRegion(region15);
        sheet.addMergedRegion(region16);
        sheet.addMergedRegion(region17);
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
