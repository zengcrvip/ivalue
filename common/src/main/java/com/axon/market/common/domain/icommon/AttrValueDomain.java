package com.axon.market.common.domain.icommon;

/**
 * Created by Duzm on 2017/8/3.
 */
public class AttrValueDomain {

    private Integer attrValueId;

    private Integer attrId;

    private String displayValue;

    private String innerValue;

    public Integer getAttrValueId() {
        return attrValueId;
    }

    public Integer getAttrId() {
        return attrId;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public String getInnerValue() {
        return innerValue;
    }

    public void setAttrValueId(Integer attrValueId) {
        this.attrValueId = attrValueId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public void setInnerValue(String innerValue) {
        this.innerValue = innerValue;
    }
}
