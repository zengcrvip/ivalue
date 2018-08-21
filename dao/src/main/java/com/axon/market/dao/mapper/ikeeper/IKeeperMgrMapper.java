package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/4/24.
 */
@Component("keeperMgrDao")
public interface IKeeperMgrMapper extends IMyBatisMapper
{
    List<Map<String,Object>> fetchActivities();

    /**
     * 查询某时间段内佣金费用
     * @param userDomain
     * @param startDate
     * @param endDate
     * @return
     */
    Map<String,Object> fetchFee(@Param(value = "user") UserDomain userDomain, @Param(value = "startDate") String startDate,@Param(value = "endDate") String endDate,@Param(value = "orgType") Integer orgType,@Param(value = "orgCode") String orgCode,@Param(value = "activityId") Integer activityId);

    /**
     * 查询排名
     * @param userDomain
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String,Object>> fetchAreaRank(@Param(value = "user") UserDomain userDomain, @Param(value = "startDate") String startDate,@Param(value = "endDate") String endDate,@Param(value = "activityId") Integer activityId);

    /**
     * 查询营业厅排名
     * @param userDomain
     * @param startDate
     * @param endDate
     * @param activityId
     * @return
     */
    List<Map<String,Object>> fetchChannelRank(@Param(value = "user") UserDomain userDomain, @Param(value = "startDate") String startDate,@Param(value = "endDate") String endDate,@Param(value = "areaCode") Integer areaCode,@Param(value = "activityId") Integer activityId);

}
