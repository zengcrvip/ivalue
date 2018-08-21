package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ikeeper.KeepWelfareRecordDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Zhuwen on 2017/8/21.
 */
@Component("keeperWelfareRecordDao")
public interface IKeeperWelfareRecordMapper extends IMyBatisMapper{
    void addWelfareRecord(KeepWelfareRecordDomain keepWelfareRecordDomain);

    List<Map<String, Object>> queryPhoneListByWelfareId(@Param("welfareId") Integer welfareId, @Param("userId") Integer userId);

    /**
     * 根据福利查询产品，逗号隔开
     * @param welfareIds 福利ID，逗号隔开
     * @return
     */
    List<Map<String,String>> queryProductByWelfareId(@Param("welfareIds") String welfareIds);

    /**
     * 根据日期及状态查询待处理的福利记录
     * @param date
     * @return
     */
    List<Map<String,Object>> queryRecordsByState(@Param("date") String date);

    /**
     * 根据记录ID修改福利任务的状态
     * @param recordId
     */
    void updateRecordById(@Param(value = "recordId") String recordId);


}
