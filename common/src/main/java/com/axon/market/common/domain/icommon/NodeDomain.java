package com.axon.market.common.domain.icommon;

/**
 * Created by yuanfei on 2017/1/17.
 */
public class NodeDomain
{
    private String id;

    private String name;

    private String pId;

    private Boolean isParent = false;

    private Boolean open = false;

    private Boolean checked = false;//是否为选中,默认不勾选

    private Boolean nocheck = false;

    private Boolean chkDisabled = false;//是否禁用，默认不禁用

    private Object element;

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

    public String getpId()
    {
        return pId;
    }

    public void setpId(String pId)
    {
        this.pId = pId;
    }

    public Boolean getIsParent()
    {
        return isParent;
    }

    public void setIsParent(Boolean isParent)
    {
        this.isParent = isParent;
    }

    public Boolean getOpen()
    {
        return open;
    }

    public void setOpen(Boolean open)
    {
        this.open = open;
    }

    public Boolean getChecked()
    {
        return checked;
    }

    public void setChecked(Boolean checked)
    {
        this.checked = checked;
    }

    public Boolean getNocheck()
    {
        return nocheck;
    }

    public void setNocheck(Boolean nocheck)
    {
        this.nocheck = nocheck;
    }

    public Boolean getChkDisabled()
    {
        return chkDisabled;
    }

    public void setChkDisabled(Boolean chkDisabled)
    {
        this.chkDisabled = chkDisabled;
    }

    public Object getElement()
    {
        return element;
    }

    public void setElement(Object element)
    {
        this.element = element;
    }
}
