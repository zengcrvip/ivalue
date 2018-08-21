package com.axon.market.dao.mapper.imodel;

import com.axon.market.common.domain.imodel.ModelDownloadSettingDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by yuanfei on 2017/7/24.
 */
@Component("modelDownloadDao")
public interface IModelDownloadMapper extends IMyBatisMapper
{
    /**
     *
     * @param createUser
     * @return
     */
    int queryModelDownloadSettingCounts(@Param(value = "createUser") Integer createUser);

    /**
     *
     * @param createUser
     * @param offset
     * @param limit
     * @return
     */
    List<Map<String, Object>> queryModelDownloadSettingByPage(@Param(value = "createUser") Integer createUser, @Param(value = "offset") int offset, @Param(value = "limit") int limit);

    /**
     * @param settingId
     * @return
     */
    Map<String,Object> queryModelDownloadSettingById(@Param(value = "settingId") int settingId);

    /**
     * 根据模型id查询我配置的模型下载属性
     * @param modelId
     * @param userId
     * @return
     */
    ModelDownloadSettingDomain queryMyModelDownloadSettingByModelId(@Param(value = "modelId") Integer modelId, @Param(value = "userId") Integer userId);

    /**
     * @param modelDownloadSettingDomain
     * @return
     */
    int insertModelDownloadSetting(@Param(value = "info") ModelDownloadSettingDomain modelDownloadSettingDomain);

    /**
     * @param modelDownloadSettingDomain
     * @return
     */
    int updateModelDownloadSetting(@Param(value = "info") ModelDownloadSettingDomain modelDownloadSettingDomain);

    /**
     * @param id
     * @return
     */
    int deleteModelDownloadSetting(@Param(value = "id") int id);

    /**
     *
     * @param copyItemId
     * @param modelId
     * @param userId
     * @return
     */
    int copyModelDownloadSetting(@Param(value = "copyItemId") int copyItemId, @Param(value = "modelId") int modelId, @Param(value = "userId") int userId);
}
