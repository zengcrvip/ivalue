package com.axon.market.common.domain.iscene;

/**
 * Created by DELL on 2016/12/26.
 */
public class UrlDomain
{
    private Integer id;
    private String url;
    private Integer urlGroupId;
    private String name;
    private Integer isDelete;

    public UrlDomain()
    {
    }

    public UrlDomain(Integer id, String url, Integer urlGroupId, String name, Integer isDelete)
    {
        this.id = id;
        this.url = url;
        this.urlGroupId = urlGroupId;
        this.name = name;
        this.isDelete = isDelete;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Integer getUrlGroupId()
    {
        return urlGroupId;
    }

    public void setUrlGroupId(Integer urlGroupId)
    {
        this.urlGroupId = urlGroupId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }
}
