package com.axon.market.core.service.icommon;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.icommon.CategoryDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.dao.mapper.icommon.ICategoryMapper;
import jdk.nashorn.internal.objects.NativeUint16Array;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by chenyu on 2017/1/17.
 */
@Service("categoryService")
public class CategoryService
{
    private static final Logger LOG = Logger.getLogger(CategoryService.class.getName());

    @Autowired
    @Qualifier("categoryDao")
    private ICategoryMapper categoryDao;

    /**
     * 分类管理列表
     * @param param
     * @return
     */
    public Table queryAllCategoriesByPage(Map<String, String> param)
    {
        try
        {
            Integer start = Integer.parseInt(param.get("start"));
            Integer length = Integer.parseInt(param.get("length"));
            Integer pId = StringUtils.isEmpty(param.get("pid")) ? null : Integer.parseInt(param.get("pid"));
            String type = param.get("type");
            String level = param.get("level");
            String name= SearchConditionUtil.optimizeCondition(param.get("name"));
            if((pId==null||pId==0)&&level!=null)
            {
                pId=null;
            }
            Integer count = categoryDao.queryAllCategoryCounts(pId, type, level,name);
            List<CategoryDomain> list = categoryDao.queryAllCategoriesByPage(start, length, pId, type, level,name);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Category Error. ", e);
            return new Table();
        }
    }

    /**
     * 查询所有的目录
     *
     * @return
     */
    public List<CategoryDomain> queryAllCategory(Integer type)
    {
        return categoryDao.queryAllCategory(type);
    }

    /**
     * 新增/修改
     *
     * @param categoryDomain
     * @param userDomain
     * @return
     */
    public Operation addOrEditCategory(CategoryDomain categoryDomain, UserDomain userDomain)
    {
        CategoryDomain category = categoryDao.queryCategoryByCondition(Integer.valueOf(categoryDomain.getType()), categoryDomain.getpId(),categoryDomain.getLevel(), categoryDomain.getName());
        if (null != category && !category.getId().equals(categoryDomain.getId()))
        {
            return new Operation(false, "目录名称【"+categoryDomain.getName()+"】已存在");
        }

        try
        {
            Boolean result;
            String message;
            //新增
            if (categoryDomain.getId() == null || categoryDomain.getId() == 0)
            {
                categoryDomain.setCreateUser(userDomain.getId());
                categoryDomain.setCreateTime(TimeUtil.formatDate(new Date()));
                categoryDomain.setStatus(0);
                result = categoryDao.createCategory(categoryDomain) == 1;
                message = result ? "新增分类成功" : "新增分类失败";
            }
            else
            {
                categoryDomain.setUpdateUser(userDomain.getId());
                categoryDomain.setUpdateTime(TimeUtil.formatDate(new Date()));
                categoryDomain.setStatus(0);
                result = categoryDao.updateCategory(categoryDomain) == 1;
                message = result ? "更新分类成功" : "更新分类失败";
            }
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Add Or Edit Category Error. ", e);
            return new Operation();
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Operation deleteCategory(Integer id, Integer userId)
    {
        try
        {
            Boolean result = categoryDao.deleteCategory(id, userId, TimeUtil.formatDate(new Date())) == 1;
            String message = result ? "删除分类成功" : "删除分类失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Delete Category Error. ", e);
            return new Operation();
        }
    }

    /**
     * 获取栏目级别下拉框
     *
     * @return
     */
    public Operation getSelectLevel()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='-1'>请选择</option>");
        sb.append("<option value='0'>一级栏目</option>");
        sb.append("<option value='1'>二级栏目</option>");
        sb.append("<option value='2'>三级栏目</option>");
        sb.append("<option value='3'>四级栏目</option>");
        sb.append("<option value='4'>五级栏目</option>");
        sb.append("<option value='5'>六级栏目</option>");
        return new Operation(true, sb.toString());
    }

    /**
     * 获取当前选择栏目级别的父级栏目
     *
     * @param level
     * @return
     */
    public Operation getParentLevel(int level, String type)
    {
        List<CategoryDomain> list = categoryDao.getParentLevel(level, type);
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='0'>请选择...</option>");
        for (CategoryDomain category : list)
        {
            if (level == 1)
            {
                sb.append("<option value='").append(category.getId()).append("'>").append(category.getpIdName() + "--" + category.getName()).append("</option>");
            }
            else
            {
                sb.append("<option value='").append(category.getId()).append("'>").append(category.getName()).append("</option>");
            }

        }
        return new Operation(true, sb.toString());
    }

    /**
     * 获取是否启用 下拉列表
     *
     * @return
     */
    public Operation getIsUsed()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<option value='1'>是</option>");
        sb.append("<option value='0'>否</option>");
        return new Operation(true, sb.toString());
    }

    /**
     * 查询子分类
     *
     * @param param
     * @return
     */
    public Integer querySubCategory(Map<String, String> param)
    {
        try
        {
            Integer pId = StringUtils.isEmpty(param.get("pid")) ? null : Integer.parseInt(param.get("pid"));
            String type = param.get("type");
            String level = param.get("level");
            String name=param.get("name");
            Integer count = categoryDao.queryAllCategoryCounts(pId, type, level, name);
            return count;
        }
        catch (Exception e)
        {
            LOG.error("Query Category Error. ", e);
            return 0;
        }
    }
}
