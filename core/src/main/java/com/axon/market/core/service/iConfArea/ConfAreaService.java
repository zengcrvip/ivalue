package com.axon.market.core.service.iConfArea;

import com.axon.market.dao.mapper.iconfArea.IConfAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.axon.market.common.domain.iconfArea.confAreaDomain;

import java.util.List;

/**
 * 地区服务 城市列表
 * 创建人:邵炜
 * 创建时间:2017年2月20日16:24:56
 * Created by gloomysw on 2017/02/20.
 */
@Component("confAreaService")
public class ConfAreaService
{
    @Autowired
    @Qualifier("confAreaDao")
    private IConfAreaMapper iConfAreaMapper;

    /**
     * 获取城市列表
     * 创建人:邵炜
     * 创建时间:2017年2月20日16:35:29
     * @return 城市列表对象集合
     */
    public List<confAreaDomain> selectConfAreaList(){
        return iConfAreaMapper.selectConfAreaList();
    }
}
