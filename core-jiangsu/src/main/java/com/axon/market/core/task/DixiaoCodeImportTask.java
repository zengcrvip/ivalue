package com.axon.market.core.task;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.cache.RedisCache;
import com.axon.market.common.cache.impl.ShopTaskLock;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.timer.RunJob;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.itag.RemoteServerService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Zhuwen on 2017/7/27.
 */
public class DixiaoCodeImportTask extends RunJob {
    private static final Logger LOG = Logger.getLogger(DixiaoCodeImportTask.class.getName());

    private RemoteServerService remoteServerService = RemoteServerService.getInstance();
    private RecommendationConfigBean recommendationConfigBean = RecommendationConfigBean.getInstance();
    private FileOperateService fileOperateService = FileOperateService.getInstance();
    private RedisCache redisCache = RedisCache.getInstance();
    private GetDixiaoFileFactory factory = new GetDixiaoFileFactory();

    @Override
    public void runBody()
    {
        RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getVoiceplusServerHost(), recommendationConfigBean.getVoiceplusServerUser(), EncryptUtil.encryption(recommendationConfigBean.getVoiceplusServerPassword(), "market"), recommendationConfigBean.getVoiceplusServerPort(), recommendationConfigBean.getVoiceplusServerConnectType());

        List<String> fileNames = fileOperateService.getCodeFileByPath(remoteServerDomain, "/");
        LOG.info("task:dixiao_code_import_task fileNames.size()="+fileNames.size());

        if (CollectionUtils.isNotEmpty(fileNames))
        {
            Calendar calendar = Calendar.getInstance();
            String taskTime = TimeUtil.formatDateToYMD(calendar.getTime());
            for (String fileName : fileNames)
            {
                ShopTaskLock shopTaskLock = new ShopTaskLock(fileName);
                if (redisCache.doAction(shopTaskLock))
                {
                    if (fileName.contains("dixiaoqudao" + taskTime))
                    {
                        //低消渠道编码文件
                        LOG.info("Get dixiao qudao Task File : " + fileName);
                        factory.executor("dixiao_qudao").execute(fileName);
                    }
                    else if(fileName.contains("dixiaopartner" + taskTime))
                    {
                        //低消合作伙伴编码文件
                        LOG.info("Get dixiao partner code File : " + fileName);
                        factory.executor("dixiao_partner").execute(fileName);
                    }
                }
            }
        }
    }
}
