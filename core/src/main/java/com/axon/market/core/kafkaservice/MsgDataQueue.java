package com.axon.market.core.kafkaservice;

import com.axon.market.common.domain.kafkaservice.LogInfo;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * topic传过来的数据队列
 * Created by zengcr on 2017/6/8.
 */
@Component("msgDataQueue")
public class MsgDataQueue implements UdpDataQueue
{
    private final BlockingQueue<LogInfo> messageQueue = new LinkedBlockingQueue<LogInfo>(200000);

    @Override
    public LogInfo take() throws InterruptedException
    {
        return messageQueue.take();
    }

    @Override
    public boolean offer(LogInfo logInfo)
    {
        return this.messageQueue.offer(logInfo);
    }
}
