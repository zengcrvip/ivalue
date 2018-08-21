package com.axon.market.core.service.isystem;

import com.axon.market.common.domain.isystem.ModelAuditHistoryDomain;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.dao.mapper.isystem.IModelAuditHistoryMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by chenyu on 2016/5/25.
 */
@Component("modelAuditHistoryService")
public class ModelAuditHistoryService
{
    private static final Logger LOG = Logger.getLogger(ModelAuditHistoryService.class.getName());

    @Autowired
    @Qualifier("modelAuditHistoryDao")
    private IModelAuditHistoryMapper modelAuditHistoryDao;

    /**
     * @return
     */
    public static ModelAuditHistoryService getInstance()
    {
        return (ModelAuditHistoryService) SpringUtil.getSingletonBean("modelAuditHistoryService");
    }

    /**
     * @param modelAuditHistoryDomain
     * @return
     */
    public int insertModelAuditHistory(ModelAuditHistoryDomain modelAuditHistoryDomain)
    {
        return modelAuditHistoryDao.insertModelAuditHistory(modelAuditHistoryDomain);
    }

    /**
     * @param modelId
     * @return
     */
    public List<ModelAuditHistoryDomain> queryModelAuditHistoryDomain(int modelId)
    {
        return modelAuditHistoryDao.queryModelAuditHistoryDomain(modelId);
    }

    /**
     * @param modelId
     * @return
     */
    public int deleteModelAuditHistory(int modelId)
    {
        return modelAuditHistoryDao.deleteModelAuditHistoryDomain(modelId);
    }

    /**
     * @param modelId
     * @return
     */
    public List<ModelAuditHistoryDomain> queryModelAuditProgress(int modelId)
    {
        return modelAuditHistoryDao.queryModelAuditProgress(modelId);
    }

    /**
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryAllModelAuditUser(int userId)
    {
        return modelAuditHistoryDao.queryAllModelAuditUser(userId);
    }
}
