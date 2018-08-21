package com.axon.market.dao.mapper.iscene;

import com.axon.market.common.domain.iscene.LocationDomain;
import com.axon.market.common.domain.iscene.LocationGroupDomain;

import com.axon.market.common.domain.iscene.ScenePilotDomain;
import com.axon.market.common.domain.iscene.TaskDomain;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2017/1/3.
 */
@Component("locationGroupDao")
public interface ILocationGroupMapper extends IMyBatisMapper
{
    //分页查询区域群组列表
    List<LocationGroupDomain> getLocationGroupList(@Param("offset") Integer offset,
                                                   @Param("limit") Integer limit,
                                                   @Param("groupName") String groupName);

    //查询分页条数
    int getLocationGroupListCount(@Param("offset") Integer offset,
                                  @Param("limit") Integer limit,
                                  @Param("groupName") String groupName);

    //新增区域群组
    int addLocationGroup(LocationGroupDomain localGroup);

    //修改区域群组名
    int editLocationGroup(@Param("id") Integer id, @Param("name") String name);

    //创建区域表
    void createLocationTable(@Param("tableName") String tableName);

    //分页查询对应的区域表
    List<LocationDomain> queryLocationList(@Param("offset") Integer offset,
                                           @Param("limit") Integer limit,
                                           @Param("tableName") String tableName);

    //查询区域表分页条数
    int queryLocationListCount(@Param("tableName") String tableName);

    //删除地区表数据
    int delLocation(@Param("id") Integer id, @Param("tableName") String tableName);

    //删除后更新地区群组count字段
    int updateAfterDelete(@Param("tableName") String tableName);

    //批量上传数据到数据库
    int batchUploadLocation(Map<String, Object> map);

    //查询符合条件的locationGroup表
    List<LocationGroupDomain> queryLocationGroupList(@Param("name") String uploadName);

    //更新locationGroup表中的tableName字段
    int updateTableName(@Param("tableName") String tableName,
                        @Param("id") Integer id);

    //删除废表
    void deleteTable(@Param("table") String tableName);

    //更新删除状态的用户群组表
    int updateLocationGroup(@Param("local") LocationGroupDomain localDomain);

    //批量插入后更新count字段
    int updateCountAfterAdd(@Param("table") String tableName, @Param("table1") String tableName1);

    //查询task
    List<ScenePilotDomain> queryTaskList();

    //逻辑删除locatinGroup
    int delLocationGroup(@Param("id") Integer id);
}
