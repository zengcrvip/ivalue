package com.axon.market.service.task;
import com.axon.market.common.constant.iconsumption.DixiaoTaskStatusEnum;
import com.axon.market.common.timer.RunJob;
import com.axon.market.core.service.iconsumption.DixiaoTaskService;
import org.apache.log4j.Logger;

/**
 * Created by Zhuwen on 2017/7/31.
 */
public class DixiaoRuleMatchTask extends RunJob{
    private static final Logger LOG = Logger.getLogger(DixiaoRuleMatchTask.class.getName());

    private DixiaoTaskService dixiaoTaskService = DixiaoTaskService.getInstance();


    @Override
    public void runBody() {
        LOG.info("Start the task:dixiao_rule_match_task");
        dixiaoTaskService.insertTaskStatusGP(DixiaoTaskStatusEnum.TASK_RANKTYPE_CHOOSE.getValue());
        dixiaoTaskService.insertTaskStatusGP(DixiaoTaskStatusEnum.TASK_CITY_CHOOSE.getValue());
        dixiaoTaskService.insertTaskStatusGP(DixiaoTaskStatusEnum.TASK_CITY_CONFIRM.getValue());
        LOG.info("End the task:dixiao_rule_match_task");
    }
}
