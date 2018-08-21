package com.axon.market.dao.mapper.itag;

import com.axon.market.common.domain.itag.PropertyDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yangyang on 2016/1/26.
 */
@Component("propertyDao")
public interface IPropertyMapper extends IMyBatisMapper
{
    /**
     * @return
     */
    List<PropertyDomain> queryAllProperties();

    /**
     * @param nameSearch
     * @param tableNameSearch
     * @param columnNameSearch
     * @param userNameSearch
     * @return
     */
    Integer queryAllPropertyCounts(@Param(value = "nameSearch") String nameSearch, @Param(value = "tableNameSearch") String tableNameSearch, @Param(value = "columnNameSearch") String columnNameSearch, @Param(value = "userNameSearch") String userNameSearch);

    /**
     * @param offset
     * @param limit
     * @param nameSearch
     * @param tableNameSearch
     * @param columnNameSearch
     * @param userNameSearch
     * @return
     */
    List<PropertyDomain> queryPropertiesByPage(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit, @Param(value = "nameSearch") String nameSearch, @Param(value = "tableNameSearch") String tableNameSearch, @Param(value = "columnNameSearch") String columnNameSearch, @Param(value = "userNameSearch") String userNameSearch);

    /**
     * @param id
     * @return
     */
    PropertyDomain queryPropertyById(@Param(value = "id") Integer id);

    /**
     * @param infoList
     * @return
     */
    Integer createProperties(List<PropertyDomain> infoList);

    /**
     * @param info
     * @return <br>
     * 注意：该方法不能设置某字段为空！！！
     */
    Integer updateProperty(@Param(value = "info") PropertyDomain info);

    /**
     * @param id
     * @param userId
     * @param time
     * @return
     */
    Integer deleteProperty(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId, @Param(value = "time") String time);

    /**
     * 根据选中的属性元素ids，查询对应的属性元素
     * @param metaPropertyIds
     * @return
     */
    List<Map<String, String>> queryMetaPropertiesByIds(@Param(value = "metaPropertyIds") String metaPropertyIds);

}
