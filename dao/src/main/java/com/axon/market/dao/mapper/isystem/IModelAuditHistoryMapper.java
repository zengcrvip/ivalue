package com.axon.market.dao.mapper.isystem;

import com.axon.market.common.domain.isystem.ModelAuditHistoryDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by chenyu on 2016/5/25.
 */
@Component("modelAuditHistoryDao")
public interface IModelAuditHistoryMapper extends IMyBatisMapper
{
    /**
     * @param modelAuditHistoryDomain
     * @return
     */
    int insertModelAuditHistory(@Param(value = "info") ModelAuditHistoryDomain modelAuditHistoryDomain);

    /**
     * @param modelId
     * @return
     */
    List<ModelAuditHistoryDomain> queryModelAuditHistoryDomain(@Param(value = "modelId") int modelId);

    /**
     * @param modelId
     * @return
     */
    int deleteModelAuditHistoryDomain(@Param(value = "modelId") int modelId);

    /**
     * @param modelId
     * @return
     */
    List<ModelAuditHistoryDomain> queryModelAuditProgress(@Param(value = "modelId") int modelId);

    /**
     * @param userId
     * @return
     */
    List<Map<String, Object>> queryAllModelAuditUser(@Param(value = "userId") int userId);
}
