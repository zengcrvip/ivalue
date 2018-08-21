package com.axon.market.dao.mapper.icommon;

import com.axon.market.common.bean.MenuResult;
import com.axon.market.dao.base.IMyBatisMapper;
import com.axon.market.common.domain.icommon.MenuDomain;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/14.
 */
@Service("menuDao")
public interface IMenuMapper extends IMyBatisMapper
{
    List<MenuDomain> queryMenusByLevel(@Param(value = "level") Integer level);

    List<MenuDomain> queryAllMenus();

    List<MenuDomain> queryMenusByUser(@Param(value = "userId") Integer userId);

    Integer queryAllMenuCounts(@Param(value = "title") String title, @Param(value = "parentId") String parentId, @Param(value = "level") Integer level);

    List<Map<String, String>> queryMenusByPage(@Param(value = "title") String title, @Param(value = "parentId") String parentId, @Param(value = "level") Integer level, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    MenuDomain queryMenuById(@Param(value = "id") String id);

    MenuDomain queryMenuByTitle(@Param(value = "title") String title);

    int queryMaxSortNumberByParentId(@Param(value = "parentId") Integer parentId);

    List<MenuResult> querySortNumberByParentId(@Param(value = "parentId") Integer parentId);

    Integer createMenu(@Param(value = "info") MenuDomain menuDomain);

    Integer updateMenu(@Param(value = "info") MenuDomain menuDomain);

    Integer deleteMenu(@Param(value = "id") Integer id);

    Integer updateResetSort(@Param(value = "id") Integer id, @Param(value = "sort") Integer sort);
}
