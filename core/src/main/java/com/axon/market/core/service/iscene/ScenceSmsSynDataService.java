package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.domain.iscene.ScenceSmsRuleDomain;
import com.axon.market.common.domain.ischeduling.MarketJobDomain;
import com.axon.market.common.domain.scheduling.CdrDomain;
import com.axon.market.common.domain.scheduling.PSceneTask;
import com.axon.market.common.domain.scheduling.PTaskDomain;
import com.axon.market.dao.mapper.iscene.IScenceSmsRuleMapper;
import com.axon.market.dao.scheduling.IScenceSmsSynDataMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zengcr on 2016/11/17.
 */
@Component("scenceSmsSynDataService")
public class ScenceSmsSynDataService
{
    @Autowired
    @Qualifier("scenceSmsSynDataDao")
    private IScenceSmsSynDataMapper iScenceSmsSynDataMapper;

    @Autowired
    @Qualifier("iScenceSmsRuleMapper")
    private IScenceSmsRuleMapper iScenceSmsRuleMapper;


    /**
     * 场景营销数据同步
     * @param marketJobDomain
     * @param scenceSmsRuleDomain
     */
    @Transactional
    public void syncData(MarketJobDomain marketJobDomain,ScenceSmsRuleDomain scenceSmsRuleDomain, PTaskDomain pTaskDomain){
        this.deleteMarket(marketJobDomain);

        //同步表pdc_aide.conf_scene
        iScenceSmsSynDataMapper.insertConfScene(scenceSmsRuleDomain);

        //同步表pdc.cdr
        CdrDomain cdrDomain = new CdrDomain();
        cdrDomain.setResTitle(marketJobDomain.getMarketContent());
        iScenceSmsSynDataMapper.insertCdr(cdrDomain);
        Long cdrId = cdrDomain.getId();

        //同步表pdc_aide.conf_scene_message
        String sceneId = scenceSmsRuleDomain.getSerialNum();
        String editor = scenceSmsRuleDomain.getEditor();
        iScenceSmsSynDataMapper.insertSceneMessage(sceneId,cdrId,editor);

        //同步表pdc_aide.conf_scene_client
        if(1==scenceSmsRuleDomain.getMatchClient()){
            List<String> dataList = Arrays.asList(scenceSmsRuleDomain.getClientIds().split(","));
            if(dataList.size() > 0){
                iScenceSmsSynDataMapper.insertSceneClient(sceneId,editor,dataList);
            }
        }
        //同步表pdc_aide.conf_scene_imei
        if(1 == scenceSmsRuleDomain.getMatchTerminal()){
            List<String> dataList1 = Arrays.asList(scenceSmsRuleDomain.getTerminals().split(","));
            if(dataList1.size() > 0){
                iScenceSmsSynDataMapper.insertSceneImei(sceneId,dataList1);
            }

        }
        //同步表pdc_aide.conf_scene_website
        if(1==scenceSmsRuleDomain.getMatchSite()){
            List<String> dataList2 = Arrays.asList(scenceSmsRuleDomain.getWebSits().split(","));
            if(dataList2.size() > 0){
                iScenceSmsSynDataMapper.insertSceneWebSit(sceneId,editor,dataList2);
            }
        }

        //同步表pdc.p_task
        pTaskDomain.setcId(cdrId);
        iScenceSmsSynDataMapper.inserPTask(pTaskDomain);
        Long pTaskId = pTaskDomain.getId();

        //同步表pdc.scene_task
        PSceneTask pSceneTask = new PSceneTask();
        pSceneTask.setTaskId(pTaskId);
        pSceneTask.setSceneId(scenceSmsRuleDomain.getSerialNum());
        pSceneTask.setBeginTime(marketJobDomain.getStartTime());
        pSceneTask.setEndTime(marketJobDomain.getEndTime());
        pSceneTask.setWhereStr(pTaskDomain.getWhereStr());
        iScenceSmsSynDataMapper.insertSceneTask(pSceneTask);
    }

    /**
     * 场景营销活动删除
     */
    @Transactional
    public ServiceResult deleteMarket(MarketJobDomain marketJobDomain){
        ServiceResult result = new ServiceResult();
        Integer pTaskId = marketJobDomain.getLastTaskId();
        if(null != pTaskId){
            List<String> confSceneIds =  iScenceSmsSynDataMapper.querySceneIdByTaskId(""+pTaskId);
            if(CollectionUtils.isNotEmpty(confSceneIds)){
                for(String confSceneId : confSceneIds){
                    if(null != confSceneId && !"".equals(confSceneId)){
                        iScenceSmsSynDataMapper.deleteSceneClientById(confSceneId);
                        iScenceSmsSynDataMapper.deleteSceneImeiById(confSceneId);
                        iScenceSmsSynDataMapper.deleteSceneMessageById(confSceneId);
                        iScenceSmsSynDataMapper.deleteSceneWebSitById(confSceneId);
                        iScenceSmsSynDataMapper.deleteConfSceneById(confSceneId);
                    }
                }
            }
            iScenceSmsSynDataMapper.deleteSceneTask(""+pTaskId);
            iScenceSmsSynDataMapper.deletePTaskById(""+pTaskId);
        }

        return result;
    }

    /**
     * 重启营销活动
     * @param marketJobDomain
     * @return
     */
    public ServiceResult resumeMarket(MarketJobDomain marketJobDomain)
    {
        ServiceResult result = new ServiceResult();
        PTaskDomain pTaskDomain = iScenceSmsSynDataMapper.queryPTaskById(""+marketJobDomain.getLastTaskId());
        if (null != pTaskDomain &&  0 == pTaskDomain.getIfExecute())
        {
            pTaskDomain.setIfExecute(1);
            iScenceSmsSynDataMapper.updatePTaskStatus(pTaskDomain);
        }

        return result;
    }


    /**
     * 暂停营销活动
     * @param marketJobDomain
     * @return
     */
    public ServiceResult pauseMarket(MarketJobDomain marketJobDomain)
    {
        ServiceResult result = new ServiceResult();
        PTaskDomain pTaskDomain = iScenceSmsSynDataMapper.queryPTaskById(marketJobDomain.getLastTaskId()+"");
        if (0 == pTaskDomain.getIfExecute())
        {
            result.setRetValue(-1);
            result.setDesc("短信下发任务已经暂停");
        }
        else
        {
            pTaskDomain.setIfExecute(0);
            iScenceSmsSynDataMapper.updatePTaskStatus(pTaskDomain);
        }

        return result;
    }

    /**
     * 根据业务规则ID查询场景业务规则
     * @param senceRuleId
     * @return
     */
    public ScenceSmsRuleDomain getSceneceRule(Integer senceRuleId){
         return iScenceSmsRuleMapper.querySenceRuleById(senceRuleId);
    }

   /**
    * 将lines的号码清单插入到目标表tableName
    */
    public int syncPhone(String tableName,List<String> lines){
       return iScenceSmsSynDataMapper.syncPhone(tableName, lines);
    }

    /**
     * 创建目标表
     * @param tableName
     * @return
     */
    public int createPhoneTable(String tableName){
        return iScenceSmsSynDataMapper.createPhoneTable(tableName);
    }
}
