package com.axon.market.dao.mapper.itag;

import com.axon.market.common.domain.itag.ResourcesUserDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yuanfei on 2017/8/28.
 */
@Component("resourcesUserDao")
public interface IResourcesUserMapper extends IMyBatisMapper
{
    /**
     * 总数
     * @return
     */
    int queryResourcesUsersCount(@Param(value = "areaId") Integer areaId, @Param(value = "roleIds") String roleIds);

    /**
     * 分页查询信息
     * @param offset
     * @param maxRecord
     * @return
     */
    List<ResourcesUserDomain> queryResourcesUsersByPage(@Param(value = "offset") Integer offset,@Param(value = "limit")  Integer maxRecord, @Param(value = "areaId") Integer areaId, @Param(value = "roleIds") String roleIds);

    /**
     * 根据id查询资源用户信息
     * @param resourceId
     * @return
     */
    ResourcesUserDomain queryResourceUserModelById(@Param(value = "resourceId") Integer resourceId);

    /**
     * 新增
     * @param resourcesUserDomain
     * @return
     */
    int insertResourcesUserModel(ResourcesUserDomain resourcesUserDomain);

    /**
     * 删除资源信息
     * @param resourceId
     * @return
     */
    int deleteResourceModel(Integer resourceId);

    /**
     * 修改
     * @param resourcesUserDomain
     * @return
     */
    int editResourceModel(ResourcesUserDomain resourcesUserDomain);

    /**
     *
     * @param resourceId
     * @param refreshTime
     * @param result
     * @param totalCount
     * @param successCount
     * @param failCount
     * @return
     */
    int updateResourceRefreshInfo(@Param(value = "resourceId") Integer resourceId, @Param(value = "refreshTime") String refreshTime, @Param(value = "result") String result,@Param(value = "totalCount") Integer totalCount, @Param(value = "successCount") Integer successCount, @Param(value = "failCount") Integer failCount,@Param(value = "columns") String columns);

    /**
     * 获取表的字段信息
     * @param resourceId
     * @return
     */
    String queryTableColumnsById(@Param(value = "resourceId") Integer resourceId);

}
