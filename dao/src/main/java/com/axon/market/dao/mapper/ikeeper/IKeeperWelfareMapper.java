package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ishopKeeper.ShopKeeperWelfareDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhuwen on 2017/8/21.
 */
@Component("keeperWelfareDao")
public interface IKeeperWelfareMapper extends IMyBatisMapper {
    List<ShopKeeperWelfareDomain> queryWelfare(@Param("welfareName") String welfareName, @Param("welfareId") Integer welfareId, @Param("typeId") Integer typeId,@Param("limit") Integer limit, @Param("offset") Integer offset, @Param("areaId") Integer areaId, @Param("netType") String netType);

    int queryWelfareCount(@Param("welfareName") String welfareName, @Param("welfareId") Integer welfareId, @Param("typeId") Integer typeId, @Param("areaId") Integer areaId, @Param("netType") String netType);

    void addWelfare(ShopKeeperWelfareDomain shopKeeperWelfareDomain);

    void updateWelfare(@Param("welfareDomain") ShopKeeperWelfareDomain shopKeeperWelfareDomain);

    void deleteWelfare(@Param("welfareId") Integer welfareId);

    int queryWelfareCountByProductId(@Param("productId") String productId);

    List<ShopKeeperWelfareDomain> queryWelfareForApp();
}
