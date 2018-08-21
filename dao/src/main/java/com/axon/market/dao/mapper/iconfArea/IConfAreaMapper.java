package com.axon.market.dao.mapper.iconfArea;

import com.axon.market.common.domain.iconfArea.confAreaDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by gloomysw on 2017/02/20.
 */
@Component("confAreaDao")
public interface IConfAreaMapper extends IMyBatisMapper
{
    List<confAreaDomain> selectConfAreaList();
}
