package com.axon.market.web.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Description:
 * @Author: changrong.zeng
 * @Date: Created in 15:03 2018/7/20 .
 * https://my.oschina.net/OutOfMemory/blog/1790200
 */
@DisallowConcurrentExecution   //保证任务串行
public class FirstTask extends QuartzJobBean {
    @Autowired
    private FirstService firstService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        firstService.service();
    }

    public void setFirstService(FirstService firstService) {
        this.firstService = firstService;
    }
}
