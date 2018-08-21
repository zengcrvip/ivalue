package com.axon.market.core.task;

import com.axon.market.common.timer.RunJob;
import com.axon.market.core.service.ikeeper.inst.TwoFourSceneTaskService;

/**
 * 2转4维系任务
 * 每天凌晨执行一次，生成keeper_task_inst和keeper_task_inst_detail记录
 * Created by zengcr on 2017/8/22.
 */
public class KeeperTwoFourSceneTask extends RunJob{

    @Override
    public void runBody() {
        TwoFourSceneTaskService.getInstance().run();
    }
}
