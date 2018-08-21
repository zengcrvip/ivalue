package com.axon.market.core.service.ikeeper;

import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.ishopKeeper.ShopKeeperProductDomain;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.MD5Util;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.dao.mapper.ikeeper.IKeeperProductMapper;
import com.axon.market.dao.mapper.ikeeper.IKeeperWelfareMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Duzm on 2017/8/4.
 */
@Component("keeperProductService")
public class KeeperProductService
{
    private static final Logger LOG = Logger.getLogger(KeeperProductService.class);
    private static final HttpUtil HTTP_UTIL = HttpUtil.getInstance();

    @Autowired
    @Qualifier("keeperProductDao")
    private IKeeperProductMapper keeperProductDao;

    @Autowired
    @Qualifier("keeperWelfareDao")
    private IKeeperWelfareMapper keeperWelfareDao;

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    /**查询产品数据
     * @param productName
     * @param productCode
     * @return
     */
    public Table<ShopKeeperProductDomain> queryProduct(Integer productId, String productName, String productCode, Integer limit, Integer offset, Integer areaId, String netType)
    {
        try
        {
            int count = keeperProductDao.queryProductCount(productId, productName, productCode, areaId, netType);
            List<ShopKeeperProductDomain> list = keeperProductDao.queryProduct(productId, productName, productCode, limit, offset, areaId, netType);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("查询产品数据异常：", e);
            return new Table<>();
        }
    }

    /**根据productcode查询产品数据条数
     * @param productCode
     * @return
     */
    public int queryProductByCode(String productCode)
    {
        try
        {
            return keeperProductDao.queryProductCount(null, null, productCode,null,null);
        }
        catch (Exception e)
        {
            LOG.error("查询产品数据异常：", e);
            return 0;
        }
    }

    /**根据产品id查询产品数据
     * @param productId
     * @return
     */
    public ShopKeeperProductDomain queryProductById(Integer productId)
    {
        try
        {
            List<ShopKeeperProductDomain> list = keeperProductDao.queryProduct(productId, null, null, null, null, null,null);
            if (list == null || list.isEmpty()){
                return null;
            }
            return list.get(0);
        }
        catch (Exception e)
        {
            LOG.error("查询产品数据异常：", e);
            return null;
        }
    }
    /**新增产品数据
     * @param shopKeeperProductDomain
     * @return
     */
    public ServiceResult addProduct(ShopKeeperProductDomain shopKeeperProductDomain)
    {
        try
        {
            //产品编码不能重复
            if (keeperProductDao.queryProductCountwithoutId(null, shopKeeperProductDomain.getProductCode()) > 0) {
                return new ServiceResult(-1, "该产品编码重复，请重新输入!");
            }

            keeperProductDao.addProduct(shopKeeperProductDomain);
        }
        catch (Exception e)
        {
            return new ServiceResult(-1, "新增产品失败");
        }
        return new ServiceResult(0, "新增产品成功");
    }

    public ServiceResult updateProduct(ShopKeeperProductDomain shopKeeperProductDomain)
    {
        try
        {
            //被其他福利使用
            if (keeperWelfareDao.queryWelfareCountByProductId(String.valueOf(shopKeeperProductDomain.getProductId())) > 0)
            {
                return new ServiceResult(-1, "该产品已被使用，不允许修改");
            }

            //产品编码不能重复
            if (keeperProductDao.queryProductCountwithoutId(shopKeeperProductDomain.getProductId(), shopKeeperProductDomain.getProductCode()) > 0) {
                return new ServiceResult(-1, "该产品编码重复，请重新输入!");
            }

            keeperProductDao.updateProduct(shopKeeperProductDomain);
        }
        catch (Exception e)
        {
            return new ServiceResult(-1, "修改产品失败");
        }
        return new ServiceResult(0, "修改产品成功");
    }

    public ServiceResult deleteProduct(Integer productId)
    {
        try
        {
            //被其他福利使用
            if (keeperWelfareDao.queryWelfareCountByProductId(String.valueOf(productId)) > 0)
            {
                return new ServiceResult(-1, "该产品已被使用，不允许删除");
            }

            keeperProductDao.deleteProduct(productId);
        }
        catch (Exception e)
        {
            return new ServiceResult(-1, "删除产品失败");
        }
        return new ServiceResult(0, "删除产品成功");
    }

    public List<ShopKeeperProductDomain> queryProductListByCompositId(Integer welfareId)
    {
        try
        {
            return keeperProductDao.queryProductListByCompositId(welfareId);
        }
        catch (Exception e)
        {
            LOG.error("查询福利产品数据异常：", e);
            return null;
        }
    }

    /**
     * 获取产品组合
     *
     * @param welfareTypeId
     * @param productTypeId
     * @param productName
     * @return
     * @auther hale
     */
    public List<ShopKeeperProductDomain> queryProductGroupOfShopKeeper(Integer welfareTypeId, Integer productTypeId, String productName, Integer areaId, String netType)
    {
        try
        {
            return keeperProductDao.queryProductGroupOfShopKeeper(welfareTypeId, productTypeId, productName, areaId, netType);
        }
        catch (Exception e)
        {
            LOG.error("查询产品组合失败：", e);
            return null;
        }
    }

    /**
     * 产品校验
     *
     * @param productCode
     * @return
     * @auther hale
     */
    public Boolean checkOnlineProduct(String productCode,String netType)
    {
        try
        {
            final String partnerId = "sk2u9a";
            final String timeStamp = TimeUtil.formatDateToYMDHMS(new Date());
            final String serviceName = "checkonlineproduct";
            final String encryptKey = "d@#hQ9mP";
            Map<String, String> body = new HashMap<String, String>();
            body.put("productId", productCode);
            body.put("netType",netType.toLowerCase());
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("partnerId", partnerId);
            para.put("timeStamp", timeStamp);
            para.put("serviceName",serviceName);
            para.put("encryptStr", MD5Util.getMD5Code(partnerId + timeStamp + serviceName + encryptKey));
            para.put("param", body);
            String jsonStr = JsonUtil.objectToString(para);
            String result = HTTP_UTIL.sendHttpPostByJson(interfaceBean.getSceneOrderUrl(), jsonStr);
            Map<String, Object> map = JsonUtil.stringToObject(result, Map.class);
            if (map.get("ErrorCode") == null)
            {
                LOG.error("failed to call interface:" + interfaceBean.getSceneOrderUrl());
                return false;
            }else{
                if (map.get("ErrorCode").equals("00000"))
                {
                    return true;
                }
                return false;
            }
        }
        catch (Exception e)
        {
            LOG.error("校验产品失败：", e);
            return false;
        }
    }
}
