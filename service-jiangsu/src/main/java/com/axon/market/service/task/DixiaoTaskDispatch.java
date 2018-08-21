package com.axon.market.service.task;

import com.axon.market.common.timer.FixedDelayTask;
import com.axon.market.common.timer.NoRedoIntervalTask;
import com.axon.market.common.timer.Timer;
import com.axon.market.common.timer.TimerTask;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.task.DixiaoCodeImportTask;
import com.axon.market.core.task.DixiaoListBackupTask;
import java.text.ParseException;

/**
 * 低消相关任务调度
 * Created by Zhuwen on 2017/7/27.
 */
public class DixiaoTaskDispatch {
    /**
     * 低消明细数据备份任务
     * 每一天扫描一次
     */
    public void initDixiaoListBackupTask(Timer timer) throws ParseException
    {
        int intervalMills = 86400*1000;
        TimerTask dixiaoListBackupTask = new NoRedoIntervalTask("dixiao_list_backup_task", TimeUtil.formatDate("2017-01-01 05:00:00"), null, intervalMills, new DixiaoListBackupTask());
        //TimerTask dixiaoListBackupTask = new FixedDelayTask("dixiao_list_backup_task", null, null, intervalMills, new DixiaoListBackupTask());
        timer.addTask(dixiaoListBackupTask);
    }

    /**
     * 低消渠道文件入库
     * 每10分钟执行一次
     */
    public void initDixiaoCodeImportTask(Timer timer) throws ParseException
    {
        int intervalMills = 10*60*1000;
        TimerTask dixiaoCodeImportTask = new FixedDelayTask("dixiao_code_import_task", null, null, intervalMills, new DixiaoCodeImportTask());
        timer.addTask(dixiaoCodeImportTask);
    }

    /**
     * 创建大数据匹配规则任务
     * 每10分钟扫描一次
     */
    public void initDixiaoRuleMatchTask(Timer timer) throws ParseException
    {
        int intervalMills = 10*60*1000;
        TimerTask dixiaoRuleMatchTask = new FixedDelayTask("dixiao_rule_match_task", null, null, intervalMills, new DixiaoRuleMatchTask());
        timer.addTask(dixiaoRuleMatchTask);
    }

    /**
     * 扫描ftp风雷标志位，ftp文件给风雷
     * 每10分钟扫描一次
     */
    public void initDixiaoFtpFengleiTask(Timer timer) throws ParseException
    {
        int intervalMills = 10*60*1000;
        TimerTask dixiaoFtpFengleiTask = new FixedDelayTask("dixiao_ftp_fenglei_task", null, null, intervalMills, new DixiaoFtpFengleiTask());
        timer.addTask(dixiaoFtpFengleiTask);
    }

    /**
     * 低消线下文件推送
     * 每10分钟扫描一次
     */
    public void initDixiaoFileFtpOfflineTask(Timer timer) throws ParseException
    {
        int intervalMills = 10*60*1000;
        TimerTask dixiaoFileFtpOfflineTask = new FixedDelayTask("dixiao_file_ftp_offline_task", null, null, intervalMills, new DixiaoFileFtpOfflineTask());
        timer.addTask(dixiaoFileFtpOfflineTask);
    }

    /**
     * 低消线上文件推送
     * 每10分钟扫描一次
     */
    public void initDixiaoFileFtpOnlineTask(Timer timer) throws ParseException
    {
        int intervalMills = 5*60*1000;
        TimerTask dixiaoFileFtpOnlineTask = new FixedDelayTask("dixiao_file_ftp_online_task", null, null, intervalMills, new DixiaoFileFtpOnlineTask());
        timer.addTask(dixiaoFileFtpOnlineTask);
    }
}
