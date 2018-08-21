package com.axon.market.web.controller.ikeeper;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.ishopKeeper.ShopKeeperProductDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.ikeeper.KeeperProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by Duzm on 2017/8/4.
 */
@Controller("shopKeeperProductController")
public class ShopKeeperProductController
{
    @Autowired
    @Qualifier("keeperProductService")
    private KeeperProductService keeperProductService;

    /**
     * 各种条件查询产品列表信息
     *
     * @param paras <br/>
     * @return
     */
    @RequestMapping(value = "queryProductOfShopKeeper.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<ShopKeeperProductDomain> queryProductOfShopKeeper(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer productId = paras.get("productId") == null ? null : Integer.parseInt(String.valueOf(paras.get("productId")));
        String productName = SearchConditionUtil.optimizeCondition((String) (paras.get("productName"))).trim();
        String productCode = SearchConditionUtil.optimizeCondition((String) (paras.get("productCode"))).trim();
        Integer limit = Integer.valueOf(String.valueOf(paras.get("length")));
        Integer offset = Integer.valueOf(String.valueOf(paras.get("start")));
        Integer areaId = userDomain.getAreaId();
        String netType = paras.get("netType") == null ? null : (String) paras.get("netType");

        return keeperProductService.queryProduct(productId, productName, productCode, limit, offset, areaId, netType);
    }

    /**
     * 各种条件查询产品列表信息
     *
     * @param paras <br/>
     * @return
     */
    @RequestMapping(value = "checkProductCodeUnique.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult checkProductCodeUnique(@RequestBody Map<String, Object> paras)
    {
        String productCode = (String) (paras.get("productCode"));
        if (keeperProductService.queryProductByCode(productCode) > 0)
        {
            return new ServiceResult(-1, "该产品编码重复，请重新输入!");
        }
        else
        {
            return new ServiceResult();
        }
    }

    /**
     * 各种条件查询产品列表信息
     *
     * @param paras <br/>
     * @return
     */
    @RequestMapping(value = "queryProductById.view", method = RequestMethod.POST)
    @ResponseBody
    public ShopKeeperProductDomain queryProductById(@RequestBody Map<String, Object> paras)
    {
        Integer productId = Integer.parseInt(String.valueOf(paras.get("productId")));

        return keeperProductService.queryProductById(productId);
    }

    /**
     * 新增产品信息
     *
     * @param shopKeeperProductDomain
     * @return
     */
    @RequestMapping(value = "addProductOfShopKeeper.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult addProductOfShopKeeper(@RequestBody ShopKeeperProductDomain shopKeeperProductDomain)
    {
        return keeperProductService.addProduct(shopKeeperProductDomain);
    }

    /**
     * 编辑产品信息
     *
     * @param shopKeeperProductDomain
     * @return
     */
    @RequestMapping(value = "updateProductOfShopKeeper.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateProductOfShopKeeper(@RequestBody ShopKeeperProductDomain shopKeeperProductDomain)
    {
        return keeperProductService.updateProduct(shopKeeperProductDomain);
    }

    /**
     * 删除产品信息
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "deleteProductOfShopKeeper.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteProductOfShopKeeper(@RequestBody Map<String, Object> paras)
    {
        Integer productId = Integer.parseInt(String.valueOf(paras.get("productId")));
        return keeperProductService.deleteProduct(productId);
    }

    /**
     * 根据福利id查询该福利使用到的产品列表
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryProductListByCompositId.view", method = RequestMethod.POST)
    @ResponseBody
    public List<ShopKeeperProductDomain> queryProductListByCompositId(@RequestBody Map<String, Object> paras)
    {
        Integer welfareId = Integer.parseInt(String.valueOf(paras.get("welfareId")));
        return keeperProductService.queryProductListByCompositId(welfareId);
    }

    /**
     * 根据福利类型和产品类型获取产品列表
     *
     * @param paras
     * @return
     * @auther hale
     */
    @RequestMapping(value = "queryProductGroupOfShopKeeper.view", method = RequestMethod.POST)
    @ResponseBody
    public List<ShopKeeperProductDomain> queryProductGroupOfShopKeeper(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        Integer welfareTypeId = Integer.parseInt(String.valueOf(paras.get("welfareTypeId")));
        Integer productTypeId = StringUtils.isNotEmpty(String.valueOf(paras.get("productTypeId"))) ? Integer.parseInt(String.valueOf(paras.get("productTypeId"))) : null;
        String productName = String.valueOf(paras.get("productName"));
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer areaId = userDomain.getAreaId();
        String netType = paras.get("netType") == null ? null : (String) paras.get("netType");

        return keeperProductService.queryProductGroupOfShopKeeper(welfareTypeId, productTypeId, productName, areaId, netType);
    }

    /**
     * 根据产品code去校验产品是否可用
     *
     * @param paras
     * @return
     * @auther hale
     */
    @RequestMapping(value = "checkOnlineProduct.view", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkOnlineProduct(@RequestBody Map<String, Object> paras)
    {
        String productCode = String.valueOf(paras.get("productCode"));
        String netType = String.valueOf(paras.get("netType"));
        return keeperProductService.checkOnlineProduct(productCode, netType);
    }
}
