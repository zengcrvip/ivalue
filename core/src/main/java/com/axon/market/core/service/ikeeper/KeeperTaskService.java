package com.axon.market.core.service.ikeeper;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.ikeeper.TaskShowDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.SpringUtil;
import com.axon.market.dao.mapper.ikeeper.IKeeperTaskMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangtt on 2017/8/20.
 */
@Service("keeperTaskService")
public class KeeperTaskService
{

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    @Autowired
    @Qualifier("keeperTaskDao")
    IKeeperTaskMapper keeperTaskDao;

    private static Logger LOG = Logger.getLogger(KeeperTaskService.class.getName());

    public static  KeeperTaskService getInstance(){
        return (KeeperTaskService) SpringUtil.getSingletonBean("keeperTaskService");
    }

    /**
     * 查询掌柜业务类型
     *
     * @return
     */
    public List<Map<String, Object>> queryKeeperTaskType()
    {
        return keeperTaskDao.queryKeeperTaskType();
    }

    /**
     * 查询掌柜福利类型
     *
     * @return
     */
    public List<Map<String, Object>> queryKeeperWelfareType()
    {
        return keeperTaskDao.queryKeeperWelfareType();
    }

    /**
     * 查询掌柜策略规则
     *
     * @param typeId
     * @return
     */
    public List<Map<String, Object>> queryKeeperRuleByTypeId(Integer typeId)
    {
        return keeperTaskDao.queryKeeperRuleByTypeId(typeId);
    }

    /**
     * 导入任务客群
     *
     * @param fileInfo
     * @param tempFile
     * @return
     */
    @Transactional
    public ServiceResult importFile(Map<String, Object> fileInfo, File tempFile)
    {
        String path = tempFile.getPath();
        ServiceResult result = new ServiceResult();
        String fileId = "" + fileInfo.get("fileId");
        LineIterator iterator = null;
        List<Map<String, Object>> bufferList = new ArrayList<Map<String, Object>>();
        int rowNo = 0;
        int succNo = 0;
        int errorNo = 0;
        try
        {
            iterator = FileUtils.lineIterator(tempFile);
            while (iterator.hasNext())
            {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                String phone = iterator.next();
                if (phone == null || "".equals(phone))
                {
                    rowMap.put("rowData", "");
                    rowMap.put("status", "error");
                    rowMap.put("result", "号码不能为空");
                    errorNo++;
                }
                else if (!isPhoneFormat(phone))
                {
                    rowMap.put("rowData", phone);
                    rowMap.put("status", "error");
                    rowMap.put("result", "号码格式错误");
                    errorNo++;
                }
                else
                {
                    String encryptPhone = axonEncrypt.encrypt(phone);
                    rowMap.put("rowData", encryptPhone);
                    rowMap.put("status", "success");
                    rowMap.put("result", "导入成功");
                    succNo++;
                }
                rowMap.put("fileId", fileId);
                rowMap.put("rowNo", rowNo++);
                bufferList.add(rowMap);
                //每50个提交一次
                if (bufferList.size() % 50 == 0)
                {
                    int i = keeperTaskDao.batchImportRowData(bufferList);
                    bufferList.clear();
                    if (i < 1)
                    {
                        result.setRetValue(-1);
                        result.setDesc("上传失败");
                        return result;
                    }
                }
            }
            // 遍历完看缓冲列表是否还有数据，如果有则入库
            if (bufferList.size() > 0)
            {
                int i = keeperTaskDao.batchImportRowData(bufferList);
                if (i < 1)
                {
                    result.setRetValue(-1);
                    result.setDesc("导入失败");
                    return result;
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("导入掌柜任务客群", e);
            return new ServiceResult(-1, "导入失败");
        }
        finally
        {
            try
            {
                iterator.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (!tempFile.delete())
                {
                    LOG.error("删除临时文件异常:" + path);
                }
            }
        }
        fileInfo.put("status", "YBC");
        fileInfo.put("result", "共导入:" + rowNo + "条 ; 成功 : " + succNo + "条 ; 失败 : " + errorNo + "条。");
        keeperTaskDao.insertFile(fileInfo);
        return new ServiceResult(0, "存储文件成功, 导入:" + rowNo + "条 ; 成功 : " + succNo + "条 ; 失败 : " + errorNo + "条。");
    }

    /**
     * 保存掌柜任务客群
     *
     * @param paras
     * @return
     */
    public Map<String, Object> saveKeeperTaskCustomer(Map<String, Object> paras)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Long fileId = (Long) paras.get("fileId");
        int num = keeperTaskDao.saveKeeperTaskCustomer(fileId);
        if (num < 1)
        {
            result.put("retValue", -1);
            result.put("desc", "保存导入掌柜任务客群号码文件异常");
        }
        result.put("retValue", 0);
        result.put("desc", "OK");
        result.put("num", num);
        return result;
    }


    /**
     * 创建掌柜任务
     *
     * @param paraMap
     * @return
     */
    @Transactional
    public ServiceResult createKeeperTask(Map<String, Map<String, Object>> paraMap)
    {
        try
        {
            // 获取任务主体参数
            Map<String, Object> keeperTaskMap = paraMap.get("keeperTaskMap");
            int i = keeperTaskDao.createKeeperTask(keeperTaskMap);
            if (i < 1)
            {
                return new ServiceResult(-1, "创建任务失败");
            }
            String taskId = String.valueOf(keeperTaskMap.get("taskId"));
            if (StringUtils.isEmpty(taskId))
            {
                return new ServiceResult(-1, "创建任务失败");
            }
            // 获取任务渠道参数
            Map<String, Object> keeperChannelMap = paraMap.get("keeperChannelMap");
            keeperChannelMap.put("taskId", taskId);
            int channelType = Integer.parseInt(String.valueOf(keeperChannelMap.get("channelType")));
            if (channelType == 2)
            { // 话+
                String decodeStr = URLDecoder.decode(String.valueOf(keeperChannelMap.get("outbandContent")),"utf-8");
                keeperChannelMap.put("outbandContent",decodeStr);
                keeperTaskDao.createKeeperPhoneChannel(keeperChannelMap);
            }
            else if (channelType == 1)
            { // 短信
                String decodeStr = URLDecoder.decode(String.valueOf(keeperChannelMap.get("smsContent")),"utf-8");
                keeperChannelMap.put("smsContent",decodeStr);
                keeperTaskDao.createKeeperSmsChannel(keeperChannelMap);
            }
            else if (channelType == 0)
            { // 短信 & 话+
                //话+
                keeperChannelMap.put("channelType", 2);
                String decodeStr = URLDecoder.decode(String.valueOf(keeperChannelMap.get("outbandContent")),"utf-8");
                keeperChannelMap.put("outbandContent",decodeStr);
                keeperTaskDao.createKeeperPhoneChannel(keeperChannelMap);
                // 短信
                keeperChannelMap.put("channelType", 1);
                String decodeSmsStr = URLDecoder.decode(String.valueOf(keeperChannelMap.get("smsContent")),"utf-8");
                keeperChannelMap.put("smsContent",decodeSmsStr);
                keeperTaskDao.createKeeperSmsChannel(keeperChannelMap);
            }
            // 获取任务审批信息
            Map<String, Object> keeperAuditMap = paraMap.get("keeperAuditMap");
            keeperAuditMap.put("taskId", taskId);
            keeperTaskDao.createKeeperAudit(keeperAuditMap);
            //获取任务规则信息
            Map<String, Object> keeperRuleMap = paraMap.get("keeperRuleMap");
            keeperRuleMap.put("taskId", taskId);
            keeperTaskDao.createKeeperRemindRule(keeperRuleMap);
            if (keeperRuleMap.get("failureRuleId") != null)
            {
                keeperTaskDao.createKeeperFailureRule(keeperRuleMap);
            }
        }
        catch (Exception e)
        {
            LOG.error("createKeeperTask", e);
            return new ServiceResult(-1, "创建任务失败");
        }
        return new ServiceResult();
    }

    /**
     * 更新任务信息
     *
     * @param paraMap
     * @return
     */
    public ServiceResult updateKeeperTask(Map<String, Map<String, Object>> paraMap)
    {
        try
        {
            // 获取任务主体参数
            Map<String, Object> keeperTaskMap = paraMap.get("keeperTaskMap");
            Integer taskId = Integer.parseInt(String.valueOf(keeperTaskMap.get("taskId")));
            int i = keeperTaskDao.updateKeeperTask(keeperTaskMap);
            if (i < 1)
            {
                return new ServiceResult(-1, "更新失败");
            }
            // 获取任务渠道参数
            Map<String, Object> keeperChannelMap = paraMap.get("keeperChannelMap");
            // 更新之前失效渠道
            keeperTaskDao.deleteOldTaskChannel(taskId);
            // 插入新的渠道信息
            keeperChannelMap.put("taskId", taskId);
            int channelType = Integer.parseInt(String.valueOf(keeperChannelMap.get("channelType")));
            if (channelType == 2)
            { // 话+
                String decodeStr = URLDecoder.decode(String.valueOf(keeperChannelMap.get("outbandContent")),"utf-8");
                keeperChannelMap.put("outbandContent",decodeStr);
                keeperTaskDao.createKeeperPhoneChannel(keeperChannelMap);
            }
            else if (channelType == 1)
            { // 短信
                String decodeStr = URLDecoder.decode(String.valueOf(keeperChannelMap.get("smsContent")),"utf-8");
                keeperChannelMap.put("smsContent",decodeStr);
                keeperTaskDao.createKeeperSmsChannel(keeperChannelMap);
            }
            else if (channelType == 0)
            { // 短信 & 话+
                // 话+
                keeperChannelMap.put("channelType", 2);
                String decodeStr = URLDecoder.decode(String.valueOf(keeperChannelMap.get("outbandContent")),"utf-8");
                keeperChannelMap.put("outbandContent",decodeStr);
                keeperTaskDao.createKeeperPhoneChannel(keeperChannelMap);
                // 短信
                keeperChannelMap.put("channelType", 1);
                String decodeSmsStr = URLDecoder.decode(String.valueOf(keeperChannelMap.get("smsContent")),"utf-8");
                keeperChannelMap.put("smsContent",decodeSmsStr);
                keeperTaskDao.createKeeperSmsChannel(keeperChannelMap);
            }
            // 获取任务审批信息
            Map<String, Object> keeperAuditMap = paraMap.get("keeperAuditMap");
            // 更新之前删除审核信息
            keeperTaskDao.deleteOldTaskAudit(taskId);
            keeperAuditMap.put("taskId", taskId);
            keeperTaskDao.createKeeperAudit(keeperAuditMap);
            //获取任务规则信息
            Map<String, Object> keeperRuleMap = paraMap.get("keeperRuleMap");
            // 更新之前删除任务规则信息
            keeperTaskDao.deleteOldTaskRule(taskId);
            keeperRuleMap.put("taskId", taskId);
            keeperTaskDao.createKeeperRemindRule(keeperRuleMap);
            if (keeperRuleMap.get("failureRuleId") != null)
            {
                keeperTaskDao.createKeeperFailureRule(keeperRuleMap);
            }
            return new ServiceResult();
        }
        catch (Exception e)
        {
            LOG.error("更新掌柜任务信息失败", e);
            return new ServiceResult(-1, "更新掌柜任务信息失败");
        }
    }

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    public Table<Map<String, Object>> queryKeeperTaskByPage(Map<String, Object> param)
    {
        try
        {
            List<Map<String, Object>> resultList = keeperTaskDao.queryKeeperTaskByPage(param);
            int count = keeperTaskDao.queryKeeperTaskByCount(param);
            return new Table<>(resultList, count);
        }
        catch (Exception e)
        {
            LOG.error("queryKeeperTaskByPage", e);
            return new Table<>();
        }
    }

    /**
     * 根据id查询任务详情
     *
     * @param taskId
     * @return
     */
    public TaskShowDomain queryKeeperTaskById(Integer taskId)
    {
        return keeperTaskDao.queryTaskById(taskId);
    }

    /**
     * 删除任务
     *
     * @param taskId
     * @return
     */
    public ServiceResult deleteKeeperTask(Integer taskId)
    {
        try
        {
            int i = keeperTaskDao.deleteKeeperTask(taskId);
            if (i < 1)
            {
                return new ServiceResult(-1, "删除失败");
            }
        }
        catch (Exception e)
        {
            LOG.error("deleteKeeperTask", e);
            return new ServiceResult(-1, "操作异常");
        }
        return new ServiceResult(0, "删除成功！");
    }

    /**
     * 查询任务是否已经存在
     *
     * @param taskName
     * @return
     */
    public ServiceResult queryTaskNameIsExist(String taskName)
    {
        boolean isExist = keeperTaskDao.queryTaskNameIsExist(taskName) > 0;
        if (isExist)
        {
            return new ServiceResult(-1, "任务已经存在");
        }
        return new ServiceResult();
    }

    /**
     * 查询需要我审核的任务
     *
     * @param userId
     * @return
     */
    public Table<Map<String, Object>> queryNeedMeAuditKeeperTask(String taskName,Integer userId, Integer limit, Integer offset)
    {
        try
        {
            int count = keeperTaskDao.queryNeedMeAuditKeeperTaskCount(taskName,userId);
            List<Map<String, Object>> paramList = keeperTaskDao.queryNeedMeAuditKeeperTask(taskName,userId, limit, offset);
            return new Table<Map<String, Object>>(paramList, count);
        }
        catch (Exception e)
        {
            LOG.error("queryNeedMeAuditKeeperTask", e);
            return new Table<>();
        }
    }

    /**
     * 审核任务信息
     *
     * @param param
     * @param userId
     * @return
     */
    @Transactional
    public ServiceResult auditKeeperTask(Map<String, Object> param, Integer userId)
    {
        try
        {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            int auditResult = Integer.parseInt(String.valueOf(param.get("operate")));
            int taskId = Integer.parseInt(String.valueOf(param.get("id")));
            paramMap.put("auditState", auditResult);
            paramMap.put("auditDesc", param.get("reason"));
            paramMap.put("taskId", taskId);
            paramMap.put("userId", userId);
            int i = keeperTaskDao.auditKeeperTask(paramMap);
            if (i < 1)
            {
                return new ServiceResult(-1, "审核失败");
            }
            // 更新任务状态
            int t = keeperTaskDao.updateKeeperTaskState(auditResult, taskId);
            if (t < 1)
            {
                return new ServiceResult(-1, "审核失败");
            }
            return new ServiceResult();
        }
        catch (Exception e)
        {
            LOG.error("auditKeeperTask error", e);
            return new ServiceResult(-1, "审核异常");
        }
    }


    /**
     * 查询审批失败原因
     *
     * @param param
     * @return
     */
    public Map<String, String> queryAuditFailureReason(Map<String, Object> param)
    {
        Map<String, String> result = new HashMap<String, String>();
        String taskStr = String.valueOf(param.get("taskId"));
        if (StringUtils.isEmpty(taskStr))
        {
            result.put("reason", "");
            return result;
        }
        Integer taskId = Integer.parseInt(taskStr);
        String reason = "";
        try
        {
            reason = keeperTaskDao.queryAuditFailureReason(taskId);
        }
        catch (Exception e)
        {
            LOG.error("queryAuditFailureReason", e);
        }
        result.put("reason", reason);
        return result;
    }

    /**
     * 终止掌柜任务
     *
     * @param param
     * @return
     */
    @Transactional
    public ServiceResult terminateKeeperTask(Map<String, Object> param)
    {
        String taskStr = String.valueOf(param.get("taskId"));
        if (StringUtils.isEmpty(taskStr))
        {
            return new ServiceResult(-1, "无效的任务ID");
        }
        Integer taskId = Integer.parseInt(taskStr);
        int i = 0;
        try
        {
            i = keeperTaskDao.terminateKeeperTask(taskId);// 先终止任务
            keeperTaskDao.terminateKeeperTaskInst(taskId);// 再终止任务实例
        }
        catch (Exception e)
        {
            LOG.error("terminateKeeperTask", e);
            return new ServiceResult(-1, "终止异常");
        }
        if (i < 0)
        {
            return new ServiceResult(-1, "终止失败");
        }
        return new ServiceResult();
    }

    /**
     * 到期任务置为过期状态 (5)
     * @param date
     * @return
     */
    public int expireKeeperTask(String date){
        int i = keeperTaskDao.expireKeeperTask(date);
        return i;
    }


    /**
     * 私有方法验证手机号格式
     *
     * @param param
     * @return
     */
    private static boolean isPhoneFormat(String param)
    {
        boolean isPhone = false;
        Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9]\\d{9}$");
        Matcher m = p.matcher(param);
        boolean b = m.matches();
        if (b)
        {
            isPhone = true;
        }
        return isPhone;
    }

}
