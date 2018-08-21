package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.TaskForUserBlackListDomain;
import com.axon.market.common.domain.iscene.UserBlackListDomain;
import com.axon.market.common.domain.ischeduling.MarketJobDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wangtt on 2017/3/6.
 */
@Component("userBlackListDao")
public interface IUserBlackListMapper extends IMyBatisMapper
{
    //查询用户黑名单列表
    List<UserBlackListDomain> queryUserBlackList(@Param("offset") Integer offset,
                                                 @Param("limit") Integer limit,
                                                 @Param("mob") String mobile);

    //查询分页数
    int queryUserBlackCount(@Param("mob") String mob);

    //新增
    int addUserBlackList(@Param("domain") UserBlackListDomain domain);

    //查询屏蔽任务
    List<TaskForUserBlackListDomain> queryTaskForUserBlackList(@Param("offset") Integer offset,
                                                               @Param("limit") Integer limit,
                                                               @Param("taskName") String taskName);
    //分页查询任务列表数
    int queryTaskCount(@Param("taskName") String taskName);

    //查询所有的场景营销任务名
    List<MarketJobDomain> queryTaskName();

    //删除用户黑名单
    int delUserBlackList(@Param("id") Integer id);

    //根据ID查询手机号
    String queryMobById(@Param("id") Integer id);
}
