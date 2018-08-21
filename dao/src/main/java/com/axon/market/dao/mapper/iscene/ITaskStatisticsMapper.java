package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.StatsDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by xuan on 2017/4/6.
 */
@Component("taskStatisticsDao")
public interface ITaskStatisticsMapper  extends IMyBatisMapper
{
    List<StatsDomain> getStatsList(@Param(value = "taskName") String taskName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime,@Param(value = "type") Integer type, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    Integer getStatsListCount(@Param(value = "taskName") String taskName,@Param(value = "startTime") Date startTime,@Param(value = "endTime") Date endTime,@Param(value = "type") Integer type);
}
