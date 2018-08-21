package com.axon.market.dao.mapper.imodel;

import com.axon.market.common.domain.imodel.ModelDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yangyang on 2016/1/11.
 */
@Service("modelDao")
public interface IModelMapper extends IMyBatisMapper
{
    /**
     *
     * @param userDomain
     * @param nameSearch
     * @param createTypeSearch
     * @param userNameSearch
     * @param catalogSearch
     * @return
     */
    Integer queryAllModelCounts(@Param(value = "status") String status,@Param(value = "loginUser") UserDomain userDomain, @Param(value = "nameSearch") String nameSearch, @Param(value = "createTypeSearch") String createTypeSearch, @Param(value = "userNameSearch") String userNameSearch, @Param(value = "catalogSearch") String catalogSearch,@Param(value = "qryToday") Boolean qryToday);

    /**
     * @return
     */
    List<ModelDomain> queryAllModels();

    /**
     * 查询所有的规则模型
     * @return
     */
    List<ModelDomain> queryAllRuleModelsBySystem();

    /**
     * @param offset
     * @param limit
     * @return
     */
    List<ModelDomain> queryModelsByPage(@Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit,@Param(value = "status") String status, @Param(value = "loginUser") UserDomain userDomain, @Param(value = "nameSearch") String nameSearch, @Param(value = "createTypeSearch") String createTypeSearch, @Param(value = "userNameSearch") String userNameSearch, @Param(value = "catalogSearch") String catalogSearch,@Param(value = "qryToday") Boolean qryToday);

    /**
     * 查询所有的模型信息
     *
     * @param userDomain
     * @return
     */
    List<ModelDomain> queryAllModelsByUser( @Param(value = "loginUser") UserDomain userDomain,@Param(value = "isQueryImportModel") Boolean isQueryImportModel);

    /**
     * 查询需要我审批的模型列表
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> queryAllAuditModelsByUser(@Param(value = "userId") Integer userId);

    /**
     * 查询模型的审批记录
     *
     * @param modelIds
     * @return
     */
    List<Map<String, Object>> queryModelAuditInfo(@Param(value = "modelIds") String modelIds);

    /**
     * 根据id查询模型
     *
     * @param modelId
     * @return
     */
    ModelDomain queryModelById(@Param(value = "modelId") Integer modelId);

    /**
     * 根据ID查询审核中的模型
     * @param modelId
     * @return
     */
    ModelDomain queryAuditingModelById(@Param(value = "modelId") Integer modelId);

    /**
     * 根据id查询模型
     *
     * @param modelName
     * @return
     */
    ModelDomain queryModelByName(@Param(value = "modelName") String modelName);

    /**
     * 查询模型创建者的手机号
     *
     * @param modelId
     * @return
     */
    String queryUserPhoneOfCreateModelByModelId(@Param(value = "modelId") Integer modelId);

    /**
     * @param modelDomain
     * @return
     */
    Integer createModel(@Param(value = "info") ModelDomain modelDomain);

    /**
     * @param modelDomain
     * @return
     */
    Integer updateModel(@Param(value = "info") ModelDomain modelDomain);

    /**
     * @param id
     * @return
     */
    Integer deleteModel(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId, @Param(value = "time") String time);

    /**
     *
     * @param modelId
     * @param count
     * @param refreshTime
     * @return
     */
    Integer updateModelRefreshInfo(@Param(value = "modelId") Integer modelId, @Param(value = "count") Integer count, @Param(value = "refreshTime") String refreshTime);

    /**
     * @param modelId
     * @return
     */
    String queryModelRefreshTimeByModelId(@Param(value = "modelId") Integer modelId);

    /**设置模型审批状态
     * @param id
     * @param status
     * @return
     */
    Integer setModelStatus(@Param(value = "id") Integer id, @Param(value = "status") Integer status);

    /**
     *
     * @param modelIds
     * @return
     */
    int calcModelsUserCount(@Param(value = "modelIds") String modelIds);

    /**
     * 查询我能查看的所有模型
     * @param modelId
     * @param loginUser
     * @return
     */
    List<Map<String,Object>> queryModelsUnderMe(@Param(value = "modelId") Integer modelId, @Param(value = "loginUser") UserDomain loginUser);
}
