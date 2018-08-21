package com.axon.market.core.service.istatistics;

import com.axon.market.common.domain.iStatistics.MessageMarketingDomain;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtt on 2017/4/17.
 */
@Service("messageMarketingResultService")
public class MessageMarketingResultService
{
    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private static final Logger LOG = Logger.getLogger(MessageMarketingResultService.class.getName());

    final String QUERY_SQL = "select * from umid.rpt_smstask_push_result_day";//查询sql语句

    final String QUERY_COUNT = "select count(*) from umid.rpt_smstask_push_result_day";//分页查询sql语句


    public List queryDataFromGP(String marketName, Integer offset, Integer limit, String startTime, String endTime) throws SQLException
    {
        final List<MessageMarketingDomain> dataList = new ArrayList<>();
        String sql = QUERY_SQL + " where 1=1 ";
        if (!StringUtils.isEmpty(marketName))
        {
            sql += " and activite_name like " + "'%" + marketName + "%' ";
        }
        if (!StringUtils.isEmpty(startTime))
        {
            sql += " and  timest >= " + "\'" + startTime + "\'";
        }
        if (!StringUtils.isEmpty(endTime))
        {
            sql += " and  timest <= " + "\'" + endTime + "\'";
        }
        sql += " ORDER  BY  timest DESC ";
        if(limit != null && offset != null){
            sql += " limit " + limit + " offset " + offset;
        }
        greenPlumOperateService.query(sql, new RowCallbackHandler()
        {
            @Override
            public void processRow(ResultSet rs) throws SQLException
            {
                MessageMarketingDomain mmd = new MessageMarketingDomain();
                // 组装数据
                mmd.setTimest(rs.getString("timest"));
                mmd.setTaskId(rs.getInt("taskid"));
                mmd.setModelId(rs.getString("model_id"));
                mmd.setActiviteId(rs.getInt("activite_id"));
                mmd.setActiviteName(rs.getString("activite_name"));
                mmd.setContentId(rs.getInt("content_id"));
                mmd.setContentDesc(rs.getString("content_desc"));
                mmd.setAreaIds(rs.getString("area_ids"));
                mmd.setAreaNames(rs.getString("area_names"));
                mmd.setCreate_user(rs.getString("create_user"));
                mmd.setAreaId(rs.getInt("area_id"));
                mmd.setTarget_usernum(rs.getInt("target_usernum"));
                mmd.setSend_num(rs.getInt("send_num"));
                mmd.setSend_succ_usernum(rs.getInt("send_succ_usernum"));
                mmd.setSend_fail_usernum(rs.getInt("send_fail_usernum"));
                mmd.setRecv_succ_usernum(rs.getInt("recv_succ_usernum"));
                mmd.setFeedback_usernum(rs.getInt("feedback_usernum"));
                mmd.setFeedback_usercnt(rs.getInt("feedback_usercnt"));
                mmd.setProduct_order_cnt(rs.getInt("product_order_cnt"));
                mmd.setProduct_ordersucc_cnt(rs.getInt("product_ordersucc_cnt"));
                mmd.setProduct_orderfail_cnt(rs.getInt("product_orderfail_cnt"));
                mmd.setProduct_order_user(rs.getInt("product_order_user"));
                mmd.setProduct_ordersucc_user(rs.getInt("product_ordersucc_user"));
                mmd.setProduct_orderfail_user(rs.getInt("product_orderfail_user"));
                mmd.setMarket_type(rs.getString("market_type"));
                dataList.add(mmd);
            }
        }, 10);
        return dataList;
    }


    /**
     * 查询分页
     *
     * @param marketName
     * @param startTime
     * @param endTime
     * @return
     */
    public Integer queryCount(String marketName, String startTime, String endTime)
    {
        String sql = QUERY_COUNT + " where 1=1 ";
        if (!StringUtils.isEmpty(marketName))
        {
            sql += " and activite_name like " + "\'%" + marketName + "%\' ";
        }
        if (!StringUtils.isEmpty(startTime))
        {
            sql += " and  timest >= " + "\'" + startTime + "\'";
        }
        if (!StringUtils.isEmpty(endTime))
        {
            sql += " and  timest <= " + "\'" + endTime + "\'";
        }
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
