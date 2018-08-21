package com.axon.market.core.service.iscene;


import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.domain.iscene.ScenceSmsClientDomain;
import com.axon.market.common.domain.iscene.ScenceSmsRuleDomain;
import com.axon.market.dao.mapper.iscene.IScenceSmsRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 场景规则service
 * Created by zengcr on 2016/11/9.
 */
@Component("sceneceSmsRuleService")
public class SceneceSmsRuleService
{
    @Autowired
    @Qualifier("iScenceSmsRuleMapper")
    private IScenceSmsRuleMapper iScenceSmsRuleMapper;

    /**
     * 查询场景规则总数
     * @param sceneceName 场景规则名称
     * @param scenceStatus 场景规则状态
     * @return 场景规则总数
     */
    public int queryScenceRuleTotal(String sceneceName,String scenceStatus)
    {
        return iScenceSmsRuleMapper.queryScenceRuleTotal(sceneceName, scenceStatus);
    }

    /**
     * 查询场景规则列表
     * @param offset 每次查询的数量
     * @param limit 起始标记位
     * @param sceneceName 场景规则名称
     * @param scenceStatus 场景规则状态
     * @return
     */
    public List<ScenceSmsRuleDomain> queryScenceRuleByPage(int offset,int limit,String sceneceName,String scenceStatus)
    {
        return iScenceSmsRuleMapper.queryScenceRuleByPage(offset, limit, sceneceName, scenceStatus);
    }

    /**
     * 根据ID删除场景规则
     * @param id 主键ID
     * @return
     */
    public ServiceResult deleteSenceSmsRuleById(int id)
    {
        ServiceResult result = new ServiceResult();
        int flag = iScenceSmsRuleMapper.deleteSenceSmsRuleById(id);
        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("数据库删除操作异常");
        }
        return result;
    }

    /**
     * 查询场景规则客户端总数
     * @param clientName 客户端名称
     * @param clientType 一级分类
     * @param clientType2 二级分类
     * @return 返回客户端总数
     */
    public int queryScenceClientTotal(String clientName,String clientType,String clientType2)
    {
        return iScenceSmsRuleMapper.queryScenceClientTotal(clientName, clientType, clientType2);
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
    public List<ScenceSmsClientDomain> queryScenceClientByPage(int offset,int limit,String clientName,String clientType,String clientType2)
    {
        return iScenceSmsRuleMapper.queryScenceClientByPage(offset, limit, clientName, clientType, clientType2);
    }

    /**
     * 查询场景规则类型
     * @return
     */
    public List<Map<String,String>> querySenceRuleType(){
        return iScenceSmsRuleMapper.querySenceRuleType();
    }

    /**
     * 查询场景规则
     * @return
     */
    public List<Map<String,String>> querySenceRuleSmsType(){
        return iScenceSmsRuleMapper.querySenceRuleSmsType();
    }

    /**
     * 查询场景规则客户端一级类型
     * @return
     */
    public List<Map<String,String>> querySenceClientTypeOne(){

        return iScenceSmsRuleMapper.querySenceClientTypeOne();
    }

    /**
     * 查询场景规则客户端二级类型
     * @param scenceClientType 一级分类ID
     * @return
     */
    public List<Map<String,String>> querySenceClientTypeTwo(String scenceClientType){
        return iScenceSmsRuleMapper.querySenceClientTypeTwo(scenceClientType);
    }

    /**
     *场景短信规则终端总数
     * @param terminalName 终端名称
     * @return 终端总数
     */
    public int queryScenceTerminalTotal(String terminalName)
    {
        return iScenceSmsRuleMapper.queryScenceTerminalTotal(terminalName);
    }

    /**
     * 查询场景规则终端列表
     * @param offset 每次查询数量
     * @param limit 起始标记位
     * @param terminalName 终端名称
     * @return
     */
    public List<Map<String,String>> queryScenceTerminalByPage(int offset,int limit,String terminalName)
    {
        return iScenceSmsRuleMapper.queryScenceTerminalByPage(offset, limit, terminalName);
    }

    /**
     * 新建或修改场景规则
     * @param scenceSmsRuleDomain  场景规则实体对象
     * @return
     */
    public ServiceResult createOrUpdateSceneRule(ScenceSmsRuleDomain scenceSmsRuleDomain){
        ServiceResult result = new ServiceResult();
        if(scenceSmsRuleDomain.getMatchClient() == 0){
            scenceSmsRuleDomain.setClientIds("");
            scenceSmsRuleDomain.setChannelName("");
        }
        if(scenceSmsRuleDomain.getMatchKeywords() == 0){
            scenceSmsRuleDomain.setKeywords("");
        }
        if(scenceSmsRuleDomain.getMatchPosition() == 0){
            scenceSmsRuleDomain.setPositions("");
        }
        if(scenceSmsRuleDomain.getMatchSite() == 0){
            scenceSmsRuleDomain.setWebSits("");
        }
        if(scenceSmsRuleDomain.getMatchTerminal() == 0){
            scenceSmsRuleDomain.setTerminals("");
            scenceSmsRuleDomain.setTerminalNames("");
        }

        //新增或修改
        int flag = 0;
        Integer  id = scenceSmsRuleDomain.getId() ;
        if (id == null){
            //新增
             flag = iScenceSmsRuleMapper.createSceneRule(scenceSmsRuleDomain);
        }else{
            //修改
             flag = iScenceSmsRuleMapper.updateSceneRule(scenceSmsRuleDomain);
        }

        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("场景短信业务规则数据库新增操作异常");
        }
        return result;
    }

    /**
     *根据主键ID查询场景规则
     * @param senceRuleId 主键ID
     * @return
     */
    public ScenceSmsRuleDomain querySenceRuleById(Integer senceRuleId){
        ScenceSmsRuleDomain scenceSmsRuleDomain = iScenceSmsRuleMapper.querySenceRuleById(senceRuleId);
        return scenceSmsRuleDomain;
    }

}
