package com.axon.market.dao.mapper.isystem;

import com.axon.market.common.domain.isystem.TagAuditHistoryDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by chenyu on 2016/5/25.
 */
@Component("tagAuditHistoryDao")
public interface ITagAuditHistoryMapper extends IMyBatisMapper
{
    /**
     * @param tagAuditHistoryDomain
     * @return
     */
    int insertTagAuditHistory(@Param(value = "info") TagAuditHistoryDomain tagAuditHistoryDomain);

    /**
     * @param tagId
     * @return
     */
    List<TagAuditHistoryDomain> queryTagAuditHistoryDomain(@Param(value = "tagId") int tagId);

    /**
     * @param tagId
     * @return
     */
    int deleteTagAuditHistory(@Param(value = "tagId") int tagId);

    /**
     * @param tagId
     * @return
     */
    List<TagAuditHistoryDomain> queryTagAuditProgress(@Param(value = "tagId") int tagId);

    /**
     * @param userId
     * @return
     */
    List<Map<String, Object>> queryAllTagAuditUser(@Param(value = "userId") int userId);
}
