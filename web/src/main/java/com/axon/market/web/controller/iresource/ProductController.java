package com.axon.market.web.controller.iresource;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.icommon.CategoryTypeEnum;
import com.axon.market.common.domain.icommon.CategoryDomain;
import com.axon.market.common.domain.iresource.ProductDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.icommon.CategoryService;
import com.axon.market.core.service.iresource.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenyu on 2016/9/20.
 */
@Controller("marketProductController")
public class ProductController
{
    @Autowired
    @Qualifier("productService")
    private ProductService productService;

    @Autowired
    @Qualifier("categoryService")
    private CategoryService categoryService;
    /**
     * 列表查询
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "queryMarketProductsByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table queryMarketProductsByPage(@RequestParam Map<String, String> param, HttpSession session)
    {
        return productService.queryProductsByPage(param);
    }

    /**
     * 新增/修改
     * @param productDomain
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditProduct.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrEditProduct(@RequestBody ProductDomain productDomain, HttpSession session)
        {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return productService.addOrEditProduct(productDomain, userDomain);
    }

    /**
     * 删除
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "deleteProduct.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteProduct(@RequestBody Map<String, String> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer id = Integer.parseInt(paras.get("id"));
        return productService.deleteProduct(id, userDomain.getId());
    }

    /**
     * 获取业务类别树节点
     * @return
     */
    @RequestMapping(value = "queryAllCategoryUnderCatalog.view", method = RequestMethod.POST)
    @ResponseBody
    public List<CategoryDomain> queryAllCategoryUnderCatalog()
    {
        return categoryService.queryAllCategory(CategoryTypeEnum.CT_PRODUCT.getValue());
    }
}
