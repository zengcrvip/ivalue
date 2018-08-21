package com.axon.market.dao.mapper.iscene;

import com.axon.market.dao.base.IMyBatisMapper;
import com.axon.market.common.domain.iscene.SmsSendConfigDomain;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/24.
 */
@Component("smsSendConfigDao")
public interface ISmsSendConfigMapper extends IMyBatisMapper
{
    /**
     * @param accessNumber
     * @return
     */
    int queryAllSmsSendConfigCount(String accessNumber);

    /**
     * @param offset
     * @param maxRecord
     * @param sortColumn
     * @return
     */
    List<SmsSendConfigDomain> querySmsSendConfigByPage(@Param(value = "offset") long offset, @Param(value = "maxRecord") long maxRecord, @Param(value = "sortColumn") String sortColumn, @Param(value = "accessNumber") String accessNumber);

    /**
     * @param accessNumber
     * @return
     */
    SmsSendConfigDomain querySmsSendConfigByAccessNumber(@Param(value = "accessNumber") String accessNumber);

    /**
     * @return
     */
    List<Map<String,String>> queryAllEffectiveAccessNumbers();

    /**
     * @param id
     * @return
     */
    SmsSendConfigDomain querySmsSendConfigById(@Param(value = "id") Integer id);

    /**
     * @param sendConfigDomain
     * @return
     */
    int insertSmsSendConfig(@Param(value = "info") SmsSendConfigDomain sendConfigDomain);

    /**
     * @param sendConfigDomain
     * @return
     */
    int updateSmsSendConfig(@Param(value = "info") SmsSendConfigDomain sendConfigDomain);

    /**
     * @param id
     * @return
     */
    int deleteSmsSendConfig(@Param(value = "id") Integer id);

    /**
     * @return
     */
    List<Map<String, Object>> getAccessNumber();

    /**
     * @param host
     * @return
     */
    int getAccessNumberCountByHost(@Param(value = "host") String host);

    /**
     * @param host
     * @return
     */
    List<Map<String, Object>> getAccessNumberByHost(@Param(value = "host") String host);

    /**
     * @param map
     * @return
     */
    int batchUpdateSmsSendConfig(@Param(value = "info") Map<String, String> map);

    /**
     * @param accessNumber
     * @param offset
     * @param limit
     * @return
     */
    List<SmsSendConfigDomain> querySmsSendConfigList(@Param(value = "accessNumber") String accessNumber, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    /**
     * @param areaCode
     * @return
     */
    SmsSendConfigDomain queryAccessNumberByAreacode(@Param(value = "areaCode") Integer areaCode);
}
