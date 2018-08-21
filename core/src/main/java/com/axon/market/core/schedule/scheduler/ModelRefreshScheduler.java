package com.axon.market.core.schedule.scheduler;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.constant.isystem.MarketSystemElementCreateEnum;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.timer.NoRedoIntervalTask;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.timer.TimerTask;
import com.axon.market.common.util.MarketTimeUtils;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.schedule.job.RefreshImportModelJob;
import com.axon.market.core.schedule.job.RefreshRuleModelJob;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenyu on 2016/6/8.
 */
@Component("modelRefreshScheduler")
public class ModelRefreshScheduler
{
    private static final Logger LOG = Logger.getLogger(TagRefreshScheduler.class.getName());

    private List<String> allTags = new ArrayList<>();

    private Timer scheduler = new Timer(10);

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    /**
     * @return
     */
    public static ModelRefreshScheduler getInstance()
    {
        return (ModelRefreshScheduler) SpringUtil.getSingletonBean("modelRefreshScheduler");
    }

    /**
     * @param modelDomain
     * @return
     */
    public boolean addScheduleTag(ModelDomain modelDomain)
    {
        try
        {
            String tagName = getTagName(modelDomain);
            if (allTags.contains(tagName))
            {
                throw new RuntimeException("Model Is Exists");
            }
            scheduler.addTask(getTask(modelDomain));
            allTags.add(tagName);
            return true;
        }
        catch (Exception e)
        {
            LOG.error("Add Schedule Model Error. " + modelDomain.getName(), e);
        }
        return false;
    }

    /**
     * @param modelDomain
     * @return
     */
    public boolean removeScheduleTag(ModelDomain modelDomain)
    {
        try
        {
            String tagName = getTagName(modelDomain);
            if (!allTags.contains(tagName))
            {
                throw new RuntimeException("Tag Is Not Exists");
            }
            scheduler.removeTask(getTask(modelDomain));
            allTags.remove(getTagName(modelDomain));
            return true;
        }
        catch (Exception e)
        {
            LOG.error("Remove Schedule Model Error. " + modelDomain.getName(), e);
        }
        return false;
    }

    /**
     * @param modelDomain
     * @return
     */
    public boolean updateScheduleTag(ModelDomain modelDomain)
    {
        try
        {
            String tagName = getTagName(modelDomain);
            if (!allTags.contains(tagName))
            {
                throw new RuntimeException("Tag Is Not Exists");
            }
            scheduler.updateTask(getTask(modelDomain));
            return true;
        }
        catch (Exception e)
        {
            LOG.error("Update Schedule Model Error. " + toString(), e);
        }
        return false;
    }

    /**
     * @param modelDomain
     * @return
     */
    private String getTagName(ModelDomain modelDomain)
    {
        return "model_" + modelDomain.getName() + "_" + modelDomain.getId();
    }

    /**
     * @param modelDomain
     * @return
     */
    private TimerTask getTask(ModelDomain modelDomain) throws ParseException
    {
        if (MarketSystemElementCreateEnum.RULE.getValue().equals(modelDomain.getCreateType()))
        {
            String taskName = getTagName(modelDomain);
            Date startTime = systemConfigBean.isForceRefreshRuleModel() ? MarketTimeUtils.formatDate(MarketTimeUtils.getSpecifiedTime(systemConfigBean.getForceRefreshModelTime())) : new Date();
            long intervalMills = 86400 * 1000;
            RefreshRuleModelJob job = RefreshRuleModelJob.getInstance();
            job.setModelDomain(modelDomain);
            return new NoRedoIntervalTask(taskName, startTime, null, intervalMills, job);
        }
        else if (MarketSystemElementCreateEnum.REMOTE_IMPORT.getValue().equals(modelDomain.getCreateType()))
        {
            String taskName = getTagName(modelDomain);
            Date startTime = MarketTimeUtils.formatDate(modelDomain.getExecuteTime());
            long intervalMills = Long.parseLong(modelDomain.getIntervalTime()) * 1000;
            RefreshImportModelJob job = RefreshImportModelJob.getInstance();
            job.setModelDomain(modelDomain);
            return new NoRedoIntervalTask(taskName, startTime, null, intervalMills, job);
        }

        throw new RuntimeException("Not Support Model Schedule Type " + modelDomain.getCreateType());
    }
}
