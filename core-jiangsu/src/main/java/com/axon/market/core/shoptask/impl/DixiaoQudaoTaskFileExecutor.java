package com.axon.market.core.shoptask.impl;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.iconsumption.DixiaoCodeDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.greenplum.OperateFileDataToGreenPlum;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.itag.RemoteServerService;
import com.axon.market.core.task.GetDixiaoFileExecutor;
import com.axon.market.dao.mapper.iconsumption.IDixiaoResultMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhuwen on 2017/7/27.
 */
@Service("dixiaoQudaoTaskFileExecutor")
public class DixiaoQudaoTaskFileExecutor implements GetDixiaoFileExecutor {
    private static final Logger LOG = Logger.getLogger(DixiaoQudaoTaskFileExecutor.class.getName());

    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @Autowired
    @Qualifier("recommendationConfigBean")
    private RecommendationConfigBean recommendationConfigBean;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    @Qualifier("DixiaoResultDao")
    @Autowired
    private IDixiaoResultMapper dixiaoResultMapper;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("operateFileDataToGreenPlum")
    private OperateFileDataToGreenPlum operateFileDataToGreenPlum;

    @Override
    public void execute(String filePath)
    {
        File file = null;
        try {
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getVoiceplusServerHost(), recommendationConfigBean.getVoiceplusServerUser(), EncryptUtil.encryption(recommendationConfigBean.getVoiceplusServerPassword(), "market"), recommendationConfigBean.getVoiceplusServerPort(), recommendationConfigBean.getVoiceplusServerConnectType());
            String targetFileName = systemConfigBean.getDixiaoFileLocalPath() + "@model.business_code_from_voiceplus";

            LOG.info(filePath+" dixiao qudao downloadFile begin:");
            if (fileOperateService.downloadFile(remoteServerDomain, filePath, targetFileName)) {
                greenPlumOperateService.truncateTable("model.business_code_from_voiceplus");
                file = new File(targetFileName);
                LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
                File greenPlumFile = new File(file.getParent() + File.separator + targetFileName.substring(targetFileName.indexOf('@') + 1));
                //operateFileDataToGreenPlum.dataRefresh(iterator, greenPlumFile, null);
                String column = "business_hall_code,business_name";
                operateFileDataToGreenPlum.dataRefreshPlus(iterator, greenPlumFile,null,"",column);
            }
        }catch (Exception e) {
            LOG.error("Get dixiao qudao file Task Error. ", e);
            return;
        }
        finally
        {
            if (file != null)
            {
                FileUtils.deleteQuietly(file);
            }
        }
        LOG.info(filePath+" dixiao qudao downloadFile finish");
    }
}
