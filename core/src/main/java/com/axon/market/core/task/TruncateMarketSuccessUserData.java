package com.axon.market.core.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * 定期备份营销成功用户
 * Created by zengcr on 2017/6/20.
 */
public class TruncateMarketSuccessUserData extends RunJob
{
    private static final Logger LOG = Logger.getLogger(TruncateMarketTaskData.class.getName());
    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    private static  final String truncate_icloud_user_success = "truncate shop.icloud_user_success";

    @Override
    public void runBody()
    {
        //1、备份数据
        copyIcloudSuccessUser();
        //2、删除目标表智能云成功用户
        operateIcloudSuccessUser();

    }

    //备份数据
    private void copyIcloudSuccessUser()
    {
        Calendar calendar = Calendar.getInstance();
        String time = TimeUtil.formatDateToYMD(calendar.getTime());
        greenPlumOperateService.update(getSql(time));
    }

    //删除目标表智能云用户
    private void operateIcloudSuccessUser()
    {
        try
        {
            LOG.info("Operate icloud success User Sql : " + truncate_icloud_user_success);
            greenPlumOperateService.update(truncate_icloud_user_success);
        }
        catch (Exception e)
        {
            LOG.error("Operate icloud success User Error. ", e);
        }
    }

    private String getSql(String time)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into shop.icloud_user_success_history")
                .append(" select '").append(time).append("',")
                .append("sale_id,")
                .append("sale_boid_id,")
                .append("aim_sub_id,")
                .append("user_id,")
                .append("serial_number,")
                .append("cust_id")
                .append(" from shop.icloud_user_success");
        return sql.toString();
    }
}
