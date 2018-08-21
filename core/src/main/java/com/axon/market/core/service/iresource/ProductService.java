package com.axon.market.core.service.iresource;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.iresource.ProductDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.dao.mapper.iresource.IProductMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenyu on 2016/9/20.
 */
@Component("productService")
public class ProductService
{
    private static final Logger LOG = Logger.getLogger(ProductService.class.getName());

    @Autowired
    @Qualifier("productDao")
    private IProductMapper productDao;

    /**
     * 列表查询
     * @param param
     * @return
     */
    public Table queryProductsByPage(Map<String, String> param)
    {
        try
        {
            Integer start = Integer.parseInt(param.get("start"));
            Integer length = Integer.parseInt(param.get("length"));
            String name = SearchConditionUtil.optimizeCondition(param.get("name"));
            Integer count = productDao.queryAllProductCounts(name);
            List<ProductDomain> list = productDao.queryProductsByPage(start, length,name);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Products Error. ", e);
            return new Table();
        }
    }

    /**
     * 新增/修改
     *
     * @param productDomain
     * @return
     */
    public Operation addOrEditProduct(ProductDomain productDomain, UserDomain userDomain)
    {
        try
        {
            Boolean result;
            String message;
            //新增
            if (productDomain.getId() == null || productDomain.getId() <= 0)
            {
                productDomain.setCreateUser(userDomain.getId());
                productDomain.setCreateUserName(userDomain.getName());
                productDomain.setCreateTime(TimeUtil.formatDate(new Date()));
                result = productDao.createProduct(productDomain) == 1;
                message = result ? "新增产品成功" : "新增产品失败";
            }
            else
            {
                productDomain.setUpdateUser(userDomain.getId());
                productDomain.setUpdateTime(TimeUtil.formatDate(new Date()));
                result = productDao.updateProduct(productDomain) == 1;
                message = result ? "更新产品成功" : "更新产品失败";
            }
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("addOrEditProduct Error. ", e);
            return new Operation();
        }
    }


    /**
     * 删除
     *
     * @param id
     * @param userId
     * @return
     */
    public Operation deleteProduct(Integer id, Integer userId)
    {
        try
        {
            Boolean result = productDao.deleteProduct(id, userId, TimeUtil.formatDate(new Date())) == 1;
            String message = result ? "删除产品成功" : "删除产品失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Delete Product Error. ", e);
            return new Operation();
        }
    }

    /**
     * 列表查询
     *
     * @return
     */
    public List<ProductDomain> queryProductsAll()
    {
        try
        {
            List<ProductDomain> list = productDao.queryProductsAll();
            return list;
        }
        catch (Exception e)
        {
            LOG.error("queryProductsAll Error. ", e);
            return null;
        }
    }
}
