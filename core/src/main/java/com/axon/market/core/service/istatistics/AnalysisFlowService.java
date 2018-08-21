package com.axon.market.core.service.istatistics;

import com.axon.market.common.util.MarketTimeUtils;
import com.axon.market.dao.mapper.istatistics.IAnalysisFlowMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bry08ant on 2016/9/22.
 */
@Component("analysisFlowService")
public class AnalysisFlowService
{

    @Autowired
    @Qualifier("analysisFlowDao")
    private IAnalysisFlowMapper analysisFlowDao;

    private static final Logger LOG = Logger.getLogger(AnalysisFlowService.class.getName());

    private static final NumberFormat FORMAT_DATA = new DecimalFormat("0.00");

    private static final NumberFormat FORMAT_PERCENTAGE = new DecimalFormat("0.00%");

    /**
     * @param yearMonth
     * @return
     */
    public List<Map<String, String>> queryAnalysisFlowRate(String yearMonth)
    {
        List<Map<String,String>> analysisData = new ArrayList<>();
        try
        {
            List<Map<String,String>> listData =analysisFlowDao.queryAnalysisFlowRate(yearMonth);
            for(Map<String,String> map:listData){
                Map<String,String> newMap = new HashMap<>();
                newMap.put("name",map.get("name"));
                newMap.put("value",FORMAT_DATA.format(Double.valueOf(map.get("total_flow")) / 1024));
                analysisData.add(newMap);
            }

        }
        catch (Exception e)
        {
            LOG.error("queryAnalysisFlowRate error:",e);
            return  analysisData;
        }
        return analysisData;
    }

    /**
     * @param yearMonth
     * @return
     */
    public Map<String, String[]> queryAnalysisFlowGrow(String yearMonth)
    {
        Map<String, String[]> result = new HashMap<String, String[]>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMM");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(yearMonth.substring(0, 4)), Integer.valueOf(yearMonth.substring(4)), 1);
        String[] dates = new String[3];

        for (int i = 0; i < 3; i++)
        {
            calendar.add(Calendar.MONTH, -1);
            dates[i] = sdf.format(calendar.getTime());
        }
        String dateRange = StringUtils.join(dates,",");
        List<Map<String,String>> listData;
        try
        {
            listData = analysisFlowDao.queryAnalysisFlowGrow(dateRange);
            for(Map<String,String> map : listData){
                String month = map.get("u_date").substring(4);
                String totalFlow = FORMAT_DATA.format(Double.valueOf(map.get("total_flow")) / 1024);
                Integer utype = Integer.valueOf(map.get("utype"));
                if(null == result.get(month)){
                    String[] tempData = new String[3];
                    result.put(month, tempData);
                }
                result.get(month)[utype - 2] = totalFlow;
            }
        }
        catch (Exception e)
        {
            LOG.error("queryAnalysisFlowGrow error:" , e);
            return new HashMap<String, String[]>();
        }
        return result;
    }

    /**
     * @param yearMonth
     * @return
     */
    public List<Map<String, String>> queryAnalysisFlow(String yearMonth)
    {
        List<Map<String,String>> analysisData = new ArrayList<>();
        List<Map<String,String>> listData;
        try
        {
            listData = analysisFlowDao.queryAnalysisFlow(yearMonth);
        }
        catch (Exception e)
        {
            LOG.error("queryAnalysisFlow error:",e);
            return analysisData;
        }
        for(Map<String,String> map:listData){
            Map<String, String> result = new HashMap<String, String>();
            result.put("areaName", map.get("name"));
            result.put("flowWhole", StringUtils.isNotEmpty(map.get("flowWhole")) ? map.get("flowWhole") : "-");
            result.put("proportionWhole", StringUtils.isNotEmpty(map.get("proportionWhole")) ? FORMAT_PERCENTAGE.format(Double.valueOf(map.get("proportionWhole"))) : "-");
            result.put("flow2G", StringUtils.isNotEmpty(map.get("flow2G")) ? map.get("flow2G") : "-");
            result.put("proportion2G", StringUtils.isNotEmpty(map.get("proportion2G")) ? FORMAT_PERCENTAGE.format(Double.valueOf(map.get("proportion2G"))) : "-");
            result.put("flow3G", StringUtils.isNotEmpty(map.get("flow3G")) ? map.get("flow3G") : "-");
            result.put("proportion3G", StringUtils.isNotEmpty(map.get("proportion3G")) ? FORMAT_PERCENTAGE.format(Double.valueOf(map.get("proportion3G"))) : "-");
            result.put("flow4G", StringUtils.isNotEmpty(map.get("flow4G")) ? map.get("flow4G") : "-");
            result.put("proportion4G", StringUtils.isNotEmpty(map.get("proportion4G")) ? FORMAT_PERCENTAGE.format(Double.valueOf(map.get("proportion4G"))) : "-");
            analysisData.add(result);
        }
        return analysisData;
    }

    /**
     * @param yearMonth
     * @return
     */
    public Map<String, Object> queryProportionOfCityUser(String yearMonth)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> cityNameResult = new ArrayList<String>();
        List<String> proportion3GResult = new ArrayList<String>();
        List<String> proportion4GResult = new ArrayList<String>();
        List<Map<String,String>> listData;
        try
        {
            listData = analysisFlowDao.queryProportionOfCityUser(yearMonth);
            for(Map<String,String> map : listData){
                cityNameResult.add(map.get("name"));
                proportion3GResult.add(StringUtils.isNotEmpty(map.get("proportion3G")) ? FORMAT_DATA.format(Double.valueOf(map.get("proportion3G")) * 100) : "-");
                proportion4GResult.add(StringUtils.isNotEmpty(map.get("proportion4G")) ? FORMAT_DATA.format(Double.valueOf(map.get("proportion4G")) * 100) : "-");
            }
        }
        catch (Exception e)
        {
            LOG.error("queryProportionOfCityUser error:",e);
            return result;
        }
        result.put("cityName", cityNameResult.toArray());
        result.put("proportion3G", proportion3GResult.toArray());
        result.put("proportion4G", proportion4GResult.toArray());
        return result;
    }

    /**
     * @param yearMonth
     * @return
     */
    public Map<String, Object> queryProportionOfPackage(String yearMonth)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String,String>> listData;
        List<Map<String,String>> proportionDatas = new ArrayList<>();
        try
        {
            listData = analysisFlowDao.queryProportionOfPackage(yearMonth);
            for(Map<String,String> map : listData){
                Map<String, String> dataResult = new HashMap<String, String>();
                dataResult.put("terminalProportion2G",StringUtils.isNotEmpty(map.get("ter_2g_zb")) ? FORMAT_DATA.format(Double.valueOf(map.get("ter_2g_zb")) * 100) : "-");
                dataResult.put("terminalProportion3G",StringUtils.isNotEmpty(map.get("ter_3g_zb")) ? FORMAT_DATA.format(Double.valueOf(map.get("ter_3g_zb")) * 100) : "-");
                dataResult.put("terminalProportion4G",StringUtils.isNotEmpty(map.get("ter_4g_zb")) ? FORMAT_DATA.format(Double.valueOf(map.get("ter_4g_zb")) * 100) : "-");
                dataResult.put("packageProportion2G",StringUtils.isNotEmpty(map.get("tc_utype_2g_zb")) ? FORMAT_DATA.format(Double.valueOf(map.get("tc_utype_2g_zb")) * 100) : "-");
                dataResult.put("packageProportion3G",StringUtils.isNotEmpty(map.get("tc_utype_3g_zb")) ? FORMAT_DATA.format(Double.valueOf(map.get("tc_utype_3g_zb")) * 100) : "-");
                dataResult.put("packageProportion4G",StringUtils.isNotEmpty(map.get("tc_utype_4g_zb")) ? FORMAT_DATA.format(Double.valueOf(map.get("tc_utype_4g_zb")) * 100) : "-");
                proportionDatas.add(dataResult);
            }
        }
        catch (Exception e)
        {
            LOG.error("queryProportionOfPackage error : " , e);
            return result;
        }
        if(CollectionUtils.isNotEmpty(proportionDatas)){
            Map<String, String> proportionData = proportionDatas.get(0);
            result.put("terminalProportion", new String[]{proportionData.get("terminalProportion2G"), proportionData.get("terminalProportion3G"), proportionData.get("terminalProportion4G")});
            result.put("packageProportion", new String[]{proportionData.get("packageProportion2G"), proportionData.get("packageProportion3G"), proportionData.get("packageProportion4G")});
        }
        return result;
    }


//    /**
//     * @param option
//     * @return
//     */
//    public int queryTerminalBrandCounts(Map<String, Object> option)
//    {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        String searchTime = option.get("areaValue") != null?String.valueOf(option.get("searchTime").toString().substring(0,10)):String.valueOf(calendar.getTime().toString().substring(0, 10));
//        String city = option.get("areaValue") != null ? String.valueOf(option.get("areaValue")) : "99999";
//        String terminalOS = String.valueOf(option.get("terminalOS"));
//        String brand = String.valueOf(option.get("brand"));
//        String sql = "select count(DISTINCT enbrand) from icas_analyse.c_total_terminal where $CONDITION;";
//        String conditions = " 1=1  and enbrand!='' ";
//        if (StringUtils.isNotEmpty(searchTime))
//        {
//            conditions += " and cdate = '" + searchTime+"'";
//        }
//        if (StringUtils.isNotEmpty(city))
//        {
//            conditions += " and city_code = " + city;
//        }
//        if (StringUtils.isNotEmpty(brand))
//        {
//            conditions += " and brand='" + brand + "' and enbrand!=''";
//        }
//        if (StringUtils.isNotEmpty(terminalOS))
//        {
//            if (terminalOS == "1")
//            {
//                conditions += " and os!='unknow' and os!='none' and os!='' and os is not null";
//            } else if (terminalOS == "0")
//            {
//                conditions += " and (os='none' or os='' or os is  null)";
//            } else if (terminalOS == "2")
//            {
//                conditions += " and os='unknow'";
//            }
//        }
//        sql = sql.replace("$CONDITION", conditions);
//
//        return icasJdbcTemplate.queryForObject(sql, Integer.class);
//    }
//
//    /**
//     *
//     * @param offset
//     * @param limit
//     * @param option
//     * @return
//     */
//    public List<Map<String, String>> queryTerminalBrandData(int offset, int limit, Map<String, Object> option)
//    {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        String searchTime = option.get("areaValue") != null?String.valueOf(option.get("searchTime").toString().substring(0,10)):String.valueOf(calendar.getTime().toString().substring(0,10));
//
//        String city = option.get("areaValue") != null ? String.valueOf(option.get("areaValue")) : "99999";
//        String terminalOS = String.valueOf(option.get("terminalOS"));
//        String brand = String.valueOf(option.get("brand"));
//
//        String sql = "select ifnull(brand,enbrand) as brand,enbrand,count(*) as num,sum(UV) as UV from icas_analyse.c_total_terminal "+
//                " where $CONDITION group by enbrand order by uv desc limit "+limit+" OFFSET "+offset;
//        String conditions = " 1=1  and enbrand!=''";
//        if (StringUtils.isNotEmpty(searchTime))
//        {
//            conditions += " and cdate = '" + searchTime+"'";
//        }
//        if (StringUtils.isNotEmpty(city))
//        {
//            conditions += " and city_code = " + city;
//        }
//        if (StringUtils.isNotEmpty(brand))
//        {
//            conditions += " and brand='" + brand + "'";
//        }
//        if (StringUtils.isNotEmpty(terminalOS))
//        {
//            if (terminalOS == "1")
//            {
//                conditions += " and os!='unknow' and os!='none' and os!='' and os is not null";
//            } else if (terminalOS == "0")
//            {
//                conditions += " and (os='none' or os='' or os is  null)";
//            } else if (terminalOS == "2")
//            {
//                conditions += " and os='unknow'";
//            }
//        }
//        sql = sql.replace("$CONDITION", conditions);
//        //1、获取数据
//        return icasJdbcTemplate.query(sql, new RowMapper<Map<String, String>>()
//        {
//            @Override
//            public Map<String, String> mapRow(ResultSet resultSet, int i) throws SQLException
//            {
//                Map<String, String> result = new HashMap<String, String>();
//                result.put("brand", resultSet.getString("brand"));
//                result.put("enbrand", resultSet.getString("enbrand"));
//                result.put("num", resultSet.getString("num"));
//                result.put("UV", resultSet.getString("UV"));
//                return result;
//            }
//        });
//    }
//
//    /**
//     * @param option
//     * @return
//     */
//    public int queryTerminalModelCounts(Map<String, Object> option)
//    {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        String searchTime = option.get("areaValue") != null?String.valueOf(option.get("searchTime").toString().substring(0,10)):String.valueOf(calendar.getTime().toString().substring(0, 10));
//        String city = option.get("areaValue") != null ? String.valueOf(option.get("areaValue")) : "99999";
//        String terminalOS = String.valueOf(option.get("terminalOS"));
//        String brand = String.valueOf(option.get("brand"));
//        String sql = "select count(0) from icas_analyse.c_total_terminal where $CONDITION;";
//        String conditions = " 1=1  and enbrand!='' ";
//        if (StringUtils.isNotEmpty(searchTime))
//        {
//            conditions += " and cdate = '" + searchTime+"'";
//        }
//        if (StringUtils.isNotEmpty(city))
//        {
//            conditions += " and city_code = " + city;
//        }
//        if (StringUtils.isNotEmpty(brand))
//        {
//            conditions += " and brand='" + brand + "' and enbrand!=''";
//        }
//        if (StringUtils.isNotEmpty(terminalOS))
//        {
//            if (terminalOS == "1")
//            {
//                conditions += " and os!='unknow' and os!='none' and os!='' and os is not null";
//            } else if (terminalOS == "0")
//            {
//                conditions += " and (os='none' or os='' or os is  null)";
//            } else if (terminalOS == "2")
//            {
//                conditions += " and os='unknow'";
//            }
//        }
//        sql = sql.replace("$CONDITION", conditions);
//
//        return icasJdbcTemplate.queryForObject(sql, Integer.class);
//    }
//
//    /**
//     *
//     * @param offset
//     * @param limit
//     * @param option
//     * @return
//     */
//    public List<Map<String, String>> queryTerminalModelData(int offset, int limit, Map<String, Object> option)
//    {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        String searchTime = option.get("areaValue") != null?String.valueOf(option.get("searchTime").toString().substring(0, 10)):String.valueOf(calendar.getTime().toString().substring(0,10));
//        String city = option.get("areaValue") != null ? String.valueOf(option.get("areaValue")) : "99999";
//        String terminalOS = String.valueOf(option.get("terminalOS"));
//        String brand = String.valueOf(option.get("brand"));
//        String searchLastTime ="";
//        if(StringUtils.isNotEmpty(searchTime))
//        {
//            try
//            {
//                calendar.setTime(df.parse(searchTime));
//                calendar.add(Calendar.DAY_OF_MONTH, -1);
//                searchLastTime = String.valueOf(df.format(calendar.getTime()).toString().substring(0, 10));
//            }catch (ParseException e){
//
//            }
//        }
//        String sql = "select ifnull(brand,enbrand) as brand,enbrand,name,UV,round(IFNULL((select uv from icas_analyse.c_total_terminal  where $CONDITION2 and terminal_id=a.terminal_id)/UV,\"--\")*100,2) as rate from icas_analyse.c_total_terminal a"+
//                " where $CONDITION order by uv desc limit "+limit+" OFFSET "+offset;
//        String conditions = " 1=1  and enbrand!=''";
//        String conditions2=" 1=1  and enbrand!=''";
//        if (StringUtils.isNotEmpty(searchTime))
//        {
//            conditions += " and cdate = '" + searchTime+"'";
//            conditions2 += " and cdate = '" + searchLastTime+"'";
//        }
//        if (StringUtils.isNotEmpty(city))
//        {
//            conditions += " and city_code = " + city;
//            conditions2 += " and city_code = " + city;
//        }
//        if (StringUtils.isNotEmpty(brand))
//        {
//            conditions += " and brand='" + brand + "'";
//            conditions2 += " and brand='" + brand + "'";
//        }
//        if (StringUtils.isNotEmpty(terminalOS))
//        {
//            if (terminalOS == "1")
//            {
//                conditions += " and os!='unknow' and os!='none' and os!='' and os is not null";
//                conditions2 += " and os!='unknow' and os!='none' and os!='' and os is not null";
//            } else if (terminalOS == "0")
//            {
//                conditions += " and (os='none' or os='' or os is  null)";
//                conditions2 += " and (os='none' or os='' or os is  null)";
//            } else if (terminalOS == "2")
//            {
//                conditions += " and os='unknow'";
//                conditions2 += " and os='unknow'";
//            }
//        }
//        sql = sql.replace("$CONDITION2", conditions2);
//        sql = sql.replace("$CONDITION", conditions);
//        //1、获取数据
//        return icasJdbcTemplate.query(sql, new RowMapper<Map<String, String>>()
//        {
//            @Override
//            public Map<String, String> mapRow(ResultSet resultSet, int i) throws SQLException
//            {
//                Map<String, String> result = new HashMap<String, String>();
//                result.put("brand", resultSet.getString("brand"));
//                result.put("name", resultSet.getString("name"));
//                result.put("UV", resultSet.getString("UV"));
//                result.put("rate", resultSet.getString("rate"));
//                return result;
//            }
//        });
//    }
//
//    /**
//     * @param option
//     * @return
//     */
//    public int queryTerminalSystemCounts(Map<String, Object> option)
//    {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        String searchTime = option.get("areaValue") != null?String.valueOf(option.get("searchTime").toString().substring(0, 10)):String.valueOf(calendar.getTime().toString().substring(0, 10));
//        String city = option.get("areaValue") != null ? String.valueOf(option.get("areaValue")) : "99999";
//        String terminalOS = String.valueOf(option.get("terminalOS"));
//        String sql = "select count(DISTINCT mobile_type) from icas_analyse.c_total_terminal where $CONDITION;";
//        String conditions = " 1=1  and enbrand!='' ";
//        if (StringUtils.isNotEmpty(searchTime))
//        {
//            conditions += " and cdate = '" + searchTime+"'";
//        }
//        if (StringUtils.isNotEmpty(city))
//        {
//            conditions += " and city_code = " + city;
//        }
//        if (StringUtils.isNotEmpty(terminalOS))
//        {
//            if (terminalOS == "1")
//            {
//                conditions += " and os!='unknow' and os!='none' and os!='' and os is not null";
//            } else if (terminalOS == "0")
//            {
//                conditions += " and (os='none' or os='' or os is  null)";
//            } else if (terminalOS == "2")
//            {
//                conditions += " and os='unknow'";
//            }
//        }
//        sql = sql.replace("$CONDITION", conditions);
//
//        return icasJdbcTemplate.queryForObject(sql, Integer.class);
//    }
//
//    /**
//     *
//     * @param offset
//     * @param limit
//     * @param option
//     * @return
//     */
//    public List<Map<String, String>> queryTerminalSystemData(int offset, int limit, Map<String, Object> option)
//    {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DAY_OF_MONTH, -1);
//        String searchTime = option.get("areaValue") != null?String.valueOf(option.get("searchTime").toString().substring(0, 10)):String.valueOf(calendar.getTime().toString().substring(0,10));
//        String city = option.get("areaValue") != null ? String.valueOf(option.get("areaValue")) : "99999";
//        String terminalOS = String.valueOf(option.get("terminalOS"));
//        String sql = "select mobile_type,count(*)num,sum(UV)UV from icas_analyse.c_total_terminal"+
//                " where $CONDITION group by mobile_type order by uv desc limit "+limit+" OFFSET "+offset;
//        String conditions = " 1=1  and enbrand!=''";
//        if (StringUtils.isNotEmpty(searchTime))
//        {
//            conditions += " and cdate = '" + searchTime+"'";
//        }
//        if (StringUtils.isNotEmpty(city))
//        {
//            conditions += " and city_code = " + city;
//        }
//        if (StringUtils.isNotEmpty(terminalOS))
//        {
//            if (terminalOS == "1")
//            {
//                conditions += " and os!='unknow' and os!='none' and os!='' and os is not null";
//            } else if (terminalOS == "0")
//            {
//                conditions += " and (os='none' or os='' or os is  null)";
//            } else if (terminalOS == "2")
//            {
//                conditions += " and os='unknow'";
//            }
//        }
//        sql = sql.replace("$CONDITION", conditions);
//        //1、获取数据
//        return icasJdbcTemplate.query(sql, new RowMapper<Map<String, String>>()
//        {
//            @Override
//            public Map<String, String> mapRow(ResultSet resultSet, int i) throws SQLException
//            {
//                Map<String, String> result = new HashMap<String, String>();
//                result.put("net", resultSet.getString("mobile_type"));
//                result.put("num", resultSet.getString("num"));
//                result.put("UV", resultSet.getString("UV"));
//                return result;
//            }
//        });
//    }
    /**
     * @param tableName
     * @return
     */
    public Map<String, Object> queryStockUserDaily(String tableName)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, String>> tableResult = new ArrayList<Map<String, String>>();
        Map<String, List<String>> chartDataTemp = new HashMap<String, List<String>>();
        Map<String, String> chartResult = new HashMap<String, String>();
        Map<String, String> tableHeadResult = new HashMap<String, String>();

        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMM");
        String[] dailyMonths = new String[4];
        Calendar cal = Calendar.getInstance();

        //获取当前月份和前两月月份
        for (int i = 0; i < dailyMonths.length; i++)
        {
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -i);
            dailyMonths[i] = sdf.format(cal.getTime());
            tableHeadResult.put("MONTH_" + i, String.valueOf(cal.get(Calendar.MONTH) + 1));
        }

        //1、获取数据
//        String sql = "select * from $TABLE d  where d.acct_month in (" + StringUtils.join(dailyMonths, ",") + ") group by d.acct_month, d.day_id ";
//        sql = sql.replace("$TABLE", tableName);
//        List<Map<String, String>> StockUserDailyList = analysisJdbcTemplate.query(sql, new RowMapper<Map<String, String>>()
//        {
//            @Override
//            public Map<String, String> mapRow(ResultSet resultSet, int i) throws SQLException
//            {
//                Map<String, String> result = new HashMap<String, String>();
//                result.put("month", resultSet.getString("acct_month"));
//                result.put("dayId", resultSet.getString("day_id"));
//                result.put("sum", resultSet.getString("real_ch"));
//                return result;
//            }
//        });
        List<Map<String, Object>> listData;
        List<Map<String, String>> StockUserDailyList = new ArrayList<Map<String, String>>();
        try
        {
            listData  =   analysisFlowDao.queryStockUserDaily(tableName,StringUtils.join(dailyMonths,","));
            for(Map<String,Object> map:listData){
                Map<String, String> dataResult = new HashMap<String, String>();
                dataResult.put("month", (String)map.get("acct_month"));
                dataResult.put("dayId", (String)map.get("day_id"));
                dataResult.put("sum", Double.toString((double)map.get("real_ch")));
                StockUserDailyList.add(dataResult);
            }
        }
        catch (Exception e)
        {
            LOG.error("queryStockUserDaily error:",e);
            return result;
        }
        //2、转变数据存储方式，将原来List<Map>结构改成Map,key:年月日，value:数据值
        Map<String, String> dateDataMap = new HashMap<String, String>();
        for (Map<String, String> stockUserDaily : StockUserDailyList)
        {
            String dayId = stockUserDaily.get("dayId");
            String month = stockUserDaily.get("month");
            String sum = stockUserDaily.get("sum");
            dateDataMap.put(month + dayId, sum);
        }

        //3、获取list<map>数据结构，map中为每行表格需要显示的信息
        for (int i = 1; i <= 31; i++)
        {
            Map<String, String> dataMap = new HashMap<String, String>();
            String day = i < 10 ? "0" + i : String.valueOf(i);
            dataMap.put("DAY", day);
            for (int j = 0; j < dailyMonths.length - 1; j++)
            {
                //将数据和保有率放入集合 SUM_0：当月，SUM_1，前一个月，SUM_2，前前一个月
                String prevData = dateDataMap.get(dailyMonths[j + 1] + day);
                String curData = dateDataMap.get(dailyMonths[j] + day);
                if (StringUtils.isEmpty(curData))
                {
                    dataMap.put("RATE_" + j, "-");
                    dataMap.put("SUM_" + j, "-");
                } else if (StringUtils.isEmpty(prevData) || "0".equals(curData) || "0".equals(prevData))
                {
                    dataMap.put("RATE_" + j, "-");
                    dataMap.put("SUM_" + j, curData);
                } else
                {
                    Double monthPre = Double.valueOf(prevData);
                    Double monthCur = Double.valueOf(curData);
                    dataMap.put("RATE_" + j, FORMAT_PERCENTAGE.format((double) monthCur / monthPre));
                    dataMap.put("SUM_" + j, FORMAT_DATA.format(monthCur));
                }
                dataMap.put("MONTH_" + j, dailyMonths[j].substring(4));

                //图标数据放入集合
                if (null == chartDataTemp.get(dailyMonths[j].substring(4)))
                {
                    List<String> chartDatas = new ArrayList<String>();
                    chartDatas.add(StringUtils.isNotEmpty(curData) ? curData : "-");
                    chartDataTemp.put(dailyMonths[j].substring(4), chartDatas);
                } else
                {
                    chartDataTemp.get(dailyMonths[j].substring(4)).add(StringUtils.isNotEmpty(curData) ? curData : "-");
                }
            }
            tableResult.add(dataMap);
        }

        //4、转换图标数据为图标需要的格式，图标数据 Map<String,String>    key:月份，value:用逗号分隔的值
        for (int i = 0; i < dailyMonths.length - 1; i++)
        {
            chartResult.put(dailyMonths[i].substring(4), StringUtils.join(chartDataTemp.get(dailyMonths[i].substring(4)), ","));
        }

        result.put("table", tableResult);
        result.put("chart", chartResult);
        result.put("tableHead", tableHeadResult);
        return result;
    }

    /**
     * @param yearMonth
     * @return
     */
    public List<Map<String, String>> queryMonthlyReservedData(String yearMonth)
    {

        List<Map<String,Object>> dataList;
        List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
        try{
            dataList = analysisFlowDao.downloadMonthlyReserved(yearMonth);
            for(Map<String,Object> map:dataList){
                Map<String, String> result = new HashMap<String, String>();
                result.put("cityName", map.get("city_name")+"");
                result.put("resultUserProperty", map.get("qudao")+"");
                result.put("resultUserCount",map.get("uv")==null?"-":FORMAT_DATA.format(map.get("uv")));
                result.put("resultUserIncome",map.get("fee")==null?"-":FORMAT_DATA.format(map.get("fee")));
                result.put("resultUserCountRate",map.get("uv_rate")==null?"-":FORMAT_DATA.format(map.get("uv_rate")));
                result.put("resultUserIncomeRate",map.get("fee_rate")==null?"-":FORMAT_DATA.format(map.get("fee_rate")));
                result.put("processUserProperty",map.get("qudao_2")+"");
                result.put("processUserCount",map.get("uv_2")==null?"-":FORMAT_DATA.format(map.get("uv_2")));
                result.put("expiresDuringYearUserCount",map.get("heyue")==null?"-":FORMAT_DATA.format(map.get("heyue")));
                result.put("expiresCumulativeUserCount",map.get("leiji")==null?"-":FORMAT_DATA.format(map.get("leiji")));
                result.put("contractExpiresRate",map.get("xuyue_rate")==null?"-":FORMAT_DATA.format(map.get("xuyue_rate")));
                result.put("singleCardUserCount",map.get("danka")==null?"-":FORMAT_DATA.format(map.get("danka")));
                result.put("singleCardUserRate",map.get("danka_rate")==null?"-":FORMAT_DATA.format(map.get("danka_rate")));
                resultList.add(result);
            }
        }
                catch (Exception e)
            {
                LOG.error("queryMonthlyReservedData error ：",e);
                return  resultList;
            }
            return resultList;
    }

    /**
     * @param yearMonth
     * @param cityName
     * @return
     */
    public List<Map<String, Object>> queryBaseMonthlyReservedDataResult(String yearMonth, String cityName)
    {
        List<Map<String,Object>> dataList;
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        try
        {
            dataList = analysisFlowDao.queryBaseMonthlyReservedDataResult(yearMonth,cityName);
            for(Map<String,Object> map:dataList){
                Map<String,Object> result = new HashMap<String,Object>();
                result.put("cityName",map.get("city_name"));
                result.put("ProfName", map.get("prof_name"));
                result.put("userCount",map.get("yonghu")==null?"-":FORMAT_DATA.format(map.get("yonghu")));
                result.put("income",map.get("shouru")==null?"-":FORMAT_DATA.format(map.get("shouru")));
                result.put("userReserved",map.get("yonghu_by") == null?"-":FORMAT_PERCENTAGE.format(map.get("yonghu_by")));
                result.put("incomeReserved",map.get("shouru_by") == null?"-":FORMAT_PERCENTAGE.format(map.get("shouru_by")));
                resultList.add(result);
            }
        }
        catch (Exception e)
        {
            LOG.error("queryBaseMonthlyReservedDataResult error ：",e);
            return  resultList;
        }
        return resultList;
    }

    /**
     * @param yearMonth
     * @param cityName
     * @return
     */
    public List<Map<String, Object>> queryBaseMonthlyReservedDataProcess(String yearMonth, String cityName)
    {
        List<Map<String,Object>> dataList;
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        try
        {
            dataList = analysisFlowDao.queryBaseMonthlyReservedDataProcess(yearMonth, cityName);
            for(Map<String,Object> map : dataList){
                Map<String,Object> result = new HashMap<String,Object>();
                result.put("cityName",map.get("city_name"));
                result.put("ProfName",map.get("prof_name"));
                result.put("userCount",map.get("yonghu") == null?"-":FORMAT_DATA.format(map.get("yonghu")));
                result.put("expiresDuringYearUserCount",map.get("heyue_yh") == null?"-":FORMAT_DATA.format(map.get("heyue_yh")));
                result.put("expiresCumulativeUserCount",map.get("daoqi_yh") == null?"-":FORMAT_DATA.format(map.get("daoqi_yh")));
                result.put("expiresRenewalRate",map.get("xuyuelv") == null?"-":FORMAT_PERCENTAGE.format(map.get("xuyuelv")));
                result.put("singleCardUserCount",map.get("gaojiazhi") == null?"-":FORMAT_PERCENTAGE.format(map.get("gaojiazhi")));
                result.put("singleCardUserRate",map.get("danka_xuyuelv") == null?"-":FORMAT_PERCENTAGE.format(map.get("danka_xuyuelv")));
                resultList.add(result);
            }
        }
        catch (Exception e)
        {
            LOG.error("queryBaseMonthlyReservedDataProcess error : ",e);
            return resultList;
        }
        return resultList;
    }

    /**
     * @return
     */
    public List<Map<String, String>> queryStockUserIncomeData()
    {
//        String sql = "select * from analysis_store";
//        return analysisJdbcTemplate.query(sql, new RowMapper<Map<String, String>>()
//        {
//            @Override
//            public Map<String, String> mapRow(ResultSet resultSet, int i) throws SQLException
//            {
//                Map<String, String> result = new HashMap<String, String>();
//                result.put("month", resultSet.getString("xh"));
//                result.put("income2G", dataHandle(resultSet.getString("2g_fee")));
//                result.put("userCounts2G", dataHandle(resultSet.getString("2g_uv")));
//                result.put("ARPU2G", dataHandle(resultSet.getString("2g_arpu")));
//                result.put("contractIncome2G", dataHandle(resultSet.getString("2g_heyue_fee")));
//                result.put("contractUserCounts2G", dataHandle(resultSet.getString("2g_heyue_uv")));
//                result.put("contractARPU2G", dataHandle(resultSet.getString("2g_heyue_arpu")));
//                result.put("income3G", dataHandle(resultSet.getString("3g_fee")));
//                result.put("userCounts3G", dataHandle(resultSet.getString("3g_uv")));
//                result.put("ARPU3G", dataHandle(resultSet.getString("3g_arpu")));
//                result.put("contractIncome3G", dataHandle(resultSet.getString("3g_heyue_fee")));
//                result.put("contractUserCounts3G", dataHandle(resultSet.getString("3g_heyue_uv")));
//                result.put("contractARPU3G", dataHandle(resultSet.getString("3g_heyue_arpu")));
//                result.put("income4G", dataHandle(resultSet.getString("4g_fee")));
//                result.put("userCounts4G", dataHandle(resultSet.getString("4g_uv")));
//                result.put("ARPU4G", dataHandle(resultSet.getString("4g_arpu")));
//                result.put("contractIncome4G", dataHandle(resultSet.getString("4g_heyue_fee")));
//                result.put("contractUserCounts4G", dataHandle(resultSet.getString("4g_heyue_uv")));
//                result.put("contractARPU4G", dataHandle(resultSet.getString("4g_heyue_arpu")));
//                result.put("incomeWhole", dataHandle(resultSet.getString("quan_fee")));
//                result.put("userCountsWhole", dataHandle(resultSet.getString("quan_uv")));
//                result.put("ARPUWhole", dataHandle(resultSet.getString("quan_arpu")));
//                result.put("contractIncomeWhole", dataHandle(resultSet.getString("quan_heyue_fee")));
//                result.put("contractUserCountsWhole", dataHandle(resultSet.getString("quan_heyue_uv")));
//                result.put("contractARPUWhole", dataHandle(resultSet.getString("quan_heyue_arpu")));
//                return result;
//            }
//        });
        List<Map<String,String>> listData;
        List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
        try
        {
            listData = analysisFlowDao.queryStockUserIncomeData();
            for(Map<String,String> map : listData){
                Map<String,String> result = new HashMap<String,String>();
                result.put("month",map.get("xh"));
                result.put("income2G", dataHandle(map.get("2g_fee")));
                result.put("userCounts2G", dataHandle(map.get("2g_uv")));
                result.put("ARPU2G", dataHandle(map.get("2g_arpu")));
                result.put("contractIncome2G", dataHandle(map.get("2g_heyue_fee")));
                result.put("contractUserCounts2G", dataHandle(map.get("2g_heyue_uv")));
                result.put("contractARPU2G", dataHandle(map.get("2g_heyue_arpu")));
                result.put("income3G", dataHandle(map.get("3g_fee")));
                result.put("userCounts3G", dataHandle(map.get("3g_uv")));
                result.put("ARPU3G", dataHandle(map.get("3g_arpu")));
                result.put("contractIncome3G", dataHandle(map.get("3g_heyue_fee")));
                result.put("contractUserCounts3G", dataHandle(map.get("3g_heyue_uv")));
                result.put("contractARPU3G", dataHandle(map.get("3g_heyue_arpu")));
                result.put("income4G", dataHandle(map.get("4g_fee")));
                result.put("userCounts4G", dataHandle(map.get("4g_uv")));
                result.put("ARPU4G", dataHandle(map.get("4g_arpu")));
                result.put("contractIncome4G", dataHandle(map.get("4g_heyue_fee")));
                result.put("contractUserCounts4G", dataHandle(map.get("4g_heyue_uv")));
                result.put("contractARPU4G", dataHandle(map.get("4g_heyue_arpu")));
                result.put("incomeWhole", dataHandle(map.get("quan_fee")));
                result.put("userCountsWhole", dataHandle(map.get("quan_uv")));
                result.put("ARPUWhole", dataHandle(map.get("quan_arpu")));
                result.put("contractIncomeWhole", dataHandle(map.get("quan_heyue_fee")));
                result.put("contractUserCountsWhole", dataHandle(map.get("quan_heyue_uv")));
                result.put("contractARPUWhole", dataHandle(map.get("quan_heyue_arpu")));
                resultList.add(result);
            }
        }
        catch (Exception e)
        {
            LOG.error("queryStockUserIncomeData error : ",e);
            return new ArrayList<Map<String,String>>();
        }
        return  resultList;
    }

    /**
     * @param yearMonth
     * @return
     */
    public List<Map<String, String>> queryBehaviorPreferences(String yearMonth)
    {
//        String sql = "select * from analysis_behavior where acct_month=$yearMonth";
//        sql = sql.replace("$yearMonth", yearMonth);
//        return analysisJdbcTemplate.query(sql, new RowMapper<Map<String, String>>()
//        {
//            @Override
//            public Map<String, String> mapRow(ResultSet resultSet, int i) throws SQLException
//            {
//                Map<String, String> result = new HashMap<String, String>();
//                result.put("appName", resultSet.getString("client_name"));
//                result.put("appType", "即时通讯");
//                result.put("useCounts", StringUtils.isNotEmpty(resultSet.getString("uv")) ? resultSet.getString("uv") : "-");
//                result.put("perCapitaUsageAmount", "-");
//                result.put("perCapitaVisitTimes", StringUtils.isNotEmpty(resultSet.getString("pv")) ? resultSet.getString("pv") : "-");
//                return result;
//            }
//        });
        List<Map<String,Object>> listData;
        List<Map<String,String>> resultData = new ArrayList<>();
        try
        {
            listData = analysisFlowDao.queryBehaviorPreferences(yearMonth);
            for(Map<String,Object> map:listData){
                Map<String,String> result = new HashMap<String, String>();
                result.put("appName", (String)map.get("client_name"));
                result.put("appType", "即时通讯");
                result.put("useCounts", map.get("uv") != null ? Long.toString((long)map.get("uv")) : "-");
                result.put("perCapitaUsageAmount", "-");
                result.put("perCapitaVisitTimes", map.get("pv") != null ? map.get("pv").toString() : "-");
                resultData.add(result);
            }
        }
        catch (Exception e)
        {
            LOG.error("queryBehaviorPreferences error",e);
            return resultData;
        }
        return resultData;
    }

    /**
     * @param response
     * @param dailyType
     * @throws IOException
     */
    public void downloadAnalysisDaily(HttpServletResponse response, String dailyType) throws IOException
    {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        String fileName = "存量用户日报(" + ("4G".equals(dailyType) ? "4G" : "全量") + ")_" + MarketTimeUtils.formatDateToYMD(new Date());
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".csv\"");

        final OutputStream os = response.getOutputStream();

        String tableName = "4G".equals(dailyType) ? "analysis_daily_4g" : "analysis_daily";
        Map<String, Object> dailyResult = queryStockUserDaily(tableName);
        List<Map<String, String>> dailyTableResult = (List<Map<String, String>>) dailyResult.get("table");
        Map<String, String> tableHeadResult = (Map<String, String>) dailyResult.get("tableHead");

        try
        {
            List<String> downloadTitle = new ArrayList<String>();
            downloadTitle.add("日期");
            for (int i = 2; i >= 0; i--)
            {
                String month = tableHeadResult.get("MONTH_" + i);
                downloadTitle.add(month + "月收入");
                downloadTitle.add(month + "月收入保有率");
            }
            os.write((StringUtils.join(downloadTitle, ",") + "\r\n").getBytes("GBK"));

            for (Map<String, String> dailyData : dailyTableResult)
            {
                List<String> tempData = new ArrayList<String>();
                tempData.add(dailyData.get("DAY"));
                for (int i = 2; i >= 0; i--)
                {
                    tempData.add(dailyData.get("SUM_" + i));
                    tempData.add(dailyData.get("RATE_" + i));
                }
                os.write((StringUtils.join(tempData, ",") + "\r\n").getBytes("GBK"));
            }
        } catch (Exception e)
        {

        } finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }

    public void downloadMonthlyReserved(HttpServletResponse response, String yearMonth) throws IOException
    {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        String fileName = "月保有情况(" + yearMonth.substring(0, 4) + "年" + yearMonth.substring(4) + "月)_" + MarketTimeUtils.formatDateToYMD(new Date());
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".csv\"");

        final OutputStream os = response.getOutputStream();


        try
        {
            os.write(("地市/部门,用户属性(结果指标),拍照用户(万户)(结果指标),拍照收入(万元)(结果指标),用户保有率(结果指标),收入保有率(结果指标),"
                    + "用户属性(过程指标),拍照用户(万户)(过程指标),终端合约年内到期用户(过程指标),终端合约当前累计到期用户(过程指标),"
                    + "终端合约到期续约率(过程指标),高价值单卡用户数(过程指标),高价值单卡签约率(过程指标)\r\n").getBytes("GBK"));

//            List<Map<String,Object>> dataList = analysisFlowDao.downloadMonthlyReserved(yearMonth);
            List<Map<String,String>> dataList = queryMonthlyReservedData(yearMonth);

            for(Map<String,String> map:dataList){
                List<String> result = new ArrayList<>();
                result.add(map.get("cityName"));
                result.add(map.get("resultUserProperty"));
                result.add(map.get("resultUserCount"));
                result.add(map.get("resultUserIncome"));
                result.add(map.get("resultUserCountRate"));
                result.add(map.get("resultUserIncomeRate"));
                result.add(map.get("processUserProperty")+"".replace("\n", ""));
                result.add(map.get("processUserCount"));
                result.add(map.get("expiresDuringYearUserCount"));
                result.add(map.get("expiresCumulativeUserCount"));
                result.add(map.get("contractExpiresRate"));
                result.add(map.get("singleCardUserCount"));
                result.add(map.get("singleCardUserRate"));
                os.write((StringUtils.join(result, ",") + "\r\n").getBytes("GBK"));
            }
        } catch (Exception e)
        {
            LOG.error("downloadMonthlyReserved error : ",e);
        } finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }

    public void downloadBaseMonthlyReserved(HttpServletResponse response, String yearMonth, String city) throws IOException
    {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        String fileName = "月保有情况_基层(" + city + "_" + yearMonth.substring(0, 4) + "年" + yearMonth.substring(4) + "月)_" + MarketTimeUtils.formatDateToYMD(new Date());
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".csv\"");

        final OutputStream os = response.getOutputStream();

        try
        {
            os.write(("用户属性,拍照用户(万户),拍照收入(万元),用户保有率,收入保有率,终端合约年内到期用户,终端合约当前累计到期用户,"
                    + "终端合约到期续约率,高价值单卡用户数,高价值单卡签约率\r\n").getBytes("GBK"));

            List<Map<String,Object>> dataList = analysisFlowDao.downloadBaseMonthlyReserved(yearMonth,city);
            for(Map<String,Object> map:dataList){
                List<String> result = new ArrayList<>();
                result.add(map.get("prof_name")+"");
                result.add(map.get("yonghu") == null?"-":FORMAT_DATA.format(map.get("yonghu")));
                result.add(map.get("shouru") == null?"-":FORMAT_DATA.format(map.get("shouru")));
                result.add(map.get("yonghu_by") == null?"-":FORMAT_PERCENTAGE.format(map.get("yonghu_by")));
                result.add(map.get("shouru_by") == null?"-":FORMAT_PERCENTAGE.format(map.get("shouru_by")));
                result.add(map.get("heyue_yh") == null?"-":FORMAT_DATA.format(map.get("heyue_yh")));
                result.add(map.get("daoqi_yh") == null?"-":FORMAT_DATA.format(map.get("daoqi_yh")));
                result.add(map.get("xuyuelv") == null?"-":FORMAT_PERCENTAGE.format(map.get("xuyuelv")));
                result.add(map.get("gaojiazhi") == null?"-":FORMAT_DATA.format(map.get("gaojiazhi")));
                result.add(map.get("danka_xuyuelv") == null?"-":FORMAT_PERCENTAGE.format(map.get("danka_xuyuelv")));
                os.write((StringUtils.join(result, ",") + "\r\n").getBytes("GBK"));
            }
        } catch (Exception e)
        {
            LOG.error("downloadBaseMonthlyReserved error : ",e);
        } finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }

    public void downloadIncomeSituation(HttpServletResponse response) throws IOException
    {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        String fileName = "存量用户收入情况_" + MarketTimeUtils.formatDateToYMD(new Date());
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".csv\"");

        final OutputStream os = response.getOutputStream();
        List<Map<String, String>> stockUserIncomeList = queryStockUserIncomeData();
        try
        {
            List<String> resultTitle = new ArrayList<String>();
            resultTitle.add("拍照/观察月");
            for (int i = 2; i < 6; i++)
            {
                String serviceType = i > 4 ? "全业务" : i + "G";
                resultTitle.add("出账收入(" + serviceType + ")");
                resultTitle.add("出账用户(" + serviceType + ")");
                resultTitle.add("ARPU(" + serviceType + ")");
                resultTitle.add("其中:合约用户出账收入(" + serviceType + ")");
                resultTitle.add("合约用户出账用户数(" + serviceType + ")");
                resultTitle.add("合约用户ARPU(" + serviceType + ")");
            }
            os.write((StringUtils.join(resultTitle, ",") + "\r\n").getBytes("GBK"));

            for (Map<String, String> stockUserIncome : stockUserIncomeList)
            {
                List<String> result = new ArrayList<String>();
                result.add(stockUserIncome.get("month"));
                result.add(stockUserIncome.get("income2G"));
                result.add(stockUserIncome.get("userCounts2G"));
                result.add(stockUserIncome.get("ARPU2G"));
                result.add(stockUserIncome.get("contractIncome2G"));
                result.add(stockUserIncome.get("contractUserCounts2G"));
                result.add(stockUserIncome.get("contractARPU2G"));
                result.add(stockUserIncome.get("income3G"));
                result.add(stockUserIncome.get("userCounts3G"));
                result.add(stockUserIncome.get("ARPU3G"));
                result.add(stockUserIncome.get("contractIncome3G"));
                result.add(stockUserIncome.get("contractUserCounts3G"));
                result.add(stockUserIncome.get("contractARPU3G"));
                result.add(stockUserIncome.get("income4G"));
                result.add(stockUserIncome.get("userCounts4G"));
                result.add(stockUserIncome.get("ARPU4G"));
                result.add(stockUserIncome.get("contractIncome4G"));
                result.add(stockUserIncome.get("contractUserCounts4G"));
                result.add(stockUserIncome.get("contractARPU4G"));
                result.add(stockUserIncome.get("incomeWhole"));
                result.add(stockUserIncome.get("userCountsWhole"));
                result.add(stockUserIncome.get("ARPUWhole"));
                result.add(stockUserIncome.get("contractIncomeWhole"));
                result.add(stockUserIncome.get("contractUserCountsWhole"));
                result.add(stockUserIncome.get("contractARPUWhole"));
                os.write((StringUtils.join(result, ",") + "\r\n").getBytes("GBK"));
            }
        } catch (Exception e)
        {
            LOG.error("downloadIncomeSituation error : ",e);
        } finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }

    /**
     * @param data
     * @return
     */
    private String dataHandle(String data)
    {
        return StringUtils.isNotEmpty(data) ? FORMAT_DATA.format(Double.valueOf(data)) : "-";
    }
}
