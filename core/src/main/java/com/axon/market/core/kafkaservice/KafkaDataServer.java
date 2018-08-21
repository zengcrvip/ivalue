package com.axon.market.core.kafkaservice;

import com.axon.market.common.domain.kafkaservice.LogInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import sun.applet.Main;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by zengcr on 2017/7/26.
 */
@Service("kafkaDataServer")
@Scope("prototype")
public class KafkaDataServer implements Runnable{
    private static int count = 0;
    //每消费1W打印一次日志
    private static final int logPreCount = 10000;
    //kafka链接超时时间
    private static final int timeOut = 2000;
    //获取数据超时时间
    private static final long EXPIRED_TIME = 30 * 60 * 1000L;

    @Resource
    private UdpDataQueue udpDataQueue;

    @Resource
    private KafkaConfiguration kafkaConfiguration;

    private String topic;

    private static final Logger log = Logger.getLogger(KafkaDataServer.class.getName());

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    @Override
    public void run() {
        log.info("ExecutorService execute a topicList is " + topic);
        String topicStr = "axon-icompaign-scene-task,axon-keeper-scene-task";
        KafkaConsumer<String, String> consumer = kafkaConfiguration.getConsumer();
        consumer.subscribe(Arrays.asList(topicStr.split(",")));
        while(true){
            try {
                ConsumerRecords<String, String> records = consumer.poll(timeOut);
                log.info("records:"+records);
                if (records != null) {
                    for (ConsumerRecord<String, String> record : records) {
                        // 读取数据到队列中
                        String content = record.value();
                        String topic = record.topic();
                        if(StringUtils.isEmpty(content)){
                            continue;
                        }
                        LogInfo info = toLogInfo(content);
                        log.info("kafka log:"+info.toString());
                        long dtime = System.currentTimeMillis() - info.getTime();
                        if (dtime > EXPIRED_TIME)
                        {
                            // 超出过期时间,直接丢弃
                            continue;
                        }
                        try {
                            if (udpDataQueue.offer(info)) {
                                count++;
                                if (count >= logPreCount) {
                                    log.info("KafkaDataServer[" + topic+ "] receive content: "+ content);
                                    count = 0;
                                }
                            }
                        } catch (Exception e) {
                            log.error("udpDataQueue offer failed!", e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("KafkaDataServer failed! the topic :"+ topic + " is exception!", e);
            }
        }
    }

    private LogInfo toLogInfo(String dataString) {
        LogInfo info = new LogInfo(dataString);
        return  info;
    }

}
