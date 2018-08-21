package com.axon.market.core.service.itag;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.isystem.TagStatusEnum;
import com.axon.market.common.domain.isystem.TagAuditHistoryDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.domain.itag.TagDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.icommon.FileUploadService;
import com.axon.market.core.service.icommon.RefreshDataService;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.core.service.isystem.TagAuditHistoryService;
import com.axon.market.dao.mapper.itag.ITagMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by chenyu on 2017/1/17.
 */
@Component("tagService")
public class TagService
{
    private static final Logger LOG = Logger.getLogger(TagService.class.getName());

    @Autowired
    @Qualifier("tagDao")
    private ITagMapper tagDao;

    @Autowired
    @Qualifier("tagAuditHistoryService")
    private TagAuditHistoryService tagAuditHistoryService;

    @Autowired
    @Qualifier("fileUploadService")
    private FileUploadService fileUploadService;

    @Autowired
    @Qualifier("refreshDataService")
    private RefreshDataService refreshDataService;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    public static TagService getInstance()
    {
        return (TagService) SpringUtil.getSingletonBean("tagService");
    }

    /**
     * @return
     */
    public List<TagDomain> queryAllTags()
    {
        try
        {
            return tagDao.queryAllTags();
        }
        catch (Exception e)
        {
            LOG.error("Query All Tags Error. ", e);
            return null;
        }
    }

    /**
     * @param offset
     * @param limit
     * @return
     */
    public Table queryTagsByPage(String searchContent, Integer offset, Integer limit)
    {
        try
        {
            Integer count = tagDao.queryAllTagCounts(searchContent);
            List<TagDomain> list = tagDao.queryTagsByPage(searchContent, offset, limit);
            return new Table(list, count);
        }
        catch (Exception e)
        {
            LOG.error("Query Tags Error. ", e);
            return new Table();
        }
    }

    /**
     * @param fullTableName
     * @return
     */
    public TagDomain queryTagByTableName(String fullTableName)
    {
        if (fullTableName != null)
        {
            String[] result = fullTableName.split("\\.");
            if (result.length == 2)
            {
                return tagDao.queryTagByTableName(result[0], result[1]);
            }
        }
        return null;
    }

    /**
     * 新增/修改标签
     *
     * @param tagDomain
     * @param userDomain
     * @return
     */
    public Operation addOrEditTag(TagDomain tagDomain, UserDomain userDomain)
    {
        // 判断标签名称是否和数据库中存在的标签名称重复
        if (isTagExisted(tagDomain))
        {
            return new Operation(false, "标签名称或者标签对应表名称重复");
        }
        try
        {
            Boolean result;
            String message;
            //新增
            if (tagDomain.getId() == null||tagDomain.getId()==0)
            {
                tagDomain.setCreateUser(userDomain.getId());
                tagDomain.setCreateTime(TimeUtil.formatDate(new Date()));
                result = tagDao.createTag(tagDomain) == 1;
                message = result ? "新增标签成功" : "新增标签失败";
            }
            else
            {
                tagDomain.setLastUpdateUser(userDomain.getId());
                tagDomain.setLastUpdateTime(TimeUtil.formatDate(new Date()));
                result = tagDao.updateTag(tagDomain) == 1;
                message = result ? "更新标签成功" : "更新标签失败";
            }
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("addOrEditRemoteServer Error. ", e);
            return new Operation();
        }
    }

    /**
     * @param tagDomain
     * @return
     */
    public Boolean isTagExisted(TagDomain tagDomain)
    {
        Boolean isTagNameExisted;
        TagDomain oldTagDomain = tagDao.queryTagByName(tagDomain.getName());
        if (oldTagDomain == null)
        {
            isTagNameExisted = false;
        }
        else
        {
            isTagNameExisted = !oldTagDomain.getId().equals(tagDomain.getId());
        }

        Boolean isTagTableNameExisted;
        oldTagDomain = tagDao.queryTagByTableName(tagDomain.getDbSchema(), tagDomain.getTableName());
        if (oldTagDomain == null)
        {
            isTagTableNameExisted = false;
        }
        else
        {
            isTagTableNameExisted = !oldTagDomain.getId().equals(tagDomain.getId());
        }

        return isTagNameExisted || isTagTableNameExisted;
    }

    /**
     * @param id
     * @return
     */
    public Operation deleteTag(Integer id, Integer userId)
    {
        try
        {
            Boolean result = tagDao.deleteTag(id, userId, TimeUtil.formatDate(new Date())) == 1;
            String message = result ? "删除标签成功" : "删除标签失败";
            return new Operation(result, message);
        }
        catch (Exception e)
        {
            LOG.error("Delete Tag Error. ", e);
            return new Operation();
        }
    }

    /**
     * @return
     */
    public List<Map<String, String>> queryAllTagSchemaAndNames()
    {
        try
        {
            return tagDao.queryAllTagSchemaAndNames();
        }
        catch (Exception e)
        {
            LOG.error("Query Tag Schema And Names Error. ", e);
            return null;
        }
    }

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
    public Integer updateTagRefreshInfo(Integer tagId, String refreshTime, String result, String resultReason, Integer totalCount, Integer successCount, Integer failCount)
    {
        return tagDao.updateTagRefreshInfo(tagId, refreshTime, result, resultReason, totalCount, successCount, failCount);
    }

    /**
     * @param tagId
     * @return
     */
    public String queryTagRefreshTimeByTagId(Integer tagId)
    {
        return tagDao.queryTagRefreshTimeByTagId(tagId);
    }

    /**
     * @param request
     * @param tagId
     * @return
     */
    public Operation loadTagDataFromImportFile(HttpServletRequest request, Integer tagId)
    {
        Operation operation = new Operation();
        try
        {
            TagDomain tagDomain = tagDao.queryTagById(tagId);
            String fileName = tagDomain.getId() + "@" + tagDomain.getDbSchema() + "." + tagDomain.getTableName();
            File file = fileUploadService.fileUpload(request, fileName);
            refreshDataService.refreshLocalImportTagData(file);

            operation.setState(true);
            operation.setMessage("上传文件成功");
        }
        catch (Exception e)
        {
            LOG.error("Upload Local Import Tag File Error. ", e);
            operation.setMessage("上传文件失败");
        }
        return operation;
    }

    /**
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryNeedMeAuditTags(int userId)
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        // 用户记录审核历史中的id
        List<Integer> tagIds = new ArrayList<Integer>();

        List<Map<String, Object>> tagList = tagDao.queryAllAuditTagsByUser(userId);
        if (CollectionUtils.isEmpty(tagList))
        {
            return result;
        }

        try
        {
            // userAuditTagIndex（key为标签id，value为用户第几个审核标签）
            Map<Integer, Integer> userAuditTagIndex = calculateUserAuditTagIndex(tagList, tagIds, userId);
            // tagAuditInfo 标签审核记录
            List<Map<String, Object>> tagAuditInfo = tagDao.queryTagAuditInfo("(" + StringUtils.join(tagIds, ",") + ")");
            // 获取用户要审核的标签
            result = calculateUserAuditTags(tagList, tagAuditInfo, userAuditTagIndex);
            // 根据id排序
            Collections.sort(result, new Comparator<Map<String, Object>>()
            {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2)
                {
                    return Integer.parseInt(String.valueOf(o1.get("id"))) - (Integer.parseInt(String.valueOf(o2.get("id"))));
                }
            });
        }
        catch (IOException e)
        {
            LOG.error("Query My Audit Tags error. ", e);
        }

        return result;
    }

    /**
     * 计算用户是第几个审核标签
     * userAuditTagIndex（key为标签id，value为用户第几个审核标签）
     *
     * @param tagList
     * @param userId
     * @throws IOException
     */
    private Map<Integer, Integer> calculateUserAuditTagIndex(List<Map<String, Object>> tagList, List<Integer> tagIds, int userId) throws IOException
    {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        // 查询出需要用户审核的标签
        Iterator<Map<String, Object>> tagIterator = tagList.iterator();
        // 遍历查询出的需要审核的标签，把id和审核顺序加入返回map中
        while (tagIterator.hasNext())
        {
            Map<String, Object> tag = tagIterator.next();
            int tagId = Integer.parseInt(String.valueOf(tag.get("id")));
            String auditUsers = String.valueOf(tag.get("tagAuditUsers"));
            // 获取标签需要审核的人
            List<Map<String, String>> auditUserList = JsonUtil.stringToObject(auditUsers, new TypeReference<List<Map<String, String>>>()
            {
            });
            Iterator<Map<String, String>> auditUserIterator = auditUserList.iterator();
            while (auditUserIterator.hasNext())
            {
                Map<String, String> auditUser = auditUserIterator.next();
                int auditUserId = Integer.parseInt(auditUser.get("auditUser"));
                if (userId == auditUserId)
                {
                    tagIds.add(tagId);
                    // 标签id为key，用户审核该标签的次序为value
                    result.put(tagId, Integer.parseInt(auditUser.get("order")));
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @param tagList
     * @param tagAuditInfoList
     * @param userAuditTagIndex
     * @return
     */
    private List<Map<String, Object>> calculateUserAuditTags(List<Map<String, Object>> tagList, List<Map<String, Object>> tagAuditInfoList, Map<Integer, Integer> userAuditTagIndex)
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(tagList);
        List<Integer> tagIds = new ArrayList<Integer>();
        // 审核历史表中存在的id集合
        List<Integer> tagAuditIds = new ArrayList<Integer>();

        Iterator<Map<String, Object>> tagAuditInfoIterator = tagAuditInfoList.iterator();
        // 遍历获得审核历史表中存在的id集合
        while (tagAuditInfoIterator.hasNext())
        {
            Map<String, Object> tagAuditInfo = tagAuditInfoIterator.next();
            tagAuditIds.add(Integer.parseInt(String.valueOf(tagAuditInfo.get("id"))));
        }

        for (Map.Entry<Integer, Integer> tagEntry : userAuditTagIndex.entrySet())
        {
            int tagId = tagEntry.getKey(), order = tagEntry.getValue();
            tagAuditInfoIterator = tagAuditInfoList.iterator();

            Map<String, Object> tagAuditInfo;
            // 判断标签是否是第一次审核
            if (tagAuditIds.contains(tagId))
            {
                while (tagAuditInfoIterator.hasNext())
                {
                    tagAuditInfo = tagAuditInfoIterator.next();
                    if (Integer.parseInt(String.valueOf(tagAuditInfo.get("id"))) == tagId && (Integer.parseInt(String.valueOf(tagAuditInfo.get("count"))) + 1) == order)
                    {
                        tagIds.add(tagId);
                        break;
                    }
                }
            }
            else
            {
                if (order == 1)
                {
                    tagIds.add(tagId);
                }
            }
        }

        Iterator<Map<String, Object>> resultIterator = result.iterator();
        // 去除不满足的标签
        while (resultIterator.hasNext())
        {
            Map<String, Object> resultMap = resultIterator.next();
            if (!tagIds.contains(Integer.parseInt(String.valueOf(resultMap.get("id")))))
            {
                resultIterator.remove();
            }
        }

        return result;
    }

    /**
     * @param id
     * @param operate
     * @param reason
     * @param userDomain
     * @return
     */
    public ServiceResult submitTagAudit(int id, String operate, String reason, UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        try
        {
            boolean isApproveAudit = "approve".equals(operate);
            TagDomain tagDomain = tagDao.queryTagById(id);
            TagAuditHistoryDomain tagAuditHistoryDomain = getTagAuditHistoryDomain(id, userDomain.getId(), operate, reason);
            int insertSuccess = tagAuditHistoryService.insertTagAuditHistory(tagAuditHistoryDomain);
            if (insertSuccess > 0 && !isApproveAudit)
            {
                setTagStatus(id, TagStatusEnum.AUDIT_REJECT.getValue());
            }
            else if (insertSuccess > 0 && isApproveAudit)
            {
                List<TagAuditHistoryDomain> tagAuditHistoryDomains = tagAuditHistoryService.queryTagAuditHistoryDomain(id);

                String auditUsers = tagAuditHistoryDomains.get(0).getAuditUsers();
                List<Map<String, Object>> auditUserList = JsonUtil.stringToObject(auditUsers, new TypeReference<List<Map<String, Object>>>()
                {
                });
                List<Integer> needApproveUsers = queryAllAuditUserId(auditUserList);
                for (TagAuditHistoryDomain domain : tagAuditHistoryDomains)
                {
                    needApproveUsers.remove(domain.getAuditUser());
                }
                if (CollectionUtils.isEmpty(needApproveUsers))
                {
                    setTagStatus(id, TagStatusEnum.READY.getValue());
                }
            }
            else
            {
                result.setRetValue(-1);
                result.setDesc("标签审核操作异常");
                return result;
            }

            String userPhone = tagDao.queryUserPhoneOfCreateModelByModelId(id);
            if (StringUtils.isNotEmpty(userPhone))
            {
                String message = MessageFormat.format(smsConfigBean.getAuditNoticeSmsContent(), "标签【" + tagDomain.getName() + "】", userDomain.getName(), isApproveAudit ? "通过" : "拒绝");
                sendSmsService.sendAuditNoticeSms(userPhone, message);
            }
        }
        catch (Exception e)
        {
            LOG.error("Submit Tag Audit error. ", e);
            result.setRetValue(-1);
            result.setDesc("标签审核操作异常");
        }
        return result;
    }

    private TagAuditHistoryDomain getTagAuditHistoryDomain(Integer tagId, Integer auditUserId, String operate, String reason)
    {
        TagAuditHistoryDomain domain = new TagAuditHistoryDomain();
        domain.setTagId(tagId);
        domain.setAuditUser(auditUserId);
        domain.setAuditResult(operate);
        domain.setRemarks(reason);
        return domain;
    }

    /**
     * @param id
     * @param status
     * @return
     */
    private int setTagStatus(Integer id, Integer status)
    {
        return tagDao.setTagStatus(id, status);
    }

    /**
     * @param auditUserList
     * @return
     */
    private List<Integer> queryAllAuditUserId(List<Map<String, Object>> auditUserList)
    {
        List<Integer> result = new ArrayList<Integer>();

        for (Map<String, Object> map : auditUserList)
        {
            result.add(Integer.parseInt(String.valueOf(map.get("auditUser"))));
        }

        return result;
    }

}
