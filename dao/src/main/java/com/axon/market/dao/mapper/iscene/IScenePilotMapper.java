package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.*;
import com.axon.market.common.domain.ischeduling.MarketJobDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuan on 2017/2/8.
 */
@Component("scenePilotDao")
public interface IScenePilotMapper  extends IMyBatisMapper
{
    List<ScenePilotDomain> getScenePilotList(@Param(value = "name") String name, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    Integer getScenePilotListCount(@Param(value = "name") String name);

    List<ScenePilotDomain> getFullPageList(@Param(value = "name") String name, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    Integer getFullPageListCount(@Param(value = "name") String name);

    Integer createScenePilot(@Param(value = "info") ScenePilotDomain scenePilotDomain);

    Integer updateScenePilot(@Param(value = "info") ScenePilotDomain scenePilotDomain);

    Integer deleteScenePilot(@Param(value = "id") Integer id);

    List<UrlGroupDomain> getUrlGroup();

    List<LocationGroupDomain> getLocationGroup();

    List<ScenesDomain> getSceneUrl();

    List<ExtStopCondConfig> getAdditionalList(@Param(value = "name") String name);

    Integer getTaskJobsCount(@Param(value = "value") Integer value);

    ScenePilotDomain queryScenePilotDomain(@Param(value = "id") Integer id);

    List<MarketJobDomain> selectTaskJobs(@Param(value = "typeValue") String typeValue);
}
