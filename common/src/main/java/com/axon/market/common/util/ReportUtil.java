package com.axon.market.common.util;

import com.axon.market.common.util.excel.ExcelTemplate;
import com.axon.market.common.util.excel.FieldsRowWriter;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengcr on 2016/12/5.
 */
public class ReportUtil
{
    private static final Logger log = Logger.getLogger(ReportUtil.class);

    private static Gson gson = new Gson();

    private static String TEMPLATE_DIR;

    private static ServletContext servletContext;

    /**
     * 设置Web应用部署目录
     */
    static{
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        servletContext = webApplicationContext.getServletContext();
        TEMPLATE_DIR = servletContext.getRealPath("/WEB-INF/excelTemplate");
    }

    /**
     * 返回ServletContext
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static ServletContext getServletContext(){
        return ReportUtil.servletContext;
    }

    /**
     * 多行报表
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param templateFile
     * @param headInfo
     * @param datas
     * @return
     * @throws Exception
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @SuppressWarnings("unchecked")
    public static ExcelTemplate genMultRowsExcel(String templateFile, String headInfo, List<Map<String,Object>> datas) throws Exception{
        File file = new File(TEMPLATE_DIR + "/" + templateFile);
        ExcelTemplate template = ExcelTemplate.createTemplate(file);
        // 列填充字段配置
        String fieldsJson = template.readCell(1, 1).toString();
        Map<String, Object> data = gson.fromJson(fieldsJson, Map.class);
        int startRow = ((Double)data.get("startRow")).intValue();
        int startCol = ((Double)data.get("startCol")).intValue();
        String[] fields = ((ArrayList<String>)data.get("fields")).toArray(new String[]{});
        // 设置报表头信息及行数据
        template.writeCell(1, 1, headInfo);
        template.writeData(startRow, datas, new FieldsRowWriter(fields, startCol));
        // 删除多余行
        template.removeRows(startRow + datas.size() + 2, template.getRowsNumber());
        return template;
    }

    /**
     * 固定行报表
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param templateFile
     * @param headInfo
     * @param datas
     * @return
     * @throws Exception
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @SuppressWarnings("unchecked")
    public static ExcelTemplate genFixedRowsExcel(String templateFile, String headInfo, Map<String,Map<String, ?>> datas) throws Exception{
        File file = new File(TEMPLATE_DIR + "/" + templateFile);
        ExcelTemplate template = ExcelTemplate.createTemplate(file);
        Map<Integer, String> rowItems = template.getRowItems(0);
        // 列填充字段配置
        String fieldsJson = template.readCell(1, 1).toString();
        Map<String, Object> data = gson.fromJson(fieldsJson, Map.class);
        int startCol = ((Double)data.get("startCol")).intValue();
        String[] fields = ((ArrayList<String>)data.get("fields")).toArray(new String[]{});
        // 设置报表头信息及行数据
        template.writeCell(1, 1, headInfo);
        template.writeItems(rowItems, datas, new FieldsRowWriter(fields, startCol));
        return template;
    }

    /**
     * 空报表
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param templateFile
     * @return
     * @throws Exception
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static ExcelTemplate genBlankExcel(String templateFile) throws Exception{
        File file = new File(TEMPLATE_DIR + "/" + templateFile);
        return ExcelTemplate.createTemplate(file);
    }

    /**
     * 解析Echart导出图表
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param datastr
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @SuppressWarnings("restriction")
    public static InputStream parseEchartBASE64Img(String datastr){
        try {
            String data = datastr.split(",")[1].replace(" ", "+");
            byte[] b = new sun.misc.BASE64Decoder().decodeBuffer(data);
            return new ByteArrayInputStream(b);
        } catch (Exception e) {
            log.error("Load Echart Export Data failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * 列表数据转换为MAP
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param dataList
     * @param keyField
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static Map<String, Map<String, ?>> listData2Map(List<Map<String, Object>> dataList, String keyField){
        Map<String, Map<String, ?>> dataMap = new HashMap<String, Map<String, ?>>();
        for(Map<String, ?> data : dataList){
            Object key = data.get(keyField);
            dataMap.put(String.valueOf(key), data);
        }
        return dataMap;
    }

    /**
     * 获取行索引
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param dataList
     * @param uniqueFld
     * @param startRow
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static Map<String, Integer> getRowsIndex(List<Map<String, Object>> dataList, String uniqueFld, int startRow){
        Map<String, Integer> rowIndex = new HashMap<String, Integer>();
        for(int i = 0; i < dataList.size(); i++){
            String value = (String)dataList.get(i).get(uniqueFld);
            if(value != null){
                rowIndex.put(value, startRow + i);
            }
        }
        return rowIndex;
    }

    /**
     * 列表数据简单汇总
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param dataList
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static void collectListData(List<Map<String, Object>> dataList, RowCollector collector){
        for(Map<String, ?> data : dataList){
            collector.collect(data);
        }
    }

//    /**
//     * 中文日期
//     * 功能描述: <br>
//     * 〈功能详细描述〉
//     *
//     * @param date
//     * @return
//     * @see [相关类/方法](可选)
//     * @since [产品/模块版本](可选)
//     */
//    public static String getCHNDate(String date){
//        try {
//            Date dt = DateUtil.strToDate(date, "yyyyMMdd");
//            return DateUtil.date2Str(dt, "yyyy年MM月dd日");
//        } catch (ParseException e) {
//            log.error("Parse Date Str error: " + e.getMessage());
//            return null;
//        }
//    }

//    /**
//     * 中文日期
//     * 功能描述: <br>
//     * 〈功能详细描述〉
//     *
//     * @param date
//     * @return
//     * @see [相关类/方法](可选)
//     * @since [产品/模块版本](可选)
//     */
//    public static String getCHNMonth(String date){
//        try {
//            Date dt = DateUtil.strToDate(date, "yyyyMMdd");
//            return DateUtil.date2Str(dt, "yyyy年MM月");
//        } catch (ParseException e) {
//            log.error("Parse Date Str error: " + e.getMessage());
//            return null;
//        }
//    }
//
//    /**
//     * 中文日期
//     * 功能描述: <br>
//     * 〈功能详细描述〉
//     *
//     * @param date
//     * @return
//     * @see [相关类/方法](可选)
//     * @since [产品/模块版本](可选)
//     */
//    public static String getCHNYear(String date){
//        try {
//            Date dt = DateUtil.strToDate(date, "yyyyMMdd");
//            return DateUtil.date2Str(dt, "yyyy年MM月");
//        } catch (ParseException e) {
//            log.error("Parse Date Str error: " + e.getMessage());
//            return null;
//        }
//    }

    /**
     * 行统计汇总工具
     * 〈一句话功能简述〉<br>
     * 〈功能详细描述〉
     *
     * @author 15050873
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public interface RowCollector{
        /**
         * 统计字段
         * 功能描述: <br>
         * 〈功能详细描述〉
         *
         * @return
         * @see [相关类/方法](可选)
         * @since [产品/模块版本](可选)
         */
        public String[] fields();

        /**
         * 计数
         * 功能描述: <br>
         * 〈功能详细描述〉
         *
         * @param field
         * @return
         * @see [相关类/方法](可选)
         * @since [产品/模块版本](可选)
         */
        public Integer count(String field);

        /**
         * 累积和
         * 功能描述: <br>
         * 〈功能详细描述〉
         *
         * @param field
         * @return
         * @see [相关类/方法](可选)
         * @since [产品/模块版本](可选)
         */
        public Double sum(String field);

        /**
         * 平均值
         * 功能描述: <br>
         * 〈功能详细描述〉
         *
         * @param field
         * @return
         * @see [相关类/方法](可选)
         * @since [产品/模块版本](可选)
         */
        public Double avg(String field);

        /**
         * 汇总
         * 功能描述: <br>
         * 〈功能详细描述〉
         *
         * @param row
         * @see [相关类/方法](可选)
         * @since [产品/模块版本](可选)
         */
        public void collect(Map<String, ?> row);

    }
}
