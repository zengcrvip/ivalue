package com.axon.market.core.schedule.job;

import com.axon.market.common.domain.imodel.ModelDomain;
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
 * Created by chenyu on 2016/6/8.
 */
@Component("refreshImportModelJob")
@Scope("prototype")
public class RefreshImportModelJob extends RunJob
{
    private static final Logger LOG = Logger.getLogger(RefreshImportModelJob.class.getName());

    @Autowired
    @Qualifier("refreshDataService")
    private RefreshDataService refreshDataService;

    private AtomicBoolean running = new AtomicBoolean(false);

    private ModelDomain modelDomain;

    public void setModelDomain(ModelDomain modelDomain)
    {
        this.modelDomain = modelDomain;
    }

    /**
     * @return
     */
    public static RefreshImportModelJob getInstance()
    {
        return SpringUtil.getPrototypeBean(RefreshImportModelJob.class);
    }

    @Override
    public void runBody()
    {
        if (!running.compareAndSet(false, true))
        {
            LOG.warn("Refresh Import Model Job Is Running , Model : " + modelDomain.getName());
            return;
        }

        try
        {
            refreshDataService.refreshRemoteImportModelData(modelDomain);
        }
        catch (Exception e)
        {
            LOG.error("Refresh Import Model Job Error. ", e);
        }
        finally
        {
            running.set(false);
        }
    }
}
