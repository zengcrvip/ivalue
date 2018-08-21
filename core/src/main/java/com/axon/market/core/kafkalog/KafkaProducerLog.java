package com.axon.market.core.kafkalog;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhuwen on 2017/9/14.
 */
public class KafkaProducerLog {
    @Autowired
    @Qualifier("kafkaOperateLogConfig")
    private static KafkaOperateLogConfig kafkaOperateLogConfig;

    private static final Logger LOG = Logger.getLogger(KafkaProducerLog.class.getName());
    private static SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();

    public static void log(String module, String webname, String desc, String operatetype, String operatorid, String operatetime){
        send(module,webname,desc,operatetype,operatorid,operatetime);
    }

    public static void send(String module, String webname, String desc, String operatetype, String operatorid, String operatetime){
        String topicStr = systemConfigBean.getKafkaFineTopic();
        String value = getValue(module, webname, desc, operatetype, operatorid, operatetime);
        if(value.isEmpty()){
            return;
        }
        KafkaProducer<byte[], byte[]> producer = kafkaOperateLogConfig.getProducer();

        try{
            ProducerRecord<byte[], byte[]> pr = new ProducerRecord<>(topicStr, "key".getBytes("utf-8"), value.getBytes("utf-8"));
            producer.send(pr, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (null != e){
                        LOG.error("记录的offset在:" + recordMetadata.offset());
                        LOG.error(e.getMessage()+e);
                    }
                }
            });
        }catch(UnsupportedEncodingException e){
            LOG.error("value.getBytes failed, value is "+value);
            return;
        }
    }

    public static String getValue(String module, String webname, String desc, String operatetype, String operatorid, String operatetime){
        Map<String, String> map = new HashMap<>();
        map.put("module", module);
        map.put("webname", webname);
        map.put("desc",desc);
        map.put("operatetype",operatetype);
        map.put("operatorid", operatorid);
        map.put("operatetime", operatetime);
        try {
            return JsonUtil.objectToString(map);
        }catch(JsonProcessingException e){
            LOG.error("call JsonUtil.objectToString failed! map is:" + map);
            return null;
        }
    }
}
