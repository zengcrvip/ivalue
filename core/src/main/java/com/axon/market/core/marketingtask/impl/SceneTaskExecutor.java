package com.axon.market.core.marketingtask.impl;

import com.axon.market.common.domain.ischeduling.MarketingPoolTaskDomain;
import com.axon.market.common.domain.kafkaservice.SceneTask;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.RedisUtil;
import com.axon.market.core.marketingtask.MarketTaskExecutor;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景任务执行器
 * Created by zengcr on 2017/7/27.
 */
@Service("sceneTaskExecutor")
public class SceneTaskExecutor implements MarketTaskExecutor
{
    private static final Logger LOG = Logger.getLogger(SceneTaskExecutor.class.getName());
    private static final  String MARKET_SCENE_TASK_LIST  = "marketSceneId:[phoneList:taskId]";
    private static final String QUERY_SQL = "select phone from model.model_";

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Override
    public String execute(MarketingPoolTaskDomain poolTaskDomain)
    {
        String marketSceneId = poolTaskDomain.getMarketTypeValue();
        if(marketSceneId == null || "".equals(marketSceneId)){
            LOG.error("场景任务的场景规则不能为空，taskID："+poolTaskDomain.getId());
            return null;
        }

        //拼场景任务对象
        SceneTask sceneTask = new SceneTask();
        sceneTask.setTaskId(poolTaskDomain.getId());
        sceneTask.setPhoneList(getTaskPhoneList(poolTaskDomain.getId(), poolTaskDomain.getMarketSegmentIds()));
        sceneTask.setAccessNumber(poolTaskDomain.getAccessNumber());
        sceneTask.setContentSms(poolTaskDomain.getMarketContent());

        //拼场景规则对象
        Map<String,String>  sceneMap = new HashMap<String,String>();
        List<SceneTask> sceneTaskList = new ArrayList<SceneTask>();


        //取出场景ID对应的任务组
        final String sceneTasks = RedisUtil.getInstance().hget(MARKET_SCENE_TASK_LIST,marketSceneId);
        try {
            if(sceneTasks != null && !"".equals(sceneTasks)){
                sceneTaskList = JsonUtil.stringToObject(sceneTasks,List.class);
            }
        } catch (IOException e) {
            LOG.error("sceneTaskList json转list对象 转换失败", e);
        }

        sceneTaskList.add(sceneTask);
        try {
            sceneMap.put(marketSceneId, JsonUtil.objectToString(sceneTaskList));
        } catch (JsonProcessingException e) {
            LOG.error("sceneTaskList List转JSON 转换失败",e);
        }
        //放入redis缓存
        RedisUtil.getInstance().hmset(MARKET_SCENE_TASK_LIST, sceneMap);

        return 0 + "-" + poolTaskDomain.getId();
    }

    private List<String> getTaskPhoneList(Integer taskId, String marketSegmentIds){
        final List<String> list = new ArrayList<String>();
        final  String sql = QUERY_SQL + marketSegmentIds;
        greenPlumOperateService.query(sql, new RowCallbackHandler() {
            Map<String, String> map = new HashMap<String, String>();
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                String phone = resultSet.getString("phone");
                list.add(phone);
            }
        }, 10000);
       return list;
    }


}
