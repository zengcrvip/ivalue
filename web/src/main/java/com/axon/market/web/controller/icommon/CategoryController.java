package com.axon.market.web.controller.icommon;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.icommon.CategoryDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.icommon.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


/**
 * Created by chenyu on 2017/1/17.
 */
@Controller("categoryController")
public class CategoryController
{
    @Autowired
    @Qualifier("categoryService")
    private CategoryService categoryService;

    /**
     * 查询分类列表
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "queryAllCategoriesByPage.view")
    @ResponseBody
    public Table queryAllCategoriesByPage(@RequestParam Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return categoryService.queryAllCategoriesByPage(param);
    }

    @RequestMapping(value = "queryAllCategory.view")
    @ResponseBody
    public List<CategoryDomain> queryAllCategory(@RequestParam String type, HttpSession session)
    {
        return categoryService.queryAllCategory(Integer.valueOf(type));
    }

    @RequestMapping(value = "queryAllCategoryByBody.view")
    @ResponseBody
    public List<CategoryDomain> queryAllCategoryByBody(@RequestBody Map<String, String> param, HttpSession session)
    {
        String type = param.get("type");
        return categoryService.queryAllCategory(Integer.valueOf(type));
    }

    /**
     * 新增/修改
     *
     * @param categoryDomain
     * @param session
     * @return
     */
    @RequestMapping(value = "addOrEditCategory.view")
    @ResponseBody
    public Operation addOrEditCategory(@RequestBody CategoryDomain categoryDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return categoryService.addOrEditCategory(categoryDomain, userDomain);
    }

    /**
     * 删除
     *
     * @param categoryDomain
     * @param session
     * @return
     */
    @RequestMapping(value = "deleteCategory.view")
    @ResponseBody
    public Operation deleteCategory(@RequestBody CategoryDomain categoryDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return categoryService.deleteCategory(categoryDomain.getId(), userDomain.getId());
    }

    /**
     * 获取栏目级别
     *
     * @return
     */
    @RequestMapping(value = "getSelectLevel.view")
    @ResponseBody
    public Operation getSelectLevel()
    {
        return categoryService.getSelectLevel();
    }

    /**
     * 获取当前选择栏目级别的父级栏目
     *
     * @return
     */
    @RequestMapping(value = "getParentLevel.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getParentLevel()
    {
        int level = -1;
        return categoryService.getParentLevel(level, "");
    }

    /**
     * 获取当前选择栏目级别的父级栏目
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "getParentLevel2.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation getParentLevel2(@RequestBody Map<String, Object> paras)
    {
        String lev = String.valueOf(paras.get("level"));
        String type = String.valueOf(paras.get("type"));
        int level = -1;
        if (!"null".equals(lev))
        {
            level = "".equals(lev) ? 0 : Integer.parseInt(lev);
        }
        return categoryService.getParentLevel(level, type);
    }

    /**
     * 获取是否启用 下拉列表
     *
     * @return
     */
    @RequestMapping(value = "getIsUsed.view")
    @ResponseBody
    public Operation getIsUsed()
    {
        return categoryService.getIsUsed();
    }


    /**
     * 查询子分类
     *
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "querySubCategory.view")
    @ResponseBody
    public Integer querySubCategory(@RequestBody Map<String, String> param, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return categoryService.querySubCategory(param);
    }
}
