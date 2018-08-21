package com.axon.market.core.service.inewScene;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.constant.inewScene.SceneCodeEnum;
import com.axon.market.common.domain.inewScene.ConfNewSceneDomain;
import com.axon.market.common.domain.inewScene.ConfNewSceneTypeDomain;
import com.axon.market.common.domain.iscene.ScenceSmsClientDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.dao.mapper.inewScene.INewSceneRuleMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 * Created by zhuwen on 2017/7/12.
 */

@Component("newSceneceRuleService")
public class NewSceneRuleService {
    private static final Logger LOG = Logger.getLogger(NewSceneRuleService.class.getName());

    @Autowired
    @Qualifier("newSceneRuleDao")
    private INewSceneRuleMapper inewScenceRuleMapper;

    /**
     * 查询场景规则客户端一级类型
     * @return
     */
    public List<Map<String,String>> queryNewSceneClientTypeOne(){

        return inewScenceRuleMapper.queryNewSceneClientTypeOne();
    }

    /**
     * 查询场景规则客户端二级类型
     * @param sceneClientType 一级分类ID
     * @return
     */
    public List<Map<String,String>> queryNewSceneClientTypeTwo(String sceneClientType){
        return inewScenceRuleMapper.queryNewSceneClientTypeTwo(sceneClientType);
    }

    /**
     * 查询场景规则客户端总数
     * @param clientName 客户端名称
     * @param clientType 一级分类
     * @param clientType2 二级分类
     * @return 返回客户端总数
     */
    public int queryNewSceneClientTotal(String clientName,String clientType,String clientType2)
    {
        return inewScenceRuleMapper.queryNewSceneClientTotal(clientName, clientType, clientType2);
    }

    /**
     * 查询场景规则客户端列表
     * @param offset 每次查询的数量
     * @param limit 起始标记位
     * @param clientName 客户端名称
     * @param clientType 一级分类
     * @param clientType2 二级分类
     * @return 场景规则客户端列表
     */
    public List<ScenceSmsClientDomain> queryNewSceneClientByPage(int offset,int limit,String clientName,String clientType,String clientType2)
    {
        return inewScenceRuleMapper.queryNewSceneClientByPage(offset, limit, clientName, clientType, clientType2);
    }

    /**
     * 查询客户端名称
     * @param clientID 客户端ID
     * @param clientTypeID 客户端类型
     * @return 场景规则客户端列表
     */
    public String queryNewSceneClientByID(String clientID,String clientTypeID)
    {
        return inewScenceRuleMapper.queryNewSceneClientByID(clientID, clientTypeID);
    }

    /**
     * 查询场景规则总数
     * @param sceneName 场景规则名称
     * @param sceneStatus 场景规则状态
     * @return 场景规则总数
     */
    public int queryNewSceneRuleTotal(String sceneName,String sceneStatus,String sceneTypeID)
    {
        return inewScenceRuleMapper.queryNewSceneRuleTotal(sceneName, sceneStatus, sceneTypeID);
    }

    /**
     * 查询场景规则列表
     * @param offset 每次查询的数量
     * @param limit 起始标记位
     * @param sceneName 场景规则名称
     * @param sceneStatus 场景规则状态
     * @return
     */
    public List<Map<String, Object>> queryNewSceneRuleByPage(int offset,int limit,String sceneName,String sceneStatus, String sceneTypeID)
    {
        List<Map<String, Object>> sceneRuleList = inewScenceRuleMapper.queryNewSceneRuleByPage(offset, limit, sceneName, sceneStatus, sceneTypeID);

        for (int i = 0;i<sceneRuleList.size();i++){
            List<Map<String,Object>> list = null;
            try{
                 list = JsonUtil.stringToObject((String)sceneRuleList.get(i).get("sceneRule"), List.class);
            } catch(Exception e) {
                LOG.error("parse error and the content is:" + sceneRuleList.get(i).get("sceneRule"));
                sceneRuleList.get(i).put("ruleName","");
                continue;
            }

            String code = (String)sceneRuleList.get(i).get("code");
            StringBuffer ruleName = new StringBuffer();
            ruleName.append("");

            if (code.equals(SceneCodeEnum.APP_SCENE.getValue())) {
                //app场景
                int index = 0;
                for (Map<String, Object> map : list) {
                    index++;
                    String appType = map.get(SceneCodeEnum.APP_SCENE.getParam1()) == null ? "": (String)map.get(SceneCodeEnum.APP_SCENE.getParam1());
                    String appid = map.get(SceneCodeEnum.APP_SCENE.getParam2()) == null ? "": (String)map.get(SceneCodeEnum.APP_SCENE.getParam2());
                    if (null != appid && !appid.equals("")) {
                        String[] applist = appid.split(",");
                        for (String id:applist){
                            String appname = inewScenceRuleMapper.queryNewSceneClientByID(id, appType);
                            if (null != appname && !"".equals(appname))
                            {
                                ruleName.append(appname);
                                if (index!=list.size()){
                                    ruleName.append("、");
                                }
                            }
                        }
                    }
                }
            } else if (code.equals(SceneCodeEnum.FLOW_SCENE.getValue())){
                //突发流量场景
                boolean add = false;
                for (Map<String, Object> map: list) {
                    Integer unitTime = map.get(SceneCodeEnum.FLOW_SCENE.getParam1()) == null ? null : (Integer)map.get(SceneCodeEnum.FLOW_SCENE.getParam1())/60000 ;
                    String increaseData = map.get(SceneCodeEnum.FLOW_SCENE.getParam2()) == null ? "" : (String)map.get(SceneCodeEnum.FLOW_SCENE.getParam2());

                    if (!increaseData.equals("")) {
                        ruleName.append("流量超套>").append(increaseData).append("M");
                        add = true;
                    }
                    if (unitTime != null) {
                        if (add) {
                            ruleName.append("&&");
                        }
                        ruleName.append("时长超套>").append(unitTime).append("分");
                    }
                }
            } else {
                LOG.error("rulecode can't be recognized and the code is:" + code);
                sceneRuleList.get(i).put("ruleName","");
                continue;
            }
            sceneRuleList.get(i).put("ruleName",ruleName.toString());
        }

        return sceneRuleList;
    }

    /**
     * 查询场景规则类型
     * @return
     */
    public List<ConfNewSceneTypeDomain> queryNewSceneType()
    {
        return inewScenceRuleMapper.queryNewSceneType();
    }

    /**
     * 新建或修改场景规则
     * @param paras
     * @param session
     * @return
     */
    public ServiceResult createOrUpdateNewSceneRule(Map<String, Object> paras, HttpSession session) {
        ServiceResult result = new ServiceResult();
        ConfNewSceneDomain newSceneDomain = new ConfNewSceneDomain();
        newSceneDomain.setSceneName((String) paras.get("sceneName"));
        newSceneDomain.setSceneTypeID((String) paras.get("sceneTypeID"));
        newSceneDomain.setStartTime((String) paras.get("startTime"));
        newSceneDomain.setEndTime((String) paras.get("endTime"));
        newSceneDomain.setCreateUser(String.valueOf(UserUtils.getLoginUser(session).getId()));
        newSceneDomain.setState((Integer)paras.get("state"));

        try{
            String ruleName = JsonUtil.objectToString(paras.get("sceneRule"));
            if (null != ruleName){
                newSceneDomain.setSceneRule(ruleName);
            }
        } catch(Exception e) {

            result.setDesc("规则解析失败");
            result.setRetValue(-1);
            return result;
        }

        int flag = 0;
        if (paras.get("id") == null){
            flag = inewScenceRuleMapper.createNewSceneRule(newSceneDomain);
        } else {
            newSceneDomain.setId((Integer)paras.get("id"));
            flag = inewScenceRuleMapper.updateNewSceneRule(newSceneDomain);
        }

        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("场景业务规则数据库操作异常");
        }
        return result;
    }

    /**
     * 根据ID删除场景规则
     * @param id 主键ID
     * @return
     */
    public ServiceResult deleteNewSceneByID(int id)
    {
        ServiceResult result = new ServiceResult();
        int flag = inewScenceRuleMapper.deleteNewSceneByID(id);
        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("数据库删除操作异常");
        }
        return result;
    }

    /**
     * 校验任务是否重复
     *
     * @param paras
     * @return
     */
    public boolean validateSceneName(Map<String, Object> paras)
    {
        int num = 0;
        try
        {
            num = inewScenceRuleMapper.querySceneNumByName(paras);
        }
        catch (Exception e)
        {
            num = 0;
            LOG.error("sceneTask validateSceneName error", e);
        }
        if (num > 0)
        {
            return true;
        }
        return false;
    }
}
