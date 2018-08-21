package com.axon.market.core.task;

import com.axon.market.common.domain.itag.TagDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.ValidateUtil;
import com.axon.market.core.schedule.scheduler.TagRefreshScheduler;
import com.axon.market.core.service.itag.TagService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenyu on 2016/6/16.
 */
public class MonitorTagRefreshTask extends RunJob
{
    private static final Logger LOG = Logger.getLogger(MonitorTagRefreshTask.class.getName());

    /**
     * 调度器
     */
    private static TagRefreshScheduler tagRefreshScheduler = TagRefreshScheduler.getInstance();

    /**
     * 元数据表查询dao
     */
    private static TagService tagService = TagService.getInstance();

    /**
     * 存放正在调度的任务
     */
    private Map<Integer, TagDomain> runningTaskMaps = new HashMap<Integer, TagDomain>();

    /**
     * @param tagDomain
     * @return
     */
    private boolean isCanRefreshBySystem(TagDomain tagDomain)
    {
        return "0".equals(tagDomain.getNeedAutoFetch());
    }

    /**
     * 调度相关参数是否修改
     *
     * @return
     */
    private boolean isScheduleChanged(TagDomain tagDomain, TagDomain oldTagDomain)
    {
        if (!tagDomain.getRemoteServerId().equals(oldTagDomain.getRemoteServerId())
                || !tagDomain.getRemoteFile().equals(oldTagDomain.getRemoteFile())
                || !tagDomain.getExecuteTime().equals(oldTagDomain.getExecuteTime())
                || !tagDomain.getIntervalTime().equals(oldTagDomain.getIntervalTime()))
        {
            return true;
        }
        return false;
    }

    /**
     * @param tagDomains
     */
    private void doDeleteTags(List<TagDomain> tagDomains)
    {
        List<Integer> oldIds = new ArrayList<Integer>(runningTaskMaps.keySet());
        List<Integer> newIds = new ArrayList<Integer>();
        for (TagDomain tagDomain : tagDomains)
        {
            newIds.add(tagDomain.getId());
        }
        oldIds.removeAll(newIds);

        if (oldIds.size() > 0)
        {
            for (int id : oldIds)
            {
                tagRefreshScheduler.removeScheduleMeta(runningTaskMaps.get(id));
                runningTaskMaps.remove(id);
            }
            LOG.info("Remove Schedule Tag : " + oldIds);
        }
    }

    /**
     * @param tagDomains
     */
    private void doModifyTags(List<TagDomain> tagDomains)
    {
        for (TagDomain tagDomain : tagDomains)
        {
            TagDomain oldMetaTableDomain = runningTaskMaps.get(tagDomain.getId());

            if (oldMetaTableDomain == null)
            {
                if (isCanRefreshBySystem(tagDomain))
                {
                    tagRefreshScheduler.addScheduleMeta(tagDomain);
                    runningTaskMaps.put(tagDomain.getId(), tagDomain);
                    LOG.info("Add Schedule Tag : " + tagDomain.getId() + " " + tagDomain.getName());
                }
            }
            else
            {
                if (isCanRefreshBySystem(tagDomain))
                {
                    if (isScheduleChanged(tagDomain, oldMetaTableDomain))
                    {
                        tagRefreshScheduler.updateScheduleMeta(tagDomain);
                        runningTaskMaps.put(tagDomain.getId(), tagDomain);
                        LOG.info("UpdateScheduleMetaTable:" + oldMetaTableDomain.getId() + " " + oldMetaTableDomain.getName());
                    }
                }
                else
                {
                    tagRefreshScheduler.removeScheduleMeta(oldMetaTableDomain);
                    runningTaskMaps.remove(oldMetaTableDomain.getId());
                    LOG.info("RemoveScheduleMetaTable:" + oldMetaTableDomain.getId() + " " + oldMetaTableDomain.getName());
                }
            }
        }
    }

    @Override
    public void runBody()
    {
        try
        {
            List<TagDomain> tagDomains = tagService.queryAllTags();

            if (ValidateUtil.isEmpty(tagDomains))
            {
                return;
            }

            //删除已经不存在的meta table
            doDeleteTags(tagDomains);

            //增加、删除（还存在，但暂时不需要调度）、修改老meta table
            doModifyTags(tagDomains);

        }
        catch (Exception e)
        {
            LOG.error("Monitor Tags Error. ", e);
        }
    }
}
