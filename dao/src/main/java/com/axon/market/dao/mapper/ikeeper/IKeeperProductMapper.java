package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.domain.ishopKeeper.ShopKeeperProductDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Duzm on 2017/8/4.
 */
@Component("keeperProductDao")
public interface IKeeperProductMapper extends IMyBatisMapper
{
    List<ShopKeeperProductDomain> queryProduct(@Param("productId") Integer productId, @Param("productName") String productName, @Param("productCode") String productCode, @Param("limit") Integer limit, @Param("offset") Integer offset, @Param("areaId") Integer areaId, @Param("netType") String netType);

    int queryProductCount(@Param("productId") Integer productId, @Param("productName") String productName, @Param("productCode") String productCode, @Param("areaId") Integer areaId, @Param("netType") String netType);

    void addProduct(@Param("shopKeeperProductDomain") ShopKeeperProductDomain shopKeeperProductDomain);

    void updateProduct(@Param("productDomain") ShopKeeperProductDomain shopKeeperProductDomain);

    void deleteProduct(@Param("productId") Integer productId);

    List<ShopKeeperProductDomain> queryProductListByCompositId(@Param("welfareId") Integer welfareId);

    List<ShopKeeperProductDomain> queryProductGroupOfShopKeeper(@Param("typeId") Integer typeId, @Param("productType") Integer productType, @Param("productName") String productName, @Param("areaId") Integer areaId, @Param("netType") String netType);

    int queryProductCountwithoutId(@Param("productId") Integer productId, @Param("productCode") String productCode);
}
