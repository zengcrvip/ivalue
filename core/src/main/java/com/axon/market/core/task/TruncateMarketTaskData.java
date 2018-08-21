package com.axon.market.core.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * 每天定时处理炒店相关任务数据
 * Created by zengcr on 2017/6/14.
 */
public class TruncateMarketTaskData  extends RunJob
{
    private static final Logger LOG = Logger.getLogger(TruncateMarketTaskData.class.getName());
    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    private static  final String truncate_icloud_user = "truncate shop.icloud_user";

    @Override
    public void runBody()
    {
        LOG.info("TruncateMarketTaskData begin");
        //1、备份数据
        copyIcloudUser();
        //2、删除目标表智能云用户
        operateIcloudUser();
        LOG.info("TruncateMarketTaskData end");

    }


    //备份数据
    private void copyIcloudUser()
    {
        Calendar calendar = Calendar.getInstance();
        String time = TimeUtil.formatDateToYMD(calendar.getTime());
        greenPlumOperateService.update(getSql(time));
    }

    //删除目标表智能云用户
    private void operateIcloudUser()
    {
        try
        {
            LOG.info("Operate icloud User Sql : " + truncate_icloud_user);
            greenPlumOperateService.update(truncate_icloud_user);
        }
        catch (Exception e)
        {
            LOG.error("Operate icloud User Error. ", e);
        }
    }

    private String getSql(String time)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into shop.icloud_user_history")
                .append(" select '").append(time).append("',")
                .append("sale_id,")
                .append("sale_boid_id,")
                .append("aim_sub_id,")
                .append("user_id,")
                .append("serial_number,")
                .append("cust_id")
                .append(" from shop.icloud_user");
        return sql.toString();
    }
}
