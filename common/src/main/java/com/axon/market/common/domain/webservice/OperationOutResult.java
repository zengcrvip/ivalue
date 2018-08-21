package com.axon.market.common.domain.webservice;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Created by zengcr on 2017/5/31.
 */
public class OperationOutResult implements Serializable
{
    private static final long serialVersionUID = 677484458789332235L;
    private OperationOut operationOut;

    @XmlElement(name = "return")
    public OperationOut getOperationOut()
    {
        return operationOut;
    }

    public void setOperationOut(OperationOut operationOut)
    {
        this.operationOut = operationOut;
    }
}
