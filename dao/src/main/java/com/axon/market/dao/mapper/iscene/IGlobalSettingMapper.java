package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.GlobalSettingDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
@Component("globalSettingDao")
public interface IGlobalSettingMapper extends IMyBatisMapper{

    /**
     * 全局设置 查询
     * @return
     */
    List<GlobalSettingDomain> queryGlobalSettings();

    /**
     * 修改 全局设置
     * @param id
     * @param number
     * @return 受影响行数
     */
    int updateGlobalSetting(@Param(value="Id") Integer id,@Param(value="Num") Integer number);

    /**
     * 根据ID查询Type
     * @param id
     * @return
     */
    int queryType(@Param(value = "Id") Integer id);
}
