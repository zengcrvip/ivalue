package com.axon.market.dao.mapper.itag;

import com.axon.market.common.domain.icommon.IdAndNameDomain;
import com.axon.market.common.domain.itag.DimensionDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yangyang on 2016/1/27.
 */
@Component("dimensionDao")
public interface IDimensionMapper extends IMyBatisMapper
{
    /**
     * @return
     */
    Integer queryAllDimensionCounts();

    /**
     * @param offset
     * @param limit
     * @return
     */
    List<DimensionDomain> queryDimensionsByPage(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    /**
     *
     * @return
     */
    List<DimensionDomain> queryAllDimensions();

    /**
     * @param dimensionDomain
     * @return
     */
    Integer createDimension(@Param(value = "info") DimensionDomain dimensionDomain);

    /**
     * @param dimensionDomain
     * @return
     */
    Integer updateDimension(@Param(value = "info") DimensionDomain dimensionDomain);

    /**
     * @param id
     * @return
     */
    Integer deleteDimension(@Param(value = "id") Integer id);

    /**
     * @return
     */
    List<IdAndNameDomain> queryAllDimensionIdAndNames();

    /**
     * @return
     */
    String queryDimensionValueById(@Param(value = "id") Integer id);
}

