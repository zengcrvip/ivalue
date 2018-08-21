package com.axon.market.core.recommendation.task.impl;

import com.axon.market.common.bean.RecommendationConfigBean;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.domain.itag.RemoteServerDomain;
import com.axon.market.common.util.EncryptUtil;
import com.axon.market.core.recommendation.task.GetRecommendationFileExecutor;
import com.axon.market.core.service.icommon.FileOperateService;
import com.axon.market.core.service.ilog.LogReminderService;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.core.service.itag.RemoteServerService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 炒蛋个性化活动处理
 * Created by zengcr on 2017/2/27.
 */
@Service("recommendationTaskFileExecutor")
public class RecommendationTaskFileExecutor implements GetRecommendationFileExecutor
{
    private static final Logger LOG = Logger.getLogger(RecommendationTaskFileExecutor.class.getName());

    @Autowired
    @Qualifier("shopTaskService")
    private ShopTaskService shopTaskService;

    @Autowired
    @Qualifier("fileOperateService")
    private FileOperateService fileOperateService;

    @Autowired
    @Qualifier("remoteServerService")
    private RemoteServerService remoteServerService;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    @Autowired
    @Qualifier("recommendationConfigBean")
    private RecommendationConfigBean recommendationConfigBean;

    @Autowired
    @Qualifier("logReminderService")
    private LogReminderService logReminderService;

    private byte[] columnByte = {0x01};

    private String columnSeparator = new String(columnByte);

    private static Map<String, String> accessNumberMap = new HashMap<String, String>()
    {
        {
            put("0000", "106557392");
            put("025", "106557392025");
            put("0510", "106557392510");
            put("0511", "106557392511");
            put("0512", "106557392512");
            put("0513", "106557392513");
            put("0514", "106557392514");
            put("0515", "106557392515");
            put("0516", "106557392516");
            put("0517", "106557392517");
            put("0518", "106557392518");
            put("0519", "106557392519");
            put("0523", "106557392523");
            put("0527", "106557392527");
        }
    };

    private static Map<String,String> baseTypeMap = new HashMap<String,String>()
    {
        {
          put("60001","1");
          put("60002","7");
          put("10001","4");
          put("20001","5");
          put("30001","2");
          put("40001","3");
          put("50001","6");
        }
    };

    private String getBaseType(String source){
        StringBuffer baseTypeBuffer = new StringBuffer();
        String baseType = null;
        if(StringUtils.isNotEmpty(source) && !"null".equals(source)){
            String[] souseArray = source.split("\\;");
            if(souseArray != null && souseArray.length > 0)
            {
                for(String item:souseArray)
                {
                    baseTypeBuffer.append(",").append(baseTypeMap.get(item));
                }
                baseType =   baseTypeBuffer.substring(1).toString();
            }
        }
        return baseType;
    }

    @Override
    public void execute(String filePath)
    {
        File file = null;
        try
        {
            RemoteServerDomain remoteServerDomain = remoteServerService.generateRemoteServerDomain(recommendationConfigBean.getServerHost(), recommendationConfigBean.getServerUser(), EncryptUtil.encryption(recommendationConfigBean.getServerPassword(), "market"), recommendationConfigBean.getServerPort(), recommendationConfigBean.getServerConnectType());


            String targetFileName = systemConfigBean.getChannelFileLocalPath() + "task";
            if (fileOperateService.downloadFile(remoteServerDomain, filePath, targetFileName))
            {
                LOG.info("shop task downloadFile bigin");
                file = new File(targetFileName);

                LineIterator iterator = FileUtils.lineIterator(file, "GBK");
                while (iterator.hasNext())
                {
                    String line = iterator.next();
                    LOG.info("shop task line"+line);
                    String[] taskColumn = line.split(columnSeparator,-1);
                    if (taskColumn != null && taskColumn.length == 14)
                    {
                        if (shopTaskService.queryRecommendationShopTaskByName(taskColumn[1], taskColumn[4], taskColumn[5], taskColumn[12]) == null)
                        {
                            //对同一任务活动历史波次做终止处理
                            List<ShopTaskDomain> taskHistoList =  shopTaskService.queryShopTaskBySaleIdAndBoid(taskColumn[0], taskColumn[4]);
                            if(taskHistoList != null && taskHistoList.size() > 0)
                            {
                                for(ShopTaskDomain task:taskHistoList)
                                {
                                    LOG.info("shop task 个性化任务终止历史任务:"+task.getId());
                                    shopTaskService.pauseItem(task.getId(),task.getTaskName(),null);
                                }
                            }

                            LOG.info("shop task begin个性化任务"+taskColumn[1]);
                            String startYear = taskColumn[7].substring(0, 4), stopYear = taskColumn[8].substring(0, 4);
                            String startMonth = taskColumn[7].substring(4, 6), stopMonth = taskColumn[8].substring(4, 6);
                            String startDay = taskColumn[7].substring(6, 8), stopDay = taskColumn[8].substring(6, 8);
                            String startHour = taskColumn[7].substring(8, 10), stopHour = taskColumn[8].substring(8, 10);
                            String startMinute = taskColumn[7].substring(10, 12), stopMinute = taskColumn[8].substring(10, 12);
                            int areaId = "0000".equals(taskColumn[3]) ? 99999 : Integer.parseInt(taskColumn[3]);
                            int taskType = "0000".equals(taskColumn[3]) ? 1 : 2;

                            ShopTaskDomain taskDomain = new ShopTaskDomain();
                            taskDomain.setTaskType(taskType);
                            taskDomain.setTaskName(taskColumn[1] + "--【" + taskColumn[6] + "】");
                            taskDomain.setTaskDesc(taskColumn[2]);
                            taskDomain.setBaseAreaId(areaId);
                            taskDomain.setBaseAreaTypes(getBaseType(taskColumn[12]));
                            taskDomain.setStartTime(startYear + "-" + startMonth + "-" + startDay);
                            taskDomain.setStopTime(stopYear + "-" + stopMonth + "-" + stopDay);
                            taskDomain.setBeginTime(startHour + ":" + startMinute);
                            taskDomain.setEndTime(stopHour + ":" + stopMinute);
                            taskDomain.setMarketContentText(taskColumn[11]);
                            taskDomain.setStatus(2);
                            taskDomain.setAimSubId(taskColumn[5]);
                            taskDomain.setBusinessId(taskColumn[9]);
                            taskDomain.setTaskWeight(Integer.parseInt(taskColumn[10]));
                            taskDomain.setDepartTypeCode(taskColumn[12]);
                            taskDomain.setAccessNumber(accessNumberMap.get(taskColumn[3]));
                            taskDomain.setMarketUser(4);
                            taskDomain.setCreateUser("100000");
                            taskDomain.setCreateTime(new Date());
                            taskDomain.setMarketLimit(0);
                            taskDomain.setSendInterval(1);
                            taskDomain.setSaleId(taskColumn[0]);
                            taskDomain.setSaleBoidId(taskColumn[4]);
                            taskDomain.setAimSubName(taskColumn[6]);
                            taskDomain.setTaskFileName(filePath);
                            shopTaskService.insertShopTask(taskDomain, null);
                        }
                    }else{
                        //记录日志
                        logReminderService.insertJXHReminder("18914764596,18951871658",filePath);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logReminderService.insertJXHReminder("18914764596,18951871658",filePath);
            LOG.error("Get Personalized Recommendation Task Error. ", e);
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
