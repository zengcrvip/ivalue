package com.axon.market.core.service.iservice;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.EncryptUtil;
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
 * Created by chenyu on 2017/3/13.
 */
@Service("recommendationFileService")
public class RecommendationFileService
{
    private static final Logger LOG = Logger.getLogger(RecommendationFileService.class.getName());

    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    @Autowired
    @Qualifier("operateFileDataToGreenPlum")
    private OperateFileDataToGreenPlum operateFileDataToGreenPlum;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("recommendationConfigBean")
    private RecommendationConfigBean recommendationConfigBean;

    public ServiceResult copyFileToBaseUser(String fileName)
    {
        ServiceResult result = new ServiceResult();
        File file = null;
        File directory = null;
        try
        {
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getiCloudServerHost(), recommendationConfigBean.getiCloudServerUser(), EncryptUtil.encryption(recommendationConfigBean.getiCloudServerPassword(), "market"), recommendationConfigBean.getiCloudServerPort(), recommendationConfigBean.getiCloudServerConnectType());

            directory = new File(systemConfigBean.getMonitorPath() + File.separator + UUID.randomUUID().toString());
            if (!directory.exists())
            {
                directory.mkdir();
            }
            String targetFileName = directory.getPath() + File.separator + "@shop.shop_target";
            if (fileOperateService.downloadFile(remoteServerDomain, "/apps/location/" + fileName, targetFileName))
            {
                file = new File(targetFileName);
                File greenPlumFile = new File(file.getParent() + File.separator + targetFileName.substring(targetFileName.indexOf('@') + 1) + "@" + UUID.randomUUID().toString());
                LineIterator iterator = FileUtils.lineIterator(file);
                operateFileDataToGreenPlum.dataRefresh(iterator, greenPlumFile, null);
            }
            else
            {
                result.setRetValue(-1);
                result.setDesc("Download file error");
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

        return result;
    }
}
