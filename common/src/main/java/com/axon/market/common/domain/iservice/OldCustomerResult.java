package com.axon.market.common.domain.iservice;

import java.util.List;

/**
 * Created by zengcr on 2017/7/29.
 * 老用户专享返回的优惠活动
 */
public class OldCustomerResult {
    //返回状态，默认成功
    private int returnValue = 0;
    //返回状态内容，默认成功
    private String returnMsg  = "success";
    private List<OldCustomerResultDomain> preferentialTasks;

    public int getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public List<OldCustomerResultDomain> getPreferentialTasks() {
        return preferentialTasks;
    }

    public void setPreferentialTasks(List<OldCustomerResultDomain> preferentialTasks) {
        this.preferentialTasks = preferentialTasks;
    }
}
