package com.axon.market.core.recommendation.task.impl;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.core.recommendation.task.GetRecommendationFileExecutor;
import com.axon.market.core.service.greenplum.OperateFileDataToGreenPlum;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.itag.RemoteServerService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

/**
 * 智能云活动成功用户
 * Created by zengcr on 2017/6/9.
 */
@Service("icloudSuccessUserFileExecutor")
public class IcloudSuccessUserFileExecutor implements GetRecommendationFileExecutor
{
    private static final Logger LOG = Logger.getLogger(RecommendationUserFileExecutor.class.getName());

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @Autowired
    @Qualifier("operateFileDataToGreenPlum")
    private OperateFileDataToGreenPlum operateFileDataToGreenPlum;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("recommendationConfigBean")
    private RecommendationConfigBean recommendationConfigBean;

    @Override
    public void execute(String filePath)
    {
        File file = null;
        File directory = null;
        try
        {
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getServerHost(), recommendationConfigBean.getServerUser(), EncryptUtil.encryption(recommendationConfigBean.getServerPassword(), "market"), recommendationConfigBean.getServerPort(), recommendationConfigBean.getServerConnectType());
            LOG.info("icloud success user copyFile bigin");
            directory = new File(systemConfigBean.getMonitorPath() + File.separator + UUID.randomUUID().toString());
            if (!directory.exists())
            {
                directory.mkdir();
            }

            String targetFileName = directory.getPath() + File.separator + "@shop.icloud_user_success";
            if (fileOperateService.downloadFile(remoteServerDomain, filePath, targetFileName))
            {
                file = new File(targetFileName);
                File greenPlumFile = new File(file.getParent() + File.separator + targetFileName.substring(targetFileName.indexOf('@') + 1));
                LineIterator iterator = FileUtils.lineIterator(file);
                operateFileDataToGreenPlum.dataRefresh(iterator, greenPlumFile, 5);
            }
        }
        catch (Exception e)
        {
            LOG.error("Get Personalized icloud success user Error. ", e);
        }
        finally
        {
            try
            {
                if (file != null)
                {
                    FileUtils.deleteQuietly(file);
                }
                if (directory != null)
                {
                    FileUtils.deleteDirectory(directory);
                }
            }
            catch (Exception e)
            {
                LOG.error("Delete Directory Error. ", e);
            }
        }
    }
}
