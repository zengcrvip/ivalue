package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.PushConfigDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuan on 2016/12/7.
 */
@Component("pushConfigDao")
public interface IPushConfigMapper extends IMyBatisMapper
{
    List<PushConfigDomain> queryPushConfig(@Param(value = "name") String name, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    Integer queryPushConfigCount(@Param(value = "name") String name);

    Integer addPushConfig(@Param(value = "info") PushConfigDomain info);

    Integer EditPushConfig(@Param(value = "info") PushConfigDomain info);

    Integer deletePushConfig(@Param(value = "id") int id);

}
