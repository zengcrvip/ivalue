package com.axon.market.service.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.core.service.iconsumption.DixiaoTaskService;
import org.apache.log4j.Logger;

/**
 * Created by Zhuwen on 2017/8/1.
 */
public class DixiaoFileFtpOfflineTask extends RunJob{
    private static final Logger LOG = Logger.getLogger(DixiaoFileFtpOfflineTask.class.getName());
    private DixiaoTaskService dixiaoTaskService = DixiaoTaskService.getInstance();

    @Override
    public void runBody() {
        LOG.info("Start the task:dixiao_file_ftp_offline_task");
        dixiaoTaskService.ftpAndUpdateOffline();
        LOG.info("End the task:dixiao_file_ftp_offline_task");
    }
}
