package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ikeeper.KeeperActivityListDomain;
import com.axon.market.common.domain.ishop.ShopListDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by zengcr on 2017/4/21.
 */
@Component("keeperListDao")
public interface IKeeperListMapper extends IMyBatisMapper
{
    int fetchActivityInfo(@Param(value = "dataList") List<Map<String,Object>> dataList);
}
