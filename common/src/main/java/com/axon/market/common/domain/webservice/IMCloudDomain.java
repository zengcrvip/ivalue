package com.axon.market.common.domain.webservice;

import java.io.Serializable;

/**
 * Created by zengcr on 2017/5/31.
 */
public class IMCloudDomain implements Serializable
{
    private static final long serialVersionUID = 677484458789332877L;
    private OperationIn operationIn;

    public OperationIn getOperationIn()
    {
        return operationIn;
    }

    public void setOperationIn(OperationIn operationIn)
    {
        this.operationIn = operationIn;
    }
}
