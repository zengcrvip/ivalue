package com.axon.market.dao.mapper.iresource;

import com.axon.market.common.domain.iresource.ProductDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chenyu on 2016/9/20.
 */
@Component("productDao")
public interface IProductMapper extends IMyBatisMapper
{
    /**
     * @return
     */
    Integer queryAllProductCounts(@Param(value = "name") String name);

    /**
     * @param limit
     * @param offset
     * @return
     */
    List<ProductDomain> queryProductsByPage(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit,@Param(value = "name") String name);

    /**
     * @param productDomain
     * @return
     */
    Integer createProduct(@Param(value = "info") ProductDomain productDomain);

    /**
     * @param productDomain
     * @return
     */
    Integer updateProduct(@Param(value = "info") ProductDomain productDomain);

    /**
     * @param id
     * @return
     */
    Integer deleteProduct(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId, @Param(value = "time") String time);

    /**
     * 查询所有未删除产品
     * @return
     */
    List<ProductDomain> queryProductsAll();

}
