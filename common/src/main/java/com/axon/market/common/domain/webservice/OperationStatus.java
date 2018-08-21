package com.axon.market.common.domain.webservice;

import java.io.Serializable;

/**
 * Created by zengcr on 2017/5/27.
 */
public class OperationStatus implements Serializable
{
    private static final long serialVersionUID = 677484458789332235L;

    private String saleBoidId;

    private String status;

    public String getSaleBoidId()
    {
        return saleBoidId;
    }

    public void setSaleBoidId(String saleBoidId)
    {
        this.saleBoidId = saleBoidId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
