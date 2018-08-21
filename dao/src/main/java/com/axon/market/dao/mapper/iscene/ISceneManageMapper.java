package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.PicturesDomain;
import com.axon.market.common.domain.iscene.ScenesDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuan on 2016/12/22.
 */
@Component("sceneManageDao")
public interface ISceneManageMapper extends IMyBatisMapper
{
    List<ScenesDomain> getSceneList(@Param(value = "client") String client, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    Integer getSceneListCount(@Param(value = "client") String client);

    Integer addScene(@Param(value = "info") ScenesDomain info);

    Integer editScene(@Param(value = "info") ScenesDomain info);

    Integer deleteScene(@Param(value = "id") int id);

    List<ScenesDomain> getSceneContent(@Param(value = "sceneId") int sceneId);

    List<PicturesDomain> getPictureUrl(@Param(value = "tempId") int tempId);
}
