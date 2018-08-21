package com.axon.market.common.domain.iconfArea;

/**
 * 城市实体
 * 创建人:邵炜
 * 创建时间:2017年2月20日16:19:06
 * Created by gloomysw on 2017/02/20.
 */
public class confAreaDomain
{
    // 主键
    private long id;

    // 地区名称
    private String name;

    // 编码
    private long code;

    // 父级ID
    private long parentId;

    // 状态 -1 删除 1未删除
    private int status;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
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

    public long getCode()
    {
        return code;
    }

    public void setCode(long code)
    {
        this.code = code;
    }

    public long getParentId()
    {
        return parentId;
    }

    public void setParentId(long parentId)
    {
        this.parentId = parentId;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
