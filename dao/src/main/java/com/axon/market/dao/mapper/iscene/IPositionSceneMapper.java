package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.MarketAreaDomain;
import com.axon.market.common.domain.iscene.PositionSceneDomain;
import com.axon.market.dao.base.IMyBatisMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 位置场景DAO
 * Created by zengcr on 2016/11/28.
 */
@Component("positionSceneDao")
public interface IPositionSceneMapper extends IMyBatisMapper
{
    /**
     * 位置场景分页总数
     *
     * @param positionScenName 位置场景名称
     * @param positionScenStau 位置场景状态
     * @return 位置场景总数量
     */
    int queryPositionSceneTotal(@Param(value = "positionScenName") String positionScenName, @Param(value = "positionScenStau") String positionScenStau);

    /**
     * 位置场景分页展示列表
     *
     * @param offset           每次查询的数量
     * @param limit            开始标记位
     * @param positionScenName 位置场景名称
     * @param positionScenStau 位置场景状态
     * @return
     */
    List<PositionSceneDomain> queryPositionSceneByPage(@Param(value = "offset") int offset, @Param(value = "limit") int limit, @Param(value = "positionScenName") String positionScenName, @Param(value = "positionScenStau") String positionScenStau);

    /**
     * 查询位置场景类别
     *
     * @return
     */
    List<Map<String, String>> queryPositonSenceType();

    /**
     * 查询基站类型
     *
     * @return
     */
    List<Map<String, String>> queryBaseAreaType();

    /**
     * 查询基站地区
     *
     * @return
     */
    List<Map<String, String>> queryBaseAreas(Map<String, Object> parasMap);

    /**
     * 查询位置场景查询基站站点总数
     * baseAreaId 地区编码ID,baseTypeId 基站类型ID,baseId 基站ID,baseName 基站名称
     *
     * @return 基站站点总数
     */
    int queryBasesTotal(Map<String, Object> parasMap);

    /**
     * 查询位置场景查询基站站点分页展示
     * offset 每次查询的数量,limit  起始标记位,baseAreaId 地区编码ID,baseTypeId 基站类型ID,baseId 基站ID,baseName 基站名称
     *
     * @return 基站站点列表
     */
    List<Map<String, String>> queryBasesByPage(Map<String, Object> parasMap);

    /**
     * 查询位置场景查询基站站点分页展示
     * baseAreaId 地区编码ID,baseTypeId 基站类型ID,baseId 基站ID,baseName 基站名称
     *
     * @return 基站站点列表
     */
    List<Map<String, String>> queryBases(Map<String, Object> parasMap);

    /**
     * 查询某一个区域的所有营业厅
     * @param areaCode
     * @param businessHallIds
     * @return
     */
    List<Map<String,Object>> queryAllBusinessHallsUnderArea(@Param(value = "areaCode") Integer areaCode,@Param(value = "targetUser") Integer targetUser,@Param(value = "businessHallIds") String businessHallIds);

    /**
     * 查询场景位置号段地区树结构
     *
     * @param areaIds 已选中的地区ID
     * @return
     */
    List<MarketAreaDomain> queryPositionBaseAreas(@Param(value = "areaIds") String areaIds, @Param(value = "areaCode") Integer areaCode, @Param(value = "businessCodes") String businessCodes);

    /**
     * 新建位置场景
     *
     * @param positionSceneDomain 位置场景对象
     * @return
     */
    int createPositionScene(PositionSceneDomain positionSceneDomain);

    /**
     * 修改位置场景
     *
     * @param positionSceneDomain 位置场景对象
     * @return
     */
    int updatePositionScene(PositionSceneDomain positionSceneDomain);

    /**
     * 根据ID删除位置场景
     *
     * @param id 主键ID
     * @return
     */
    int deletePositionSceneById(@Param(value = "id") int id);

    /**
     * 根据主键ID查询位置场景
     *
     * @param positionSceneId 主键ID
     * @return
     */
    PositionSceneDomain queryPositionSceneById(@Param(value = "positionSceneId") Integer positionSceneId);

    /**
     * 查询位置场景
     *
     * @return
     */
    List<Map<String, String>> queryPositionSceneType();

    /**
     * 查询场景导航
     *
     * @return
     */
    List<Map<String, String>> queryScenePilotType();

    /**
     * 查询被占用的优先级
     *
     * @return
     */
    List<Integer> queryPriorityLevel();

    /**
     * 根据ID查询当前场景营销任务的优先级
     *
     * @return
     */
    Integer queryPriorityLevelById(@Param("tid") Integer tid);


}