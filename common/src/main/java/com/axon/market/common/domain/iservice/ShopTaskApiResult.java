package com.axon.market.common.domain.iservice;

import java.util.List;

/**
 * Created by zengcr on 2017/7/29.
 */
public class ShopTaskApiResult {
    //返回状态，默认成功
    private int returnValue = 0;
    //返回状态内容，默认成功
    private String returnMsg  = "success";

    private List<ShopTaskApiDomain> shopTasks;

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

    public List<ShopTaskApiDomain> getShopTasks() {
        return shopTasks;
    }

    public void setShopTasks(List<ShopTaskApiDomain> shopTasks) {
        this.shopTasks = shopTasks;
    }
}
