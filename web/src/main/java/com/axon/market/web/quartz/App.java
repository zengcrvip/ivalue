package com.axon.market.web.quartz;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description:
 * @Author: changrong.zeng
 * @Date: Created in 15:06 2018/7/20 .
 */
public class App {

    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext( new String[]{"applicationContext.xml"});
    }
}
