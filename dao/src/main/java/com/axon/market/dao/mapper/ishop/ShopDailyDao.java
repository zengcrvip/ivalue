package com.axon.market.dao.mapper.ishop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/28.
 */
@Component("shopDailyDao")
public class ShopDailyDao
{
    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * @param dateTime
     * @param locationType
     * @return
     */
    public Map<String, Object> getTotalData(String dateTime, Integer locationType)
    {
        String locationTypeSql = "";
        if (locationType != -1)
        {
            locationTypeSql += " and r.base_type_id=" + locationType + "";
        }

        String sql = "SELECT COALESCE(sum(c. STATUS),0) AS baseNameTotal,COALESCE(sum(c. STATUS),0) AS statusTotal,COALESCE(sum(c.provinceTaskNum),0) AS provinceTaskNumTotal,COALESCE(sum(c.cityTaskNum),0) AS cityTaskNumTotal,COALESCE(sum(c.baseTaskNum),0) AS baseTaskNumTotal,COALESCE(sum(c.executeTaskNum),0) AS executeTaskNumTotal,CASE WHEN (sum(c.provinceTaskNum) + sum(c.cityTaskNum) + sum(c.baseTaskNum)) <> 0 THEN round(sum(c.executeTaskNum) * 100 / (sum(c.provinceTaskNum) + sum(c.cityTaskNum) + sum(c.baseTaskNum)),2) || '%' ELSE 0.00 || '%' END AS executeTaskTotalRate,COALESCE(max(c.cityTargetUser),0) AS cityTargetUserTotal,COALESCE(sum(c.appointUser),0) AS appointUserTotal,COALESCE(sum(c.cityResidentUser),0) AS cityResidentUserTotal,COALESCE(sum(c.baseCoverUser),0) AS baseCoverUserTotal,CASE WHEN (sum(c.baseCoverUser)) <> 0 THEN round(sum(c.cityResidentUser) * 100 / sum(c.baseCoverUser),2) || '%' ELSE 0.00 || '%' END AS residentTotalRate,CASE WHEN (sum(c.sendUserNum)) <> 0 THEN round(sum(c.recvSuccUserNum) * 100 / sum(c.sendUserNum),2) || '%' ELSE 0.00 || '%' END AS smsTotalRate FROM (SELECT r.base_type_desc AS baseTypeName,r.base_name AS baseName,r. STATUS AS STATUS,r.province_task_num AS provinceTaskNum,r.city_task_num AS cityTaskNum,r.base_task_num AS baseTaskNum,r.execute_task_num AS executeTaskNum,CASE WHEN (r.province_task_num + r.city_task_num + r.base_task_num) <> 0 THEN round(r.execute_task_num * 100 / (r.province_task_num + r.city_task_num + r.base_task_num),2) || '%' ELSE 0.00 || '%' END AS executeTaskRate,r.business_type_num AS businessTypeNum,r.city_target_user AS cityTargetUser,r.appoint_user AS appointUser,r.city_resident_user AS cityResidentUser,r.base_cover_user AS baseCoverUser,CASE WHEN r.base_cover_user <> 0 THEN round(r.city_resident_user * 100 / r.base_cover_user,2) || '%' ELSE 0.00 || '%' END AS residentRate,CASE WHEN r.send_user <> 0 THEN round(r.recv_succ_user * 100 / r.send_user,2) || '%' ELSE 0.00 || '%' END AS smsRate,r.send_user AS sendUserNum,r.recv_succ_user AS recvSuccUserNum FROM umid.rpt_base_push_result_day AS r WHERE r.timest = " + dateTime + " " + locationTypeSql + " ORDER BY r.base_type_id desc) AS c";

        return jdbcTemplate.queryForMap(sql);
    }
}
