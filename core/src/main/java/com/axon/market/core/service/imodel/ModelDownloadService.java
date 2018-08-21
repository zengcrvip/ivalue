package com.axon.market.core.service.imodel;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.domain.imodel.ModelDownloadSettingDomain;
import com.axon.market.core.service.itag.PropertyService;
import com.axon.market.dao.mapper.imodel.IModelDownloadMapper;
  import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by yuanfei on 2017/7/24.
 */
@Component("modelDownloadService")
public class ModelDownloadService
{
    @Autowired
    @Qualifier("modelDownloadDao")
    private IModelDownloadMapper modelDownloadDao;

    @Autowired
    @Qualifier("propertyService")
    private PropertyService propertyService;

    /**
     * @return
     */
    public int queryModelDownloadSettingCounts(Integer userId)
    {
        return modelDownloadDao.queryModelDownloadSettingCounts(userId);
    }

    /**
     * @param limit
     * @param offset
     * @return
     */
    public List<Map<String, Object>> queryModelDownloadSettingByPage(Integer userId ,int offset, int limit)
    {
        List<Map<String, Object>> list = modelDownloadDao.queryModelDownloadSettingByPage(userId, offset, limit);
        // set元属性id和name对应的map
        //setMetaPropertyIdAndName(list);
        return list;
    }

    /**
     * @param modelDownloadSetting
     */
    private void setMetaPropertyIdAndName(Map<String,Object> modelDownloadSetting)
    {
        if (modelDownloadSetting != null)
        {
            List<Map<String, String>> metaPropertyIdAndNames = new ArrayList<Map<String, String>>();
            String metaPropertyIds = String.valueOf(modelDownloadSetting.get("metaPropertyIds"));
            if (StringUtils.isNotEmpty(metaPropertyIds))
            {
                List<Map<String, String>> metaProperties = propertyService.queryMetaPropertiesByIds(metaPropertyIds);
                for (Map<String, String> metaProperty : metaProperties)
                {
                    Map<String, String> idAndName = new HashMap<>();
                    idAndName.put("id", String.valueOf(metaProperty.get("id")));
                    idAndName.put("name", String.valueOf(metaProperty.get("name")));
                    metaPropertyIdAndNames.add(idAndName);
                }
                modelDownloadSetting.put("metaProperties", metaPropertyIdAndNames);
            }
        }
    }

    /**
     * 根据id查询模型下载配置信息
     * @param settingId
     * @return
     */
    public Map<String,Object> queryModelDownloadSettingById(int settingId)
    {
        Map<String,Object> modelDownloadDomain = modelDownloadDao.queryModelDownloadSettingById(settingId);
        setMetaPropertyIdAndName(modelDownloadDomain);
        return modelDownloadDomain;
    }

    /**
     * 查询我的模型下载配置
     * @param modelId
     * @param userId
     * @return
     */
    public ModelDownloadSettingDomain queryMyModelDownloadSettingByModelId(Integer modelId,Integer userId)
    {
        return modelDownloadDao.queryMyModelDownloadSettingByModelId(modelId, userId);
    }

    /**
     * @param modelDownloadSettingDomain
     * @return
     */
    public ServiceResult insertModelDownloadSetting(ModelDownloadSettingDomain modelDownloadSettingDomain)
    {
        ServiceResult result = new ServiceResult();

        int flag = modelDownloadDao.insertModelDownloadSetting(modelDownloadSettingDomain);
        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("客户群下载配置信息插入失败");
        }

        return result;
    }

    /**
     * @param modelDownloadSettingDomain
     * @return
     */
    public ServiceResult updateModelDownloadSetting(ModelDownloadSettingDomain modelDownloadSettingDomain)
    {
        ServiceResult result = new ServiceResult();

        int flag = modelDownloadDao.updateModelDownloadSetting(modelDownloadSettingDomain);
        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("客户群下载配置信息更新失败");
        }

        return result;
    }

    /**
     * @param id
     * @return
     */
    public ServiceResult deleteModelDownloadSetting(int id)
    {
        ServiceResult result = new ServiceResult();

        int flag = modelDownloadDao.deleteModelDownloadSetting(id);
        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("客户群下载配置信息删除失败");
        }

        return result;
    }

    /**
     *
     * @return
     */
    public ServiceResult copyModelDownloadSetting(int copyItemId,int modelId,int userId)
    {
        ServiceResult result = new ServiceResult();
        int flag = modelDownloadDao.copyModelDownloadSetting(copyItemId,modelId,userId);
        if (flag != 1)
        {
            result.setRetValue(-1);
            result.setDesc("客户群下载配置信息复制失败");
        }

        return result;
    }
}
