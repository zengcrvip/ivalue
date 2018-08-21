package com.axon.market.dao.mapper.ilog;

import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 短信提醒DAO
 * Created by zengcr on 2017/7/12.
 */
@Component("logReminderDao")
public interface ILogReminderMapper extends IMyBatisMapper {

    /**
     * 新增短信通知
     * @param phone
     * @param moduleId
     * @param smsContent
     * @return
     */
     int insertLogReminder(@Param(value = "phone") String phone,@Param(value = "moduleId") Integer moduleId, @Param(value = "smsContent") String smsContent);
}
