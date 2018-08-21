package com.axon.market.core.service.ishop;

import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hale on 2017/5/8.
 */
@Component("shopWeeklyService")
public class ShopWeeklyService
{
    private static final Logger LOG = Logger.getLogger(ShopBlackService.class.getName());

    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    /**查询地市周报信息
     * @param paras
     * @return
     */
    public List<Map<String, Object>> queryShopWeeklyByPage(Map<String, Object> paras)
    {
        String dateTime = String.valueOf(paras.get("dateTime"));
        Integer locationType = Integer.valueOf(String.valueOf(paras.get("locationType")));
        String baseId = String.valueOf(paras.get("baseId"));
        String areaCode = String.valueOf(paras.get("areaCode"));
        String lastFriday;//上周周五
        String thisFriday;//当前周五
        if (StringUtils.isEmpty(dateTime))
        {
            thisFriday = getSunday(dateTime).get("thisFriday");
            lastFriday = getSunday(dateTime).get("lastFriday");
        }
        else
        {
            String[] dateTimeArray = dateTime.split("-");
            if (dateTimeArray.length == 3)
            {
                dateTime = dateTimeArray[0] + dateTimeArray[1] + dateTimeArray[2];
                thisFriday = getSunday(dateTime).get("thisFriday");
                lastFriday = getSunday(dateTime).get("lastFriday");
            }
            else
            {
                thisFriday = getSunday(dateTime).get("thisFriday");
                lastFriday = getSunday(dateTime).get("lastFriday");
            }
        }
        //炒店类型
        String locationTypeSql = "";
        if (locationType != -1)
        {
            locationTypeSql = " and r.base_type_id=" + locationType + "";
        }
        //营业厅id
        String baseIdSql = "";
        if (!StringUtils.isEmpty(baseId) && !"null".equals(baseId))
        {
            baseIdSql = " and r.baseid=" + baseId + "";
        }
        //地市编码
        String areaCodeSql = "";
        if (!"".equals(areaCode) && !"99999".equals(areaCode) && !"null".equals(areaCode))
        {
            areaCodeSql = " and r.city_code = " + areaCode + "";
        }

        try
        {
           String sql = "select\n" +
                   "\tcityCode,\n" +
                   "\tcityName,\n" +
                   "\tbaseType,\n" +
                   "\tbaseTypeName,\n" +
                   "\tbaseName,\n" +
                   "\tSTATUS,\n" +
                   "\tbaseRank,\n" +
                   "\tcityRank,\n" +
                   "\tprovinceTaskNum,\n" +
                   "\tcityTaskNum,\n" +
                   "\tshopTaskNum,\n" +
                   "\texecuteTaskNum,\n" +
                   "\texecuteTaskRate,\n" +
                   "\tbusinessTypeNum,\n" +
                   "\tchangZhuSendNum,\n" +
                   "\tliuDongSendNum,\n" +
                   "\tzhiDingSendNum, \n" +
                   "\tchangZhuRate,\n" +
                   "\ttotalSendNum,\n" +
                   "\tsmsRate,\n" +
                   "\tcurrentTotalSendNumSum,\n" +
                   "\tlastTotalSendNumSum,\n" +
                   "\tcase when currentTotalSendNumSum <> 0 \n" +
                   "\tthen round( (currentTotalSendNumSum-lastTotalSendNumSum) * 100 / currentTotalSendNumSum,2) || '%' \n" +
                   "\tELSE 0.00 || '%' END AS ringRate,\n" +
                   "\tbanLiNum \n" +
                   "\t\n" +
                   "from (\n" +
                   "\tSELECT \n" +
                   "\tr.city_code as cityCode,\n" +
                   "\tr.city_name as cityName,\n" +
                   "\tr.base_type_id AS baseType,\n" +
                   "\tr.base_type_desc AS baseTypeName,\n" +
                   "\tr.base_name AS baseName,\n" +
                   "\tCASE WHEN(r.STATUS = 1) THEN '是'  ELSE '否' END AS STATUS,\n" +
                   "\trank()over(partition by base_type_id order by total_send_num desc) as baseRank,\n"+
                   "\trank()over(partition by city_code order by total_send_num desc) as cityRank,\n"+
                   "\tr.province_task_num AS provinceTaskNum,\n" +
                   "\tr.city_task_num AS cityTaskNum,\n" +
                   "\tr.shop_task_num AS shopTaskNum,\n" +
                   "\tr.excute_task_num AS executeTaskNum,\n" +
                   "\tCASE WHEN ( r.province_task_num + r.city_task_num + r.shop_task_num ) <> 0 \n" +
                   "\tTHEN round( r.excute_task_num * 100 / (r.province_task_num + r.city_task_num + r.shop_task_num),2 ) || '%' \n" +
                   "\tELSE 0.00 || '%' \n" +
                   "\tEND AS executeTaskRate,\n" +
                   "\tr.business_type_num AS businessTypeNum,\n" +
                   "\tr.changzhu_send_num as changZhuSendNum,\n" +
                   "\tr.liudong_send_num as liuDongSendNum,\n" +
                   "\tr.zhiding_send_num as zhiDingSendNum, \n" +
                   "\tCASE when r.city_resident_user <> 0 \n" +
                   "\tthen round( (r.base_resident_user*100 / r.city_resident_user),2 ) || '%' \n" +
                   "\tELSE 0.00 || '%' end as changZhuRate, \n" +
                   "\tr.total_send_num as totalSendNum,\n" +
                   "\tCASE WHEN (r.changzhu_send_num+r.liudong_send_num+r.zhiding_send_num) <> 0 \n" +
                   "\tTHEN round(r.recive_num * 100 /(r.changzhu_send_num+r.liudong_send_num+r.zhiding_send_num),2) || '%' \n" +
                   "\tELSE 0.00 || '%' \n" +
                   "\tEND AS smsRate,\n" +
                   "\tCOALESCE(a.currentTotalSendNumSum,0) as currentTotalSendNumSum,\n" +
                   "\tCOALESCE(b.lastTotalSendNumSum,0) as lastTotalSendNumSum,\n"+
                   "\tr.banli_num as banLiNum \n" +
                   "\tFROM umid.rpt_chaodian_week AS r \n" +
                   "\tleft join (select COALESCE(SUM(total_send_num),0) as currentTotalSendNumSum,baseid\n" +
                   "\tfrom umid.rpt_chaodian_week where timest="+thisFriday+" group by baseid ) AS a on r.baseid = a.baseid\n" +
                   "\tleft join (select COALESCE(SUM(total_send_num),0) as lastTotalSendNumSum,baseid \n" +
                   "\tfrom umid.rpt_chaodian_week where timest="+lastFriday+" group by baseid ) AS b on r.baseid = b.baseid\n"+
                   "\twhere 1=1 "+areaCodeSql+locationTypeSql+baseIdSql+"and r.timest = "+thisFriday+
                   ") as k"+"\t\nORDER BY k.baseType,k.totalSendNum DESC\n" ;
            return greenPlumOperateService.query(sql);
        }
        catch (Exception e)
        {
            LOG.error("queryShopWeeklyByPage error." + e);
            return null;
        }
    }

    /**
     * @param paras
     * @return
     */
    public Map<String, Object> queryShopWeeklyTotal(Map<String, Object> paras)
    {
        String dateTime = String.valueOf(paras.get("dateTime"));
        Integer locationType = Integer.valueOf(String.valueOf(paras.get("locationType")));
        String baseId = String.valueOf(paras.get("baseId"));
        String areaCode = String.valueOf(paras.get("areaCode"));

        if (StringUtils.isEmpty(dateTime))
        {
            dateTime = null;
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
                dateTime = null;
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
                return new HashMap<String,Object>();
            }
        }
        catch (Exception e)
        {
            LOG.error("queryShopWeeklyTotal error." + e);
            return new HashMap<String,Object>();
        }
    }

    /**
     * @param dateTime
     * @param locationType
     * @return
     */
    private Map<String, Object> getTotalData(String dateTime, String areaCode, String baseId, Integer locationType)
    {
        Map<String,String> map = getSunday(dateTime);
        String thisSunday = map.get("thisSunday");
        String lastSunday = map.get("lastSunday");
        String lastSaturday = map.get("lastSaturday");
        String thisFriday = map.get("thisFriday");
        String lastFriday = map.get("lastFriday");

        String locationTypeSql = "";
        String businessTypeLocationTypeSql = "";
        String totalSenNumSql = "";
        if (locationType != -1)//营业厅类型
        {
            locationTypeSql = " and r.base_type_id=" + locationType + "";
            businessTypeLocationTypeSql = " AND c.location_type_id = " + locationType + "";
            totalSenNumSql = " and base_type_id = "+locationType+"";
        }

        String areaCodeSql = "";
        String areaCodeSql2 = "";
        String areaCodeSql3 = "";
        if (!"".equals(areaCode) && !"99999".equals(areaCode))//地市编码
        {
            areaCodeSql = " and c.city_code = " + areaCode + "";
            areaCodeSql2 = " and r.city_code = " + areaCode + "";
            areaCodeSql3 = " and city_code = "+ areaCode + "";
        }

        String baseIdSql = "";
        String baseIdSql2 = "";
        if (!StringUtils.isEmpty(baseId) && !"null".equals(baseId))//营业厅id
        {
            baseIdSql = " and c.base_id = " + baseId + "";
            baseIdSql2 = " and r.baseid=" + baseId + "";
        }


        String sql ="SELECT t.* , COALESCE(business_type_num_total,0) as businessTypeNumTotal,\n" +
                "\tcase when currentTotalSendNumSum <> 0 \n" +
                "\tthen round( (currentTotalSendNumSum-lastTotalSendNumSum) * 100 / currentTotalSendNumSum,2) || '%'\n" +
                "\telse 0.00 || '%' end as totalRingRate\n" +
                "\tFROM( \n"+
                "\tSELECT \n" +
                "\tCOALESCE(sum(c. STATUS),0) AS baseNameTotal,\n" +
                "\tCOALESCE(sum(c. STATUS),0) AS statusTotal,\n" +
                "\tCOALESCE(sum(c. STATUS),0) AS qudaoRateTotal,\n" +
                "\tCOALESCE(sum(c. STATUS),0) AS cityRateTotal,\n" +
                "\tCOALESCE(sum(c.provinceTaskNum),0) AS provinceTaskNumTotal,\n" +
                "\tCOALESCE(sum(c.cityTaskNum),0) AS cityTaskNumTotal,\n" +
                "\tCOALESCE(sum(c.shopTaskNum),0) AS baseTaskNumTotal,\n" +
                "\tCOALESCE(sum(c.executeTaskNum),0) AS executeTaskNumTotal,\n" +
                "\tCASE WHEN (sum(c.provinceTaskNum) + sum(c.cityTaskNum) + sum(c.shopTaskNum)) <> 0 \n" +
                "\tTHEN round(sum(c.executeTaskNum) * 100 / (sum(c.provinceTaskNum) + sum(c.cityTaskNum) + sum(c.shopTaskNum)),2) || '%' \n" +
                "\tELSE 0.00 || '%' END AS executeTaskTotalRate,\n" +
                "\t(select count(distinct b.business_type) execute_taskbus_num\n" +
                "from dware.push_shop_task_execute_history a\n" +
                "inner join dware.push_shop_task_pool b on a.task_id = b.id and b.status in (2, 4, 6) and b.date between "+lastSaturday+" and "+thisFriday+"\n" +
                "inner join dware.push_conf_baseinfo c on a.base_id = c.base_id and c.timest = "+thisFriday+"\n" +
                "and a.date between "+lastSaturday+" and "+thisFriday+"\n" +
                "where 1=1 "+businessTypeLocationTypeSql+areaCodeSql+baseIdSql+
                "\n)  AS  business_type_num_total,"+
                "\n" +
                "\tCOALESCE(sum(c.changzhuSendNum),0) AS changzhuSendTotal,\n" +
                "\tCOALESCE(sum(c.liudongSendNum),0) AS liudongSendTotal,\n" +
                "\tCOALESCE(sum(c.zhidingSendNum),0) AS zhidingSendTotal,\n" +
                "\t\n" +
                "\tCASE WHEN (sum(c.city_resident_user)) <> 0 THEN round(sum(c.base_resident_user)*100/sum(c.city_resident_user),2) || '%'\n" +
                "\tELSE 0.00 || '%' END AS changZhuTotalRate,\n" +
                "\tCOALESCE(sum(c.total_send_num),0) AS totalSendNum,\n" +
                "\tCASE WHEN (sum(c.changZhuSendNum+c.liuDongSendNum+c.zhiDingSendNum)) <> 0 THEN round(sum(c.reciveNum) * 100 / sum(c.changZhuSendNum+c.liuDongSendNum+c.zhiDingSendNum),2) || '%' ELSE 0.00 || '%' END AS smsTotalRate,\n" +
                "\t(select COALESCE(SUM(total_send_num),0) as currentTotalSendNumSum \n" +
                "\tfrom umid.rpt_chaodian_week where timest="+thisFriday+totalSenNumSql+areaCodeSql3+" ) as currentTotalSendNumSum,"+
                "\t(select COALESCE(SUM(total_send_num),0) as lastTotalSendNumSum \n" +
                "\t from umid.rpt_chaodian_week where timest="+lastFriday+totalSenNumSql+areaCodeSql3+" ) as lastTotalSendNumSum,"+
                "\tCOALESCE(sum(c.banLiNum),0) AS totalBanLiNum\n" +
                "\tFROM \n" +
                "\t(SELECT\n" +
                "\tr.timest as timest,\n"+
                "\tr.city_code as cityCode,\n" +
                "\tr.city_name as cityName,\n" +
                "\tr.base_type_id AS baseType,\n" +
                "\tr.base_type_desc AS baseTypeName,\n" +
                "\tr.base_name AS baseName,\n" +
                "\tr.STATUS AS STATUS,\n" +
                "\tr.province_task_num AS provinceTaskNum,\n" +
                "\tr.city_task_num AS cityTaskNum,\n" +
                "\tr.shop_task_num AS shopTaskNum,\n" +
                "\tr.excute_task_num AS executeTaskNum,\n" +
                "\tr.changzhu_send_num as changZhuSendNum,\n" +
                "\tr.liudong_send_num as liuDongSendNum,\n" +
                "\tr.zhiding_send_num as zhiDingSendNum, \n" +
                "\tr.base_resident_user as base_resident_user, \n" +
                "\tr.city_resident_user as city_resident_user,\n" +
                "\tr.total_send_num as total_send_num,\n" +
                "\tr.business_type_num AS businessTypeNum,\n" +
                "\tr.recive_num as reciveNum,\n" +
                "\tr.banli_num as banLiNum \n" +
                "\tFROM umid.rpt_chaodian_week AS r \n" +
                "\tWHERE r.timest = " + thisFriday + "" + areaCodeSql2 + "" + baseIdSql2 + "" + locationTypeSql + "" +
                "\t)\n" +
                "\tAS c) AS t";
        return greenPlumOperateService.queryForMap(sql);
    }

    /**
     * 传入日期格式为yyyyMMdd或者null
     * 根据传入日期或当前日期寻找周日
     * 每周日上午跑数据入表，时间戳为本周五！
     * @param dateTime
     * @return
     * @throws ParseException
     */
    private Map<String,String> getSunday(String dateTime){
        Map<String,String> map = new HashMap<String,String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date;
        if(StringUtils.isEmpty(dateTime))
        {
            date = new Date();
        }else{
            try
            {
                date = format.parse(dateTime);
            }
            catch (ParseException e)
            {
                LOG.error("date格式转换异常",e);
                date = new Date();
            }
        }
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int dateOfWeek = c.get(Calendar.DAY_OF_WEEK);//获取星期几
            if(dateOfWeek == 1){//今天是周日,找到本周五
                map.put("thisSunday", format.format(date));//当前周日
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 7);
                map.put("lastSunday", format.format(c.getTime()));//上一个周日
                //获取上周六
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) -1);
                map.put("lastSaturday",format.format(c.getTime()));//上周六
                //获取这周五
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) +6);
                map.put("thisFriday", format.format(c.getTime()));//这周五
                //获取上周五
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 7);
                map.put("lastFriday", format.format(c.getTime()));//上周五
                return map;
            }else{//不是周日,找到周日
//                c.add(Calendar.DAY_OF_MONTH, -1);
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - dateOfWeek + 1);
                map.put("thisSunday", format.format(c.getTime()));//当前周日
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 7);
                map.put("lastSunday", format.format(c.getTime()));//上一个周日
                //获取上周六
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) -1);
                map.put("lastSaturday",format.format(c.getTime()));//上周六
                //获取这周五
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) +6);
                map.put("thisFriday",format.format(c.getTime()));//这周五
                //获取上周五
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 7);
                map.put("lastFriday", format.format(c.getTime()));//上周五
                return map;
            }
    }



    public HSSFWorkbook getShopWeeklyExcelData(List<Map<String, Object>> dataList, Map<String, Object> dataTotalList,String menuName)
    {
        String[] fields = new String[]{"basetypename","cityname", "basename", "status","baserank","cityrank","provincetasknum", "citytasknum", "shoptasknum", "executetasknum", "executetaskrate", "businesstypenum","changzhusendnum","liudongsendnum","zhiDingSendNum","changzhurate","totalsendnum","smsrate","ringrate","banlinum"};

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
        HSSFCell c6 = row.createCell(1);
        c6.setCellValue(new HSSFRichTextString("地市名称"));
        c6.setCellStyle(style);
        HSSFCell c1 = row.createCell(2);
        c1.setCellValue(new HSSFRichTextString("门店情况"));
        c1.setCellStyle(style);
        HSSFCell c2 = row.createCell(4);
        c2.setCellValue(new HSSFRichTextString("业务办理排名"));
        c2.setCellStyle(style);
        HSSFCell c3 = row.createCell(6);
        c3.setCellValue(new HSSFRichTextString("任务执行情况"));
        c3.setCellStyle(style);
        HSSFCell c4 = row.createCell(12);
        c4.setCellValue(new HSSFRichTextString("引流情况"));
        c4.setCellStyle(style);
        HSSFCell c5 = row.createCell(19);
        c5.setCellValue(new HSSFRichTextString("业务办理情况"));
        c5.setCellStyle(style);

        HSSFRow row2 = sheet.createRow(1);
        HSSFCell c15 = row2.createCell(2);
        c15.setCellValue(new HSSFRichTextString("名称"));
        c15.setCellStyle(style);
        HSSFCell c16 = row2.createCell(3);
        c16.setCellValue(new HSSFRichTextString("上线状态"));
        c16.setCellStyle(style);

        HSSFCell c17 = row2.createCell(4);
        c17.setCellValue(new HSSFRichTextString("渠道内排名"));
        c17.setCellStyle(style);
        HSSFCell c18 = row2.createCell(5);
        c18.setCellValue(new HSSFRichTextString("全市排名"));
        c18.setCellStyle(style);
        HSSFCell c19 = row2.createCell(6);
        c19.setCellValue(new HSSFRichTextString("省级任务总数"));
        c19.setCellStyle(style);
        HSSFCell c20 = row2.createCell(7);
        c20.setCellValue(new HSSFRichTextString("地市任务总数"));
        c20.setCellStyle(style);
        HSSFCell c21 = row2.createCell(8);
        c21.setCellValue(new HSSFRichTextString("营业厅任务总数"));
        c21.setCellStyle(style);
        HSSFCell c22 = row2.createCell(9);
        c22.setCellValue(new HSSFRichTextString("任务执行总数"));
        c22.setCellStyle(style);

        HSSFCell c23 = row2.createCell(10);
        c23.setCellValue(new HSSFRichTextString("任务执行率"));
        c23.setCellStyle(style);
        HSSFCell c24 = row2.createCell(11);
        c24.setCellValue(new HSSFRichTextString("业务类型数量"));
        c24.setCellStyle(style);
        HSSFCell c25 = row2.createCell(12);
        c25.setCellValue(new HSSFRichTextString("常驻引流人次"));
        c25.setCellStyle(style);
        HSSFCell c26 = row2.createCell(13);
        c26.setCellValue(new HSSFRichTextString("流动拜访人次"));
        c26.setCellStyle(style);
        HSSFCell c27 = row2.createCell(14);
        c27.setCellValue(new HSSFRichTextString("指定导入人次"));
        c27.setCellStyle(style);

        HSSFCell c28 = row2.createCell(15);
        c28.setCellValue(new HSSFRichTextString("常驻引流到达率"));
        c28.setCellStyle(style);
        HSSFCell c29 = row2.createCell(16);
        c29.setCellValue(new HSSFRichTextString("引流总人次"));
        c29.setCellStyle(style);
        HSSFCell c30 = row2.createCell(17);
        c30.setCellValue(new HSSFRichTextString("短信到达率"));
        c30.setCellStyle(style);
        HSSFCell c31 = row2.createCell(18);
        c31.setCellValue(new HSSFRichTextString("周环增长率"));
        c31.setCellStyle(style);
        HSSFCell c32 = row2.createCell(19);
        c32.setCellValue(new HSSFRichTextString("办理人次"));
        c32.setCellStyle(style);

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
        Region region3 = new Region(0, (short) 4, 0, (short) 5);
        Region region4 = new Region(0, (short) 6, 0, (short) 11);
        Region region5 = new Region(0, (short) 12, 0, (short) 18);
        sheet.addMergedRegion(region1);
        sheet.addMergedRegion(region6);
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);
        sheet.addMergedRegion(region5);
        return workbook;
    }


    private void setExcelCellValue(Map<String, Object> list, Integer length, String cellName, HSSFSheet sheet)
    {
        String[] totalFields = new String[]{"basenametotal","basenametotal", "statustotal", "qudaoratetotal", "cityratetotal", "provincetasknumtotal", "citytasknumtotal", "basetasknumtotal", "executetasknumtotal", "executetasktotalrate", "businesstypenumtotal", "changzhusendtotal", "liudongsendtotal", "zhidingsendtotal", "changzhutotalrate", "totalsendnum","smstotalrate","totalringrate","totalbanlinum"};

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
