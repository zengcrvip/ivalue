package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.AdditionalConditionDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by DELL on 2016/12/6.
 */
@Component("additionalDao")
public interface IAdditionalConditionMapper extends IMyBatisMapper
{

    /**
     * 导航指标查询
     *
     * @return
     */
    List<AdditionalConditionDomain> queryAdditionalCondition(@Param("name") String name, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * 新增导航指标
     * @param additionalCondition
     * @return
     */
    int addAdditionalCondition(@Param("domain") AdditionalConditionDomain additionalCondition);

    /**
     * 修改导航指标
     * @param additionalCondition
     * @return
     */
    int editAdditionalCondition(@Param("domain") AdditionalConditionDomain additionalCondition);

    /**
     * 删除导航指标
     * @param id
     * @param userId
     * @param userName
     * @return
     */
    int deleteAdditionCondition(@Param("id") Integer id, @Param("name") String userName, @Param("uid") Integer userId);
}
