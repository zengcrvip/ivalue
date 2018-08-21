package com.axon.market.core.task;

import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.shoptask.impl.DixiaoPartnerTaskFileExecutor;
import com.axon.market.core.shoptask.impl.DixiaoQudaoTaskFileExecutor;

/**
 * Created by Administrator on 2017/7/27.
 */
public class GetDixiaoFileFactory {
    public GetDixiaoFileExecutor executor(String type) {
        switch (type) {
            case "dixiao_qudao": {
                return (DixiaoQudaoTaskFileExecutor) SpringUtil.getSingletonBean("dixiaoQudaoTaskFileExecutor");
            }
            case "dixiao_partner": {
                return (DixiaoPartnerTaskFileExecutor) SpringUtil.getSingletonBean("dixiaoPartnerTaskFileExecutor");
            }
            default: {
                return null;
            }
        }
    }
}