package com.axon.market.common.domain.iservice;

/**
 * Created by zengcr on 2017/9/4.
 */
public class ShopTaskResult {
    //返回状态，默认成功
    private int returnValue = 0;
    //返回状态内容，默认成功
    private String returnMsg  = "success";

    private ShopTaskApiDomain shopTask;

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

    public ShopTaskApiDomain getShopTask() {
        return shopTask;
    }

    public void setShopTask(ShopTaskApiDomain shopTask) {
        this.shopTask = shopTask;
    }
}
