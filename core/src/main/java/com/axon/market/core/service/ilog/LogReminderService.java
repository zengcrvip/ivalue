package com.axon.market.core.service.ilog;

import com.axon.market.dao.mapper.ilog.ILogReminderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 错误短信提醒服务类
 * Created by zengcr on 2017/7/12.
 */
@Service("logReminderService")
public class LogReminderService {

    @Qualifier("logReminderDao")
    @Autowired
    private ILogReminderMapper iLogReminderMapper;

    /**
     * 新增短信通知
     * @param phone
     * @param moduleId
     * @param smsContent
     * @return
     */
    public int insertLogReminder(String phone,Integer moduleId,String smsContent){
        return iLogReminderMapper.insertLogReminder(phone, moduleId, smsContent);
    }

    /**
     * 精细化文件处理失败提醒
     * @param phones
     * @param fileName
     */
    public void insertJXHReminder(String phones,String fileName){
        String[] phoneList = phones.split(",");
        for(int i=0;i<phoneList.length;i++)
        {
            insertLogReminder(phoneList[i],62,"炒店店精细化活动文件处理异常告警，具体文件：" + fileName);
        }
    }

}
