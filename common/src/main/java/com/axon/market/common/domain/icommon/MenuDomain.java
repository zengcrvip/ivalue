package com.axon.market.common.domain.icommon;

import java.util.ArrayList;
import java.util.List;

public class MenuDomain
{
	private Integer id;

	private String permission;
	
	private String title;
	
	private String icon;
	
	private String url;
	
	private String className;
	
	private String idName;
	
	private int sortNo;
	
	private Integer parentId;

	private String location;

	private int level;
	
	private List<MenuDomain> childrenMenu = new ArrayList<MenuDomain>();

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getIdName()
	{
		return idName;
	}

	public void setIdName(String idName)
	{
		this.idName = idName;
	}

	public int getSortNo()
	{
		return sortNo;
	}

	public void setSortNo(int sortNo)
	{
		this.sortNo = sortNo;
	}

	public Integer getParentId()
	{
		return parentId;
	}

	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}

	public List<MenuDomain> getChildrenMenu()
	{
		return childrenMenu;
	}

	public void setChildrenMenu(List<MenuDomain> childrenMenu)
	{
		this.childrenMenu = childrenMenu;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}
}
