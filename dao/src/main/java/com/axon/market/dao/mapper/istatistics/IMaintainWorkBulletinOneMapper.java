package com.axon.market.dao.mapper.istatistics;

import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Chris on 2017/7/21.
 */
@Component("MaintainWorkBulletinOneDao")
public interface IMaintainWorkBulletinOneMapper extends IMyBatisMapper
{
    List<Map<String,Object>> queryMaintainWorkBulletinOne(@Param(value = "yearMonth") String yearMonth);

}
