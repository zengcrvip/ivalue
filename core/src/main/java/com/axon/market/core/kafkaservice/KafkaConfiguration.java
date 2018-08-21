package com.axon.market.core.kafkaservice;

import com.axon.market.common.bean.SystemConfigBean;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Created by zengcr on 2017/6/7
 * kafka相关配置
 */
@Service
public class KafkaConfiguration
{
	private static final Logger LOG = Logger.getLogger(KafkaConfiguration.class.getName());
	private SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();
	
	private static KafkaConsumer<String, String> kc;

	protected KafkaConsumer<String, String> getConsumer()
	{
		if(StringUtils.isEmpty(systemConfigBean.getKafkaServers()))
		{
			return null;
		}
		try
		{
			if(kc == null) {
				Properties props = new Properties();
				props.put("bootstrap.servers", systemConfigBean.getKafkaServers());
				props.put("group.id", systemConfigBean.getKafkaGroupId());
				props.put("enable.auto.commit", "true");
				props.put("auto.commit.interval.ms", "1000");
				props.put("reconnect.backoff.ms", "50");
				props.put("session.timeout.ms", "30000");
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
}
