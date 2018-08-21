package com.axon.market.core.service.istatistics;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/6/26.
 */

@Service("FirstLogin4GService")

public class FirstLogin4GService
{

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private  static final Logger LOG = Logger.getLogger(TrafficOrderService.class.getName());
    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    /**
     * 查询4G首登网相关数据
     *@param(startTime,endTime)
     *
     * @return
     */

    public List<Map<String, Object>> queryFirstLogin4G(String startTime,String endTime,Integer offset, Integer limit)
    {
        String sql = "SELECT a.timest as date, \n" +
                "\tb.scene_name as type, \n" +
                "\ta.scene_condition as condition, \n" +
                "\ta.catch_user_num as catchnum, \n" +
                "\ta.gift_user_num as giftnum, \n" +
                "\ta.send_user_num as sendnum \n" +
                "\tFROM umid.rpt_onnet_scene_day a\n" +
                "\tJOIN uaide.task_scene_type b ON a.scene_type=b.scene_type \n" +
                "\tWHERE timest between "+startTime+ " and "+endTime+" order by timest desc,task_id asc ";
        List<Map<String,Object>> dataList=new ArrayList<>();

        if(limit != null && offset != null){
            sql += " limit " + limit + " offset " + offset;
        }

        try
        {
            dataList = greenPlumOperateService.query(sql);
        }
        catch (Exception e)
        {
            LOG.error("查询4G首登网信息失败",e);
            return dataList;
        }
        return dataList;
    }

    public int queryCount(String startTime,String endTime)
    {
        String sql="SELECT count(*)FROM umid.rpt_onnet_scene_day where timest between "+startTime+ " and "+endTime ;

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
