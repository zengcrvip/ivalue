package com.axon.market.core.task;

import com.axon.market.common.bean.SqlBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.greenplum.OperateFileDataToGreenPlum;
import com.axon.market.core.service.ibaseinfoType.BaseinfoTypeService;
import com.axon.market.core.service.ishop.ShopTaskService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.List;

/**
 * Created by chenyu on 2017/3/12.
 */
public class TruncateShopTaskData extends RunJob
{
    private static final Logger LOG = Logger.getLogger(TruncateShopTaskData.class.getName());

    private BaseinfoTypeService baseinfoTypeService = BaseinfoTypeService.getInstance();

    private GreenPlumOperateService greenPlumOperateService = GreenPlumOperateService.getInstance();

    private ShopTaskService shopTaskService = ShopTaskService.getInstance();

    private OperateFileDataToGreenPlum operateFileDataToGreenPlum = OperateFileDataToGreenPlum.getInstance();

    private SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();

    private SqlBean sqlBean = SqlBean.getInstance();

    @Override
    public void runBody()
    {
        operateShopRecommendationTask();
        //备份当前用户，删除已发送用户
        operateShopUser();
        //每天更新个性化任务给实时监控的炒店
        operateShopBase();
        //每天清除实时监控返回的用户
        operateShopTarget();
        //备份删除炒店精细化任务
        operateShopRecommendation();
    }

    private void operateShopRecommendationTask()
    {
        try
        {
            LOG.info("Operate Shop Recommendation Task. ");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            int result = shopTaskService.updateRecommendationTaskStatus(TimeUtil.formatDateToYMD(calendar.getTime()));
            LOG.info("Update Shop Recommendation Task. Count : " + result);
        }
        catch (Exception e)
        {
            LOG.error("Operate Shop Recommendation Task Error. ", e);
        }
    }

    private void operateShopUser()
    {
        try
        {
            copyUserToUserHistory();
            LOG.info("Operate Shop User Sql : " + sqlBean.getTruncateShopUserSql());
            greenPlumOperateService.update(sqlBean.getTruncateShopUserSql());
        }
        catch (Exception e)
        {
            LOG.error("Operate Shop User Error. ", e);
        }
    }

    private void operateShopBase()
    {
        try
        {
            LOG.info("Operate Truncate Shop Base Sql : " + sqlBean.getTruncateShopBaseSql());
            greenPlumOperateService.update(sqlBean.getTruncateShopBaseSql());

            List<Integer> list = baseinfoTypeService.queryAllShops();
            if (CollectionUtils.isNotEmpty(list))
            {
                StringBuffer sql = new StringBuffer(sqlBean.getInsertShopBaseSql());
                sql.append(" ");
                for (Integer baseId : list)
                {
                    sql.append("(").append(baseId).append("),");
                }
                LOG.info("Operate Insert Shop Base Sql : " + sql.toString().substring(0, sql.length() - 1));
                greenPlumOperateService.update(sql.toString().substring(0, sql.length() - 1));
            }
        }
        catch (Exception e)
        {
            LOG.error("Operate Shop Base Error. ", e);
        }
    }

    private void operateShopTarget()
    {
        try
        {
            LOG.info("Operate Shop Target Sql : " + sqlBean.getTruncateShopTargetSql());
            greenPlumOperateService.update(sqlBean.getTruncateShopTargetSql());
        }
        catch (Exception e)
        {
            LOG.error("Operate Shop Target Error. ", e);
        }
    }

    private void operateShopRecommendation()
    {
        try
        {
            LOG.info("Operate Back Up Shop Recommendation Sql : " + sqlBean.getBackUpShopRecommendationExecuteSql());
            greenPlumOperateService.update(sqlBean.getBackUpShopRecommendationExecuteSql());

            LOG.info("Operate Shop Recommendation Sql : " + sqlBean.getTruncateShopRecommendationExecuteSql());
            greenPlumOperateService.update(sqlBean.getTruncateShopRecommendationExecuteSql());
        }
        catch (Exception e)
        {
            LOG.error("Operate Shop Recommendation Error. ", e);
        }
    }

    private void copyUserToUserHistory()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String time = TimeUtil.formatDateToYMD(calendar.getTime());

        greenPlumOperateService.update(getSql(time));
    }

    private String getSql(String time)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into shop.shop_user_history")
                .append(" select '").append(time).append("',")
                .append("sale_id,")
                .append("sale_boid_id,")
                .append("aim_sub_id,")
                .append("user_id,")
                .append("serial_number,")
                .append("cust_id")
                .append(" from shop.shop_user");
        return sql.toString();
    }
}
