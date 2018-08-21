package com.axon.market.core.schedule.job;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.MarketTimeUtils;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.rule.RuleNode;
import com.axon.market.core.service.icommon.RefreshDataService;
import com.axon.market.core.service.imodel.ModelService;
import com.axon.market.core.service.itag.TagService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by chenyu on 2016/6/20.
 */
@Component("refreshRuleModelJob")
@Scope("prototype")
public class RefreshRuleModelJob extends RunJob
{
    private static final Logger LOG = Logger.getLogger(RefreshRuleModelJob.class.getName());

    @Autowired
    @Qualifier("tagService")
    private TagService tagService;

    @Autowired
    @Qualifier("modelService")
    private ModelService modelService;

    @Autowired
    @Qualifier("refreshDataService")
    private RefreshDataService refreshDataService;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    private AtomicBoolean running = new AtomicBoolean(false);

    private ModelDomain modelDomain;

    public void setModelDomain(ModelDomain modelDomain)
    {
        this.modelDomain = modelDomain;
    }

    /**
     * @return
     */
    public static RefreshRuleModelJob getInstance()
    {
        return SpringUtil.getPrototypeBean(RefreshRuleModelJob.class);
    }

    @Override
    public void runBody()
    {
        if (!running.compareAndSet(false, true))
        {
            LOG.warn("Refresh Rule Model Job Is Running , Model : " + modelDomain.getName());
            return;
        }

        Integer modelId = modelDomain.getId();
        try
        {
            String rule = modelDomain.getRule();
            List<RuleNode> nodes = JsonUtil.stringToObject(rule, new TypeReference<List<RuleNode>>()
            {
            });
            Boolean isCanRefresh = isCanRefresh(modelId, nodes);
            if (isCanRefresh)
            {
                refreshDataService.refreshRuleModelData(modelDomain, nodes);
            }
        }
        catch (Exception e)
        {
            LOG.error("Refresh Rule Model Job Error. ", e);
        }
        finally
        {
            running.set(false);
        }
    }

    /**
     * @param modelId
     * @param nodes
     * @return
     * @throws ParseException
     */
    private Boolean isCanRefresh(Integer modelId, List<RuleNode> nodes) throws ParseException
    {
        Boolean isCanRefresh = true;
        // 系统是否设置了强制刷新
        if (!systemConfigBean.isForceRefreshRuleModel())
        {
            Date nowDate = new Date();
            Date time = MarketTimeUtils.formatDate(modelService.queryModelRefreshTimeByModelId(modelId));
            if (!MarketTimeUtils.formatDateToYMD(nowDate).equals(MarketTimeUtils.formatDateToYMD(time)))
            {
                for (RuleNode node : nodes)
                {
                    if (node.getNodeType().equals("data"))
                    {
                        Integer id = Integer.valueOf(node.getOperateParams().get("id"));
                        time = MarketTimeUtils.formatDate(tagService.queryTagRefreshTimeByTagId(id));
                        if (!MarketTimeUtils.formatDateToYMD(nowDate).equals(MarketTimeUtils.formatDateToYMD(time)))
                        {
                            isCanRefresh = false;
                            break;
                        }
                    }
                }
            }
            else
            {
                isCanRefresh = false;
            }
        }
        return isCanRefresh;
    }
}
