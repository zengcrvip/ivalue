package com.axon.market.core.kafkaservice;

import com.axon.market.common.domain.kafkaservice.LogInfo;
import com.axon.market.core.kafkaservice.impl.SceneMsgExecutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by fgtg on 2017/7/26.
 */
@Service("kafkaTaskDispatch")
@Scope("prototype")
public class KafkaTaskDispatch implements Runnable
{
    private static final Logger log = Logger.getLogger(KafkaTaskDispatch.class.getName());
    private static final String APP_SCENE = "R002";
    private static final String GPS_SCENE = "L001";

    @Resource
    private UdpDataQueue udpDataQueue;

    @Qualifier("sceneMsgExecutor")
    @Autowired
    private SceneMsgExecutor sceneMsgExecutor;


    @Override
    public void run()
    {
        log.info("KafkaTaskDispatch start!");
        while (true){
            try
            {
                LogInfo logInfo = udpDataQueue.take();
                log.info("logInfo take: " + logInfo);
                if(logInfo != null)
                {
                    sceneMsgExecutor.execute(logInfo);
                }
            }
            catch (Exception e)
            {
                log.error("logInfo Dispatch error",e);
            }
        }
    }

}
