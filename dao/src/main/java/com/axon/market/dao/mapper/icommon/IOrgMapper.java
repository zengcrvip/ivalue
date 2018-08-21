package com.axon.market.dao.mapper.icommon;

import com.axon.market.common.domain.icommon.OrgDomain;
import com.axon.market.common.domain.icommon.OrgTypeDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hale on 2017/8/1.
 */
@Component("orgDao")
public interface IOrgMapper extends IMyBatisMapper
{
    /**
     * 查询部门、业务组织
     *
     * @param orgName   组织名称
     * @param orgTypeId 组织类型 1:部门组织 2:业务组织
     * @return
     */
    List<OrgDomain> queryRootOrgByOrgType(@Param(value = "orgName") String orgName, @Param(value = "orgTypeId") Integer orgTypeId);

    /**
     * 根据parentId 查询组织
     *
     * @param parentId
     * @return
     */
    List<OrgDomain> queryOrgListByParentId(@Param(value = "parentId") Integer parentId);

    /**
     * 根据组织Id 查询组织
     *
     * @param orgId
     * @return
     */
    List<OrgDomain> queryOrgListByOrgId(@Param(value = "orgId") Integer orgId);

    /**
     * 查询是否存在同名组织
     *
     * @param orgName
     * @param parentId
     * @return
     */
    OrgDomain queryOrgByOrgName(@Param(value = "orgName") String orgName, @Param(value = "parentId") Integer parentId);

    /**
     * 新增 组织
     *
     * @param orgDomain
     * @return
     */
    Integer addOrg(@Param(value = "orgDomain") OrgDomain orgDomain);

    /**
     * 更新 组织
     *
     * @param orgDomain
     * @return
     */
    Integer updateOrg(@Param(value = "orgDomain") OrgDomain orgDomain);

    /**
     * 删除组织
     *
     * @param orgId
     * @return
     */
    Integer deleteOrg(@Param(value = "orgId") Integer orgId);
}
