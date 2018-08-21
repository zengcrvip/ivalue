package com.axon.market.core.service.isystem;

import com.axon.market.common.domain.isystem.TagAuditHistoryDomain;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.dao.mapper.isystem.ITagAuditHistoryMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("tagAuditHistoryService")
public class TagAuditHistoryService
{
    private static final Logger LOG = Logger.getLogger(TagAuditHistoryService.class.getName());

    @Autowired
    @Qualifier("tagAuditHistoryDao")
    private ITagAuditHistoryMapper tagAuditHistoryDao;

    /**
     * @return
     */
    public static TagAuditHistoryService getInstance()
    {
        return (TagAuditHistoryService) SpringUtil.getSingletonBean("marketJobAuditHistoryDao");
    }

    /**
     * @param tagJobAuditHistory
     * @return
     */
    public int insertTagAuditHistory(TagAuditHistoryDomain tagJobAuditHistory)
    {
        return tagAuditHistoryDao.insertTagAuditHistory(tagJobAuditHistory);
    }

    /**
     * @param segmentId
     * @return
     */
    public List<TagAuditHistoryDomain> queryTagAuditHistoryDomain(int segmentId)
    {
        return tagAuditHistoryDao.queryTagAuditHistoryDomain(segmentId);
    }

    /**
     * @param tagId
     * @return
     */
    public int deleteTagAuditHistory(int tagId)
    {
        return tagAuditHistoryDao.deleteTagAuditHistory(tagId);
    }


    /**
     * @param tagId
     * @return
     */
    public List<TagAuditHistoryDomain> queryTagAuditProgress(int tagId)
    {
        return tagAuditHistoryDao.queryTagAuditProgress(tagId);
    }

    /**
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryAllTagAuditUser(int userId)
    {
        return tagAuditHistoryDao.queryAllTagAuditUser(userId);
    }
}
