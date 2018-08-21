package com.axon.market.common.domain.ikeeper;

import java.util.Date;

/**
 * Created by Zhuwen on 2017/8/21.
 */
public class KeepWelfareRecordCusDomain {
    //主键id
    private int id;
    //赠送记录id
    private int recordId;
    //产品编码
    private String productCode;
    //客户号码
    private String phone;
    //订购状态
    private int state;
    //订购结果描述
    private String orderDesc;
    //订购时间
    private Date orderTime;
    //处理状态
    private int smsState;

    //网别
    private String netType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public int getSmsState() {
        return smsState;
    }

    public void setSmsState(int smsState) {
        this.smsState = smsState;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }
}
