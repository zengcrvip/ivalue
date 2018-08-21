package com.axon.market.web.quartz;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @Author: changrong.zeng
 * @Date: Created in 15:04 2018/7/20 .
 */
@Component("firstService")
public class FirstService implements Serializable {

    private static final long serialVersionUID = 1L;

    public void service() {
        System.out.println(new SimpleDateFormat("YYYYMMdd HH:mm:ss").format(new Date()) + "---start FirstService");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(new SimpleDateFormat("YYYYMMdd HH:mm:ss").format(new Date()) + "---end FirstService");
    }
}
