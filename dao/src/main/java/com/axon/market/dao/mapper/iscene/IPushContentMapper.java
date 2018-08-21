package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.PushConfigDomain;
import com.axon.market.common.domain.iscene.PushContentDomain;
import com.axon.market.common.domain.iscene.PushDomain;
import com.axon.market.common.domain.iscene.TaskDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuan on 2016/12/9.
 */
@Component("pushContentDao")
public interface IPushContentMapper extends IMyBatisMapper
{
    List<PushContentDomain> getPushList(@Param(value = "kind") String kind, @Param(value = "name") String name, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    Integer getPushListCount(@Param(value = "kind") String kind, @Param(value = "name") String name);

    List<TaskDomain> getTaskList(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    Integer getTaskListCount();

    Integer addPushContent(@Param(value = "info") PushDomain info);

    Integer editPushContent(@Param(value = "info") PushDomain info);

    Integer deletePush(@Param(value = "id") int id);

    List<PushConfigDomain> getSelectPushConfig();

    List<PushContentDomain> getContent(@Param(value = "id") int id);
}