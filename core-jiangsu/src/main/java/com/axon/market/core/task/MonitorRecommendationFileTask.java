package com.axon.market.core.task;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.cache.RedisCache;
import com.axon.market.common.cache.impl.ShopTaskLock;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.recommendation.task.GetRecommendationFileFactory;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.itag.RemoteServerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.List;

/**
 * Created by chenyu on 2017/3/12.
 */
public class MonitorRecommendationFileTask extends RunJob
{
    private static final Logger LOG = Logger.getLogger(MonitorRecommendationFileTask.class.getName());

    private GetRecommendationFileFactory factory = new GetRecommendationFileFactory();

    private RemoteServerService remoteServerService = RemoteServerService.getInstance();

    private FileOperateService fileOperateService = FileOperateService.getInstance();

    private RecommendationConfigBean recommendationConfigBean = RecommendationConfigBean.getInstance();

    private RedisCache redisCache = RedisCache.getInstance();

    @Override
    public void runBody()
    {
        RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getServerHost(), recommendationConfigBean.getServerUser(), EncryptUtil.encryption(recommendationConfigBean.getServerPassword(), "market"), recommendationConfigBean.getServerPort(), recommendationConfigBean.getServerConnectType());

        List<String> fileNames = fileOperateService.getAllFileByPath(remoteServerDomain, "/");
        LOG.info("task:recommendation_file_task fileNames.size()="+fileNames.size());

        if (CollectionUtils.isNotEmpty(fileNames))
        {
            Calendar calendar = Calendar.getInstance();
            String taskTime = TimeUtil.formatDateToYMD(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String userTime = TimeUtil.formatDateToYMD(calendar.getTime());
            for (String fileName : fileNames)
            {
                ShopTaskLock shopTaskLock = new ShopTaskLock(fileName);
                if (redisCache.doAction(shopTaskLock))
                {
                    if (fileName.contains("AZ1101" + taskTime))
                    {
                        //炒店任务
                        LOG.info("Get Recommendation shop Task File : " + fileName);
                        factory.executor("task").execute(fileName);
                    }
                    else if (fileName.contains("AZ1102" + userTime))
                    {
                        //炒店用户
                        LOG.info("Get Recommendation shop User File : " + fileName);
                        factory.executor("user").execute(fileName);
                    }
                    else if(fileName.contains("AZ1103" + taskTime))
                    {
                        //炒店渠道
                        LOG.info("Get Recommendation shop channel File : " + fileName);
                        factory.executor("shopChannel").execute(fileName);
                    }
                    else if(fileName.contains("AZ0102" + userTime))
                    {
                        //智能云用户
                        LOG.info("Get Recommendation icloud User File : " + fileName);
                        factory.executor("icloudUser").execute(fileName);
                    }
                    else if(fileName.contains("AZ0103" + userTime))
                    {
                        //智能云活动成功用户
                        LOG.info("Get Recommendation icloud success User File : " + fileName);
                        factory.executor("icloudSuccessUser").execute(fileName);
                    }
                    else if(fileName.contains("AZ0105" + taskTime))
                    {
                        //低消活动配置信息
                        LOG.info("Get dixiao configuration File : " + fileName);
                        factory.executor("dixiao_config").execute(fileName);
                    }
                    else if(fileName.contains("AZ0106" + userTime))
                    {
                        //低消活动用户信息
                        LOG.info("Get dixiao user File : " + fileName);
                        factory.executor("dixiao_user").execute(fileName);
                    }
                }
            }
        }
    }
}
