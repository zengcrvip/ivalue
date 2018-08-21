package com.axon.market.core.kafkalog;

import com.axon.market.common.bean.SystemConfigBean;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Created by Zhuwen on 2017/9/14.
 */
@Service("kafkaOperateLogConfig")
public class KafkaOperateLogConfig {
    private static final Logger LOG = Logger.getLogger(KafkaOperateLogConfig.class.getName());
    private static SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();

    private static KafkaConsumer<String, String> kc;
    private static KafkaProducer<byte[], byte[]> kp;

    public static KafkaConsumer<String, String> getConsumer()
    {
        try
        {
            if(kc == null) {
                Properties props = new Properties();
                props.put("bootstrap.servers", systemConfigBean.getKafkaServers());
                props.put("group.id", systemConfigBean.getKafkaLogGroupId());
                props.put("enable.auto.commit", "true");
                props.put("auto.commit.interval.ms", "1000");
                props.put("reconnect.backoff.ms", "50");
                props.put("session.timeout.ms", "30000");
                props.put("serializer.class", "kafka.serializer.StringEncoder");
                props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                kc = new KafkaConsumer<String, String>(props);
            }
        }
        catch (Exception e)
        {
            LOG.error("Consumer Config properties put information error!", e);
        }
        return kc;
    }


    public static KafkaProducer<byte[], byte[]> getProducer()
    {
        try
        {
            if(kp == null) {
                Properties props = new Properties();
                props.put("bootstrap.servers", systemConfigBean.getKafkaServers());
                props.put("producer.type", "sync");
                props.put("request.required.acks", "1");
                props.put("serializer.class", "kafka.serializer.DefaultEncoder");
                props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
                props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
                props.put("bak.partitioner.class", "kafka.producer.DefaultPartitioner");
                props.put("bak.key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                props.put("bak.value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

                kp = new KafkaProducer<byte[], byte[]>(props);
            }
        }
        catch (Exception e)
        {
            LOG.error("Producer Config properties put information error!", e);
        }
        return kp;
    }

}
