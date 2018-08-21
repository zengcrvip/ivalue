package com.axon.market.core.kafkalog;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.kafkaservice.OperateLogInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhuwen on 2017/9/13.
 */
@Service("kafkaConsumeLog")
public class KafkaConsumeLog implements Runnable{
    //kafka链接超时时间
    private static final int timeOut = 2000;

    //一次批量插入数据量
    private static final int BATCHNO = 1000;

    private static SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();
    private static final Logger log = Logger.getLogger(KafkaConsumeLog.class.getName());

    @Resource
    private KafkaOperateLogConfig kafkaOperateLogConfig;

    @Override
    public void run() {
        log.info("####################KafkaConsumeLog start!");
        KafkaConsumer<String, String> consumer = kafkaOperateLogConfig.getConsumer();
        List<String> list = new ArrayList<>();
        list.add(systemConfigBean.getKafkaFineTopic());
        consumer.subscribe(list);
        while(true){
            ConsumerRecords<String, String> records = consumer.poll(timeOut);
            if (records != null){
                List<OperateLogInfo> infolist = new ArrayList<>();
                int batchno = 0;
                for (ConsumerRecord<String, String> record : records){
                    try{
                        String content = record.value();
                        if(StringUtils.isEmpty(content)){
                            continue;
                        }
                        OperateLogInfo logInfo = toLogInfo(content);
                        infolist.add(logInfo);
                        log.info("kafka log:" + logInfo.toString());
                        batchno++;
                        if(batchno>= BATCHNO){
//                            webOperateLogMapper.insertLog(infolist);
                            infolist.clear();
                            batchno = 0;
                        }
                    }catch(ClassCastException e){
                        log.error("####################KafkaConsumeLog error,errorinfo is:" + e);
                        continue;
                    }
                }
                if (!infolist.isEmpty()){
//                    webOperateLogMapper.insertLog(infolist);
                    infolist.clear();
                }
            }
        }
    }

    private OperateLogInfo toLogInfo(String dataString){
        OperateLogInfo log = new OperateLogInfo(dataString);
        return log;
    }
}
