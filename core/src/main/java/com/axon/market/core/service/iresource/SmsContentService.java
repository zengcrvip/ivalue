package com.axon.market.core.service.iresource;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.icommon.CategoryTypeEnum;
import com.axon.market.common.domain.icommon.CategoryDomain;
import com.axon.market.common.domain.iresource.ProductDomain;
import com.axon.market.common.domain.iresource.SmsContentDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.dao.mapper.icommon.ICategoryMapper;
import com.axon.market.dao.mapper.iresource.IProductMapper;
import com.axon.market.dao.mapper.iresource.ISmsContentMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenyu on 2017/1/17.
 */
@Component("smsContentService")
public class SmsContentService
{
    private static final Logger LOG = Logger.getLogger(SmsContentService.class.getName());

    @Autowired
    @Qualifier("smsContentDao")
    private ISmsContentMapper smsContentDao;

    @Autowired
    @Qualifier("categoryDao")
    private ICategoryMapper categoryDao;

    @Autowired
    @Qualifier("productDao")
    private IProductMapper productDao;

    /**
     * @param param
     * @return
     */
    public Table querySmsContentsByPage(Map<String, String> param)
    {
        try
        {
            String searchContent = SearchConditionUtil.optimizeCondition(param.get("searchContent"));
            String key = SearchConditionUtil.optimizeCondition(param.get("key"));
            String mob = SearchConditionUtil.optimizeCondition(param.get("mob"));
            Integer offset = Integer.parseInt(param.get("start"));
            Integer limit = Integer.parseInt(param.get("length"));
            Integer count = smsContentDao.querySmsContentCounts(searchContent, key, mob);
            List<SmsContentDomain> list = smsContentDao.querySmsContentsByPage(searchContent, key, mob, offset, limit);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Sms Contents Error. ", e);
            return new Table();
        }
    }

    /**
     * @return
     * @see ISmsContentMapper#querySmsContentById(int)
     */
    public SmsContentDomain querySmsContentById(int contentId)
    {
        return smsContentDao.querySmsContentById(contentId);
    }

    /**
     * 新增/修改
     *
     * @param smsContent
     * @return
     */
    public Operation addOrEditSmsContent(SmsContentDomain smsContent, UserDomain userDomain)
    {
        try
        {
            String message;
            Boolean result;
            //新增
            if (smsContent.getId() == null || smsContent.getId() <= 0)
            {
                smsContent.setCreateUser(userDomain.getId());
                smsContent.setCreateUserName(userDomain.getName());
                smsContent.setCreateTime(TimeUtil.formatDate(new Date()));
                result = smsContentDao.insertSmsContent(smsContent) == 1;
                message = result ? "短信营销话术创建成功" : "短信营销话术创建失败";
            }
            else
            {
                smsContent.setUpdateUser(userDomain.getId());
                smsContent.setUpdateTime(TimeUtil.formatDate(new Date()));
                result = smsContentDao.updateSmsContent(smsContent) == 1;
                message = result ? "短信营销话术更新成功" : "短信营销话术更新失败";
            }

            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("addOrEditSmsContent Error. ", e);
            return new Operation();
        }
    }

    /**
     * 删除
     *
     * @param contentId
     * @return
     */
    public Operation deleteSmsContentById(Integer contentId, Integer userId)
    {
        try
        {
            Boolean result = smsContentDao.deleteSmsContent(contentId, userId) == 1;
            String message = result ? "删除短信内容成功" : "删除短信内容失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Delete Sms Contents Error. ", e);
            return new Operation();
        }
    }

    /**
     * 目录查询可见的模型
     *
     * @return
     */
    public List<CategoryDomain> queryAllProductUnderCatalog()
    {
        try
        {
            List<CategoryDomain> categoryList = categoryDao.queryAllCategory(CategoryTypeEnum.CT_PRODUCT.getValue());
            //如果目录不存在，则不需要再进行查询
            if (CollectionUtils.isNotEmpty(categoryList))
            {
                for (CategoryDomain categoryDomain : categoryList)
                {
                    categoryDomain.setIsParent(true);
                }

                List<ProductDomain> modelList = productDao.queryProductsAll();
                for (ProductDomain productDomain : modelList)
                {
                    CategoryDomain categoryDomain = new CategoryDomain();
                    categoryDomain.setId(productDomain.getId());
                    categoryDomain.setName(productDomain.getName());
                    categoryDomain.setpId(Integer.valueOf(productDomain.getCatalogId()));
                    categoryDomain.setIsParent(false);
                    categoryList.add(categoryDomain);
                }
            }

            return categoryList;
        }
        catch (Exception ex)
        {
            LOG.error("queryAllProductUnderCatalog ", ex);
            return null;
        }
    }

    public Table queryContentByBusinessType(Map<String, String> param)
    {
        try
        {
            String searchContent = SearchConditionUtil.optimizeCondition(String.valueOf(param.get("searchContent")));
            String key = SearchConditionUtil.optimizeCondition(String.valueOf(param.get("key")));
            Integer businessType = Integer.parseInt(param.get("businessType"));
            Integer offset = Integer.parseInt(param.get("start"));
            Integer limit = Integer.parseInt(param.get("length"));
            Integer count = smsContentDao.queryContentByBusinessTypeCount(businessType);
            List<SmsContentDomain> list = smsContentDao.queryContentByBusinessType(businessType, searchContent, key, offset, limit);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Sms Contents Error. ", e);
            return new Table();
        }
    }
}
