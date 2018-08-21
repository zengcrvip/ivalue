package com.axon.market.dao.mapper.iresource;

import com.axon.market.common.domain.iresource.ProductDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import com.axon.market.common.domain.iresource.SmsContentDomain;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
@Component("smsContentDao")
public interface ISmsContentMapper extends IMyBatisMapper
{
    /**
     * @param searchContent
     * @param key
     * @param mob
     * @return
     */
    Integer querySmsContentCounts(@Param(value = "searchContent") String searchContent, @Param(value = "key") String key, @Param(value = "mob") String mob);

    /**
     * @param searchContent
     * @param key
     * @param mob
     * @param offset
     * @param limit
     * @return
     */
    List<SmsContentDomain> querySmsContentsByPage(@Param(value = "searchContent") String searchContent, @Param(value = "key") String key, @Param(value = "mob") String mob, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    /**
     * 根据id查询短信内容
     *
     * @param contentId
     * @return
     */
    SmsContentDomain querySmsContentById(@Param(value = "contentId") int contentId);

    /**
     * @param smsContent
     * @return
     */
    Integer insertSmsContent(@Param(value = "smsContent") SmsContentDomain smsContent);

    /**
     * @param smsContent
     * @return
     */
    Integer updateSmsContent(@Param(value = "smsContent") SmsContentDomain smsContent);

    /**
     * @param contentId
     * @return
     */
    Integer deleteSmsContent(@Param(value = "contentId") Integer contentId, @Param(value = "userId") Integer userId);

    /**
     * 查询营销产品
     *
     * @return
     */
    List<ProductDomain> queryAllProductUnderCatalog();

    List<SmsContentDomain> queryContentByBusinessType(@Param(value = "businessType") Integer businessType, @Param(value = "searchContent") String searchContent, @Param(value = "key") String key, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    Integer queryContentByBusinessTypeCount(Integer businessType);
}
