package com.axon.market.core.service.icommon;

import com.axon.market.common.domain.icommon.market.JumpLinkDomain;
import com.axon.market.common.domain.icommon.market.PTaskDomain;
import com.axon.market.common.domain.icommon.market.PdrDomain;
import com.axon.market.dao.mapper.icommon.ISmsMarketMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chenyu on 2017/2/15.
 */
@Service("smsMarketService")
public class SmsMarketService
{
    private static final Logger LOG = Logger.getLogger(SmsMarketService.class.getName());

    @Autowired
    @Qualifier("smsMarketMapper")
    private ISmsMarketMapper smsMarketMapper;

    public Integer createPTask(PTaskDomain pTaskDomain)
    {
        try
        {
            return smsMarketMapper.createPTask(pTaskDomain);
        }
        catch (Exception e)
        {
            LOG.error("Create P Task Error. ", e);
            return null;
        }
    }

    public Integer updatePTask(Integer taskId, Integer count)
    {
        try
        {
            return smsMarketMapper.updatePTask(taskId, count);
        }
        catch (Exception e)
        {
            LOG.error("Update P Task Error. ", e);
            return null;
        }
    }

    public Integer createPdr(List<PdrDomain> pdrDomains)
    {
        try
        {
            return smsMarketMapper.createPdr(pdrDomains);
        }
        catch (Exception e)
        {
            LOG.error("Create Pdr Error. ", e);
            return null;
        }
    }

    public Integer createJumpLink(List<JumpLinkDomain> pdrDomains)
    {
        try
        {
            return smsMarketMapper.createJumpLink(pdrDomains);
        }
        catch (Exception e)
        {
            LOG.error("Create Jump Link Error. ", e);
            return null;
        }
    }
}
