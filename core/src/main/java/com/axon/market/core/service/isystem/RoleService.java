package com.axon.market.core.service.isystem;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.domain.isystem.RoleDomain;
import com.axon.market.dao.mapper.isystem.IRoleMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/5.
 */
@Component("roleService")
public class RoleService
{
    @Autowired
    @Qualifier("roleDao")
    private IRoleMapper roleDao;

    /**
     * @see IRoleMapper#queryRolesCount(Map)
     */
    public int queryRolesCount(Map<String, Object> condition)
    {
        return roleDao.queryRolesCount(condition);
    }

    /**
     * @return
     * @see IRoleMapper#queryAllRole()
     */
    public List<Map<String, Object>> queryAllRole()
    {
        return roleDao.queryAllRole();
    }

    /**
     * @see IRoleMapper#queryRolesByPage(long, long, String, Map)
     */
    public List<RoleDomain> queryRolesByPage(long offset, long maxRecord, String sortColumn, Map<String, Object> condition)
    {
        return roleDao.queryRolesByPage(offset, maxRecord, sortColumn, condition);
    }

    /**
     * 查询权限
     */
    public List<Map<String, Object>> queryAllPermissions(String type)
    {
        List<Map<String, Object>> permissions = new ArrayList<Map<String, Object>>();
        //如果是DATA类型就查询数据权限，否则查询菜单权限
        if ("DATA".equals(type))
        {
            permissions = roleDao.queryAllDataPermissions();
        }
        else
        {
            permissions = roleDao.queryAllMenuPermissions();
        }

        return permissions;
    }

    /**
     * @return
     * @see IRoleMapper#insertRole(RoleDomain)
     */
    public ServiceResult insertRole(RoleDomain role)
    {
        ServiceResult result = new ServiceResult();
        if (1 != roleDao.insertRole(role))
        {
            result.setRetValue(-1);
            result.setDesc("数据库新增操作失败！");
        }
        return result;
    }

    /**
     * @return
     * @see IRoleMapper#updateRole(RoleDomain)
     */
    public ServiceResult updateRole(RoleDomain role)
    {
        ServiceResult result = new ServiceResult();
        if (1 != roleDao.updateRole(role))
        {
            result.setRetValue(-1);
            result.setDesc("数据库更新操作失败！");
        }
        return result;
    }

    /**
     * @return
     * @see IRoleMapper#deleteRole(Integer)
     */
    public ServiceResult deleteRole(Integer id)
    {
        ServiceResult result = new ServiceResult();

        // 检查该角色是否被用户占用，被用户使用的角色无法删除
        List<String> userNames = roleDao.queryUserNameUnderRoleByRoleId(id);

        if (CollectionUtils.isNotEmpty(userNames))
        {
            result.setRetValue(-1);
            result.setDesc("该角色被用户 "+ StringUtils.join(userNames,",")+"占用，无法删除！");
            return result;
        }

        if (1 != roleDao.deleteRole(id))
        {
            result.setRetValue(-1);
            result.setDesc("数据库删除操作失败！");
        }
        return result;
    }
}
