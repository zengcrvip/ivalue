package com.axon.market.core.service.icommon;

import com.axon.market.common.domain.icommon.AreaDomain;
import com.axon.market.common.domain.icommon.NodeDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.dao.mapper.icommon.IAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/1/17.
 */
@Component("areaService")
public class AreaService
{
    @Autowired
    @Qualifier("areaDao")
    private IAreaMapper areaDao;

    public static AreaService getInstance()
    {
        return (AreaService) SpringUtil.getSingletonBean("areaService");
    }

    /**
     * @see IAreaMapper#queryUserAreas()
     * @return
     */
    public List<NodeDomain> queryUserAreas()
    {
        List<AreaDomain> areaList = areaDao.queryUserAreas();
        List<NodeDomain> nodeList = new ArrayList<NodeDomain>();
        for (AreaDomain areaDomain : areaList)
        {
            NodeDomain nodeDomain = new NodeDomain();
            nodeDomain.setId(String.valueOf(areaDomain.getId()));
            nodeDomain.setName(areaDomain.getName());
            nodeDomain.setpId(String.valueOf(areaDomain.getParentId()));
            nodeDomain.setElement(areaDomain);
//            nodeDomain.setIsParent(true);
            nodeList.add(nodeDomain);
        }
        return nodeList;
    }

    /**
     * @see IAreaMapper#queryUserAreas()
     * @return
     */
    public List<AreaDomain> queryAllAreas()
    {
        return areaDao.queryUserAreas();
    }

    /**
     * 查询用用户地区编码
     * @return
     */
    public List<NodeDomain> queryUserAreasCode()
    {
        List<AreaDomain> areaList = areaDao.queryUserAreasCode();
        List<NodeDomain> nodeList = new ArrayList<NodeDomain>();
        for (AreaDomain areaDomain : areaList)
        {
            NodeDomain nodeDomain = new NodeDomain();
            nodeDomain.setId(String.valueOf(areaDomain.getCode()));
            nodeDomain.setName(areaDomain.getName());
            nodeDomain.setpId(String.valueOf(areaDomain.getParentId()));
            nodeDomain.setIsParent(true);
            nodeList.add(nodeDomain);
        }
        return nodeList;
    }

    /**
     * 根据用户的token查询区域
     * @param userDomain
     * @return
     */
    public List<Map<String,Object>> queryAreaByToken(UserDomain userDomain)
    {
        return areaDao.queryAreaByToken(userDomain.getAreaCode());
    }

    /**
     * @see IAreaMapper#queryChannelByToken(String)
     * @return
     */
    public List<Map<String,Object>> queryChannelByToken(UserDomain userDomain)
    {
        return areaDao.queryChannelByToken(userDomain.getBusinessHallIds());
    }

    /**
     * @see IAreaMapper#queryChannelByToken(String)
     * @return
     */
    public boolean belongArea(Integer province, Integer city)
    {
        Integer count = areaDao.queryAreaByProvince(province, city);
        if (count > 0) {
            return true;
        }
        else {
            return false;
        }
    }

}
