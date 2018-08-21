package com.axon.market.core.service.icommon;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.domain.icommon.NodeDomain;
import com.axon.market.common.domain.icommon.OrgDomain;
import com.axon.market.common.domain.icommon.OrgTypeDomain;
import com.axon.market.dao.mapper.icommon.IOrgMapper;
import com.axon.market.dao.mapper.ikeeper.IKeeperUserMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hale on 2017年8月24日11:22:48.
 */
@Component("orgService")
public class OrgService
{
    private static final Logger LOG = Logger.getLogger(OrgService.class);

    @Autowired
    @Qualifier("orgDao")
    private IOrgMapper orgDao;

    @Autowired
    @Qualifier("keeperUserDao")
    private IKeeperUserMapper keeperUserDao;

    /**
     * 查询部门、业务组织
     *
     * @param orgName   组织名称
     * @param orgTypeId 组织类型 1:部门组织 2:业务组织
     * @return
     */
    public List<OrgDomain> queryRootOrgByOrgType(String orgName, Integer orgTypeId)
    {
        return orgDao.queryRootOrgByOrgType(orgName, orgTypeId);
    }

    /**
     * 根据组织ID 查询组织
     *
     * @param orgId
     * @return
     */
    public List<OrgDomain> queryOrgListByOrgId(Integer orgId)
    {
        return orgDao.queryOrgListByOrgId(orgId);
    }

    /**
     * 查询业务组织
     *
     * @return
     */
    public List<NodeDomain> queryBusinessOrg()
    {
        List<OrgDomain> areaList = orgDao.queryRootOrgByOrgType("", 2);
        List<NodeDomain> nodeList = new ArrayList<NodeDomain>();
        for (OrgDomain orgDomain : areaList)
        {
            NodeDomain nodeDomain = new NodeDomain();
            nodeDomain.setId(String.valueOf(orgDomain.getOrgId()));
            nodeDomain.setName(orgDomain.getOrgName());
            nodeDomain.setpId(String.valueOf(orgDomain.getParentId()));
            nodeDomain.setElement(orgDomain);
            nodeList.add(nodeDomain);
        }
        return nodeList;
    }

    /**
     * 新增组织
     *
     * @param orgDomain
     * @return
     */
    public Operation addOrg(OrgDomain orgDomain)
    {
        // 校验orgTypeId 非空
        if (null == orgDomain.getOrgTypeId())
        {
            return new Operation(false, "组织类型为空");
        }
        // 校验校验orgName 非空
        if (null == orgDomain.getOrgName() || "".equals(orgDomain.getOrgName()))
        {
            return new Operation(false, "组织名称为空");
        }

        try
        {
            OrgDomain oldOrgDomain = orgDao.queryOrgByOrgName(orgDomain.getOrgName(), orgDomain.getParentId());
            if (null != oldOrgDomain)
            {
                return new Operation(false, "已经存在同名的组织了");
            }

            Boolean result = orgDao.addOrg(orgDomain) == 1;
            String message = result ? "新增组织成功" : "新增组织失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("addOrg  Error. ", e);
            return new Operation(false, "新增组织失败");
        }
    }

    /**
     * 修改组织
     *
     * @param orgDomain
     * @return
     */
    public Operation updateOrg(OrgDomain orgDomain)
    {
        // 校验orgTypeId 非空
        if (null == orgDomain.getOrgTypeId())
        {
            return new Operation(false, "组织类型为空");
        }
        // 校验orgName 非空
        if (null == orgDomain.getOrgName() || "".equals(orgDomain.getOrgName()))
        {
            return new Operation(false, "组织名称为空");
        }

        try
        {
            OrgDomain oldOrgDomain = orgDao.queryOrgByOrgName(orgDomain.getOrgName(), orgDomain.getParentId());
            if (null != oldOrgDomain)
            {
                return new Operation(false, "已经存在同名的组织了");
            }

            Boolean result = orgDao.updateOrg(orgDomain) == 1;
            String message = result ? "修改组织成功" : "修改组织失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("updateOrg Error. ", e);
            return new Operation(true, "修改组织失败");
        }
    }

    /**
     * 删除组织
     *
     * @param orgId
     * @param orgTypeId
     * @return
     */
    public Operation deleteOrg(Integer orgId, Integer orgTypeId)
    {
        try
        {
            List<OrgDomain> childrenList = orgDao.queryOrgListByParentId(orgId);
            if (childrenList.size() > 0)
            {
                return new Operation(false, "该组织下面有子组织，无法删除");
            }

            Integer userCount = getOrgUserCount(orgId, orgTypeId);
            if (userCount > 0)
            {
                return new Operation(false, "该组织下面已有" + userCount + "个人员，无法删除");
            }

            Boolean result = orgDao.deleteOrg(orgId) == 1;
            String message = result ? "删除组织成功" : "删除组织失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("deleteOrg Error. ", e);
            return new Operation(true, "删除组织失败");
        }
    }

    /**
     * 获取组织下人员数量
     *
     * @param orgId     组织Id
     * @param orgTypeId 组织类型 1：部门组织 2：业务组织
     * @return
     */
    private int getOrgUserCount(Integer orgId, Integer orgTypeId)
    {
        if (orgTypeId == 1) // 部门组织
        {
            return keeperUserDao.querySystemOrgByOrgId(orgId);
        }
        else if (orgTypeId == 2)    // 业务组织
        {
            return keeperUserDao.queryBusinessOrgByOrgId(orgId);
        }
        else
        {
            return 0;
        }
    }
}
