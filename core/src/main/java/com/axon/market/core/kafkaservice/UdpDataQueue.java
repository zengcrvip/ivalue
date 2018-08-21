package com.axon.market.core.kafkaservice;


import com.axon.market.common.domain.kafkaservice.LogInfo;

/**
 * 数据接入层,将kafka的报文内容转换为LogInfo对象,从而方便后续流程处理
 * 
 * @author zengcr
 * 
 */
public interface UdpDataQueue
{
	/**
	 * 从队列中取LogInfo.队列为空时,线程被阻塞
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	LogInfo take() throws InterruptedException;

	/**
	 * 向队列中添加LogInfo实例. 添加失败时返回false
	 * 
	 * @param logInfo
	 * @return
	 */
	boolean offer(LogInfo logInfo);
}
