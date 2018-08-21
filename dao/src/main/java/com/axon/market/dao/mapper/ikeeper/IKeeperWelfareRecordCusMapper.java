package com.axon.market.dao.mapper.ikeeper;

import com.axon.market.common.domain.ikeeper.KeepWelfareRecordCusDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Created by Zhuwen on 2017/8/21.
 */
@Component("keeperWelfareRecordCusDao")
public interface IKeeperWelfareRecordCusMapper extends IMyBatisMapper {
    void addWelfareRecordCus(@Param(value = "dataList") List<KeepWelfareRecordCusDomain> dataList);

    /**
     *根据记录ID查询送福利产品成功的用户
     * @param recordId
     * @return
     */
    Set<String> queryCustPhoneListByRecordId(@Param(value = "recordId") String recordId);
}
