package com.axon.market.core.service.iscene;


import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.domain.iscene.MarketAreaDomain;
import com.axon.market.common.domain.iscene.PositionSceneDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.dao.mapper.iscene.IPositionSceneMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 位置场景规则 service
 * Created by zengcr on 2016/11/28.
 */
@Component("positionSceneService")
public class PositionSceneService
{
    @Qualifier("positionSceneDao")
    @Autowired
    private IPositionSceneMapper iPositionSceneMapper;

    /**
     * 位置场景分页总数
     * @param positionScenName 位置场景名称
     * @param positionScenStau 位置场景状态
     * @return 位置场景总数量
     */
    public int queryPositionSceneTotal(String positionScenName,String positionScenStau)
    {
        return iPositionSceneMapper.queryPositionSceneTotal(positionScenName, positionScenStau);
    }

    /**
     * 位置场景分页展示列表
     * @param offset 每次查询的数量
     * @param limit 开始标记位
     * @param positionScenName 位置场景名称
     * @param positionScenStau 位置场景状态
     * @return
     */
    public List<PositionSceneDomain> queryPositionSceneByPage(int offset,int limit,String positionScenName,String positionScenStau)
    {
        return iPositionSceneMapper.queryPositionSceneByPage(offset, limit, positionScenName, positionScenStau);
    }

    /**
     * 查询位置场景类别
     * @return
     */
    public List<Map<String,String>> queryPositonSenceType()
    {
      return iPositionSceneMapper.queryPositonSenceType();
    }

    /**
     * 查询基站类型
     * @return
     */
    public List<Map<String,String>> queryBaseAreaType()
    {
        return iPositionSceneMapper.queryBaseAreaType();
    }

    /**
     * 查询基站地区
     * @return
     */
    public List<Map<String,String>>  queryBaseAreas(Map<String,Object> parasMap){
       return iPositionSceneMapper.queryBaseAreas(parasMap);
    }

    /**
     * 查询位置场景查询基站站点总数
     *  baseAreaId 地区编码ID,baseTypeId 基站类型ID,baseId 基站ID,baseName 基站名称
     * @return 基站站点总数
     */
    public int queryBasesTotal(Map<String,Object> parasMap)
    {
        return iPositionSceneMapper.queryBasesTotal(parasMap);
    }

    /**
     * 查询位置场景查询基站站点分页展示
     *  offset 每次查询的数量,limit  起始标记位,baseAreaId 地区编码ID,baseTypeId 基站类型ID,baseId 基站ID,baseName 基站名称
     * @return 基站站点列表
     */
    public List<Map<String,String>> queryBasesByPage(Map<String,Object> parasMap)
    {
        return iPositionSceneMapper.queryBasesByPage(parasMap);
    }

    /**
            * 查询位置场景查询基站站点分页展示
    *  baseAreaId 地区编码ID,baseTypeId 基站类型ID,baseId 基站ID,baseName 基站名称
    * @return 基站站点列表
    */
    public List<Map<String,String>> queryBases(Map<String,Object> parasMap)
    {
        return iPositionSceneMapper.queryBases(parasMap);
    }

    /**
     * 查询场景位置号段地区树结构
     * @param areaIds 已选中的地区ID
     * @return
     */
    public String queryPositionBaseAreas(String areaIds,Integer areaCode, String businessCodes)
    {
        try
        {
            List<MarketAreaDomain> marketAreas = iPositionSceneMapper.queryPositionBaseAreas(areaIds,areaCode,businessCodes);
            return JsonUtil.objectToString(marketAreas);
        }
        catch (JsonProcessingException e)
        {
        }
        return null;
    }

    /**
     * 新建或修改位置场景
     * 主键ID不存在，新增；主键ID存在，修改
     * @param positionSceneDomain 位置场景对象
     * @return
     */
    public ServiceResult createOrUpdatePositionScen(PositionSceneDomain positionSceneDomain){
        ServiceResult result = new ServiceResult();
        //新增或修改
        int flag = 0;
        Integer  id = positionSceneDomain.getId() ;
        Integer isSengReport = positionSceneDomain.getIsSengReport();
        if(isSengReport == 0){
            positionSceneDomain.setReportPhone(null);
        }
        if (id == null){
            flag = iPositionSceneMapper.createPositionScene(positionSceneDomain);
        }else{
            flag = iPositionSceneMapper.updatePositionScene(positionSceneDomain);
        }

        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("位置场景数据库操作异常");
        }
        return result;
    }

    /**
     * 根据ID删除位置场景
     * @param id 主键ID
     * @return
     */
    public ServiceResult deletePositionSceneById(int id)
    {
        ServiceResult result = new ServiceResult();
        int flag = iPositionSceneMapper.deletePositionSceneById(id);
        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("数据库删除操作异常");
        }

        return result;
    }

    /**
     * 根据主键ID查询位置场景
     * @param positionSceneId 主键ID
     * @return
     */
    public PositionSceneDomain queryPositionSceneById(Integer positionSceneId){
        PositionSceneDomain positionSceneDomain = iPositionSceneMapper.queryPositionSceneById(positionSceneId);
        return positionSceneDomain;
    }

    /**
     * 查询位置场景
     * @return
     */
    public List<Map<String,String>> queryPositionSceneType(){
        return iPositionSceneMapper.queryPositionSceneType();
    }

    /**
     * 查询场景导航
     * @return
     */
    public List<Map<String,String>> queryScenePilotType(){
        return iPositionSceneMapper.queryScenePilotType();
    }


    /**
     * 查询场景导航优先级
     *
     * @return initNUm  初始化优先级
     *          minNum   当前可用最大优先级
     *          maxNum  本次显示最小可用优先级
     *          list    所有已被占用的优先级
     */
    public Map<String, Object> queryPriorityLevel(Integer tid)
    {
        // 查询出已经被占用的优先级
        List<Integer> list = iPositionSceneMapper.queryPriorityLevel();
        // 定义1-100的默认优先级集合
        List<Integer> allLevel = new ArrayList<Integer>();
        for (int i = 1; i <= 100; i++)
        {
            allLevel.add(i);
        }
        // 已占用的优先级集合与所有优先级取差集
        boolean ret = allLevel.removeAll(list);
        // 每次前台显示的是前20个可用优先级
        Integer minNum = allLevel.get(0);
        Integer maxNum = allLevel.get(19);
        Integer initNum = -1;
        if (tid == 0)
        {
            //新增
            initNum = minNum;
        }
        if (tid > 0)
        {
            //修改,查询出当前任务的优先级
            initNum = iPositionSceneMapper.queryPriorityLevelById(tid);
            list.remove(initNum);
            //查不到当前任务的优先级则初始化设置
            if(initNum == null || initNum == -1){
                initNum = minNum;
            }
            if(minNum>initNum){
                minNum = initNum;
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("minNum", minNum);// 当前可用最大优先级
        map.put("initNum", initNum);//初始化显示的优先级
        map.put("maxNum", maxNum);// 本次显示最小可用优先级
        map.put("skipNum", list);// 所有已被占用的优先级
        return map;
    }
}
