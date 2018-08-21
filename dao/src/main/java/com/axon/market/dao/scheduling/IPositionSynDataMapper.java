package com.axon.market.dao.scheduling;

import com.axon.market.common.domain.iscene.PositionBaseDomain;
import com.axon.market.common.domain.isystem.PositionDataSynDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 位置场景数据同步至目标库DAo
 * Created by zengcr on 2016/12/8.
 */
@Service("positionSynDataDao")
public interface IPositionSynDataMapper extends IMyBatisMapper
{
    //创建目标表
    int createPhoneTable(@Param(value = "tableName") String tableName);

    //同步号码至目标库
    int syncPhone(@Param(value = "tableName") String tableName, @Param(value = "dataList") List<String> dataList);
    int syncPhone2(@Param(value = "tableName") String tableName, @Param(value = "dataList") List<Map<String,Object>> dataList);

    String getConfSegmentId(@Param(value = "areaCode") String areaCode);

    int insertPTask(PositionDataSynDomain positionDataSynDomain);

    int updatePTask(PositionDataSynDomain positionDataSynDomain);

    int deleteSynData(@Param(value = "pTaskId") Integer pTaskId);

    int deletePTask(@Param(value = "pTaskIds") String pTaskIds);

    int pauseSynData(@Param(value = "pTaskId") Integer pTaskId);

    int resumeSynData(@Param(value = "pTaskId") Integer pTaskId);

    //根据ID删除conf_base_info
    int deleteConfBaseInfoById(@Param(value = "baseIds") String baseIds);
    //同步conf_base_info
    int syncConfBaseInfo(@Param(value = "shopList") List<PositionBaseDomain> shopList);

}
