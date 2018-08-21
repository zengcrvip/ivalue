package com.axon.market.core.task;

import com.axon.market.common.constant.isystem.ModelStatusEnum;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.ValidateUtil;
import com.axon.market.core.schedule.scheduler.ModelRefreshScheduler;
import com.axon.market.core.service.imodel.ModelService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/5/13.
 */
public class MonitorRuleModelJob extends RunJob
{
    /**
     * LOG
     */
    private static final Logger LOG = Logger.getLogger(MonitorRuleModelJob.class.getName());

    /**
     * 调度器
     */
    private static ModelRefreshScheduler modelRefreshScheduler = ModelRefreshScheduler.getInstance();

    /**
     * 客户群查询service
     */
    private static ModelService modelService = ModelService.getInstance();

    /**
     * 存放正在调度的任务
     */
    private Map<Integer, ModelDomain> runningSegmentMaps = new HashMap<Integer, ModelDomain>();

    /**
     *
     * @param modelDomain
     * @return
     */
    private boolean isCanRefreshBySystem(ModelDomain modelDomain)
    {
        boolean isReady = modelDomain.getStatus().equals(ModelStatusEnum.READY.getValue())
                || modelDomain.getStatus().equals(ModelStatusEnum.REFRESHING.getValue());
        boolean isRefreshType = modelDomain.getCreateType().equals("rule");
        if (isReady && isRefreshType)
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @param modelDomain
     * @param oldModel
     * @return
     */
    private boolean isScheduleChanged(ModelDomain modelDomain, ModelDomain oldModel)
    {
        if (!modelDomain.getRule().equals(oldModel.getRule()))
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @param models
     */
    private void doDeleteModels(List<ModelDomain> models)
    {
        List<Integer> oldIds = new ArrayList<Integer>(runningSegmentMaps.keySet());
        List<Integer> newIds = new ArrayList<Integer>();
        for(ModelDomain modelDomain : models)
        {
            newIds.add(modelDomain.getId());
        }
        oldIds.removeAll(newIds);

        if(oldIds.size() > 0 )
        {
            for(int id : oldIds)
            {
                modelRefreshScheduler.removeScheduleTag(runningSegmentMaps.get(id));
                runningSegmentMaps.remove(id);
            }
            LOG.info("Remove RuleSegmentRefreshSchedule Models:" + oldIds);
        }
    }

    /**
     *
     * @param models
     */
    private void doModifyModels(List<ModelDomain> models)
    {
        for(ModelDomain model : models)
        {
            ModelDomain oldModel = runningSegmentMaps.get(model.getId());

            if(oldModel == null)
            {
                if(isCanRefreshBySystem(model))
                {
                    modelRefreshScheduler.addScheduleTag(model);
                    runningSegmentMaps.put(model.getId(), model);
                    LOG.info("Add RuleSegmentRefreshSchedule Model:" + model.getId() + " " + model.getName());
                }
            }
            else
            {
                if(isCanRefreshBySystem(model))
                {
                    if(isScheduleChanged(model, oldModel))
                    {
                        modelRefreshScheduler.updateScheduleTag(model);
                        runningSegmentMaps.put(model.getId(), model);
                        LOG.info("Update RuleSegmentRefreshSchedule Model:" + oldModel.getId() + " " + oldModel.getName());
                    }
                }
                else
                {
                    modelRefreshScheduler.removeScheduleTag(oldModel);
                    runningSegmentMaps.remove(oldModel.getId());
                    LOG.info("Remove RuleSegmentRefreshSchedule Model:" + oldModel.getId() + " " + oldModel.getName());
                }
            }
        }
    }

    @Override
    public void runBody()
    {
        try
        {
            List<ModelDomain> models = modelService.queryAllRuleModelsBySystem();

            if(ValidateUtil.isEmpty(models))
            {
                return;
            }
            //删除已经不存在的model（客户群被删除了）
            doDeleteModels(models);
            //增加、删除（客户群还存在，但暂时不需要调度刷新）、修改老model
            doModifyModels(models);

        }
        catch (Exception e)
        {
            LOG.error("Monitor Rule Model Refresh error ", e);
        }
    }
}
