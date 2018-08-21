package com.axon.market.common.domain.webservice;

import java.io.Serializable;

/**
 * Created by yuanfei on 2017/5/25.
 */
public class Product implements Serializable
{
    private static final long serialVersionUID = 677484458789332877L;

    private String id;

    private String name;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
