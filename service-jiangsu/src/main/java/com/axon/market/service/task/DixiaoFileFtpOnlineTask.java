package com.axon.market.service.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.core.service.iconsumption.DixiaoTaskService;
import org.apache.log4j.Logger;

/**
 * Created by Zhuwen on 2017/8/1.
 */
public class DixiaoFileFtpOnlineTask extends RunJob{
    private static final Logger LOG = Logger.getLogger(DixiaoFileFtpOnlineTask.class.getName());
    private DixiaoTaskService dixiaoTaskService = DixiaoTaskService.getInstance();

    @Override
    public void runBody() {
        LOG.info("Start the task:dixiao_file_ftp_online_task");
        dixiaoTaskService.ftpAndUpdateOnline();
        LOG.info("End the task:dixiao_file_ftp_online_task");
    }

}
