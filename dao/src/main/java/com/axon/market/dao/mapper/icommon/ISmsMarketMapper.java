package com.axon.market.dao.mapper.icommon;

import com.axon.market.common.domain.icommon.market.JumpLinkDomain;
import com.axon.market.common.domain.icommon.market.PTaskDomain;
import com.axon.market.common.domain.icommon.market.PdrDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yuanfei on 2017/1/17.
 */
@Component("smsMarketMapper")
public interface ISmsMarketMapper extends IMyBatisMapper
{
    Integer createPTask(@Param(value = "info") PTaskDomain pTaskDomain);

    Integer updatePTask(@Param(value = "taskId") Integer taskId, @Param(value = "count") Integer count);

    Integer createPdr(@Param(value = "list") List<PdrDomain> list);

    int createJumpLink(List<JumpLinkDomain> jumpLinkList);
}
