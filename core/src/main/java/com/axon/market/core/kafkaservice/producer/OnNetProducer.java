package com.axon.market.core.kafkaservice.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.axon.market.common.bean.SystemConfigBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

@Service("onNetProducer")
public class OnNetProducer
{

	private static final int BATCH_SIZE = 100;

	private Producer<String, String> producer;
	private final List<KeyedMessage<String, String>> messageList = new ArrayList<KeyedMessage<String, String>>(BATCH_SIZE);
	private Object syncObject = new boolean[0];
	private SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();

	Log log = LogFactory.getLog(getClass());

	/**
	 * 生产一条信息
	 * @param message
	 */
	public void produce(String message)
	{
		synchronized (syncObject)
		{
			KeyedMessage<String, String> kafkaMessage = new KeyedMessage<String, String>(systemConfigBean.getKafkaFineTopic(), message);
			messageList.add(kafkaMessage);
			if (messageList.size() >= BATCH_SIZE)
			{
				producer.send(messageList);
				log.info("The messages have been produced! size : " + messageList.size());
				messageList.clear();
			}
		}
	}

	/**
	 * 每5分钟检测一次,如果缓存不为空,则全部发送并清空
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	protected void checkMessageList()
	{
		synchronized (syncObject)
		{
			if (!messageList.isEmpty())
			{
				producer.send(messageList);
				log.info("The messages have been produced per 5 min! size : " + messageList.size());
				messageList.clear();
			}
		}
	}

	@PostConstruct
	public void reset()
	{
		if (producer != null)
		{
			producer.close();
		}
		Properties props = new Properties();
		props.put("metadata.broker.list", systemConfigBean.getKafkaServers());
		props.put("request.required.acks", "-1");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");
		producer = new Producer<String, String>(new ProducerConfig(props));
	}

}
