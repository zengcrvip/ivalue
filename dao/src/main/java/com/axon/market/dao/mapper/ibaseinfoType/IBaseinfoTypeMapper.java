package com.axon.market.dao.mapper.ibaseinfoType;

import com.axon.market.common.domain.ibaseInfoType.baseinfoTypeDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取我的炒店类型表数据
 * 创建人:邵炜
 * 创建时间:2017年2月20日17:21:19
 * Created by gloomysw on 2017/02/20.
 */
@Component("baseinfoTypeDao")
public interface IBaseinfoTypeMapper extends IMyBatisMapper
{
    /**
     * 获取所有类型数据集合
     * 创建人:邵炜
     * 创建时间:2017年2月20日17:22:02
     * @return 炒店类型表实体类集合
     */
    List<baseinfoTypeDomain> selectBaseinfoTypeAll();

    List<Integer> queryAllShops();
}
