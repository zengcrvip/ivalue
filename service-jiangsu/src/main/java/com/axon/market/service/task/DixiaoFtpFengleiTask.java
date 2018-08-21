package com.axon.market.service.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.core.service.iconsumption.DixiaoTaskService;
import org.apache.log4j.Logger;

/**
 * Created by Zhuwen on 2017/7/31.
 */
public class DixiaoFtpFengleiTask extends RunJob{
    private static final Logger LOG = Logger.getLogger(DixiaoFtpFengleiTask.class.getName());
    private DixiaoTaskService dixiaoTaskService = DixiaoTaskService.getInstance();

    @Override
    public void runBody() {
        LOG.info("Start the task:dixiao_ftp_fenglei_task");
        dixiaoTaskService.sendToFenglei();
        LOG.info("End the task:dixiao_ftp_fenglei_task");
    }
}
