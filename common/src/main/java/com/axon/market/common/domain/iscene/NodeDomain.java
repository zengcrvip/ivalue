package com.axon.market.common.domain.iscene;

/**
 * Created by Administrator on 2016/5/16.
 */
public class NodeDomain
{
    private String pId;

    private String isParent = "false";

    private String open = "false";

    private String checked = "false";//是否为选中,默认不勾选

    private boolean nocheck = false;

    private boolean chkDisabled = false;//是否禁用，默认不禁用

    public String getpId()
    {
        return pId;
    }

    public void setpId(String pId)
    {
        this.pId = pId;
    }

    public String getIsParent()
    {
        return isParent;
    }

    public void setIsParent(String isParent)
    {
        this.isParent = isParent;
    }

    public String getOpen()
    {
        return open;
    }

    public void setOpen(String open)
    {
        this.open = open;
    }

    public String getChecked()
    {
        return checked;
    }

    public void setChecked(String checked)
    {
        this.checked = checked;
    }

    public boolean isNocheck()
    {
        return nocheck;
    }

    public void setNocheck(boolean nocheck)
    {
        this.nocheck = nocheck;
    }

    public boolean isChkDisabled()
    {
        return chkDisabled;
    }

    public void setChkDisabled(boolean chkDisabled)
    {
        this.chkDisabled = chkDisabled;
    }

}
