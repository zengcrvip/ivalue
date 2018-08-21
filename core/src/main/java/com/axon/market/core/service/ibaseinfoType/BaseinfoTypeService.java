package com.axon.market.core.service.ibaseinfoType;

import com.axon.market.common.domain.ibaseInfoType.baseinfoTypeDomain;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.dao.mapper.ibaseinfoType.IBaseinfoTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;

/**
 * 我的炒店类型表服务类
 * 创建人:邵炜
 * 创建时间:2017年2月20日17:23:18
 * Created by gloomysw on 2017/02/20.
 */
@Component("baseinfoTypeService")
public class BaseinfoTypeService
{
    @Autowired
    @Qualifier("baseinfoTypeDao")
    private IBaseinfoTypeMapper iBaseinfoTypeMapper;

    public static BaseinfoTypeService getInstance()
    {
        return (BaseinfoTypeService) SpringUtil.getSingletonBean("baseinfoTypeService");
    }

    /**
     * 查询炒店类型表所有数据
     * 创建人:邵炜
     * 创建时间:2017年2月20日17:26:00
     * @return 炒店类型表实体类集合
     */
    public List<baseinfoTypeDomain> selectBaseinfoTypeAll()
    {
        return iBaseinfoTypeMapper.selectBaseinfoTypeAll();
    }

    public List<Integer> queryAllShops()
    {
        return iBaseinfoTypeMapper.queryAllShops();
    }
}
