package com.axon.market.common.bean;

/**
 * Created by hale on 2016/12/6.
 */
public class Operation
{
    /**
     * 操作结果状态
     */
    private Boolean state;

    /**
     * 操作结果状态信息
     */
    private String message;

    public Operation()
    {
        this.state = false;
        this.message = "操作失败，请联系管理员";
    }

    public Operation(Boolean state, String message)
    {
        this.state = state;
        this.message = message;
    }

    public Boolean getState()
    {
        return state;
    }

    public void setState(Boolean state)
    {
        this.state = state;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
