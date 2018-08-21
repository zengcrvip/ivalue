package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.ScenceSmsClientDomain;
import com.axon.market.common.domain.iscene.ScenceSmsRuleDomain;
import com.axon.market.dao.base.IMyBatisMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 场景规则DAO
 * Created by zengcr on 2016/11/9.
 */
@Component("iScenceSmsRuleMapper")
public interface IScenceSmsRuleMapper extends IMyBatisMapper
{
    /**
     * 查询场景规则总数
     * @param sceneceName 场景规则名称
     * @param scenceStatus 场景规则状态
     * @return 场景规则总数
     */
    int queryScenceRuleTotal(@Param(value = "sceneceName") String sceneceName, @Param(value = "scenceStatus") String scenceStatus);
    /**
     * 查询场景规则列表
     * @param offset 每次查询的数量
     * @param limit 起始标记位
     * @param sceneceName 场景规则名称
     * @param scenceStatus 场景规则状态
     * @return
     */
    List<ScenceSmsRuleDomain> queryScenceRuleByPage(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "sceneceName") String sceneceName, @Param(value = "scenceStatus") String scenceStatus);
    /**
     * 根据ID删除场景规则
     * @param id 主键ID
     * @return
     */
    int deleteSenceSmsRuleById(@Param(value = "id") int id);
    /**
     * 查询场景规则客户端总数
     * @param clientName 客户端名称
     * @param clientType 一级分类
     * @param clientType2 二级分类
     * @return 返回客户端总数
     */
    int queryScenceClientTotal(@Param(value = "clientName") String clientName, @Param(value = "clientType") String clientType, @Param(value = "clientType2") String clientType2);
    /**
     * 查询场景规则客户端列表
     * @param offset 每次查询的数量
     * @param limit 起始标记位
     * @param clientName 客户端名称
     * @param clientType 一级分类
     * @param clientType2 二级分类
     * @return 场景规则客户端列表
     */
    List<ScenceSmsClientDomain> queryScenceClientByPage(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "clientName") String clientName, @Param(value = "clientType") String clientType, @Param(value = "clientType2") String clientType2);
    /**
     * 查询场景规则类型
     * @return
     */
    List<Map<String,String>> querySenceRuleType();
    /**
     * 查询场景规则
     * @return
     */
    List<Map<String,String>> querySenceRuleSmsType();
    /**
     * 查询场景规则客户端一级类型
     * @return
     */
    List<Map<String,String>> querySenceClientTypeOne();
    /**
     * 查询场景规则客户端二级类型
     * @param scenceClientType 一级分类ID
     * @return
     */
    List<Map<String,String>> querySenceClientTypeTwo(@Param(value = "scenceClientType") String scenceClientType);
    /**
     *场景短信规则终端总数
     * @param terminalName 终端名称
     * @return 终端总数
     */
    int queryScenceTerminalTotal(@Param(value = "terminalName") String terminalName);
    /**
     * 查询场景规则终端列表
     * @param offset 每次查询数量
     * @param limit 起始标记位
     * @param terminalName 终端名称
     * @return
     */
    List<Map<String,String>> queryScenceTerminalByPage(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "terminalName") String terminalName);
    /**
     * 新建场景规则
     * @param scenceSmsRuleDomain  场景规则实体对象
     * @return
     */
    int createSceneRule(ScenceSmsRuleDomain scenceSmsRuleDomain);
    /**
     * 修改场景规则
     * @param scenceSmsRuleDomain  场景规则实体对象
     * @return
     */
    int updateSceneRule(ScenceSmsRuleDomain scenceSmsRuleDomain);
    /**
     *根据主键ID查询场景规则
     * @param senceRuleId 主键ID
     * @return
     */
    ScenceSmsRuleDomain querySenceRuleById(@Param(value = "senceRuleId") Integer senceRuleId);

}
