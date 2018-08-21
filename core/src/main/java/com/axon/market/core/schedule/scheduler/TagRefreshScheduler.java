package com.axon.market.core.schedule.scheduler;

import com.axon.market.common.domain.itag.TagDomain;
import com.axon.market.common.timer.NoRedoIntervalTask;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.timer.TimerTask;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.schedule.TimeConversionService;
import com.axon.market.core.schedule.job.RefreshTagJob;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenyu on 2016/6/16.
 */
@Component("tagRefreshScheduler")
public class TagRefreshScheduler
{
    private static final Logger LOG = Logger.getLogger(TagRefreshScheduler.class.getName());

    private List<String> allScheduler = new ArrayList<String>();

    private Timer scheduler = new Timer(5);

    @Autowired
    @Qualifier("timeConversionService")
    private TimeConversionService timeConversionService;

    /**
     * @return
     */
    public static TagRefreshScheduler getInstance()
    {
        return (TagRefreshScheduler) SpringUtil.getSingletonBean("tagRefreshScheduler");
    }

    /**
     * @param tagDomain
     * @return
     */
    public boolean addScheduleMeta(TagDomain tagDomain)
    {
        try
        {
            String jobName = getJobName(tagDomain);
            if (allScheduler.contains(jobName))
            {
                throw new RuntimeException("Tag Is Exists");
            }
            scheduler.addTask(getTask(tagDomain));
            allScheduler.add(jobName);
            return true;
        }
        catch (Exception e)
        {
            LOG.error("Add Schedule Tag Error. " + tagDomain.getName(), e);
        }
        return false;
    }

    /**
     * @param tagDomain
     * @return
     */
    public boolean removeScheduleMeta(TagDomain tagDomain)
    {
        try
        {
            String jobName = getJobName(tagDomain);
            if (!allScheduler.contains(jobName))
            {
                throw new RuntimeException("Tag Is Not Exists");
            }
            scheduler.removeTask(getTask(tagDomain));
            allScheduler.remove(getJobName(tagDomain));
            return true;
        }
        catch (Exception e)
        {
            LOG.error("Remove Schedule Tag Error. " + tagDomain.getName(), e);
        }
        return false;
    }

    /**
     * @param tagDomain
     * @return
     */
    public boolean updateScheduleMeta(TagDomain tagDomain)
    {
        try
        {
            String jobName = getJobName(tagDomain);
            if (!allScheduler.contains(jobName))
            {
                throw new RuntimeException("Tag Is Not Exists");
            }
            scheduler.updateTask(getTask(tagDomain));
            return true;
        }
        catch (Exception e)
        {
            LOG.error("Update Schedule Tag Error. " + tagDomain.getName(), e);
        }
        return false;
    }

    /**
     * @param tagDomain
     * @return
     */
    private String getJobName(TagDomain tagDomain)
    {
        return "tag_" + tagDomain.getName() + "_" + tagDomain.getId();
    }

    /**
     * @param tagDomain
     * @return
     */
    private TimerTask getTask(TagDomain tagDomain) throws ParseException
    {
        String taskName = getJobName(tagDomain);
        Date startTime = TimeUtil.formatDate(tagDomain.getExecuteTime());
        Long intervalMills = timeConversionService.getTaskIntervalSeconds(tagDomain.getIntervalTime()) * 1000;
        if (null != intervalMills)
        {
            RefreshTagJob job = RefreshTagJob.getInstance();
            job.setTagDomain(tagDomain);
            return new NoRedoIntervalTask(taskName, startTime, null, intervalMills, job);
        }
        throw new RuntimeException("No Task");
    }
}
