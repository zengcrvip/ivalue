package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.TempleTypeDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuan on 2017/1/4.
 */
@Component("templeTypeDao")
public interface ITempleTypeMapper extends IMyBatisMapper
{
    /**
     * 查询模型列表
     *
     * @param name   模型名称
     * @param offset
     * @param limit
     * @return 模型列表
     */
    List<TempleTypeDomain> queryTempleType(@Param(value = "name") String name, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    /**
     * 查询模型总数
     *
     * @param name 模型名称
     * @return 模型数量
     */
    Integer queryTempleTypeCount(@Param(value = "name") String name);

    /**
     * 新增模型
     *
     * @param info 模型model
     * @return
     */
    Integer addTempleType(@Param(value = "info") TempleTypeDomain info);

    /**
     * 更新模型
     *
     * @param info 模型model
     * @return
     */
    Integer editTempleType(@Param(value = "info") TempleTypeDomain info);

    /**
     * 删除模型
     *
     * @param id 模型id
     * @return
     */
    Integer deleteTempleType(@Param(value = "id") Integer id);
}
