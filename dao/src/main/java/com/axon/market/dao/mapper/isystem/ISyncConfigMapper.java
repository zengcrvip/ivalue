package com.axon.market.dao.mapper.isystem;

import com.axon.market.common.domain.isystem.MonitorConfigDomain;
import com.axon.market.common.domain.isystem.SyncConfigDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuan on 2017/4/24.
 */
@Component("syncConfigDao")
public interface ISyncConfigMapper extends IMyBatisMapper
{
    /**
     * @return
     */
    Integer querySyncConfigCounts(@Param(value = "queryType") String queryType);

    /**
     * @param limit
     * @param offset
     * @return
     */
    List<SyncConfigDomain> querySyncConfig(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit,@Param(value = "queryType") String queryType);

    /**
     * @param id
     * @return
     */
    List<SyncConfigDomain> querySyncById(@Param(value = "id") Integer id);

    /**
     * 新增模型
     *
     * @param info 同步配置model
     * @return
     */
    Integer addSync(@Param(value = "info") SyncConfigDomain info);

    /**
     * 更新模型
     *
     * @param info 同步配置model
     * @return
     */
    Integer editSync(@Param(value = "info") SyncConfigDomain info);

    /**
     * 删除
     * @param id
     * @return
     */
    Integer deleteSync(@Param(value = "id") Integer id);
}
