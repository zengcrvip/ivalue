package com.axon.market.core.service.iflow;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.domain.iflow.OldCustomerDomain;
import com.axon.market.common.domain.iservice.OldCustomerResultDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.*;
import com.axon.market.common.util.excel.ExcelReader;
import com.axon.market.dao.mapper.iflow.IOldCustomerMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangtt on 2017/7/24.
 */
@Service("oldCustomerService")
public class OldCustomerService
{
    private static Logger LOG = Logger.getLogger(OldCustomerService.class.getName());

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    @Autowired
    @Qualifier("oldCustomerDao")
    private IOldCustomerMapper oldCustomerDao;


    public static OldCustomerService getInstance()
    {
        return (OldCustomerService) SpringUtil.getSingletonBean("oldCustomerService");
    }

    /**
     * 文件保存
     *
     * @param fileInfo 源文件
     * @param is       输入流
     * @param type     导入用户文件类别
     * @return
     */
    @Transactional
    public ServiceResult storeFile(Map<String, Object> fileInfo, InputStream is, String type)
    {
        ServiceResult result = new ServiceResult();
        Boolean isExceed = false;
        Boolean isExceedShopUserBlackList = false;
        List<String[]> fileData = null;
        String fileId = "" + fileInfo.get("fileId");

        int rowNo = 0;
        try
        {
            fileData = readXlsFile(is, 2);
            if (fileData.size() == 0)
            {
                return new ServiceResult(-1, "数据行数为0");
            }
            else
            {
                for (String[] row : fileData)
                {
                    if (!row[0].startsWith("1") && !row[0].startsWith("86"))
                    {
                        continue;
                    }

                    if ((type == "appointUser" || type == "blackUser") && rowNo >= 5000)
                    {
                        isExceed = true;
                        break;
                    }
                    else if (type == "shopUserBlackList" && rowNo >= 5000)
                    {
                        isExceedShopUserBlackList = true;
                        break;
                    }

                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    rowMap.put("fileId", fileId);
                    rowMap.put("rowNo", rowNo++);
                    if (type != "shopUserBlackList")
                    {
                        String encryptPhone = axonEncrypt.encrypt(row[0]);
                        rowMap.put("rowData", encryptPhone);
                    }
                    else
                    {
                        rowMap.put("rowData", row[0]);
                    }
                    rowMap.put("status", "success");
                    rowMap.put("result", "导入完成");
                    if (row.length == 1)
                    {
                        //校验号码
                        if (row[0] == null || "".equals(row[0]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "号码不能为空");
                        }
                        else
                        {
                            if (!isPhoneFormat(row[0]))
                            {
                                rowMap.put("status", "error");
                                rowMap.put("result", "号码格式不规范");
                            }
                        }
                    }
                    else
                    {
                        rowMap.put("status", "error");
                        rowMap.put("result", "不符合模板规定列数");
                    }
                    oldCustomerDao.insertRow(rowMap);
                }
            }
            fileInfo.put("status", "YBC");
            fileInfo.put("result", "已存储");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOG.error("shop task storeFile", e);
            fileInfo.put("status", "error");
            fileInfo.put("result", "存储失败");
            return new ServiceResult(-1, "存储内容异常");
        }
        oldCustomerDao.insertFile(fileInfo);

        if (isExceed)
        {
            return new ServiceResult(0, "存储文件超过5000条 只存储5000条, 共导入用户:" + (rowNo));
        }
        else if (isExceedShopUserBlackList)
        {
            return new ServiceResult(0, "存储文件超过5000条 只存储5000条, 共导入用户:" + (rowNo));
        }


        return new ServiceResult(0, "存储文件成功, 总号码数:" + rowNo);
    }

    /**
     * 文件导入
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
                    int i = oldCustomerDao.batchImportRowData(bufferList);
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
                int i = oldCustomerDao.batchImportRowData(bufferList);
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
            LOG.error("导入老用户优惠指定用户失败", e);
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
        fileInfo.put("result", "已存储");
        oldCustomerDao.insertFile(fileInfo);
        return new ServiceResult(0, "存储文件成功, 导入:" + rowNo + "行 ; 成功 : " + succNo + "行 ; 失败 : " + errorNo + "行。");
    }


    /**
     * 保存线下营业厅信息
     *
     * @param fileInfo
     * @param is
     * @return
     */
    @Transactional
    public ServiceResult storeConfBaseInfoFile(Map<String, Object> fileInfo, InputStream is, Integer maxNum)
    {
        List<String> baseInfoCodeList = queryBaseInfoCodeList();
        ServiceResult result = new ServiceResult();
        boolean isFull = false;
        List<String[]> fileData = null;
        String fileId = "" + fileInfo.get("fileId");
        int rowNo = 0;

        try
        {
            fileData = readXlsFile(is, 0);//从第一行开始读
            if (fileData.size() == 0)
            {
                return new ServiceResult(-1, "数据行数为0");
            }
            else
            {
                for (String[] row : fileData)
                {
                    // 正则判断是否是中文
                    if (isContainChinese(row[0]))
                    {
                        continue;
                    }
                    if (rowNo >= maxNum)
                    {
                        isFull = true;
                        break;
                    }
                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    rowMap.put("fileId", fileId);
                    rowMap.put("rowNo", rowNo++);
                    rowMap.put("rowData", row[0]);
                    rowMap.put("status", "success");
                    rowMap.put("result", "导入完成");
                    if (row.length == 1)
                    {
                        if (StringUtils.isEmpty(row[0]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "营业厅编码不能为空");
                        }
                        //  营业厅编码格式校验
                        if (!baseInfoCodeList.contains(row[0]))
                        {
                            rowMap.put("status", "error");
                            rowMap.put("result", "营业厅编码不存在");
                        }

                    }
                    else
                    {
                        rowMap.put("status", "error");
                        rowMap.put("result", "不符合模板规定列数");
                    }
                    oldCustomerDao.insertRow(rowMap);
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("upload ConfBaseInfo error", e);
            fileInfo.put("status", "error");
            fileInfo.put("result", "存储失败");
            return new ServiceResult(-1, "存储失败");
        }
        fileInfo.put("status", "Odd");//文件入临时表成功标识
        fileInfo.put("result", "已存储");
        oldCustomerDao.insertFile(fileInfo);
        if (isFull)
        {
            return new ServiceResult(0, "存储文件超过" + maxNum + "条 只存储" + maxNum + "条, 共导入营业厅:" + (rowNo));
        }
        return new ServiceResult(0, "存储文件成功, 总号码数:" + rowNo);
    }

    /**
     * 临时保存导入指定用户数据
     *
     * @return
     */
    public Map<String, Object> saveAppointUsersImport(Map<String, Object> paras)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Long fileId = (Long) paras.get("fileId");
        int num = oldCustomerDao.saveAppointUsersImport(fileId);
        if (num < 1)
        {
            result.put("retValue", -1);
            result.put("desc", "保存导入指定用户号码文件异常");
        }
        result.put("retValue", 0);
        result.put("desc", "OK");
        result.put("num", num);
        return result;
    }


    /**
     * 临时保存导入免打扰用户数据
     *
     * @return
     */
    public Map<String, Object> saveBlackUsersImport(Map<String, Object> paras)
    {
        Long fileId = (Long) paras.get("fileId");
        Map<String, Object> result = new HashMap<String, Object>();
        int num = oldCustomerDao.saveBlackUsersImport(fileId);
        if (num < 1)
        {
            result.put("retValue", -1);
            result.put("desc", "保存导入免打扰用户号码文件异常");
            return result;
        }
        result.put("retValue", 0);
        result.put("desc", "OK");
        result.put("num", num);
        return result;
    }

    /**
     * 临时保存指定营业厅编码
     *
     * @param paras
     * @return
     */
    public Map<String, Object> saveBaseInfoImport(Map<String, Object> paras)
    {
        Long fileId = (Long) paras.get("fileId");
        Map<String, Object> result = new HashMap<String, Object>();
        int num = oldCustomerDao.saveBaseInfoImport(fileId);
        if (num < 1)
        {
            result.put("retValue", -1);
            result.put("desc", "保存导入指定营业厅编码文件异常");
            return result;
        }
        result.put("retValue", 0);
        result.put("desc", "OK");
        result.put("num", num);
        return result;
    }

    /**
     * 新增老用户营销任务
     *
     * @param oldCustomerDomain
     * @param userDomain
     * @return
     */
    @Transactional
    public ServiceResult insertOldCustomerPreferentialActivity(OldCustomerDomain oldCustomerDomain, UserDomain userDomain) throws Exception
    {
        ServiceResult serviceResult = new ServiceResult();
        // 第一步 数据入 preferential_task 表
        // (1) 获取营销地区信息组装字符串
        String marketAreaCode = oldCustomerDomain.getMarketAreaCode();
        if (StringUtils.isEmpty(marketAreaCode))
        {
            //取不到地区编码，返回空
            oldCustomerDomain.setAreaDesc("");
        }
        else
        {
            List<String> cityNameList = oldCustomerDao.queryMarketAreaDesc(marketAreaCode);
            StringJoiner stringJoiner = new StringJoiner(",");
            for (String str : cityNameList)
            {
                stringJoiner.add(str);
            }
            oldCustomerDomain.setAreaDesc(stringJoiner + "");
        }
        oldCustomerDao.insertOlderCustomer(oldCustomerDomain);
        Integer taskId = oldCustomerDomain.getTaskId();// 获取新增任务的id
        // （2）处理线下营业厅
        String baseType = oldCustomerDomain.getBaseType();// 多个类型英文逗号分隔
        String businessFileId = oldCustomerDomain.getAppointBusinessHall();
        // 第二步 任务绑定指定营业厅
        if (!StringUtils.isEmpty(businessFileId))
        {
            int i = oldCustomerDao.insertTaskToBase(businessFileId, baseType, taskId, marketAreaCode);
            // 更新指定线下营业厅描述
//           int baseInfoCount = oldCustomerDao.queryAppointBaseInfoById(taskId);
        }
        serviceResult.setRetValue(0);
        serviceResult.setDesc("新增成功");
        return serviceResult;
    }

    /**
     * 查询营业厅类型
     *
     * @return
     */
    public List<Map<String, Object>> queryLocationType()
    {
        return oldCustomerDao.queryLocationType();
    }

    /**
     * 分页查询老用户优惠活动
     *
     * @param param
     * @return
     */
    public Table<Map<String, Object>> queryOldCustomerByPage(Map<String, Object> param)
    {
        String name = param.get("taskName") == null ? "" : String.valueOf(param.get("taskName"));
        String taskName = SearchConditionUtil.optimizeCondition(name);
        String taskSource = String.valueOf(param.get("taskSource"));
        String areaCode = String.valueOf(param.get("areaCode"));
        String status = String.valueOf(param.get("status"));
        String limit = String.valueOf(param.get("length"));
        String offset = String.valueOf(param.get("start"));
        String userAreaId = String.valueOf(param.get("userAreaId"));
        String taskType = String.valueOf(param.get("taskType"));
        try
        {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("taskName", taskName);
            paramMap.put("taskSource", taskSource);
            paramMap.put("areaCode", areaCode);
            paramMap.put("status", status);
            paramMap.put("userAreaId", userAreaId);
            paramMap.put("taskType", taskType);
            int count = oldCustomerDao.queryOldCustomerByCount(paramMap);
            paramMap.put("limit", limit);
            paramMap.put("offset", offset);
            List<Map<String, Object>> dataList = oldCustomerDao.queryOldCustomerByPage(paramMap);
            return new Table<>(dataList, count);
        }
        catch (Exception e)
        {
            LOG.error("queryOldCustomerByPage error ", e);
            return new Table<>();
        }
    }

    /**
     * 分页查询已上线老用户优惠活动
     *
     * @param param
     * @return
     */
    public Table<Map<String, Object>> queryAllOldCustomerByPage(Map<String, Object> param)
    {
        String name = param.get("taskName") == null ? "" : String.valueOf(param.get("taskName"));
        String taskName = SearchConditionUtil.optimizeCondition(name);
        String taskSource = String.valueOf(param.get("taskSource"));
        String areaCode = String.valueOf(param.get("areaCode"));
        String status = String.valueOf(param.get("status"));
        String limit = String.valueOf(param.get("length"));
        String offset = String.valueOf(param.get("start"));
        String userAreaId = String.valueOf(param.get("userAreaId"));
        String taskType = String.valueOf(param.get("taskType"));
        try
        {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("taskName", taskName);
            paramMap.put("taskSource", taskSource);
            paramMap.put("areaCode", areaCode);
            paramMap.put("status", status);
            paramMap.put("userAreaId", userAreaId);
            paramMap.put("taskType", taskType);
            int count = oldCustomerDao.oldCustomerCheckOutCounts(paramMap);
            paramMap.put("limit", limit);
            paramMap.put("offset", offset);
            List<Map<String, Object>> dataList = oldCustomerDao.oldCustomerCheckOut(paramMap);
            return new Table<>(dataList, count);
        }
        catch (Exception e)
        {
            LOG.error("queryOnlineOldCustomerByPage error ", e);
            return new Table<>();
        }
    }


    /**
     * 预览老用户活动优惠信息
     *
     * @param taskId
     * @return
     */
    public OldCustomerDomain previewOldCustomer(Integer taskId)
    {
        try
        {
            OldCustomerDomain oldCustomerDomain = oldCustomerDao.previewOldCustomer(taskId);
//            int baseInfoCount = oldCustomerDao.queryAppointBaseInfoById(taskId);
//            oldCustomerDomain.setAppointBusinessHallDesc("成功导入线下营业厅:" + baseInfoCount + "个");
            return oldCustomerDomain;
        }
        catch (Exception e)
        {
            LOG.error("query oldCustomerTaskDetail error:", e);
            return null;
        }
    }

    /**
     * 更新老用户活动信息
     *
     * @param domain
     * @return
     */
    @Transactional
    public ServiceResult updateOldCustomer(OldCustomerDomain domain, Integer userId) throws Exception
    {
        ServiceResult serviceResult = new ServiceResult();
        // 第一步 处理营销地区描述
        Integer taskId = domain.getTaskId();
//        String oldBaseType = oldCustomerDao.queryOldCustomerBaseType(taskId);
        String baseType = domain.getBaseType();
        String marketAreaCode = domain.getMarketAreaCode();
        if (StringUtils.isEmpty(marketAreaCode))
        {
            //取不到地区编码，返回空
            domain.setAreaDesc("");
        }
        else
        {
            List<String> cityNameList = oldCustomerDao.queryMarketAreaDesc(marketAreaCode);
            StringJoiner stringJoiner = new StringJoiner(",");
            for (String str : cityNameList)
            {
                stringJoiner.add(str);
            }
            domain.setAreaDesc(stringJoiner + "");
        }
        // 第二步 更新 preferential_task 表数据
        int i = oldCustomerDao.updateOldCustomer(domain, userId);
        // 第三步 更新 preferential_task_2_base 表数据
        String baseInfoFileId = domain.getAppointBusinessHall();
        if (!StringUtils.isEmpty(baseInfoFileId))
        {
            // 更新了营业厅编码
            // (1) 删除已经绑定的营业厅
            oldCustomerDao.deleteOldCustomerBaseInfo(taskId);
            // (2) 重新插入2base表
            oldCustomerDao.insertTaskToBase(baseInfoFileId, baseType, taskId, marketAreaCode);
        }
        // 第四步 删除审核历史
        oldCustomerDao.delAuditHistory(taskId);
        serviceResult.setRetValue(0);
        serviceResult.setDesc("更新成功");
        return serviceResult;
    }

    /**
     * 审批
     *
     * @param param
     * @return
     */
    @Transactional
    public ServiceResult auditOldCustomerTask(Map<String, Object> param, UserDomain userDomain)
    {
        ServiceResult result = new ServiceResult();
        Integer userId = userDomain.getId();
        String auditResult = "against";// approve:通过；against;拒绝
        Boolean isAgree = false;// 默认不通过
        if (Integer.parseInt(String.valueOf(param.get("operate"))) == 0)
        {
            auditResult = "approve";
            isAgree = true;
        }
        Integer taskId = Integer.parseInt(String.valueOf(param.get("id")));
        String auditDesc = String.valueOf(param.get("reason"));
        try
        {
            //先把审批信息记录进历史表
            Map<String, Object> auditInfo = new HashMap<String, Object>();
            auditInfo.put("taskId", taskId);
            auditInfo.put("auditUserId", userId);
            auditInfo.put("auditResult", auditResult);
            auditInfo.put("remarks", auditDesc);
            int i = oldCustomerDao.insertIntoAuditHistory(auditInfo);
            if (i > 0 && isAgree)
            {
                // 通过
                // 查询审批历史
                List<Map<String, Object>> historyList = oldCustomerDao.queryOldCustomerAuditHistory(taskId);
                String auditUsers = String.valueOf(historyList.get(0).get("auditUsers"));
                List<Map<String, Object>> auditUserList = JsonUtil.stringToObject(auditUsers, new TypeReference<List<Map<String, Object>>>()
                {
                });
                // 获取该任务所有审核人id
                List<Integer> auditUserIds = queryAllAuditUserId(auditUserList);
                for (Map<String, Object> map : historyList)
                {
                    auditUserIds.remove(map.get("auditUser"));
                }
                if (CollectionUtils.isEmpty(auditUserIds))
                {
                    //我是最终审批人，审批通过
                    oldCustomerDao.updateOldCustomerById(ShopTaskStatusEnum.TASK_READY.getValue(), taskId);
                    // todo ...
                }
            }
            else if (i > 0 && !isAgree)
            {
                // 不通过
                oldCustomerDao.updateOldCustomerById(ShopTaskStatusEnum.TASK_FAIL.getValue(), taskId);
            }
            else
            {
                // 异常
                result.setRetValue(-1);
                result.setDesc("审核操作异常");
                return result;
            }
            result.setRetValue(0);
            result.setDesc("审批成功");
            return result;
        }
        catch (Exception e)
        {
            LOG.error("auditOldCustomerTask error", e);
            result.setRetValue(-1);
            result.setDesc("审批操作异常");
            return result;
        }
    }

    /**
     * 查出该任务所有审核人
     *
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


    /**
     * 查询需要审核的老用户活动
     *
     * @param paraMap
     * @return
     */
    public List<Map<String, Object>> queryNeedMeAuditOldCustomer(Map<String, Object> paraMap) throws IOException
    {
        //返回结果集
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<Integer> realNeedMeAuditIds = new ArrayList<Integer>();
        List<Integer> needMeAuditTaskId = new ArrayList<Integer>();
        Integer userId = (Integer) paraMap.get("userId");
        //查出所有的待审核任务
        List<Map<String, Object>> oldCustomerList = oldCustomerDao.queryAllNeedAuditTask(paraMap);
        if (CollectionUtils.isEmpty(oldCustomerList))
        {
            return result;
        }
        //筛选出需要我审核的任务
        Map<Integer, Integer> needMeAuditTask = checkOutNeedMeAudit(oldCustomerList, needMeAuditTaskId, userId);
        //查出任务审核历史信息
        List<Map<String, Object>> auditHistoryInfo = oldCustomerDao.queryAuditHistoryInfo(org.apache.commons.lang.StringUtils.join(needMeAuditTaskId, ","));
        //获取用户要审核的任务信息
        //1.获取审核历史信息中存在的任务
        List<Integer> historyIds = new ArrayList<Integer>();
        for (Map<String, Object> historyInfo : auditHistoryInfo)
        {
            historyIds.add((Integer) historyInfo.get("taskId"));
        }
        // 2.遍历每个需要我审核的任务
        for (Map.Entry<Integer, Integer> entry : needMeAuditTask.entrySet())
        {
            int taskId = entry.getKey();
            int myAuditOrder = entry.getValue();
            if (historyIds.contains(taskId))
            { // 如果任务id存在于审批历史任务ids中
                // 检查当前审批人是不是我
                for (Map<String, Object> historyInfo : auditHistoryInfo)
                {
                    Integer historyTaskId = Integer.parseInt(String.valueOf(historyInfo.get("taskId")));
                    Integer order = Integer.parseInt(String.valueOf(historyInfo.get("countNum"))) + 1;
                    if (historyTaskId == taskId && order == myAuditOrder)
                    {
                        realNeedMeAuditIds.add(taskId);
                    }
                }
            }
            else
            {
                //不存在审批历史中并且当前审批任务是我
                if (myAuditOrder == 1)
                {
                    realNeedMeAuditIds.add(taskId);
                }
            }
        }
        // 剔除不是我审核的任务
        result = oldCustomerList;
        Iterator<Map<String, Object>> resultIterator = result.iterator();
        // 去除不满足的任务
        while (resultIterator.hasNext())
        {
            Map<String, Object> resultMap = resultIterator.next();
            if (!realNeedMeAuditIds.contains(Integer.parseInt(String.valueOf(resultMap.get("id")))))
            {
                resultIterator.remove();
            }
        }
        return result;
    }

    /**
     * 查出需要我审核的任务信息
     *
     * @param oldCustomerList   所有需要审核的活动
     * @param needMeAuditTaskId 需要我审核的活动
     * @param userId            用户（我）的ID
     * @return
     * @throws IOException
     */
    private Map<Integer, Integer> checkOutNeedMeAudit(List<Map<String, Object>> oldCustomerList, List<Integer> needMeAuditTaskId, int userId) throws IOException
    {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        // 遍历出每一条活动数据以及活动创建人的审核人列表
        for (Map<String, Object> map : oldCustomerList)
        {
            Integer taskId = Integer.parseInt(String.valueOf(map.get("id")));
            String marketingAuditUsers = String.valueOf(map.get("oldCustomerAuditUsers"));
            List<Map<String, String>> auditUsers = JsonUtil.stringToObject(marketingAuditUsers, new TypeReference<List<Map<String, String>>>()
            {
            });
            // 遍历活动创建人的每个审核人
            for (Map<String, String> auditUser : auditUsers)
            {
                int auditUserId = Integer.parseInt(auditUser.get("auditUser"));
                if (auditUserId == userId)
                {
                    //将该条任务记录到待审批的列表中
                    needMeAuditTaskId.add(taskId);
                    result.put(taskId, Integer.parseInt(auditUser.get("order")));
                    break;
                }
            }

        }
        return result;
    }

    /**
     * 删除老用户活动
     *
     * @param taskId
     * @param userId
     * @return
     */
    public ServiceResult deleteOldCustomer(Integer taskId, Integer userId)
    {
        ServiceResult result = new ServiceResult();
        if (1 != oldCustomerDao.handleOldCustomer(taskId, userId, ShopTaskStatusEnum.TASK_DELETE.getValue()))
        {
            result.setDesc("数据库删除异常");
            result.setRetValue(-1);
        }
        return result;
    }

    /**
     * 任务上线
     *
     * @param taskId
     * @param userId
     * @return
     */
    public ServiceResult executeOldCustomerTask(Integer taskId, Integer userId)
    {
        ServiceResult result = new ServiceResult();
        if (1 != oldCustomerDao.handleOldCustomer(taskId, userId, ShopTaskStatusEnum.TASK_MARKET_SEND.getValue()))
        {
            result.setDesc("数据库操作异常");
            result.setRetValue(-1);
        }
        return result;
    }

    /**
     * 查询任务审批拒绝原因
     *
     * @param taskId
     * @return
     */
    public String getOldCustomerTaskAuditReason(int taskId)
    {
        List<String> strList = oldCustomerDao.getOldCustomerTaskAuditReason(taskId);
        String reason = strList.get(0);
        if (StringUtils.isEmpty(reason))
        {
            return "";
        }
        else
        {
            return reason;
        }
    }

    /**
     * 查询所有已经上线的任务
     *
     * @return
     */
    public List<OldCustomerDomain> queryAllOnlineTask()
    {
        List<OldCustomerDomain> oldCustomerList = oldCustomerDao.queryAllOnlineTask();
        return oldCustomerList;
    }

    /**
     * 检查是否在黑名单或者白名单中
     *
     * @param userPhone
     * @return
     */
    public boolean checkUsers(String userPhone, String fileId, String type)
    {
        boolean isTrue = false;
        String encodePhone = axonEncrypt.encrypt(userPhone);
        if ("appoint".equals(type))
        {
            isTrue = oldCustomerDao.checkAppointUsers(fileId, encodePhone) > 0;

        }
        else if ("black".equals(type))
        {
            isTrue = oldCustomerDao.checkBlackUsers(fileId, encodePhone) > 0;
        }
        return isTrue;
    }

    /**
     * 查询所有的地区编码
     *
     * @return
     */
    public List<Integer> queryAllAreaCode()
    {
        return oldCustomerDao.queryAllAreaCode();
    }


    /**
     * 对外提供的查询接口查询数据组装
     *
     * @param taskId
     * @return
     */
    public OldCustomerResultDomain queryOldCustomerResult(Integer taskId)
    {
        OldCustomerResultDomain domain = new OldCustomerResultDomain();
        Map<String, Object> map = oldCustomerDao.queryOldCustomerResult(taskId);
        String onlineLink = String.valueOf(map.get("onlineLink"));
        String appointBusinessHall = String.valueOf(map.get("appointBusinessHall"));
        domain.setMarketName(String.valueOf(map.get("marketName")));
        domain.setMarketContent(String.valueOf(map.get("marketContent")));
        domain.setOnlineLink(onlineLink);
        if (StringUtils.isEmpty(onlineLink) && StringUtils.isEmpty(appointBusinessHall))
        {
            return null;
        }
        else if (StringUtils.isEmpty(onlineLink) && !StringUtils.isEmpty(appointBusinessHall))
        {
            domain.setMarketType(2);
        }
        else if (!StringUtils.isEmpty(onlineLink) && StringUtils.isEmpty(appointBusinessHall))
        {
            domain.setMarketType(1);
        }
        else
        {
            domain.setMarketType(3);
        }
        return domain;
    }

    /**
     * 终止任务
     *
     * @param taskId
     * @param userId
     * @return
     */
    public ServiceResult terminateOldCustomerTask(Integer taskId, Integer userId)
    {
        int i = oldCustomerDao.terminateOldCustomerTask(taskId, ShopTaskStatusEnum.TASK_STOP.getValue(), userId);
        ServiceResult result = new ServiceResult();
        if (i < 1)
        {
            result.setDesc("终止任务失败");
            result.setRetValue(-1);
        }
        return result;
    }

    /**
     * 查询出所有当前时间是任务截止时间的任务
     * 更新任务状态为过期状态 ; 过期状态码：7
     */
    public int expireOldCustomer()
    {
        Calendar calendar = Calendar.getInstance();
        String time = TimeUtil.formatDateToYMDDevide(calendar.getTime());
        int i = oldCustomerDao.expireOldCustomer(time);
        return i;
    }

    /**
     * 查询所有上线营业厅的营业厅编码
     *
     * @return
     */
    public List<String> queryBaseInfoCodeList()
    {
        return oldCustomerDao.queryBaseInfoCodeList();
    }


    /**
     * (改造)获取用户老用专享审批人
     *
     * @param userId
     * @return
     */
    public Map<String, Object> queryAuditStr(Integer userId)
    {
        Map<String, Object> map = oldCustomerDao.queryAuditStr(userId);
        return map;
    }

    /**
     * 查询任务是否已经删除
     * @param taskId
     * @return
     */
    public boolean queryTaskIsDel(Integer taskId)
    {
        boolean isDel = false;
        int status = oldCustomerDao.queryTaskIsDel(taskId);
        if (status == -1)
        {
            isDel = true;
        }
        return isDel;
    }


    /**
     * 私有方法
     * 读取EXCEL数据
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param is
     * @param startRow
     * @return
     * @throws Exception
     */
    private List<String[]> readXlsFile(InputStream is, int startRow) throws Exception
    {
        List<String[]> list = new ArrayList<String[]>();
        ExcelReader excel = new ExcelReader(is);
        int rowNum = excel.getRowNums() - startRow;
        int count = 0;
        for (int i = startRow; count < rowNum; i++)
        {
            String[] row = excel.readRow(i);
            if (row != null)
            {
                list.add(row);
                count++;
            }
        }
        return list;
    }


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

    private static boolean isContainChinese(String str)
    {
        boolean isTrue = false;
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher m = p.matcher(str);
        boolean b = m.find();
        if (b)
        {
            isTrue = true;
        }
        return isTrue;
    }

}
