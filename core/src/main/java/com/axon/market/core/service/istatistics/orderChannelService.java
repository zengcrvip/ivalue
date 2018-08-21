package com.axon.market.core.service.istatistics;

import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/7/7.
 */

@Service("orderChannelService")

public class orderChannelService
{
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private  static final Logger LOG = Logger.getLogger(orderChannelService.class.getName());
    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    String queryOrderChannelSql="SELECT row_number() over (order by order_channel) as rowid,\n" +
                                "\tb.channelname as orderchannel,\n"+
                                "\torder_num as ordernum,\n"+
                                "\tsucc_num as succnum,\n"+
                                "\tround(succ_rate*100 , 2) || '%' as succrate,\n"+
                                "\torder_user_num as orderusernum,\n"+
                                "\tsucc_user_num as succusernum,\n"+
                                "\tround(succ_user_rate*100 , 2) || '%' as succuserrate,\n"+
                                "\tround(order_channel_rate*100 , 2) || '%' as orderchannelrate\n"+
                                "\tFROM DATATABLE a join uaide.push_partners_forward_display b on a.order_channel=b.channelnum where city_code=99999 and timest=ORDERTIME";


    public List<Map<String, Object>> queryMonthlyOrderChannel(String currentMon,Integer offset,Integer limit)
    {
        String sql1=queryOrderChannelSql.replace("DATATABLE","umid.rpt_analyse_sp_channel_month").replace("ORDERTIME",currentMon);
        List<Map<String, Object>> dataList = new ArrayList<>();

        if(limit != null && offset != null){
            sql1 += " limit " + limit + " offset " + offset;
        }

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

    public List<Map<String, Object>> queryOrderChannel(String orderTime,Integer offset,Integer limit)
    {
        String sql2=queryOrderChannelSql.replace("DATATABLE","umid.rpt_analyse_sp_channel_day").replace("ORDERTIME",orderTime);
        List<Map<String, Object>> dataList = new ArrayList<>();

        if(limit != null && offset != null){
            sql2 += " limit " + limit + " offset " + offset;
        }

        try
        {
            dataList = greenPlumOperateService.query(sql2);
        }
        catch (Exception e){
            LOG.error("查询流量包日报订购信息失败",e);
            return dataList;
        }
        return dataList;
    }




    public int queryMonthlyCount(String currentMon)
    {
        String sql="SELECT count(*)FROM umid.rpt_analyse_sp_channel_month a join uaide.push_partners_forward_display b on a.order_channel=b.channelnum where city_code=99999 and timest = "+currentMon ;

        List list = jdbcTemplate.query(sql, new RowMapper<Object>()
        {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException
            {
                int count = rs.getInt(1);
                return count;
            }
        });
        return (Integer) list.get(0);
    }


    public int queryCount(String orderTime)
    {
        String sql="SELECT count(*)FROM umid.rpt_analyse_sp_channel_day a join uaide.push_partners_forward_display b on a.order_channel=b.channelnum where city_code=99999 and timest = "+orderTime ;

        List list = jdbcTemplate.query(sql, new RowMapper<Object>()
        {
            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException
            {
                int count = rs.getInt(1);
                return count;
            }
        });
        return (Integer) list.get(0);
    }



}
