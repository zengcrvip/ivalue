package com.axon.market.dao.mapper.icommon;

import com.axon.market.common.domain.icommon.AreaDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/1/17.
 */
@Component("areaDao")
public interface IAreaMapper extends IMyBatisMapper
{
    /**
     * 查询区域
     * @return
     */
    List<AreaDomain> queryUserAreas();

    /**
     * 查询用户地区编码
     * @return
     */
    List<AreaDomain>  queryUserAreasCode();

    /**
     * 根据token查询区域
     * @return
     */
    List<Map<String,Object>> queryAreaByToken(@Param(value = "areaCode") Integer areaCode);

    /**
     * 根据token查询营业厅
     * @param businessHallIds
     * @return
     */
    List<Map<String,Object>> queryChannelByToken(@Param(value = "businessHallIds") String businessHallIds);

    /**
     * 根据token查询营业厅
     * @param province, city
     * @return
     */
    Integer queryAreaByProvince(@Param(value = "province") Integer province, @Param(value = "city") Integer city);

}
