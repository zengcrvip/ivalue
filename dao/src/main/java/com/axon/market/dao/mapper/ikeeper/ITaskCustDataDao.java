package com.axon.market.dao.mapper.ikeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 掌柜任务目标用户查询
 * Created by zengcr on 2017/8/16.
 */
@Component("taskCustDataDao")
public class ITaskCustDataDao {

    /**
     * 生日维系目标用户查询
     */
    private static final String BIRTHDAY_QUERY_SQL = "select b.phone from uapp.push_user_birthday_day b join uaide.keeper_user_maintain u on b.phone = u.user_phone and u.status = 1 where b.birth_date = ";

    /**
     * 优惠到期续航目标用户查询
     * 优惠到期续航  uapp.push_user_treaty_endurance_day （续航--用户合约持续0~6个月【大于等于0且小于6】）
     */
    private static final String DISCOUNT_EXPIRY_ENDURANCE_SQL = "select b.phone from uapp.push_user_treaty_endurance_day b join uaide.keeper_user_maintain u on b.phone = u.user_phone and u.status = 1";

    /**
     * 优惠到期搭桥目标用户查询
     * 优惠到期搭桥   uapp.push_user_treaty_crosslink_day （搭桥--用户合约持续6~12个月【大于等于6且小于等于12】）
     */
    private static final String DISCOUNT_EXPIRY_CROSSLINK_SQL = "select b.phone from uapp.push_user_treaty_crosslink_day  b join uaide.keeper_user_maintain u on b.phone = u.user_phone and u.status = 1";

    /**
     * 优惠到期合约到期 用户查询
     * 优惠到期合约到期   uapp.push_user_treaty_expiring_day  （合约到期 -- 合约剩余小于2个月）
     */
    private static final String DISCOUNT_EXPIRY_EXPIRING_SQL = "select b.phone from uapp.push_user_treaty_expiring_day b join uaide.keeper_user_maintain u on b.phone = u.user_phone and u.status = 1";

    /**
     * 2转3
     */
    private static final String TOP_TEN_TWO_TRANSFER_FOUR_SQL = "select t.phone from uapp.lab_2gter_transfer_tag as t inner join uaide.keeper_user_maintain as m on t.phone = m.user_phone where m.maintain_user_id = ";


    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    /**
     * 根据生日时间查出目标客户中当天过生日的用户
     * @param birthDate
     * @return
     */
    public Set<String> queryCustPhoneNumberByBirthDate(String birthDate){
        Set<String> list = new HashSet<String>();
        String sql = BIRTHDAY_QUERY_SQL + "'" + birthDate + "'";
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                list.add(rs.getString("phone"));
            }
        });
        return list;
    }

    /**
     * 根据优惠到期类型查出目标客户中不同类型的用户
     * @param ruleValue
     * @return
     */
    public Set<String> queryCustPhoneNumberByDiscountType(Integer ruleValue) {
        String sql = null;
        switch (ruleValue) {
            //对应keeper.keeper_rule表中的rule_id
            case 6:
                sql = DISCOUNT_EXPIRY_ENDURANCE_SQL;
                break;
            case 7:
                sql = DISCOUNT_EXPIRY_CROSSLINK_SQL;
                break;
            case 8:
                sql = DISCOUNT_EXPIRY_EXPIRING_SQL;
                break;
        }
        Set<String> set = new HashSet<String>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                set.add(rs.getString("phone"));
            }
        });
        return set;
    }

    /**
     * 根据维系员工的id 查询2转4任务对应的维系TOP10用户
     * @param userId
     * @return
     */
    public Set<String> queryTopTenCustomerPhoneOfTwoTransferFour(Integer userId)
    {
        String sql = TOP_TEN_TWO_TRANSFER_FOUR_SQL +  userId + " order by t.transfer_rank desc  limit 10";
        Set<String> topTenPhone = new HashSet<>();
        jdbcTemplate.query(sql, new RowCallbackHandler()
        {
            @Override
            public void processRow(ResultSet rs) throws SQLException
            {
                topTenPhone.add(rs.getString("phone"));
            }
        });
        return topTenPhone;
    }
}
