package com.axon.market.core.task;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import org.apache.log4j.Logger;
import java.util.Calendar;



/**
 * Created by Zhuwen on 2017/7/27.
 */
public class DixiaoListBackupTask extends RunJob {
    private static final Logger LOG = Logger.getLogger(DixiaoListBackupTask.class.getName());

    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    private SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();

    @Override
    public void runBody()
    {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) !=systemConfigBean.getDixiaoBackDay()){
            LOG.info("Not the day to execute for task:dixiao_list_backup_task, the day should be "+systemConfigBean.getDixiaoBackDay());
            return;
        }
        calendar.add(Calendar.MONTH,-1);
        LOG.info("Start the task:dixiao_list_backup_task");
        StringBuffer backupsql = new StringBuffer();
        backupsql.append("insert into model.dixiao_list_backup");
        backupsql.append(" (sale_id,sale_boid_id,aim_sub_id,user_id,cust_id,phone,name,sex,area_code,sys_name,main_prod_name,main_prod_code,net_time,pin_prod_id,pin_charge_id,pin_level,lll_arpu,ll_arpu,l_arpu,a_arpu,a_volume,a_voice,is_roam,terminal,level_type,mapid,method,isonline,business_hall_code,taskid,monthcode)");
        backupsql.append(" select ");
        backupsql.append(" sale_id,sale_boid_id,aim_sub_id,user_id,cust_id,phone,name,sex,area_code,sys_name,main_prod_name,main_prod_code,net_time,pin_prod_id,pin_charge_id,pin_level,lll_arpu,ll_arpu,l_arpu,a_arpu,a_volume,a_voice,is_roam,terminal,level_type,mapid,method,isonline,business_hall_code,taskid,'");
        backupsql.append(TimeUtil.formatDateToYM(calendar.getTime())).append("'");
        backupsql.append(" from model.dixiao_list");

        try {
            greenPlumOperateService.update(backupsql.toString());
            greenPlumOperateService.truncateTable("model.dixiao_list");
        } catch (Exception e)
        {
            LOG.error("failed to execute the task", e);
        }
        LOG.info("End the task:dixiao_list_backup_task");
    }
}
