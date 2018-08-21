package com.axon.market.dao.mapper.inewScene;

import com.axon.market.common.domain.iscene.ScenceSmsClientDomain;
import com.axon.market.common.domain.inewScene.*;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by zhuwen on 2017/7/12.
 */
@Component("newSceneRuleDao")
public interface INewSceneRuleMapper extends IMyBatisMapper {

    /**
     * 查询场景规则客户端一级类型
     * @return
     */
    List<Map<String,String>> queryNewSceneClientTypeOne();

    /**
     * 查询场景规则客户端二级类型
     * @param sceneClientType 一级分类ID
     * @return
     */
    List<Map<String,String>> queryNewSceneClientTypeTwo(@Param(value = "sceneClientType") String sceneClientType);

    /**
     * 查询场景规则客户端总数
     * @param clientName 客户端名称
     * @param clientType 一级分类
     * @param clientType2 二级分类
     * @return 返回客户端总数
     */
    int queryNewSceneClientTotal(@Param(value = "clientName") String clientName, @Param(value = "clientType") String clientType, @Param(value = "clientType2") String clientType2);
    /**
     * 查询场景规则客户端列表
     * @param offset 每次查询的数量
     * @param limit 起始标记位
     * @param clientName 客户端名称
     * @param clientType 一级分类
     * @param clientType2 二级分类
     * @return 场景规则客户端列表
     */
    List<ScenceSmsClientDomain> queryNewSceneClientByPage(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "clientName") String clientName, @Param(value = "clientType") String clientType, @Param(value = "clientType2") String clientType2);

    /**
     * 查询appid和apptype查询客户端名称
     * @param clientID 客户端ID
     * @param clientTypeID 客户端类型
     * @return 客户端名称
     */
    String queryNewSceneClientByID(@Param(value = "clientID") String clientID, @Param(value = "clientTypeID") String clientTypeID);

    /**
     * 查询场景规则总数
     * @param sceneName 场景规则名称
     * @param sceneStatus 场景规则状态
     * @return 场景规则总数
     */
    int queryNewSceneRuleTotal(@Param(value = "sceneName") String sceneName, @Param(value = "sceneStatus") String sceneStatus, @Param(value = "sceneTypeID") String sceneTypeID);
    /**
     * 查询场景规则列表
     * @param offset 每次查询的数量
     * @param limit 起始标记位
     * @param sceneName 场景规则名称
     * @param sceneStatus 场景规则状态
     * @param sceneTypeID 场景类型
     * @return
     */
    List<Map<String, Object>> queryNewSceneRuleByPage(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "sceneName") String sceneName, @Param(value = "sceneStatus") String sceneStatus, @Param(value = "sceneTypeID") String sceneTypeID);

    /**
     * 查询场景类型
     * @return
     */
    List<ConfNewSceneTypeDomain> queryNewSceneType();

    /**
     * 生成场景规则数据
     * @return
     */
    int createNewSceneRule(ConfNewSceneDomain newSceneDomain);

    /**
     * 编辑场景规则数据
     * @return
     */
    int updateNewSceneRule(ConfNewSceneDomain newSceneDomain);

    /**
     * 将场景规则数据置失效
     * @return
     */
    int deleteNewSceneByID(@Param(value = "id") int id);

    int querySceneNumByName(Map<String, Object> paras);
}
