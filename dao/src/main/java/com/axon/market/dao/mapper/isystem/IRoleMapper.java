package com.axon.market.dao.mapper.isystem;

import com.axon.market.common.domain.isystem.RoleDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/5.
 */
@Component("roleDao")
public interface IRoleMapper extends IMyBatisMapper
{
    /**
     * 根据条件查询所有角色的数目
     *
     * @param condition
     * @return
     */
    int queryRolesCount(@Param(value = "condition") Map<String, Object> condition);

    /**
     * 查询角色树形展现
     *
     * @return
     */
    List<Map<String, Object>> queryAllRole();

    /**
     * 根据条件查询角色列表
     *
     * @param offset
     * @param maxRecord
     * @param sortColumn
     * @param condition
     * @return
     */
    List<RoleDomain> queryRolesByPage(@Param(value = "offset") long offset, @Param(value = "maxRecord") long maxRecord, @Param(value = "sortColumn") String sortColumn, @Param(value = "condition") Map<String, Object> condition);

    /**
     * 查询所有的菜单权限
     * @return
     */
    List<Map<String, Object>> queryAllMenuPermissions();
    /**
     * 查询所有的数据权限
     *
     * @return
     */
    List<Map<String, Object>> queryAllDataPermissions();

    /**
     * 查询用户的权限
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> queryUserDataPermissionList(@Param(value = "userId") Integer userId);

    /**
     * 插入角色
     *
     * @param roleDomain
     * @return
     */
    int insertRole(@Param(value = "role") RoleDomain roleDomain);

    /**
     * 更新角色信息
     *
     * @param roleDomain
     * @return
     */
    int updateRole(@Param(value = "role") RoleDomain roleDomain);

    /**
     * 根据id删除角色
     *
     * @param id
     * @return
     */
    int deleteRole(@Param(value = "id") Integer id);

    /**
     * 根据角色id查询占用该角色的用户名称
     * @param id
     * @return
     */
    List<String> queryUserNameUnderRoleByRoleId(@Param(value = "id") Integer id);
}
