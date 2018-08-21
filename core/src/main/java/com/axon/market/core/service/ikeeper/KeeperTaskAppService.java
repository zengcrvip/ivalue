package com.axon.market.core.service.ikeeper;

//import com.axon.icloud.scene.sms.common.SmsResponse;
import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.constant.ikeeper.KeeperAppResponseCodeEnum;
import com.axon.market.common.constant.ikeeper.KeeperTaskInstStateEnum;
import com.axon.market.common.domain.ikeeper.*;
import com.axon.market.common.domain.ishopKeeper.KeeperCashflowDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.ThreadPoolUtil;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.core.service.greenplum.GreenPlumOperateService;
import com.axon.market.core.service.icommon.PhonePlusService;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.core.service.isystem.UserService;
import com.axon.market.dao.mapper.ikeeper.IKeeperTaskAppMapper;
import com.axon.market.dao.mapper.ikeeper.IKeeperTaskMapper;
import com.axon.market.dao.mapper.ikeeper.IKeeperUserMapper;
import com.axon.market.dao.mapper.ikeeper.ITaskTodoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by yuanfei on 2017/8/15.
 */
@Service("keeperTaskAppService")
public class KeeperTaskAppService
{
    private static final Logger LOG = Logger.getLogger(KeeperTaskAppService.class.getName());

    @Autowired
    @Qualifier("keeperTaskAppDao")
    private IKeeperTaskAppMapper keeperTaskAppDao;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("keeperTaskDao")
    private IKeeperTaskMapper keeperTaskDao;

    @Autowired
    @Qualifier("taskTodoDao")
    private ITaskTodoMapper taskTodoDao;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Autowired
    @Qualifier("phonePlusService")
    private PhonePlusService phonePlusService;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("keeperSendSmsService")
    private KeeperSendSmsService keeperSendSmsService;

    @Autowired
    @Qualifier("greenPlumOperateService")
    private GreenPlumOperateService greenPlumOperateService;

    @Autowired
    @Qualifier("keeperUserDao")
    private IKeeperUserMapper keeperUserDao;

    // 短信渠道对应的类型
    private static final Integer CHANNEL_TYPE_SMS = 1;

    // 话+渠道对应的类型
    private static final Integer CHANNEL_TYPE_CALL = 2;

    /**
     *
     * @param token
     * @return Map<状态，类型集合<Map<(任务类型名称，任务类型ID，任务数，用户数等),值>>>
     */
    public List<Map<String,Object>> queryMyKeeperTask(String token)
    {
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        Map<String,Integer> totalCountByState = new HashMap<String,Integer>();

        //任务状态:1：待执行 2：执行中 3：已执行 4：已失效
        // Map<状态，对应的类型信息> 临时表
        Map<String,List<Map<String,Object>>> tempResult = new HashMap<String,List<Map<String,Object>>>(){
            {
                put(KeeperTaskInstStateEnum.READY.getValue(),new ArrayList<>());
                put(KeeperTaskInstStateEnum.EXECUTED.getValue(),new ArrayList<>());
                put(KeeperTaskInstStateEnum.EXECUTING.getValue(),new ArrayList<>());
                put(KeeperTaskInstStateEnum.INVALID.getValue(),new ArrayList<>());
            }
        };

        UserDomain userDomain = userService.queryUserByToken(token);
        //查询按照状态，任务，类型分组的信息
        List<Map<String,Object>> taskInstGroupDetailList = keeperTaskAppDao.queryMyKeeperTaskInstGroupDetail(userDomain.getId());

        List<Map<String, Object>> attrValueList = keeperTaskDao.queryKeeperTaskType();

        //初始化状态_类型Map<state_typeId,Map<类型|类型名称|用户数|任务数，值>
        Map<String,Map<String,Object>> subDetailInfo = new HashMap<String,Map<String,Object>>(){{
            for (Map<String, Object> attrValue : attrValueList)
            {
                for (Map.Entry<String,List<Map<String,Object>>> _tempResult : tempResult.entrySet())
                {
                    String state = _tempResult.getKey();
                    String typeName = String.valueOf(attrValue.get("typeName"));
                    String typeId = String.valueOf(attrValue.get("typeId"));
                    put(state+"_"+typeId,new HashMap<String,Object>(){{
                        put("typeName",typeName);
                        put("typeId",typeId);
                        put("taskCount", 0);
                        put("userCount", 0);
                    }});
                }
            }
        }};
        for (Map<String,Object> taskInstGroupDetail : taskInstGroupDetailList) {
            String state = String.valueOf(String.valueOf(taskInstGroupDetail.get("state")));
             String typeId = String.valueOf(String.valueOf(taskInstGroupDetail.get("typeId")));
            String userCount = String.valueOf(String.valueOf(taskInstGroupDetail.get("userCount")));
            String id = state + "_" + typeId;
            Object taskCount = subDetailInfo.get(id).get("taskCount");
            Object subUserCount = subDetailInfo.get(id).get("userCount");
            subDetailInfo.get(id).put("userCount", Integer.valueOf(String.valueOf(subUserCount)) + Integer.valueOf(String.valueOf(userCount)));
            subDetailInfo.get(id).put("taskCount", 1 + Integer.valueOf(String.valueOf(taskCount)));

            // 统计每个状态的任务数目
            if (totalCountByState.get(state) != null)
            {
                totalCountByState.put(state,totalCountByState.get(state) + 1);
            }
            else
            {
                totalCountByState.put(state, 1);
            }
        }

        // 将分类的信息按照状态放入临时Map中
        for (Map.Entry<String,Map<String,Object>> tempMap : subDetailInfo.entrySet())
        {
            String[] stateType = tempMap.getKey().split("_");

            tempResult.get(stateType[0]).add(tempMap.getValue());
        }

        // 设置返回信息值 包括状态，任务总数，分类的具体信息
        for (Map.Entry<String,List<Map<String,Object>>> tempMap : tempResult.entrySet())
        {
            result.add(new HashMap<String,Object>(){{
                put("state",tempMap.getKey());
                put("total",totalCountByState.get(tempMap.getKey()) == null? 0 : totalCountByState.get(tempMap.getKey()));
                put("data",tempMap.getValue());
            }});
        }

        return result;
    }

    /**
     *
     * @param state
     * @param typeId
     * @param token
     * @return
     */
    public Map<String,Object> queryTaskInstDetailsByStateAndType(String state,Integer typeId,String token)
    {
        Map<String,Object> result = new HashMap<String,Object>();
        Map<String,Map<String,Object>> taskInstGroupCountResult = new HashMap<String,Map<String,Object>>(){
            {
                put(KeeperTaskInstStateEnum.READY.getValue(),new HashMap<String,Object>(){{
                    put("stateType", KeeperTaskInstStateEnum.READY.getValue());
                    put("stateCount", 0);
                }});
                put(KeeperTaskInstStateEnum.EXECUTED.getValue(),new HashMap<String,Object>(){{
                    put("stateType", KeeperTaskInstStateEnum.EXECUTED.getValue());
                    put("stateCount", 0);
                }});
                put(KeeperTaskInstStateEnum.EXECUTING.getValue(),new HashMap<String,Object>(){{
                    put("stateType", KeeperTaskInstStateEnum.EXECUTING.getValue());
                    put("stateCount", 0);
                }});
                put(KeeperTaskInstStateEnum.INVALID.getValue(),new HashMap<String,Object>(){{
                    put("stateType", KeeperTaskInstStateEnum.INVALID.getValue());
                    put("stateCount", 0);
                }});
            }
        };
        UserDomain userDomain = userService.queryUserByToken(token);
        List<TaskInstDetailShowDomain> taskInstDetailList = keeperTaskAppDao.queryTaskInstDetailsByStateAndType(Integer.valueOf(state), typeId, userDomain.getId());

        //号码解密脱敏处理
        for (TaskInstDetailShowDomain taskInstDetail : taskInstDetailList)
        {
            if (StringUtils.isNotEmpty(taskInstDetail.getTelephone()))
            {
                taskInstDetail.setTelephone(AxonEncryptUtil.getInstance().decryptDesensitization(taskInstDetail.getTelephone()));
            }
        }

        // 查询显示标头(待执行，执行中，已完成，已失效)的任务数据量
        List<Map<String,Object>> taskInstGroupCount = keeperTaskAppDao.queryTaskInstGroupCountByType(typeId, userDomain.getId());
        List<Map<String,Object>> taskInstGroupCountList = new ArrayList<Map<String,Object>>();
        for (Map<String,Object> taskInstGroup : taskInstGroupCount)
        {
            taskInstGroupCountResult.replace(String.valueOf(taskInstGroup.get("stateType")), taskInstGroup);
        }

        for (Map.Entry<String,Map<String,Object>> groupCountInfo : taskInstGroupCountResult.entrySet())
        {
            taskInstGroupCountList.add(groupCountInfo.getValue());
        }
        result.put("stateGroup",taskInstGroupCountList);
        result.put("currentState",state);
        result.put("taskList",taskInstDetailList);
        return result;
    }

    /**
     * 短信维系动作
     * @param autograph
     * @param token
     */
    public ResultVo maintainCustomerSmsReminder(boolean autograph, String token,Integer detailId)
    {
        ResultVo result = new ResultVo();
        String message;

        TaskInstDetailDomain taskInstDetailDomain = keeperTaskAppDao.queryEffectiveTaskDetailById(detailId);
        //判断该任务的有效性
        if (null == taskInstDetailDomain)
        {
            return KeeperAppResponseCodeEnum.DB_DATA_INVALID.getValue("该任务已失效");
        }

        try
        {
            UserDomain userdomain = userService.queryUserByToken(token);
            TaskChannelDomain taskChannel = keeperTaskAppDao.queryTaskChannelByDetailId(detailId, CHANNEL_TYPE_SMS);
            message = taskChannel.getChannelContent() + (autograph ? "[" + userdomain.getKeeperUser().getSmsSignature() + "]":"");

            ThreadPoolUtil.submit(new Runnable() {
                @Override
                public void run() {
                    //短信验证码发送
//                    SmsResponse result = keeperSendSmsService.sendSms(AxonEncryptUtil.getInstance().decryptWithoutCountrycode(taskInstDetailDomain.getTelephone()), message, taskChannel.getTaskId(), taskChannel.getChannelPhone());
//                    if (result.getStatus() == 0) {
//                        // 将某个任务中某个用户的维系操作信息保存进记录表
//                        keeperTaskAppDao.insertMaintainedCustomerUnderTaskRecord(String.valueOf(detailId), CHANNEL_TYPE_SMS);
//                    }
                }
            });

            // 根据任务实例详情ID修改状态
            if (1 != keeperTaskAppDao.updateMaintainOperationStatus(detailId, "sms", null, 1))
            {
                LOG.error("Update Task Inst Status fail");
            }

        }
        catch (Exception e)
        {
            result = KeeperAppResponseCodeEnum.SMS_SEND_ERR.getValue();
            LOG.error("sms send error");
        }
        return result;
    }

    /**
     * 电话维系动作
     * @param token
     * @param detailId
     * @return
     */
    public ResultVo maintainCustomerCallReminder(String token, Integer detailId)
    {
        ResultVo result  = new ResultVo();
        TaskInstDetailDomain taskInstDetailDomain = keeperTaskAppDao.queryEffectiveTaskDetailById(detailId);
        //判断该任务的有效性
        if (null == taskInstDetailDomain)
        {
            return KeeperAppResponseCodeEnum.DB_DATA_INVALID.getValue("该任务已失效");
        }

        try
        {
            UserDomain userdomain = userService.queryUserByToken(token);
            String userPhone = userdomain.getTelephone();

            //获取到任务对应的渠道触发规则限制等
            TaskChannelDomain taskChannel = keeperTaskAppDao.queryTaskChannelByDetailId(detailId, CHANNEL_TYPE_CALL);

            if (taskChannel == null)
            {
                return KeeperAppResponseCodeEnum.DB_RESULT_ERR.getValue("任务话+渠道信息不存在，请联系管理员");
            }

            // 只有未完成电话或打电话次数没有达到限制才能继续打电话
            if (taskInstDetailDomain.getCallResult() == 1 || taskInstDetailDomain.getCallTimes() >= taskChannel.getTriggerLimit())
            {
                return KeeperAppResponseCodeEnum.CALL_ERR.getValue("电话维系动作已完成，无需再次操作");
            }

            Map<String,String> callPhoneObj = phonePlusService.initCallToPhonePlus(userPhone, AxonEncryptUtil.getInstance().decryptWithoutCountrycode(taskInstDetailDomain.getTelephone()), taskChannel.getChannelPhone());
            TaskInstResultDomain taskInstResult = packageTaskInstResult(callPhoneObj, detailId);

            // 根据任务实例详情ID修改状态
            if (1 != keeperTaskAppDao.updateMaintainOperationStatus(detailId, "call", taskChannel.getTriggerLimit(), null))
            {
                return KeeperAppResponseCodeEnum.DB_OPERATE_ERR.getValue("任务实例状态更新失败");
            }

            //调话+接口后将结果入库
            keeperTaskAppDao.insertTaskInstResult(taskInstResult);
            // 将保存到结果表的id传到前端，方便进行确认调用话+使用
            callPhoneObj.put("taskInstResultId", String.valueOf(taskInstResult.getId()));
            result.setResultObj(callPhoneObj);
        }
        catch (Exception e)
        {
            LOG.error("Keeper Task Call maintain fail,"+e);
            return KeeperAppResponseCodeEnum.CALL_ERR.getValue();
        }

        return result;
    }

    /**
     * 反馈本次外呼通话的结果信息
     * 先条用话+接口查询本次通话的结果信息，
     * 更新针对该任务该客户外呼的状态，并将获取到的确认结果保存
     *
     * @param detailId
     * @param serialId
     * @param resultDesc
     * @return
     */
    public ResultVo feedbackCallReminderResult(Integer detailId, String serialId, String resultDesc,String businessTendency,Integer taskInstResultId)
    {
        ResultVo result = new ResultVo();
        Integer bTendency = StringUtils.isNotEmpty(businessTendency)? Integer.valueOf(businessTendency): 0;
        try
        {
            //根据之前调用话+的返回信息查询上次通话的结果信息
            Map<String,String> phoneCallResult = phonePlusService.submitResultToPhonePlus(serialId, resultDesc);
            //返回结果为0说明上次通话是接通的
            Integer status = "0".equals(phoneCallResult.get("result_code"))? 1 : 0;

            // 根据任务实例详情ID修改状态
            if (1 != keeperTaskAppDao.updateMaintainOperationStatus(detailId, "feedBack", null, status))
            {
                return KeeperAppResponseCodeEnum.DB_OPERATE_ERR.getValue("任务实例状态更新失败");
            }

            // 获取上一次话+外呼的通话记录并将结果入库保存
            TaskInstResultDomain taskInstResult = new TaskInstResultDomain();
            taskInstResult.setDetailId(detailId);
            taskInstResult.setBusinessTendency(bTendency);
            taskInstResult.setId(taskInstResultId);
            taskInstResult.setConfirmResult(JsonUtil.objectToString(phoneCallResult));
            keeperTaskAppDao.confirmTaskInstResult(taskInstResult);

            // 将某个任务中某个用户的维系操作信息保存进记录表
            keeperTaskAppDao.insertMaintainedCustomerUnderTaskRecord(String.valueOf(detailId), CHANNEL_TYPE_CALL);
        }
        catch (Exception e)
        {
            LOG.error("feedback message fail,"+e);
            return KeeperAppResponseCodeEnum.CALL_ERR.getValue("结果反馈失败");
        }
        return result;
    }

    /**
     *
     * @param welfareId
     * @return
     */
    public ResultVo queryWelfareProductsByWelfareId(Integer welfareId)
    {
        ResultVo result = new ResultVo();
        Map<String,Object> welfareProduct =  keeperTaskAppDao.queryWelfareProductsByWelfareId(welfareId);
        if (welfareProduct == null)
        {
            return KeeperAppResponseCodeEnum.DB_RESULT_ERR.getValue();
        }
        result.setResultObj(welfareProduct);
        return result;
    }

    /**
     * 根据号码查询客户的信息
     * @param detailId
     * @return
     */
    public ResultVo queryCustomerDetail(Integer detailId)
    {
        ResultVo result = new ResultVo();
        try
        {
            TaskInstDetailDomain taskInstDetailDomain = keeperTaskAppDao.queryTaskInstDetailByDetailId(detailId);

            String sql = "select phone,user_name,city_code,city_name,main_pkg,cbss_bss_tag,utype,arpu_befor1mon,arpu_befor2mon,arpu_befor3mon,avg_flow_3mon " +
                        "from uapp.push_user_crm_info_day where phone = " + Long.valueOf(taskInstDetailDomain.getTelephone());

            KeeperCustomerDomain keeperCustomerDomain = jdbcTemplate.queryForObject(sql, new RowMapper<KeeperCustomerDomain>()
            {
                @Override
                public KeeperCustomerDomain mapRow(ResultSet resultSet, int i) throws SQLException
                {
                    KeeperCustomerDomain keeperCustomer = new KeeperCustomerDomain();
                    keeperCustomer.setTelephone(AxonEncryptUtil.getInstance().decryptDesensitization(resultSet.getString("phone")));
                    keeperCustomer.setUserName(resultSet.getString("user_name"));
                    keeperCustomer.setCityCode(resultSet.getString("city_code"));
                    keeperCustomer.setCityName(resultSet.getString("city_name"));
                    keeperCustomer.setMainPkg(resultSet.getString("main_pkg"));
                    keeperCustomer.setCbssBssTag(resultSet.getString("cbss_bss_tag"));
                    keeperCustomer.setuType(resultSet.getString("utype"));
                    keeperCustomer.setArpuBefor1mon(resultSet.getInt("arpu_befor1mon"));
                    keeperCustomer.setArpuBefor2mon(resultSet.getInt("arpu_befor2mon"));
                    keeperCustomer.setArpuBefor3mon(resultSet.getInt("arpu_befor3mon"));
                    keeperCustomer.setAvgFlow3mon(resultSet.getInt("avg_flow_3mon"));
                    return keeperCustomer;
                }
            });
            result.setResultObj(keeperCustomerDomain);
        }
        catch (Exception e)
        {
            result = KeeperAppResponseCodeEnum.DB_RESULT_ERR.getValue("该客户详细资料丢失，请联系管理员。");
        }

        return result;
    }

    /**
     * @see IKeeperUserMapper#queryInterfaceManUnderSameOrg(String)
     * @param token
     * @return
     */
    public ResultVo queryInterfaceManUnderSameOrg(String token)
    {
        ResultVo result = new ResultVo();
        Map<String,Object> interfaceMan = keeperUserDao.queryInterfaceManUnderSameOrg(token);
        if (null == interfaceMan.get("id"))
        {
            return KeeperAppResponseCodeEnum.DB_RESULT_ERR.getValue("未匹配到相关接口人");
        }
        result.setResultObj(interfaceMan.get("name"));
        return result;
    }

    /**
     * 任务转发
     * @param token
     * @return
     */
    @Transactional
    public ResultVo forwardingTask(String token,Integer detailId)
    {
        // 1、先查出当前该条任务实例详情对应的所有同任务，同类型的用户
        TaskInstDomain taskInstDomain = keeperTaskAppDao.queryTaskInstByDetailId(detailId);

        //2、查询接口人
        Map<String,Object> interfaceMan = keeperUserDao.queryInterfaceManUnderSameOrg(token);
        Integer interfaceManId = Integer.valueOf(String.valueOf(interfaceMan.get("id")));

        // 3、查询接口人对应的同任务的任务实例
        TaskInstDomain taskInstOfInterfaceMan = keeperTaskAppDao.queryTaskInstByUserAndTask(interfaceManId, taskInstDomain.getTaskId());

        //4、如果没有接口人对应的该任务的任务实例信息则创建一条
        if (taskInstOfInterfaceMan == null)
        {
            taskInstOfInterfaceMan = packageTaskInst(taskInstDomain,interfaceManId);
            if (1 != keeperTaskAppDao.insertTaskInst(taskInstOfInterfaceMan))
            {
                return KeeperAppResponseCodeEnum.DB_OPERATE_ERR.getValue("创建接口人对应任务实例失败");
            }
        }

        // 更新对应客户的任务实例信息
        if ( 1 != keeperTaskAppDao.forwardingTask(detailId, taskInstOfInterfaceMan.getTaskInstId()))
        {
            LOG.error("task forwarding err");
            return KeeperAppResponseCodeEnum.DB_OPERATE_ERR.getValue("任务转发失败");
        }
        return new ResultVo();
    }

    /**
     * 获取当前维系用户选择的类型，状态下的所有任务模板
     * @param typeId
     * @return
     */
    public ResultVo queryTaskSmsTemplates(String token,Integer typeId, Integer state)
    {
        ResultVo result = new ResultVo();
        UserDomain userdomain = userService.queryUserByToken(token);
        List<Map<String,Object>> smsTemplateList = keeperTaskAppDao.queryBatchSmsTaskTemplates(userdomain.getId(), typeId, state);
        if (CollectionUtils.isEmpty(smsTemplateList))
        {
            return KeeperAppResponseCodeEnum.DB_RESULT_ERR.getValue("任务对应的短信发送模板信息丢失，请联系管理员。");
        }
        result.setResultObj(smsTemplateList);
        return result;
    }

    /**
     * 批量短信维系动作
     * @param token
     * @param typeId
     * @param state
     * @return
     */
    public ResultVo maintainCustomerBatchSmsReminder(String token,Integer typeId, Integer state, String taskSmsDetails)
    {
        ResultVo result = new ResultVo();
        try
        {
            Map<String,Object> taskSmsDetailMap = JsonUtil.stringToObject(taskSmsDetails, Map.class);
            List<Integer> selectedTaskIds = new ArrayList<Integer>();
            for (Map.Entry<String,Object> taskSmsDetail : taskSmsDetailMap.entrySet())
            {
                selectedTaskIds.add(Integer.valueOf(String.valueOf(taskSmsDetail.getKey())));
            }
            UserDomain userdomain = userService.queryUserByToken(token);
            List<Map<String,Object>> smsDetailList = keeperTaskAppDao.queryBatchSmsTaskDetails(userdomain.getId(), typeId, state, StringUtils.join(selectedTaskIds,","));
            Map<String,Object> sendResult = keeperSendSmsService.sendBatchSms(smsDetailList, taskSmsDetailMap, userdomain.getKeeperUser().getSmsSignature());
            result.setResultObj(sendResult.get("count"));

            //批量修改任务实例详情的状态
            int updateCount = keeperTaskAppDao.batchUpdateTaskInstDetailSmsState(String.valueOf(sendResult.get("detailIds")));
            LOG.info("[Batch Send Sms] Update TaskInstDetail count = " + updateCount);

            keeperTaskAppDao.insertMaintainedCustomerUnderTaskRecord(String.valueOf(sendResult.get("successDetailIds")), CHANNEL_TYPE_SMS);
        }
        catch (IOException e)
        {
            result = KeeperAppResponseCodeEnum.PARAM_FORMAT_ERR.getValue();
        }

        return result;
    }

    /**
     * 查询所有任务维系操作成功的记录信息【定时调度切任务踢重专用】
     *
     * @return
     */
    public List<TaskInstRecordDomain> queryMaintainedCustomerUnderTaskRecords()
    {
        return keeperTaskAppDao.queryMaintainedCustomerUnderTaskRecords();
    }

    /**
     * 校验任务详情是否生效
     * @param detailId
     * @return
     */
    private boolean checkIsTaskDetailEffective(Integer detailId)
    {
        if (keeperTaskAppDao.queryEffectiveTaskDetailById(detailId) != null)
        {
            return true;
        }
        return false;
    }

    /**
     * 拼接话+返回
     * @param resultObj
     * @param detailId
     * @return
     */
    private TaskInstResultDomain packageTaskInstResult(Map<String,String> resultObj, Integer detailId) throws JsonProcessingException
    {
        TaskInstResultDomain taskInstResultDomain = new TaskInstResultDomain();
        taskInstResultDomain.setDetailId(detailId);
        taskInstResultDomain.setResultType(2); //2为话+
        taskInstResultDomain.setResultDesc(JsonUtil.objectToString(resultObj));
        taskInstResultDomain.setResultStatus("0".equals(resultObj.get("result_code")) ? 1 : 0);
        return taskInstResultDomain;
    }

    public ResultVo queryTodoTaskCount(Integer userid)
    {
        ResultVo result = new ResultVo();
        try
        {
            result.setResultObj(taskTodoDao.queryTodoTaskCount(userid));
        }
        catch (Exception e)
        {
            return new ResultVo("-1", "系统出错，请联系系统管理员!");
        }
        return result;
    }

    public ResultVo queryTodoTaskByDate(String createdate, Integer userid)
    {
        ResultVo result = new ResultVo();
        try
        {
            result.setResultObj(taskTodoDao.queryTodoTaskList(createdate, userid));
        }
        catch (Exception e)
        {
            return new ResultVo("-1", "系统出错，请联系系统管理员!");
        }
        return result;
    }

    public ResultVo insertTodoTask(TaskTodoDomain taskdomain)
    {
        try
        {
            taskTodoDao.insertTodoTask(taskdomain);
        }
        catch (Exception e)
        {
            return new ResultVo("-1", "系统出错，请联系系统管理员!");
        }
        return new ResultVo();
    }

    public ResultVo deleteTodoTask(Integer taskid)
    {
        try
        {
            taskTodoDao.deleteTodoTask(taskid);
        }
        catch (Exception e)
        {
            return new ResultVo("-1", "系统出错，请联系系统管理员!");
        }
        return new ResultVo();
    }

    public ResultVo setTodoTaskStatus(Integer taskid)
    {
        try
        {
            taskTodoDao.updateTaskStatus(taskid);
        }
        catch (Exception e)
        {
            return new ResultVo("-1", "系统出错，请联系系统管理员!");
        }
        return new ResultVo();
    }

    public ResultVo queryUnreadTaskCount(Integer userid)
    {
        ResultVo result = new ResultVo();
        try
        {
            result.setResultObj(taskTodoDao.queryUnreadTaskCount(userid));
        }
        catch (Exception e)
        {
            return new ResultVo("-1", "系统出错，请联系系统管理员!");
        }
        return result;
    }

    public ResultVo queryCashflow(Integer userid)
    {
        ResultVo result = new ResultVo();

        Map<String, Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        String currentday = TimeUtil.formatDateToYMD(calendar.getTime());
        String currentmonth = TimeUtil.formatDateToYM(calendar.getTime());
        List<KeeperCashflowDomain> list = new ArrayList<>();
        String sql = "select timest,cssh_flow,cz_cash_flow,cflow_chg_trend,cflow_rise_days,cflow_drop_days from umid.rpt_keeper_cash_flows_day where mtain_userid = " + userid + " and timest like '" + currentmonth + "%' order by timest";
        try
        {
            greenPlumOperateService.query(sql, new RowCallbackHandler()
            {
                @Override
                public void processRow(ResultSet resultSet) throws SQLException
                {
                    KeeperCashflowDomain domain = new KeeperCashflowDomain();
                    String date = resultSet.getString("timest");
                    domain.setSetdate(date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8));
                    domain.setCashflow(resultSet.getLong("cssh_flow"));
                    domain.setCzcashflow(resultSet.getLong("cz_cash_flow"));
                    domain.setTrend(resultSet.getInt("cflow_chg_trend"));
                    domain.setRisedays(resultSet.getInt("cflow_rise_days"));
                    domain.setDropdays(resultSet.getInt("cflow_drop_days"));
                    list.add(domain);

                    if (currentday.equals(resultSet.getString("timest")))
                    {
                        map.put("cashflow", domain.getCashflow());
                        map.put("czcashflow", domain.getCzcashflow());
                        map.put("risedays", domain.getRisedays());
                        map.put("dropdays", domain.getDropdays());
                    }
                }
            }, 0);
            map.put("cashflowlist", list);
            result.setResultObj(map);
        }
        catch (Exception e)
        {
            return new ResultVo("-1", "系统出错，请联系系统管理员!");
        }
        return result;
    }

    /**
     * 查询H5页面首页实时任务提醒
     * @param token
     * @return
     */
    public ResultVo queryRealTimeReminder(String token)
    {
        ResultVo result = new ResultVo();
        List<String> reminders = new ArrayList<String>();
        try {
            List<Map<String,Object>> reminderList = keeperTaskAppDao.queryRealTimeReminder(token);
            for(Map<String,Object> reminderMap : reminderList){
                String phone = AxonEncryptUtil.getInstance().decryptWithoutCountrycode(String.valueOf(reminderMap.get("phone")));
                Integer typeId = Integer.parseInt("" + reminderMap.get("ruleId"));
                if(StringUtils.isEmpty(phone) || null == typeId){
                    continue;
                }
                String reminderStr = (typeId == 3) ? "用户套内流量使用超过80%" : (typeId == 4) ? "用户余额不足10元" : "用户4G首登网";
                reminders.add(phone + reminderStr);
            }
            result.setResultObj(reminders);
        } catch (NumberFormatException e) {
            return new ResultVo("-1", "系统出错，请联系系统管理员!");
        }
        return result;
    }

    /**
     * 包装任务实例
     * @param taskInst
     * @return
     */
    private TaskInstDomain packageTaskInst(TaskInstDomain taskInst, Integer interfaceManId)
    {
        TaskInstDomain taskInstDomain = new TaskInstDomain();
        taskInstDomain.setExecDate(taskInst.getExecDate());
        taskInstDomain.setExecUserId(interfaceManId);
        taskInstDomain.setTaskId(taskInst.getTaskId());
        taskInstDomain.setTaskName(taskInst.getTaskName());
        taskInstDomain.setTypeId(taskInst.getTypeId());
        taskInstDomain.setEffDate(taskInst.getEffDate());
        taskInstDomain.setExpDate(taskInst.getExpDate());
        taskInstDomain.setWelfareIds(taskInst.getWelfareIds());
        taskInstDomain.setState(taskInst.getState());
        return taskInstDomain;
    }
}
