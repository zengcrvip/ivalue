package com.axon.market.core.recommendation.task;

import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.recommendation.task.impl.*;

/**
 * Created by chenyu on 2017/3/12.
 */
public class GetRecommendationFileFactory
{
    public GetRecommendationFileExecutor executor(String type)
    {
        switch (type)
        {
            case "task":
            {
                return (RecommendationTaskFileExecutor) SpringUtil.getSingletonBean("recommendationTaskFileExecutor");
            }
            case "user":
            {
                return (RecommendationUserFileExecutor) SpringUtil.getSingletonBean("recommendationUserFileExecutor");
            }
            case "shopChannel":
            {
                return (RecommendationTaskChannelExecutor) SpringUtil.getSingletonBean("recommendationTaskChannelExecutor");
            }
            case "icloudUser":
            {
                return (IcloudUserFileExecutor)SpringUtil.getSingletonBean("icloudUserFileExecutor");
            }
            case "icloudSuccessUser":
            {
                return  (IcloudSuccessUserFileExecutor)SpringUtil.getSingletonBean("icloudSuccessUserFileExecutor");
            }
            case "dixiao_config":
            {
                return  (DixiaoConfigFileExecutor)SpringUtil.getSingletonBean("dixiaoConfigFileExecutor");
            }
            case "dixiao_user":
            {
                return  (DixiaoUserFileExecutor)SpringUtil.getSingletonBean("dixiaoUserFileExecutor");
            }
            default:
            {
                return null;
            }
        }
    }
}
