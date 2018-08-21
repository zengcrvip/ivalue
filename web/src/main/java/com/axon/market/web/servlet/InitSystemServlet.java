package com.axon.market.web.servlet;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.monitor.FileAlterationListener;
import com.axon.market.common.monitor.FileAlterationMonitor;
import com.axon.market.common.monitor.FileAlterationObserver;
import com.axon.market.common.timer.FixedDelayTask;
import com.axon.market.common.timer.NoRedoIntervalTask;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.timer.TimerTask;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.task.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import java.text.ParseException;

/**
 * 初始化servlet，加载一些任务监控
 * Created by chenyu on 2016/5/31.
 */
public class InitSystemServlet extends HttpServlet
{
    /**
     * LOG
     */
    private static final Logger LOG = Logger.getLogger(InitSystemServlet.class.getName());

    private SystemConfigBean systemConfigBean = (SystemConfigBean) SpringUtil.getSingletonBean("systemConfigBean");

    /**
     * 线程池创建7个线程跑监控程序
     */
    private Timer timer = new Timer(10);

    @Override
    public void init()
    {
        LOG.info("System Init Start......");

        try
        {
            // 标签刷新任务
            initTagRefreshTask(timer);
            // 启动文件监控
            initFileMonitor();
        }
        catch (Exception e)
        {
            LOG.error("Init Task Error. ", e);
        }

        LOG.info("System Init Finish......");
    }

    /**
     * 定时刷新模型标签
     */
    private  void initTagRefreshTask(Timer timer)
    {
        // 系统配置中读取监控间隔时间
        int intervalMills = 300 * 1000;
        TimerTask tagTask = new FixedDelayTask("monitor_import_tag", null, null, intervalMills, new MonitorTagRefreshTask());
        timer.addTask(tagTask);
    }

    /**
     * 初始化文件监控
     */
    private void initFileMonitor()
    {
        LOG.info("Begin Init File Monitor");
        FileAlterationListener listener = new com.axon.market.core.monitor.FileAlterationListener();
        FileAlterationObserver observer = new FileAlterationObserver(systemConfigBean.getMonitorPath(), listener);
        FileAlterationMonitor monitor = new FileAlterationMonitor(observer);
        monitor.start();
        LOG.info("Start Monitor Path:" + systemConfigBean.getMonitorPath());
    }

}
