package com.axon.market.core.service.iscene;


import com.axon.market.common.domain.iscene.AdditionalConditionDomain;
import com.axon.market.dao.mapper.iscene.IAdditionalConditionMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by DELL on 2016/12/6.
 */
@Service("additionalService")
public class AdditionalConditionService
{
    @Autowired
    @Qualifier("additionalDao")
    private IAdditionalConditionMapper additionalDao;

    private static final Logger LOG = Logger.getLogger(AdditionalConditionService.class.getName());

    /**
     * 初始查询和模糊查询
     *
     * @param name
     * @param limit
     * @param offset
     * @return
     */
    public List<AdditionalConditionDomain> queryAdditionalDomain(String name, int limit, int offset)
    {
        return additionalDao.queryAdditionalCondition(name, limit, offset);
    }

    /**
     * 新增
     *
     * @param ac
     * @return
     */
    public boolean addAdditionalDomain(AdditionalConditionDomain ac)
    {
        try
        {
            int i = additionalDao.addAdditionalCondition(ac);
            if (i > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOG.error("add addAdditionalDomain error" + e.toString());
            return false;
        }
    }

    /**
     * 修改
     *
     * @param ac
     * @return
     */
    public boolean editAdditionalCondition(AdditionalConditionDomain ac)
    {
        try
        {
            int i = additionalDao.editAdditionalCondition(ac);
            if (i > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            LOG.error("edit AdditionalCondition failed :" + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除
     *
     * @param id
     * @param userName
     * @param userId
     * @return
     */
    public boolean deleteAdditionCondition(Integer id, String userName, Integer userId)
    {
        try
        {
            int i = additionalDao.deleteAdditionCondition(id, userName, userId);
            if (i > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            LOG.error("delete AdditionalCondition failed :" + e.toString());
            e.printStackTrace();
            return false;
        }

    }
}
