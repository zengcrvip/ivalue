package com.axon.market.dao.scheduling;

import com.axon.market.common.domain.iscene.ScenceSmsRuleDomain;
import com.axon.market.common.domain.scheduling.CdrDomain;
import com.axon.market.common.domain.scheduling.PSceneTask;
import com.axon.market.common.domain.scheduling.PTaskDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zengcr on 2016/11/17.
 */
@Service("scenceSmsSynDataDao")
public interface IScenceSmsSynDataMapper extends IMyBatisMapper
{
    //入库pdc_aide.conf_scene
    int insertConfScene(ScenceSmsRuleDomain scenceSmsRuleDomain);

    //入库pdc.cdr
    int insertCdr(CdrDomain cdrDomain);

    //入库pdc_aide.conf_scene_client
    int insertSceneClient(@Param(value = "sceneId") String sceneId, @Param(value = "editor") String editor, @Param(value = "dataList") List<String> dataList);

    // 入库pdc_aide.conf_scene_imei
    int insertSceneImei(@Param(value = "sceneId") String sceneId, @Param(value = "dataList") List<String> dataList);

    // 入库pdc_aide.conf_scene_message
    int insertSceneMessage(@Param(value = "sceneId") String sceneId, @Param(value = "cdrId") Long cdrId, @Param(value = "editor") String editor);

    //入库pdc_aide.conf_scene_website
    int insertSceneWebSit(@Param(value = "sceneId") String sceneId, @Param(value = "editor") String editor, @Param(value = "dataList") List<String> dataList);

    //入库pdc.p_task
    int inserPTask(PTaskDomain pTaskDomain);

    //入库pdc.scene_task
    int insertSceneTask(PSceneTask pSceneTask);

    //根据场景规则名称查询表pdc_aide.conf_scene主键ID
    List<String> queryConfSceneIdByName(ScenceSmsRuleDomain scenceSmsRuleDomain);

    //根据业务场景名称查询pdc.p_task主键ID
    List<String> queryPTaskIdByName(PTaskDomain pTaskDomain);

    //根据业务场景名称查询pdc.p_task数据
    PTaskDomain queryPTaskByName(@Param(value = "taskTitle") String taskTitle);

    //根据业务场景ID查询pdc.p_task数据
    PTaskDomain queryPTaskById(@Param(value = "taskId") String taskId);

    //修改pdc.p_task状态
    int updatePTaskStatus(PTaskDomain pTaskDomain);

    //删除pdc_aide.conf_scene
    int deleteConfSceneById(@Param(value = "confSceneId") String confSceneId);

    //删除pdc_aide.conf_scene_client
    int deleteSceneClientById(@Param(value = "confSceneId") String confSceneId);

    // 删除pdc_aide.conf_scene_imei
    int deleteSceneImeiById(@Param(value = "confSceneId") String confSceneId);

    // 删除pdc_aide.conf_scene_message
    int deleteSceneMessageById(@Param(value = "confSceneId") String confSceneId);

    //删除pdc_aide.conf_scene_website
    int deleteSceneWebSitById(@Param(value = "confSceneId") String confSceneId);

    //删除pdc.p_task
    int deletePTaskById(@Param(value = "pTaskId") String pTaskId);

    //删除表pdc.scene_task
    int deleteSceneTask(@Param(value = "pTaskId") String pTaskId);

    //根据场景ID查询任务ID
    List<String> querySceneIdByTaskId(@Param(value = "pTaskId") String pTaskId);

    //创建目标表
    int createPhoneTable(@Param(value = "tableName") String tableName);

    //同步号码至目标库
    int syncPhone(@Param(value = "tableName") String tableName, @Param(value = "dataList") List<String> dataList);











}
