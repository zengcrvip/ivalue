package com.axon.market.core.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.core.service.ikeeper.inst.SceneCareTaskService;

/**
 * 掌柜场景关怀维系任务
 * 每天凌晨执行一次，生成keeper_task_inst和keeper_task_inst_detail记录
 * Created by zengcr on 2017/8/22.
 */
public class KeeperSceneCareTask extends RunJob {
    @Override
    public void runBody() {
        SceneCareTaskService.getInstance().run();
    }
}
