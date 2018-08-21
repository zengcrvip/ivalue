package com.axon.market.dao.mapper.itag;

import com.axon.market.common.domain.itag.TagDomain;
import com.axon.market.common.util.ValidateUtil;
import com.axon.market.dao.base.IMyBatisMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * Created by yangyang on 2016/1/27.
 */
@Component("tagDao")
public interface ITagMapper extends IMyBatisMapper
{
    /**
     * @return
     */
    Integer queryAllTagCounts(@Param(value = "searchContent") String searchContent);

    /**
     * @return
     */
    List<TagDomain> queryAllTags();

    /**
     * @param offset
     * @param limit
     * @return
     */
    List<TagDomain> queryTagsByPage(@Param(value = "searchContent") String searchContent, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

    /**
     * 根据id查询标签
     *
     * @param tagId
     * @return
     */
    TagDomain queryTagById(@Param(value = "tagId") Integer tagId);

    /**
     * @param name
     * @return
     */
    TagDomain queryTagByName(@Param(value = "name") String name);

    /**
     * @param dbSchema
     * @param tableName
     * @return
     */
    TagDomain queryTagByTableName(@Param(value = "dbSchema") String dbSchema, @Param(value = "tableName") String tableName);

    /**
     * 查询需要我审批的模型列表
     *
     * @param auditUserId
     * @return
     */
    List<Map<String, Object>> queryAllAuditTagsByUser(@Param(value = "auditUserId") Integer auditUserId);

    /**
     * 查询标签的审批记录
     *
     * @param tagIds
     * @return
     */
    List<Map<String, Object>> queryTagAuditInfo(@Param(value = "tagIds") String tagIds);

    /**
     * 根据标签id查询创建人的号码
     *
     * @param tagId
     * @return
     */
    String queryUserPhoneOfCreateModelByModelId(@Param(value = "tagId") Integer tagId);

    /**
     * 修改状态
     *
     * @param id
     * @param status
     * @return
     */
    Integer setTagStatus(@Param(value = "id") int id, @Param(value = "status") Integer status);

    /**
     * @param tagDomain
     * @return
     */
    Integer createTag(@Param(value = "info") TagDomain tagDomain);

    /**
     * @param tagDomain
     * @return <br>
     * 注意：该方法不能设置某字段为空！！！
     */
    Integer updateTag(@Param(value = "info") TagDomain tagDomain);

    /**
     * @param id
     * @return
     */
    Integer deleteTag(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId, @Param(value = "time") String time);

    /**
     * @return
     */
    List<Map<String, String>> queryAllTagSchemaAndNames();

    /**
     * @param tagId
     * @param refreshTime
     * @param result
     * @param resultReason
     * @param totalCount
     * @param successCount
     * @param failCount
     * @return
     */
    Integer updateTagRefreshInfo(@Param(value = "tagId") Integer tagId, @Param(value = "refreshTime") String refreshTime, @Param(value = "result") String result, @Param(value = "resultReason") String resultReason, @Param(value = "totalCount") Integer totalCount, @Param(value = "successCount") Integer successCount, @Param(value = "failCount") Integer failCount);

    /**
     * @param tagId
     * @return
     */
    String queryTagRefreshTimeByTagId(@Param(value = "tagId") Integer tagId);
}
