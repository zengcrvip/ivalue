package com.axon.market.core.recommendation.task.impl;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.recommendation.task.GetRecommendationFileExecutor;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 炒店个性化用户文件处理
 * Created by zengcr on 2017/3/7.
 */
@Service("recommendationUserFileExecutor")
public class RecommendationUserFileExecutor implements GetRecommendationFileExecutor
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
        File monitorFile = null;
        try
        {
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getServerHost(), recommendationConfigBean.getServerUser(), EncryptUtil.encryption(recommendationConfigBean.getServerPassword(), "market"), recommendationConfigBean.getServerPort(), recommendationConfigBean.getServerConnectType());
            LOG.info("shop task user copyFile bigin");
            directory = new File(systemConfigBean.getMonitorPath() + File.separator + UUID.randomUUID().toString());
            if (!directory.exists())
            {
                directory.mkdir();
            }

            String targetFileName = directory.getPath() + File.separator + "@shop.shop_user";
            if (fileOperateService.downloadFile(remoteServerDomain, filePath, targetFileName))
            {
                file = new File(targetFileName);
                File greenPlumFile = new File(file.getParent() + File.separator + targetFileName.substring(targetFileName.indexOf('@') + 1));
                LineIterator iterator = FileUtils.lineIterator(file);
                operateFileDataToGreenPlum.dataRefresh(iterator, greenPlumFile, 5);

                targetFileName = file.getParent() + File.separator + "@shop.shop_monitor_user";
                monitorFile = new File(targetFileName);
                file.renameTo(monitorFile);
                greenPlumFile = new File(monitorFile.getParent() + File.separator + targetFileName.substring(targetFileName.indexOf('@') + 1));
                iterator = FileUtils.lineIterator(monitorFile);
                operateFileDataToGreenPlum.dataRefresh(iterator, greenPlumFile, null);
            }
        }
        catch (Exception e)
        {
            LOG.error("Get Personalized Recommendation Task Error. ", e);
        }
        finally
        {
            try
            {
                if (file != null)
                {
                    FileUtils.deleteQuietly(file);
                }
                if (monitorFile != null)
                {
                    FileUtils.deleteQuietly(monitorFile);
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
