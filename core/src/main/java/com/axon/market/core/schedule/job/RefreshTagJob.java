package com.axon.market.core.schedule.job;

import com.axon.market.common.domain.itag.TagDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.service.icommon.RefreshDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by chenyu on 2016/6/16.
 */
@Component("refreshTagJob")
@Scope("prototype")
public class RefreshTagJob extends RunJob
{
    private static final Logger LOG = Logger.getLogger(RefreshTagJob.class.getName());

    @Autowired
    @Qualifier("refreshDataService")
    private RefreshDataService refreshDataService;

    private AtomicBoolean running = new AtomicBoolean(false);

    private TagDomain tagDomain;

    public void setTagDomain(TagDomain tagDomain)
    {
        this.tagDomain = tagDomain;
    }

    /**
     * @return
     */
    public static RefreshTagJob getInstance()
    {
        return SpringUtil.getPrototypeBean(RefreshTagJob.class);
    }

    @Override
    public void runBody()
    {
        if (!running.compareAndSet(false, true))
        {
            LOG.warn("Refresh Tag Job Is Running , Tag : " + tagDomain.getName());
            return;
        }

        try
        {
            refreshDataService.refreshRemoteImportTagData(tagDomain);
        }
        catch (Exception e)
        {
            LOG.error("Refresh Tag Job Error. ", e);
        }
        finally
        {
            running.set(false);
        }
    }
}
