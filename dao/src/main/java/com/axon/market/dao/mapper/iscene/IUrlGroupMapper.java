package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.UrlDomain;
import com.axon.market.common.domain.iscene.UrlGroupDomain;
import com.axon.market.dao.base.IMyBatisMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by DELL on 2016/12/23.
 */
@Component("urlGroupDao")
public interface IUrlGroupMapper extends IMyBatisMapper
{
    //查询网址分类群组
    List<UrlGroupDomain> queryUrlGroup(@Param("offset") Integer offset,
                                       @Param("limit") Integer limit,
                                       @Param("groupName") String groupName,
                                       @Param("urlName") String urlName,
                                       @Param("urlWord") String urlWord);
    //分页查询网址分类条数
    int queryUrlGroupCount(@Param("groupName") String groupName,
                           @Param("urlName") String urlName,
                           @Param("urlWord") String urlWord);
    //查询网址
    List<UrlDomain> queryUrl(@Param("offset") Integer offset,
                             @Param("limit") Integer limit,
                             @Param("id") Integer id);
    //分页查询网址条数
    int queryUrlCount(@Param("id") Integer id);

    //查询导入类别的Id值
    UrlGroupDomain queryUrlByName(@Param("name") String name);

    void returnAddId(@Param("url") UrlGroupDomain url);

    //批量上传url
    void batchUpload(@Param("path") String fileNamePath);

    //更新url群组中count字段
    int updateUrlGroup(@Param("count") int count, @Param("fileName") String fileName);

    //修改url群组名称
    int updateUrlGroupName(@Param("newName") String name, @Param("id") Integer id, @Param("provinceId") Integer provinceId);

    //新增URL群组名称
    int addUrlGroupName(UrlGroupDomain url);

    //查询需要删除的网址分类是否在未删除的任务中
    int selectIsExistInTask(@Param("uid") Integer id);

    //根据id查询网址群组是否存在
    int selectIsExistById(@Param("id") Integer id);

    //根据Id删除网址群组
    int delUrlGroupById(@Param("id") Integer id);

    //根据Id删除单个网址
    int delUrlById(@Param("id") Integer Id);

    //删除后更新网址群组的统计字段
    int updateUrlGroupCount(@Param("count") Integer count, @Param("id") Integer groupId);

    //新增单条url数据
    int addUrl(@Param("urlDomain") UrlDomain urlDomain);
}
