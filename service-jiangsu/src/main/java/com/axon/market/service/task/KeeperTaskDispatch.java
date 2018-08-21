package com.axon.market.service.task;

import com.axon.market.common.timer.NoRedoIntervalTask;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.timer.TimerTask;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.task.*;

import java.text.ParseException;

/**
 * 掌柜任务处理
 * Created by zengcr on 2017/8/21.
 */
public class KeeperTaskDispatch {

    /**
     * 掌柜生日维系任务处理
     * @throws ParseException
     */
    public  void initBirthdayTask(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask birthdayTask = new NoRedoIntervalTask("keeper_birthday_task", TimeUtil.formatDate("2017-05-01 00:30:00"), null, intervalMills, new KeeperBirthdayTask());
        timer.addTask(birthdayTask);
    }


    /**
     * 场景关怀任务处理
     * @throws ParseException
     */
    public  void initSceneCareTask(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask sceneCareTask = new NoRedoIntervalTask("keeper_scene_care_task", TimeUtil.formatDate("2017-05-01 01:00:00"), null, intervalMills, new KeeperSceneCareTask());
        timer.addTask(sceneCareTask);
    }

    /**
     * 2转4任务处理
     * @throws ParseException
     */
    public  void initTwoFourSceneTask(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask twoFourSceneTask = new NoRedoIntervalTask("keeper_two_four_task", TimeUtil.formatDate("2017-05-01 03:00:00"), null, intervalMills, new KeeperTwoFourSceneTask());
        timer.addTask(twoFourSceneTask);
    }


    /**
     * 优惠到期维系任务处理
     * @throws ParseException
     */
    public  void initDiscountExpiryTask(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask discountExpiryTask = new NoRedoIntervalTask("keeper_discount_expiry_task", TimeUtil.formatDate("2017-05-01 02:00:00"), null, intervalMills, new KeeperDiscountExpiryTask());
        timer.addTask(discountExpiryTask);
    }

    /**
     * 掌柜任务到期下线
     * @param timer
     * @throws ParseException
     */
    public void initKeeperTaskOverdue(Timer timer) throws ParseException
    {
        int intervalMills = 86400 * 1000;
        TimerTask expireKeeperTask = new NoRedoIntervalTask("keeper_task_expire",TimeUtil.formatDate("2017-01-01 04:40:00"),null,intervalMills,new KeeperOverdueTask());
        timer.addTask(expireKeeperTask);
    }
}
