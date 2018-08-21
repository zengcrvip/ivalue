package com.axon.market.common.domain.webservice;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * Created by zengcr on 2017/5/31.
 */
public class SessionStateChageOutResult implements Serializable
{
    private static final long serialVersionUID = 677484458789332235L;
    private SessionStateChageOut sessionStateChageOut;

    @XmlElement(name = "return")
    public SessionStateChageOut getSessionStateChageOut()
    {
        return sessionStateChageOut;
    }

    public void setSessionStateChageOut(SessionStateChageOut sessionStateChageOut)
    {
        this.sessionStateChageOut = sessionStateChageOut;
    }
}
