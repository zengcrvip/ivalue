package com.axon.market.dao.mapper.ishop;

import com.axon.market.common.domain.iscene.BannedHostsDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/16.
 */
@Component("marketRecordDao")
public interface IMarketRecordMapper extends IMyBatisMapper
{

    List<Map<String, String>> queryMarketRecordByPage(Map<String,Object> parasMap);

    Integer queryMarketRecordByCount(Map<String,Object> parasMap);
}
