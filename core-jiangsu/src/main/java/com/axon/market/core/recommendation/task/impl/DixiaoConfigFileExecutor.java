package com.axon.market.core.recommendation.task.impl;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.constant.iconsumption.DixiaoTaskStatusEnum;
import com.axon.market.common.domain.iconsumption.DixiaoTaskDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.recommendation.task.GetRecommendationFileExecutor;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.iconsumption.DixiaoTaskService;
import com.axon.market.core.service.ilog.LogReminderService;
import com.axon.market.core.service.itag.RemoteServerService;
import com.axon.market.dao.mapper.iconsumption.IDixiaoResultMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

/**
 * Created by zhuwen on 2017/7/4.
 */
@Service("dixiaoConfigFileExecutor")
public class DixiaoConfigFileExecutor implements GetRecommendationFileExecutor {
    private static final Logger LOG = Logger.getLogger(DixiaoConfigFileExecutor.class.getName());

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

    @Autowired
    @Qualifier("logReminderService")
    private LogReminderService logReminderService;

    @Qualifier("dixiaoTaskService")
    @Autowired
    private DixiaoTaskService dixiaoTaskService;

    @Qualifier("DixiaoResultDao")
    @Autowired
    private IDixiaoResultMapper dixiaoResultMapper;

    private byte[] columnByte = {0x01};
    private String columnSeparator = new String(columnByte);

    @Override
    public void execute(String filePath) {
        File file = null;
        final String configName = filePath.substring(filePath.lastIndexOf("/")+1);
        try {
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getServerHost(), recommendationConfigBean.getServerUser(), EncryptUtil.encryption(recommendationConfigBean.getServerPassword(), "market"), recommendationConfigBean.getServerPort(), recommendationConfigBean.getServerConnectType());
            String targetFileName = systemConfigBean.getDixiaoFileLocalPath() + configName;
            LOG.info("dixiao config downloadFile begin");
            if (fileOperateService.downloadFile(remoteServerDomain, filePath, targetFileName)) {
                file = new File(targetFileName);
                LineIterator iterator = FileUtils.lineIterator(file, "UTF-8");
                while (iterator.hasNext()) {
                    String line = iterator.next();
                    String[] taskColumn = line.split(columnSeparator);
                    if (taskColumn != null && taskColumn.length == 9 ) {
                        String saleid = taskColumn[0].trim();
                        String boid = taskColumn[4].trim();
                        LOG.info("dixiao task begin低消任务"+saleid);

                        //如果有saleid相同的任务，将原任务置为失效
                        DixiaoTaskDomain taskDomain = null;
                        taskDomain = dixiaoResultMapper.queryDixiaoTaskBySaleID(saleid);
                        if (taskDomain != null){
                            if (!taskDomain.getBoid().equals(boid)){
                                LOG.info("dixiao task will be overwritten, old saleid is"+saleid+" boid is"+taskDomain.getBoid());
                                dixiaoTaskService.setTaskInvalidBySaleID(saleid);
                            }else {
                                continue;
                            }
                        }

                        //根据文件内容构造任务数据
                        DixiaoTaskDomain task = new DixiaoTaskDomain();
                        task.setMonthcode(TimeUtil.formatDateToYM(new Date()));
                        task.setSaleid(saleid);
                        task.setSalename(taskColumn[1]);
                        task.setSaledesc(taskColumn[2]);
                        task.setSale_eparchy_code(taskColumn[3]);
                        task.setBoid(boid);
                        task.setAim_sub_name(taskColumn[6]);
                        task.setStart_date(taskColumn[7]);
                        task.setEnd_date(taskColumn[8]);
                        task.setStatus(DixiaoTaskStatusEnum.TASK_INIT.getValue());
                        task.setConfig_file_name(configName);
                        dixiaoResultMapper.insertDixiaoTask(task);

                    } else {
                        //记录日志
                        logReminderService.insertJXHReminder("18914764596,18951871658", filePath);
                        LOG.error("this line:" + line + " has error, the column length is:" + taskColumn.length + " ignore");
                    }
                }
            }
            LOG.info("Process dixiao config file "+configName+" successfully!");
        } catch (Exception e) {
            LOG.error("Get dixiao config file Task Error. ", e);
        }
    }
}
