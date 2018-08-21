package com.axon.market.common.bean;

import com.axon.market.common.util.SpringUtil;

/**
 * Created by yuanfei on 2017/9/5.
 */
public class VerificationCodeBean
{
    /**
     * 万能图片验证码: 4位
     */
    private String code;

    /**
     * 是否使用万能验证码 默认false
     */
    private Boolean openUniversal;

    public static VerificationCodeBean getInstance()
    {
        return (VerificationCodeBean) SpringUtil.getSingletonBean("verificationCodeBean");
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Boolean getOpenUniversal()
    {
        return openUniversal;
    }

    public void setOpenUniversal(Boolean openUniversal)
    {
        this.openUniversal = openUniversal;
    }
}
