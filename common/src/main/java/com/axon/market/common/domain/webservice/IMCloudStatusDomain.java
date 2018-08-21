package com.axon.market.common.domain.webservice;

import java.io.Serializable;

/**
 * Created by zengcr on 2017/5/31.
 */
public class IMCloudStatusDomain implements Serializable
{
    private static final long serialVersionUID = 677484458789332877L;
    private OperationStatus arg0;

    public OperationStatus getArg0()
    {
        return arg0;
    }

    public void setArg0(OperationStatus arg0)
    {
        this.arg0 = arg0;
    }
}
