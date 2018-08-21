package com.axon.market.common.domain.iscene;

/**
 * 场景规则客户端对象实体
 * Created by zengcr on 2016/11/10.
 */
public class ScenceSmsClientDomain
{
    //主键ＩＤ
    private Integer id;
    //客户端名称
    private String name;
    //二级分类id
    private Integer typeid2;
    //客户端二级分类
    private String type2;
    //一级分类id
    private Integer typeid1;
    //客户端一级分类
    private String type1;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public String getType2()
    {
        return type2;
    }

    public void setType2(String type2)
    {
        this.type2 = type2;
    }

    public String getType1()
    {
        return type1;
    }

    public void setType1(String type1)
    {
        this.type1 = type1;
    }

    public Integer getTypeid2() {
        return typeid2;
    }

    public Integer getTypeid1() {
        return typeid1;
    }

    public void setTypeid2(Integer typeid2) {
        this.typeid2 = typeid2;
    }

    public void setTypeid1(Integer typeid1) {
        this.typeid1 = typeid1;
    }
}
