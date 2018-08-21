package com.axon.market.core.service.itag;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.icommon.CategoryTypeEnum;
import com.axon.market.common.domain.icommon.CategoryDomain;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.itag.PropertyDomain;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.icommon.CategoryService;
import com.axon.market.dao.mapper.imodel.IModelMapper;
import com.axon.market.dao.mapper.itag.IPropertyMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyang on 2016/3/11.
 */
@Component("propertyService")
public class PropertyService
{
    private static final Logger LOG = Logger.getLogger(PropertyService.class.getName());

    @Autowired
    @Qualifier("propertyDao")
    private IPropertyMapper propertyDao;

    @Autowired
    @Qualifier("categoryService")
    private CategoryService categoryService;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("modelDao")
    private IModelMapper modelDao;

    /**
     * @param offset
     * @param limit
     * @param nameSearch
     * @param tableNameSearch
     * @param columnNameSearch
     * @param userNameSearch
     * @return
     */
    public Table queryPropertiesByPage(Integer offset, Integer limit, String nameSearch, String tableNameSearch, String columnNameSearch, String userNameSearch)
    {
        try
        {
            Integer count = propertyDao.queryAllPropertyCounts(nameSearch, tableNameSearch, columnNameSearch, userNameSearch);
            List<PropertyDomain> list = propertyDao.queryPropertiesByPage(offset, limit, nameSearch, tableNameSearch, columnNameSearch, userNameSearch);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Properties Error. ", e);
            return new Table();
        }
    }

    /**
     * @param id
     * @return
     */
    public PropertyDomain queryPropertyById(Integer id)
    {
        return propertyDao.queryPropertyById(id);
    }

    /**
     * @param infoList
     */
    public Operation createProperties(List<PropertyDomain> infoList)
    {
        try
        {
            // 判断新增属性有无重复名称
            List<String> newNames = new ArrayList<String>();
            if (infoList != null)
            {
                for (PropertyDomain info : infoList)
                {
                    if (!newNames.contains(info.getName()))
                    {
                        newNames.add(info.getName());
                    }
                    else
                    {
                        return new Operation(false, "属性名称不能重复：" + info.getName());
                    }
                }
            }
            // 判断新增属性与原有属性有无重复名称
            List<PropertyDomain> allProperties = propertyDao.queryAllProperties();
            List<String> oldNames = new ArrayList<String>();
            if (allProperties != null)
            {
                for (PropertyDomain property : allProperties)
                {
                    oldNames.add(property.getName());
                }
            }
            newNames.retainAll(oldNames);
            if (newNames.size() != 0)
            {
                return new Operation(false, "以下属性名称已经存在：" + newNames);
            }

            Boolean result = propertyDao.createProperties(infoList) > 0;
            String message = result ? "新增属性成功" : "新增属性失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Create Properties Error. ", e);
            return new Operation();
        }
    }

    /**
     * @param info
     * @return
     */
    public Operation updateProperty(PropertyDomain info)
    {
        try
        {
            Boolean result = propertyDao.updateProperty(info) == 1;
            String message = result ? "更新属性成功" : "更新属性失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Update Property Error. ", e);
            return new Operation();
        }
    }

    /**
     * @param id
     * @param userId
     * @return
     */
    public Operation deleteProperty(Integer id, Integer userId)
    {
        // TODO 增加删除属性引用判断
        try
        {
            Boolean result = propertyDao.deleteProperty(id, userId, TimeUtil.formatDate(new Date())) == 1;
            String message = result ? "删除属性成功" : "删除属性失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Delete Property Error. ", e);
            return new Operation();
        }
    }

    /**
     * 查询所有元数据分类信息
     * @return
     */
    public List<CategoryDomain> queryAllPropertiesUnderCategory()
    {
        List<CategoryDomain> propertyCatalogList = categoryService.queryAllCategory(CategoryTypeEnum.CT_META_DATA.getValue());
        // 查询所有属性
        List<PropertyDomain> properties = propertyDao.queryAllProperties();
        if (CollectionUtils.isNotEmpty(properties))
        {
            for (PropertyDomain propertyDomain : properties)
            {
                CategoryDomain categoryDomain = new CategoryDomain();
                categoryDomain.setId(propertyDomain.getId());
                categoryDomain.setpId(Integer.parseInt(propertyDomain.getCatalogId()));
                categoryDomain.setName(propertyDomain.getName());
                categoryDomain.setIsParent(false);
                categoryDomain.setValueType(propertyDomain.getValueType());
                categoryDomain.setElement(propertyDomain);
                propertyCatalogList.add(categoryDomain);
            }
        }
        return propertyCatalogList;
    }

    /**
     *
     * @return
     */
    public List<CategoryDomain> queryAllPropertiesAndImportModelUnderCategory(UserDomain userDomain)
    {
        List<CategoryDomain> result = new ArrayList<CategoryDomain>();
        List<CategoryDomain> propertyCatalogList = categoryService.queryAllCategory(CategoryTypeEnum.CT_META_DATA.getValue());
        // 查询所有属性
        List<PropertyDomain> properties = propertyDao.queryAllProperties();
        if (CollectionUtils.isNotEmpty(properties))
        {
            for (PropertyDomain propertyDomain : properties)
            {
                CategoryDomain categoryDomain = new CategoryDomain();
                categoryDomain.setId(propertyDomain.getId());
                categoryDomain.setpId(Integer.parseInt(propertyDomain.getCatalogId()));
                categoryDomain.setName(propertyDomain.getName());
                categoryDomain.setIsParent(false);
                categoryDomain.setValueType(propertyDomain.getValueType());
                categoryDomain.setElement(propertyDomain);
                propertyCatalogList.add(categoryDomain);
            }
        }
        result.addAll(propertyCatalogList);

        List<CategoryDomain> modelCategoryList = categoryService.queryAllCategory(CategoryTypeEnum.CT_MODEL.getValue());
        //如果目录不存在，则不需要再进行查询
        if (CollectionUtils.isNotEmpty(modelCategoryList))
        {
            List<ModelDomain> modelList = modelDao.queryAllModelsByUser(userDomain, true);
            for (ModelDomain modelDomain : modelList)
            {
                if (StringUtils.isNotEmpty(modelDomain.getCatalogId()))
                {
                    CategoryDomain categoryDomain = new CategoryDomain();
                    categoryDomain.setId(modelDomain.getId());
                    categoryDomain.setName(modelDomain.getName());
                    categoryDomain.setpId(Integer.valueOf(modelDomain.getCatalogId()));
                    categoryDomain.setIsParent(false);
                    categoryDomain.setElement(modelDomain);
                    categoryDomain.setValueType("model");
                    modelCategoryList.add(categoryDomain);
                }
            }
        }
        result.addAll(modelCategoryList);
        return result;
    }

    /**
     * 根据属性id获取当前属性对应的表数据的刷新时间
     * @param propertyId
     * @return
     */
    public String queryDataRefreshTime(Integer propertyId)
    {
        String sql = "select uaide.conf_market_property_update.ctime as refreshTime from uaide.conf_market_property_update where uaide.conf_market_property_update.id = " +propertyId;
        String refreshTime = "";
        try
        {
            refreshTime = jdbcTemplate.queryForObject(sql,String.class);
            if (StringUtils.isNotEmpty(refreshTime))
            {
                refreshTime  = "上次数据更新时间：" + refreshTime;
            }
            else
            {
                refreshTime = "暂无最新数据";
            }
        }
        catch (Exception e)
        {
            refreshTime = "数据未同步或网络异常";
        }

        return refreshTime;
    }

    /**
     * 根据选中的属性元素ids，查询对应的属性元素
     * @param metaPropertyIds
     * @return
     */
    public List<Map<String, String>> queryMetaPropertiesByIds(String metaPropertyIds)
    {
        return propertyDao.queryMetaPropertiesByIds(metaPropertyIds);
    }
}
