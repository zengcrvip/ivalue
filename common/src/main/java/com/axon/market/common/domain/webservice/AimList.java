package com.axon.market.common.domain.webservice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuanfei on 2017/5/25.
 */
public class AimList implements Serializable
{
    private static final long serialVersionUID = 677484458789332877L;

    private List<AimSub> aimSub;

    public List<AimSub> getAimSub()
    {
        return aimSub;
    }

    public void setAimSub(List<AimSub> aimSub)
    {
        this.aimSub = aimSub;
    }
}
