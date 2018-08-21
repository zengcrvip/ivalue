package com.axon.market.web.controller.icommon;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.domain.icommon.NodeDomain;
import com.axon.market.common.domain.icommon.OrgDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.core.service.icommon.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@Controller
public class OrgController
{
    @Autowired
    @Qualifier("orgService")
    private OrgService orgService;

    /**
     * 查询部门、业务组织
     *
     * @param paras
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "queryRootOrgByOrgType.view", method = RequestMethod.POST)
    @ResponseBody
    public List<OrgDomain> queryRootOrgByOrgType(@RequestBody Map<String, Object> paras) throws UnsupportedEncodingException
    {
        String orgName = SearchConditionUtil.optimizeCondition(URLDecoder.decode(String.valueOf(paras.get("orgName")), "UTF-8")).trim();
        Integer orgTypeId = Integer.parseInt(String.valueOf(paras.get("orgTypeId")));
        return orgService.queryRootOrgByOrgType(orgName, orgTypeId);
    }

    /**
     * 查询组织详情
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryOrgListByOrgId.view", method = RequestMethod.POST)
    @ResponseBody
    public List<OrgDomain> queryOrgListByOrgId(@RequestBody Map<String, Object> paras)
    {
        Integer orgId = Integer.parseInt(String.valueOf(paras.get("orgId")));
        return orgService.queryOrgListByOrgId(orgId);
    }

    /**
     * 新增组织
     *
     * @param orgDomain
     * @return
     */
    @RequestMapping(value = "addOrg.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation addOrg(@RequestBody OrgDomain orgDomain)
    {
        return orgService.addOrg(orgDomain);
    }

    /**
     * 修改组织
     *
     * @param orgDomain
     * @return
     */
    @RequestMapping(value = "updateOrg.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation updateOrg(@RequestBody OrgDomain orgDomain)
    {
        return orgService.updateOrg(orgDomain);
    }

    /**
     * 删除组织
     *
     * @param paras
     * @return
     */
    @RequestMapping(value = "deleteOrg.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteOrg(@RequestBody Map<String, Object> paras)
    {
        Integer orgId = Integer.parseInt(String.valueOf(paras.get("orgId")));
        Integer orgTypeId = Integer.parseInt(String.valueOf(paras.get("orgTypeId")));
        return orgService.deleteOrg(orgId, orgTypeId);
    }

    /**
     * 查询 业务组织
     *
     * @return
     */
    @RequestMapping(value = "queryBusinessOrg.view", method = RequestMethod.POST)
    @ResponseBody
    public List<NodeDomain> queryBusinessOrg()
    {
        return orgService.queryBusinessOrg();
    }
}
