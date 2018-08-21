package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.isystem.PositionDataSynDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 位置场景信息同步-本地
 * Created by zengcr on 2017/2/8.
 */
@Service("positionLocalSynDataDao")
public interface IPositionLocalSynDataMapper extends IMyBatisMapper
{
    //创建目标表
    int createPhoneTable(@Param(value = "tableName") String tableName);

    //同步号码至目标库
    int syncPhone(@Param(value = "tableName") String tableName, @Param(value = "dataList") List<String> dataList);

    String getConfSegmentId(@Param(value = "areaCode") String areaCode);

    int insertPTask(PositionDataSynDomain positionDataSynDomain);

    int updatePTask(PositionDataSynDomain positionDataSynDomain);

    int deleteSynData(@Param(value = "pTaskId") Integer pTaskId);

    int pauseSynData(@Param(value = "pTaskId") Integer pTaskId);

    int resumeSynData(@Param(value = "pTaskId") Integer pTaskId);

}
