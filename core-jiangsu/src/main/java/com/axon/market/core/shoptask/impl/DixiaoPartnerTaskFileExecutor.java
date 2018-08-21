package com.axon.market.core.shoptask.impl;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.iconsumption.DixiaoCodeDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.EncryptUtil;
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
@Service("dixiaoPartnerTaskFileExecutor")
public class DixiaoPartnerTaskFileExecutor implements GetDixiaoFileExecutor {
    private static final Logger LOG = Logger.getLogger(DixiaoPartnerTaskFileExecutor.class.getName());

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

    @Override
    public void execute(String filePath)
    {
        File file = null;
        try {
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getVoiceplusServerHost(), recommendationConfigBean.getVoiceplusServerUser(), EncryptUtil.encryption(recommendationConfigBean.getVoiceplusServerPassword(), "market"), recommendationConfigBean.getVoiceplusServerPort(), recommendationConfigBean.getVoiceplusServerConnectType());
            String targetFileName = systemConfigBean.getDixiaoFileLocalPath() + "@dixiao_partner";

            LOG.info(filePath+" dixiao partner downloadFile begin:");
            if (fileOperateService.downloadFile(remoteServerDomain, filePath, targetFileName)) {
                file = new File(targetFileName);
                LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");

                boolean isDel = false;
                List<DixiaoCodeDomain> list = new ArrayList();
                while (iterator.hasNext()) {
                    String line = iterator.next();
                    String[] taskColumn = line.split("\\|");
                    if (taskColumn != null && taskColumn.length == 2 ) {
                        if (!isDel){
                            dixiaoResultMapper.deletePartnerCode();
                            isDel = true;
                        }
                        DixiaoCodeDomain codeDomain = new DixiaoCodeDomain();
                        codeDomain.setCode(taskColumn[0]);
                        codeDomain.setName(taskColumn[1]);
                        list.add(codeDomain);
                    }else{
                        LOG.error("this line:" + line + " has error, the column length is:" + taskColumn.length + " ignore");
                    }
                }

                if (list.size()>0){
                    dixiaoResultMapper.insertPartnerCode(list);
                    dixiaoResultMapper.deleteInvalidPartner();
                }
            }
        }catch (Exception e) {
            LOG.error("Get dixiao partner file Task Error. ", e);
            return;
        }
        finally
        {
            if (file != null)
            {
                FileUtils.deleteQuietly(file);
            }
        }
        LOG.info(filePath+" dixiao partner downloadFile finish");
    }
}
