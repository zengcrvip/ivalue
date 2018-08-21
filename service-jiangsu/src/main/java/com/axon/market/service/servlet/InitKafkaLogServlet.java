package com.axon.market.service.servlet;

import com.axon.market.common.util.SpringUtil;
import com.axon.market.core.kafkalog.KafkaConsumeLog;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by Zhuwen on 2017/9/13.
 */
public class InitKafkaLogServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(InitKafkaLogServlet.class.getName());

    @Override
    public void init() throws ServletException {
        LOG.info("KafkaLogServlet Init Start......");

        try{
            KafkaConsumeLog kafkaConsumeLog = (KafkaConsumeLog) SpringUtil.getSingletonBean("kafkaConsumeLog");
            Thread kafkaDataServerThread = new Thread(kafkaConsumeLog,"kafkaConsumeLog-thread");
            kafkaDataServerThread.start();
        }catch(Exception e){
            LOG.error("KafkaLogServlet Init error \r\n" + e);
        }
    }
}
