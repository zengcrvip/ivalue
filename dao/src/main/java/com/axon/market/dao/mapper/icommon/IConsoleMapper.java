package com.axon.market.dao.mapper.icommon;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/2/22.
 */
@Component("consoleDao")
public interface IConsoleMapper extends IMyBatisMapper
{
    /**
     * 查询数目
     * @param businessCodes
     * @param areaCode
     * @param type
     * @return
     */
    int queryMyShopTaskCount(@Param(value = "businessCodes") String businessCodes,@Param(value = "areaCode") Integer areaCode,@Param(value = "type") Integer type);
    /**
     * 查询所有代办的炒店任务
     * @param businessCodes
     * @param areaCode
     * @return
     */
    List<Map<String,Object>> queryMyShopTaskByPage(@Param(value = "businessCodes") String businessCodes,@Param(value = "areaCode") Integer areaCode,@Param(value = "type") Integer type,@Param(value = "offset") long offset,@Param(value = "maxRecord") long maxRecord);

    /**
     * 查询所有业务类型对应的任务信息列表
     * @return
     */
    Map<String,Object> queryShopTaskTypeCount(@Param(value = "businessCodes") String businessCodes,@Param(value = "areaCode") Integer areaCode);
}
