package com.axon.market.service.servlet;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.service.task.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;

/**
 * 初始化servlet，加载一些任务监控
 * Created by zengcr on 2016/11/11.
 */
public class InitSystemServlet extends HttpServlet
{
    private static final Logger LOG = Logger.getLogger(InitSystemServlet.class.getName());

    private SystemConfigBean systemConfigBean = (SystemConfigBean) SpringUtil.getSingletonBean("systemConfigBean");

    /**
     * 线程池创建20个线程跑监控程序
     */
    private Timer timer = new Timer(30);

    @Override
    public void init()
    {
        LOG.info("System Init Start......");

        try
        {

            // ========================模型定时任务处理 begin ===================================
            ModelTimingTaskDispatch modelTimingTaskDispatch = new ModelTimingTaskDispatch();
            // 标签刷新任务
//            modelTimingTaskDispatch.initTagRefreshTask(timer);
            // 启动客户群刷新监控
            modelTimingTaskDispatch.initMonitorModelRefreshTask(timer);
            // ========================模型定时任务处理 end ===================================


            // ========================炒店定时任务处理 begin ===================================
            ShopTimingTaskDispatch shopTimingDispatch = new ShopTimingTaskDispatch();
            // 炒店任务监控
            shopTimingDispatch.initMonitorShopTask(timer);
            // 炒店用户分类任务
            shopTimingDispatch.initShopUserClassificationTask(timer);
            // 删除数据任务
            shopTimingDispatch.initTruncateDataTask(timer);
            //将地区的号码文件加载的redis缓存
            shopTimingDispatch.initPhoneSegmentToRedis(timer);
            //检查营业厅执行情况
            shopTimingDispatch.initShopTaskExecute(timer);
            //========================炒店定时任务处理 end =======================================


            // ========================精细化定时任务处理 begin ===================================
            JXHTimingTaskDispatch jxhTimingTaskDispatch = new JXHTimingTaskDispatch();
            // 个性化任务用户文件监控
            jxhTimingTaskDispatch.initMonitorRecommendationFileTask(timer);
            // 个性化任务报表任务
            jxhTimingTaskDispatch.initRecommendationReportTask(timer);
            // 将精细化的AZ1102渠道文件对应的渠道解析到炒店渠道中
            jxhTimingTaskDispatch.pushRecommendationChannelToShop(timer);
            // ========================精细化定时任务处理 end ======================================


            // ========================普通自建定时营销任务处理 begin ===================================
            MarketTimingTaskDispatch marketTimingTaskDispatch = new MarketTimingTaskDispatch();
            // 营销任务监控
            marketTimingTaskDispatch.initMonitorMarketTask(timer);
            //每天定时处理普通任务数据
            marketTimingTaskDispatch.initTruncateMarketDataTask(timer);
            //生成营销任务到任务池
            marketTimingTaskDispatch.initCreateMarketingTasksToPool(timer);
            // ========================通自建定时营销任务处理  end ===================================



            // ========================低消任务处理 begin ===================================
            DixiaoTaskDispatch dixiaoTaskDispatch = new DixiaoTaskDispatch();
            //定时备份低消明细数据
            dixiaoTaskDispatch.initDixiaoListBackupTask(timer);
            //低消任务渠道编码和合作编码文件监控
            dixiaoTaskDispatch.initDixiaoCodeImportTask(timer);
            //创建大数据执行规则匹配任务
            dixiaoTaskDispatch.initDixiaoRuleMatchTask(timer);
            //ftp文件给风雷
            dixiaoTaskDispatch.initDixiaoFtpFengleiTask(timer);
            //线下推送话+和更新统计
            dixiaoTaskDispatch.initDixiaoFileFtpOfflineTask(timer);
            //线上推送话+
            dixiaoTaskDispatch.initDixiaoFileFtpOnlineTask(timer);
            // ========================低消任务处理  end ===================================


            //=======================老用户优惠活动专题处理 begin ==========================
            // 定时任务修改任务状态，过期任务置为过期状态
            OldCustomerDispatch oldCustomerDispatch = new OldCustomerDispatch();
            oldCustomerDispatch.initOldCustomerTask(timer);

            // ======================老用户优惠活动专题处理 end ============================


            //=======================掌柜处理 begin ==========================
            KeeperTaskDispatch keeperTaskDispatch = new KeeperTaskDispatch();
            //掌柜生日维系任务定期处理
            keeperTaskDispatch.initBirthdayTask(timer);
            //掌柜优惠到期维系任务定期处理
            keeperTaskDispatch.initDiscountExpiryTask(timer);
            //掌柜2转4维系任务定期处理
            keeperTaskDispatch.initTwoFourSceneTask(timer);
            //掌柜场景关怀维系任务定期处理
            keeperTaskDispatch.initSceneCareTask(timer);
            // 掌柜任务到期下线
            keeperTaskDispatch.initKeeperTaskOverdue(timer);
            // ======================掌柜处理 end ============================



        }
        catch (Exception e)
        {
            LOG.error("Init Task Error. ", e);
        }

        LOG.info("System Init Finish......");
    }

}
