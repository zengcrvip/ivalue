package com.axon.market.core.recommendation.task.impl;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.ishop.ShopTaskChannelDomain;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.core.recommendation.task.GetRecommendationFileExecutor;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.core.service.itag.RemoteServerService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *炒店个性化任务渠道处理
 * Created by zengcr on 2017/6/15.
 */
@Service("recommendationTaskChannelExecutor")
public class RecommendationTaskChannelExecutor implements GetRecommendationFileExecutor
{
    private static final Logger LOG = Logger.getLogger(RecommendationTaskChannelExecutor.class.getName());

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @Autowired
    @Qualifier("shopTaskService")
    private ShopTaskService shopTaskService;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("recommendationConfigBean")
    private RecommendationConfigBean recommendationConfigBean;

    private byte[] columnByte = {0x01};

    private String columnSeparator = new String(columnByte);

    @Override
    public void execute(String filePath)
    {
        File file = null;
        try
        {
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getServerHost(), recommendationConfigBean.getServerUser(), EncryptUtil.encryption(recommendationConfigBean.getServerPassword(), "market"), recommendationConfigBean.getServerPort(), recommendationConfigBean.getServerConnectType());
            String targetFileName = systemConfigBean.getChannelFileLocalPath() + "shopChannel";

            if (fileOperateService.downloadFile(remoteServerDomain, filePath, targetFileName))
            {
                LOG.info("shop channel downloadFile bigin");
                file = new File(targetFileName);
                LineIterator iterator = FileUtils.lineIterator(file, "GBK");
                List<ShopTaskChannelDomain> dataList = new ArrayList<ShopTaskChannelDomain>();
                while (iterator.hasNext())
                {
                    String line = iterator.next();
                    LOG.info("shop channel line"+line);
                    String[] taskColumn = line.split(columnSeparator);
                    if (taskColumn != null && taskColumn.length == 4)
                    {
                        ShopTaskChannelDomain taskChannelDomain = new ShopTaskChannelDomain();
                        taskChannelDomain.setSaleId(taskColumn[0]);
                        taskChannelDomain.setSaleBoidId(taskColumn[1]);
                        taskChannelDomain.setAimSubId(taskColumn[2]);
                        taskChannelDomain.setDepartId(taskColumn[3]);
                        taskChannelDomain.setFileName(filePath);
                        dataList.add(taskChannelDomain);
                    }
                }
                if(dataList != null && dataList.size() > 0)
                {
                    shopTaskService.insertShopTaskChannel(dataList);
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Get Personalized Recommendation shop channel Error. ", e);
        }
        finally
        {
            if (file != null)
            {
                FileUtils.deleteQuietly(file);
            }
        }

    }
}
