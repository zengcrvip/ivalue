package com.axon.market.common.bean;

import com.axon.market.common.util.SpringUtil;

/**
 * 系统个性化变量
 * Created by zengcr on 2017/8/22.
 */
public class SystemPersonConfig {
    public static SystemPersonConfig getInstance()
    {
        return (SystemPersonConfig) SpringUtil.getSingletonBean("systemPersonConfig");
    }

    //掌柜任务实例开关
    private boolean keeperSwitch;

    public boolean isKeeperSwitch() {
        return keeperSwitch;
    }

    public void setKeeperSwitch(boolean keeperSwitch) {
        this.keeperSwitch = keeperSwitch;
    }
}
