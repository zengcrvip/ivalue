package com.axon.market.dao.mapper.icommon;

import com.axon.market.common.domain.icommon.CategoryDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chenyu on 2017/1/23.
 */
@Component("categoryDao")
public interface ICategoryMapper extends IMyBatisMapper
{
    /**
     * @param pId
     * @param type
     * @return
     */
    Integer queryAllCategoryCounts(@Param(value = "pId") Integer pId, @Param(value = "type") String type,@Param(value = "level") String level,@Param(value = "name") String name);

    /**
     * @param start
     * @param length
     * @param pId
     * @param type
     * @return
     */
    List<CategoryDomain> queryAllCategoriesByPage(@Param(value = "start") Integer start, @Param(value = "length") Integer length, @Param(value = "pId") Integer pId, @Param(value = "type") String type,@Param(value = "level") String level,@Param(value = "name") String name);

    /**
     * 查询所有目录分类
     * @param type
     * @return
     */
    List<CategoryDomain> queryAllCategory(@Param(value = "type") Integer type);

    /**
     * 在同一父节点下，根据类型，层级和名称查询目录信息
     * @param type
     * @param level
     * @param name
     * @return
     */
    CategoryDomain queryCategoryByCondition(@Param(value = "type") Integer type,@Param(value = "pId") Integer pId,@Param(value = "level") Integer level,@Param(value = "name") String name);

    /**
     * @param categoryDomain
     * @return
     */
    Integer createCategory(@Param(value = "info") CategoryDomain categoryDomain);

    /**
     * @param categoryDomain
     * @return
     */
    Integer updateCategory(@Param(value = "info") CategoryDomain categoryDomain);

    /**
     * @param id
     * @param userId
     * @param time
     * @return
     */
    Integer deleteCategory(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId, @Param(value = "time") String time);

    List<CategoryDomain> getParentLevel(@Param(value = "level") Integer level,@Param(value = "type") String type);
}
