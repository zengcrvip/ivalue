package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.PicturesDomain;
import com.axon.market.common.domain.iscene.TempleTypeDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hale on 2016/12/13.
 */
@Component("imageMgrDao")
public interface IImageMgrMapper extends IMyBatisMapper
{
    List<PicturesDomain> queryImageMgrList(@Param(value = "name") String name, @Param(value = "offset") int offset, @Param(value = "limit") int limit);

    int queryImageMgrListCount(@Param(value = "name") String name);

    List<TempleTypeDomain> queryTempleTypeList();

    int queryScenesCount(@Param(value = "imgUrl") String imgUrl);

    int addImage(@Param(value = "info") PicturesDomain picturesDomain);

    int deleteImage(@Param(value = "id") int id, @Param(value = "isDelete") int isDelete, @Param(value = "editUserId") int editUserId, @Param(value = "editUserName") String editUserName);
}
