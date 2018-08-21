package com.axon.market.service.servlet;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.kafkalog.KafkaConsumeLog;
import com.axon.market.core.kafkalog.KafkaProducerLog;
import com.axon.market.core.kafkaservice.KafkaDataServer;
import com.axon.market.core.kafkaservice.KafkaTaskDispatch;
import com.axon.market.core.kafkaservice.producer.OnNetProducer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zengcr on 2017/7/26.
 * 初始化kafka消费端
 */
public class InitKafkaServlet extends HttpServlet
{
    private static final Logger LOG = Logger.getLogger(InitKafkaServlet.class.getName());
    private SystemConfigBean systemConfigBean = SystemConfigBean.getInstance();

    @Override
    public void init() throws ServletException
    {
        LOG.info("KafkaServlet Init Start......");

        String topic = systemConfigBean.getKafkaFineTopic();
        if(StringUtils.isEmpty(topic))
        {
            return;
        }
        try
        {
            // 启动精细化任务kafka用户读取客户端
            KafkaDataServer kafkaDataServer = (KafkaDataServer) SpringUtil.getSingletonBean("kafkaDataServer");
            kafkaDataServer.setTopic(topic);
            Thread kafkaDataServerThread = new Thread(kafkaDataServer,"kafkaDataServer-thread");
            kafkaDataServerThread.start();
            //启动线程消费kafka传送过来的用户数据
            KafkaTaskDispatch kafkaTaskDispatch = (KafkaTaskDispatch)SpringUtil.getSingletonBean("kafkaTaskDispatch");
            ExecutorService service = Executors.newFixedThreadPool(4);
            service.execute(kafkaTaskDispatch);
            service.execute(kafkaTaskDispatch);
            service.execute(kafkaTaskDispatch);
            service.execute(kafkaTaskDispatch);

            //启动生产者生产数据
            List<String> list = new ArrayList<String>(){
                {add("oneoneoneone1");
                 add("twotwotwotwo2");
                 add("threethreethreethree3");
                 add("fourfourfourfour4");
                }
            };
            OnNetProducer producer = (OnNetProducer)SpringUtil.getSingletonBean("onNetProducer");
            for(String s: list){
                producer.produce(s);
            }

            //启动日志生产日志
            KafkaProducerLog.send("1","2","3","4","5","6");
            KafkaProducerLog.send("7","8","9","10","11","12");
            KafkaProducerLog.send("13","14","15","16","17","18");


        }
        catch (Exception e)
        {
            LOG.error("KafkaServlet Init error \r\n" + e);
        }
    }
}
