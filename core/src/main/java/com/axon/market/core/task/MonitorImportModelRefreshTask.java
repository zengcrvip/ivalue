package com.axon.market.core.task;

import com.axon.market.common.constant.isystem.MarketSystemElementCreateEnum;
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
 * Created by chenyu on 2016/6/8.
 */
public class MonitorImportModelRefreshTask extends RunJob
{
    /**
     * LOG
     */
    private static final Logger LOG = Logger.getLogger(MonitorImportModelRefreshTask.class.getName());

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
    private Map<Integer, ModelDomain> runningTaskMaps = new HashMap<Integer, ModelDomain>();

    /**
     * @param modelDomain
     * @return
     */
    private boolean isCanRefreshBySystem(ModelDomain modelDomain)
    {
        if (modelDomain.getCreateType().equals(MarketSystemElementCreateEnum.REMOTE_IMPORT.getValue()))
        {
            return true;
        }
        return false;
    }

    /**
     * @param model
     * @param oldModel
     * @return
     */
    private boolean isScheduleChanged(ModelDomain model, ModelDomain oldModel)
    {
        if (!model.getIntervalTime().equals(oldModel.getIntervalTime())
                || !model.getExecuteTime().equals(oldModel.getExecuteTime()))
        {
            return true;
        }
        return false;
    }

    /**
     * @param models
     */
    private void doDeleteModels(List<ModelDomain> models)
    {
        List<Integer> oldIds = new ArrayList<Integer>(runningTaskMaps.keySet());
        List<Integer> newIds = new ArrayList<Integer>();
        for (ModelDomain model : models)
        {
            newIds.add(model.getId());
        }
        oldIds.removeAll(newIds);

        if (oldIds.size() > 0)
        {
            for (int id : oldIds)
            {
                modelRefreshScheduler.removeScheduleTag(runningTaskMaps.get(id));
                runningTaskMaps.remove(id);
            }
            LOG.info("Remove Schedule Import Models : " + oldIds);
        }
    }

    /**
     * @param models
     */
    private void doModifyModels(List<ModelDomain> models)
    {
        for (ModelDomain model : models)
        {
            ModelDomain oldModel = runningTaskMaps.get(model.getId());

            if (oldModel == null)
            {
                if (isCanRefreshBySystem(model))
                {
                    modelRefreshScheduler.addScheduleTag(model);
                    runningTaskMaps.put(model.getId(), model);
                    LOG.info("Add Schedule Import Model : " + model.getId() + " " + model.getName());
                }
            }
            else
            {
                if (isCanRefreshBySystem(model))
                {
                    if (isScheduleChanged(model, oldModel))
                    {
                        modelRefreshScheduler.updateScheduleTag(model);
                        runningTaskMaps.put(model.getId(), model);
                        LOG.info("Update Schedule Import Model :" + oldModel.getId() + " " + oldModel.getName());
                    }
                }
                else
                {
                    modelRefreshScheduler.removeScheduleTag(oldModel);
                    runningTaskMaps.remove(oldModel.getId());
                    LOG.info("Remove Schedule Import Model : " + oldModel.getId() + " " + oldModel.getName());
                }
            }
        }
    }

    @Override
    public void runBody()
    {
        try
        {
            List<ModelDomain> models = modelService.queryAllModels();

            if (ValidateUtil.isEmpty(models))
            {
                return;
            }

            //删除已经不存在的tag（标签被删除了）
            doDeleteModels(models);
            //增加、删除（标签还存在，但暂时不需要调度刷新）、修改老tag
            doModifyModels(models);

        }
        catch (Exception e)
        {
            LOG.error("Monitor Import Model Refresh Error. ", e);
        }
    }
}
