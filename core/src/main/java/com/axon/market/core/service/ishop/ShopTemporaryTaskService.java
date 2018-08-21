package com.axon.market.core.service.ishop;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.constant.ishop.ShopTaskClassifyEnum;
import com.axon.market.common.domain.ishop.*;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.*;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.core.service.isystem.UserService;
import com.axon.market.dao.mapper.iscene.IPositionBaseMapper;
import com.axon.market.dao.mapper.ishop.IShopListMapper;
import com.axon.market.dao.mapper.ishop.IShopTaskMapper;
import com.axon.market.dao.mapper.ishop.IShopTemporaryTaskMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;


/**
 * 炒店任务service
 * Created by zengcr on 2017/2/14.
 */
@Component("shopTemporaryTaskService")
public class ShopTemporaryTaskService {
    private static final Logger LOG = Logger.getLogger(ShopTemporaryTaskService.class.getName());

    @Qualifier("shopTemporaryTaskDao")
    @Autowired
    private IShopTemporaryTaskMapper iShopTempTaskMapper;

    @Autowired
    @Qualifier("shopListDao")
    private IShopListMapper iShopListMapper;

    @Qualifier("shopTaskDao")
    @Autowired
    private IShopTaskMapper iShopTaskMapper;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("axonEncrypt")
    private AxonEncryptUtil axonEncrypt;

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Qualifier("positionBaseDao")
    @Autowired
    private IPositionBaseMapper iPositionBaseMapper;

    public static ShopTemporaryTaskService getInstance() {
        return (ShopTemporaryTaskService) SpringUtil.getSingletonBean("shopTemporaryTaskService");
    }

    /**
     * 获取炒店任务总数
     * shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态
     *
     * @return 总数
     */
    public int queryShopTempTaskTotal(Map<String, Object> paras) {
        return iShopTempTaskMapper.queryShopTempTaskTotal(paras);
    }

    /**
     * 分页查询炒店任务
     * offset 起始标记位,limit 限制大小,shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态
     *
     * @return
     */
    public List<ShopTemporaryTaskDomain> queryShopTempTaskByPage(Map<String, Object> paras) {
        return iShopTempTaskMapper.queryShopTempTaskByPage(paras);
    }
    /**
     * 查询单个炒店任务
     * offset 起始标记位,limit 限制大小,shopTaskId 炒店任务ID,shopTaskName 炒店任务名称,taskStatus 炒店任务状态
     *
     * @return
     */
    public ShopTemporaryTaskDomain queryShopTempTaskById(Integer taskId) {
        return iShopTempTaskMapper.queryShopTempTaskById(taskId);
    }
    /**
     * 插入炒店临时任务
     *
     * @param taskDomain
     * @return
     */
    @Transactional
    public ServiceResult insertShopTempTask(ShopTemporaryTaskDomain taskDomain, UserDomain userDomain) {
        ServiceResult result = new ServiceResult();
        taskDomain = setDefault(taskDomain);
        ShopListDomain baseinfo = setBaseinfo(taskDomain, userDomain);

        //任务名称不能重复
        Map<String, Object> parasMap = new HashMap<String, Object>();
        parasMap.put("taskName", taskDomain.getTaskName());
        if (validateTaskName(parasMap)) {
            result.setRetValue(-1);
            result.setDesc("项目名称重复");
            return result;
        }

        try {
            //第一步：插入炒店临时任务打点信息
            iShopListMapper.insertMyShopList(baseinfo, userDomain.getId());

            //String businIdStr = String.valueOf(baseinfo.getBaseId());
            //taskDomain.setBaseAreaId(Integer.parseInt(businIdStr));
            taskDomain.setBaseIds(String.valueOf(baseinfo.getBaseId()));

            //第二步：插入炒店临时促销任务
            iShopTempTaskMapper.insertShopTempTask(taskDomain);

            //第三步：插入炒店临时促销任务对应的炒店
            String baseIds = taskDomain.getBaseIds();
            iShopTaskMapper.insertShopTaskToBaseByIds(taskDomain);

            //第三步：审批通过，插入待执行任务池
            if (ShopTaskStatusEnum.TASK_READY.getValue() == taskDomain.getStatus())
            {
                long currentTime = System.currentTimeMillis();
                long startTime = MarketTimeUtils.formatDate(taskDomain.getStartTime() + " 00:00:00").getTime();
                long endTime = MarketTimeUtils.formatDate(taskDomain.getStopTime() + " 23:59:00").getTime();
                if (currentTime > startTime && currentTime < endTime)
                {
                    iShopTaskMapper.insertShopTaskPool(taskDomain);
                    //第四步：修改炒店任务的下次营销时间
                    Map<String, Object> paras = new HashMap<String, Object>();
                    paras.put("shopTaskId", taskDomain.getId());
                    iShopTaskMapper.updateShopTaskNextTime(paras);
                }
                //开始时间大于当前时间
                if (currentTime < startTime && currentTime < endTime)
                {
                    //修改炒店任务的下次营销时间
                    Map<String, Object> paras = new HashMap<String, Object>();
                    paras.put("shopTaskId", taskDomain.getId());
                    iShopTaskMapper.updateShopTaskNextTimeAsStart(paras);
                }
            } else
            {
                //催单
                if (userDomain != null)
                {
                    reminderItem(taskDomain.getId(), taskDomain.getTaskName(), userDomain);
                }
            }


        } catch (Exception e) {
            LOG.error("shop task insert error", e);
            result.setRetValue(-1);
            result.setDesc("炒店任务新增操作失败");
        }
        return result;
    }

    /**
     * 催单炒店任务
     *
     * @param id
     * @return
     */
    public ServiceResult reminderItem(Integer id, String taskName, UserDomain loginDomain)
    {
        ServiceResult serviceResult = new ServiceResult();
        try
        {
            Map<String, Object> paras = new HashMap<String, Object>();
            paras.put("taskId", id);
            paras.put("reminder", true);
            List<ShopTaskAuditHistoryDomain> shopTaskAuditHistories = iShopTaskMapper.queryShopTaskAuditHistoryDomain(paras);
            String auditUsers = loginDomain.getMarketingAuditUsers();
            List<Map<String, Object>> auditUserList = JsonUtil.stringToObject(auditUsers, new TypeReference<List<Map<String, Object>>>() {
            });
            List<Integer> needApproveUsers = queryAllAuditUserId(auditUserList);
            for (ShopTaskAuditHistoryDomain shopTaskAuditHistory : shopTaskAuditHistories)
            {
                needApproveUsers.remove(shopTaskAuditHistory.getAuditUser());
            }
            //如果贷审批人为空
            if (CollectionUtils.isEmpty(needApproveUsers))
            {
                serviceResult.setRetValue(-1);
                serviceResult.setDesc("该任务的审批人为空，请找系统管理员核实！");
            }
            else
            {
                Integer userId = needApproveUsers.get(0);
                UserDomain userDomain = userService.queryUserById(userId);
                String telephone = axonEncrypt.decrypt(userDomain.getTelephone());
                if (StringUtils.isNotEmpty(telephone))
                {
                    String message = MessageFormat.format(smsConfigBean.getReminderNoticeSmsContent(), userDomain.getName(), "炒店营销任务[" + taskName + "]");
                    sendSmsService.sendReminderNoticeSms(telephone, message);
                }
            }
        }
        catch (Exception e)
        {
            serviceResult.setRetValue(-1);
            serviceResult.setDesc("炒店临时任务催单处理失败");
            LOG.error("shop task炒店催单功能处理失败", e);
        }
        return serviceResult;
    }

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
     * 更新炒店临时任务
     *
     * @param taskDomain
     * @return
     */
    @Transactional
    public ServiceResult updateShopTempTask(ShopTemporaryTaskDomain taskDomain, UserDomain userDomain) {
        ServiceResult result = new ServiceResult();
        taskDomain = setDefault(taskDomain);
        ShopListDomain baseinfo = setBaseinfo(taskDomain, userDomain);

        Map<String, Object> parasMap = new HashMap<String, Object>();
        parasMap.put("taskName", taskDomain.getTaskName());
        parasMap.put("shopTaskId", taskDomain.getId());
        if (validateTaskName(parasMap)) {
            result.setRetValue(-1);
            result.setDesc("项目名称重复");
            return result;
        }

        try {
            //根据taskid查询临时促销点
            try {
                Long baseId = Long.valueOf(iShopTaskMapper.queryBaseIdByTaskId(taskDomain.getId()));
                baseinfo.setBaseId(baseId);
                taskDomain.setBaseIds(String.valueOf(baseId));


            } catch (NumberFormatException e)
            {
                result.setRetValue(-1);
                result.setDesc("无法获取临时促销点");
                return result;
            }

            //第一步：更新炒店临时任务打点信息
            /*Integer createUserId = userDomain.getId();
            String baseIdArray = taskDomain.getBaseIds();
            String cityCode = taskDomain.getCityCode();*/
            int number = iShopListMapper.updateMyShopList(baseinfo, userDomain.getId(), "", "");

            //第二步：更新炒店临时任务
            int rowsSult = iShopTempTaskMapper.updateShopTempTask(taskDomain);

            //临时任务编辑后，炒店id并没有变，因此shop_task_2_base也不需要重建
            //iShopTaskMapper.deleteShopTaskToBase(taskDomain);
            //iShopTaskMapper.insertShopTaskToBaseByIds(taskDomain);

            //审批通过，插入待执行任务池
            if (ShopTaskStatusEnum.TASK_READY.getValue() == taskDomain.getStatus())
            {
                iShopTaskMapper.updateShopTaskPool(taskDomain);
                //第三步：修改炒店任务的下次营销时间
                Map<String, Object> paras = new HashMap<String, Object>();
                paras.put("shopTaskId", taskDomain.getId());
                iShopTaskMapper.updateShopTaskNextTime(paras);
            }
            else
            {
                //催单
                if (userDomain != null)
                {
                    reminderItem(taskDomain.getId(), taskDomain.getTaskName(), userDomain);
                }
            }
        } catch (Exception e) {
            LOG.error("shop task insert error", e);
            result.setRetValue(-1);
            result.setDesc("炒店临时任务更新操作失败");
        }
        return result;
    }

    /**
     * 根据用户ID校验任务是否重复
     *
     * @param paras
     * @return
     */
    public boolean validateTaskName(Map<String, Object> paras) {
        int num = 0;
        try {
            num = iShopTempTaskMapper.queryShopTempTaskNumByName(paras);
        } catch (Exception e) {
            num = 0;
            LOG.error("shop task validateTaskName error", e);
        }
        if (num > 0) {
            return true;
        }
        return false;
    }


    /**
     * 炒店新增修改时默认是配置
     *
     * @param taskDomain
     * @return
     */
    private ShopTemporaryTaskDomain setDefault(ShopTemporaryTaskDomain taskDomain) {
        taskDomain.setBeginTime("09:00");
        taskDomain.setEndTime("18:00");
        if (null != taskDomain.getMarketUser() && "4".equals(taskDomain.getMarketUser())) {
            taskDomain.setSceneType(31);
            taskDomain.setSceneTypeName("精细化平台");
        } else {
            taskDomain.setSceneType(1);
            taskDomain.setSceneTypeName("互联网部");
        }
        taskDomain.setMonitorInterval(1);
        taskDomain.setChannelId(1);
        taskDomain.setMonitorType(1);
        taskDomain.setTriggerChannelId(0);
        taskDomain.setTaskWeight(99);
        //taskDomain.setTaskClassifyId(ShopTaskClassifyEnum.TASK_TEMP_PROMOTION.getValue());
        taskDomain.setStatus(1);
        taskDomain.setBaseAreaId(taskDomain.getCityCode());
        taskDomain.setBaseAreaTypes("3");
        return taskDomain;
    }

    /**
     * 炒店新增修改时默认是配置
     *
     * @param taskDomain
     * @return
     */
    private ShopListDomain setBaseinfo(ShopTemporaryTaskDomain taskDomain, UserDomain userDomain) {
        ShopListDomain baseinfo = new ShopListDomain();
        baseinfo.setBaseName("临时促销点");
        baseinfo.setStatus("1");
        baseinfo.setAddress(taskDomain.getAddressDetail()); // 地址
        baseinfo.setLat(taskDomain.getLatitude()); // 经度
        baseinfo.setLng(taskDomain.getLongitude());// 纬度
        baseinfo.setRadius(taskDomain.getRadius());// 半径
        baseinfo.setCityName(taskDomain.getBaseAreaName());// 城市名称
        baseinfo.setCityCode(taskDomain.getCityCode());// 城市编码
        baseinfo.setDistrictCode(taskDomain.getAreaId()); // 地区编码
        baseinfo.setCityAreaCode(taskDomain.getCityId());
        baseinfo.setcreateUserid(userDomain.getId());//创建用户id
        baseinfo.setLocationTypeId(3);//3表示临时促销点

        if (ShopTaskClassifyEnum.TASK_TEMP_PROMOTION_XY.getValue() == taskDomain.getTaskClassifyId()) {
            baseinfo.setBusinessHallCode("XY"+String.format("%03d", taskDomain.getCityCode())+TimeUtil.formatDateToYMDHMS(new Date()));
        } else if (ShopTaskClassifyEnum.TASK_TEMP_PROMOTION_JK.getValue() == taskDomain.getTaskClassifyId()) {
            baseinfo.setBusinessHallCode("JK"+String.format("%03d", taskDomain.getCityCode())+TimeUtil.formatDateToYMDHMS(new Date()));
        } else if(ShopTaskClassifyEnum.TASK_TEMP_PROMOTION_GZ.getValue() == taskDomain.getTaskClassifyId()) {
            baseinfo.setBusinessHallCode("GZ"+String.format("%03d", taskDomain.getCityCode())+TimeUtil.formatDateToYMDHMS(new Date()));
        }

        return baseinfo;
    }

    /**
     * 根据ID删除炒店临时任务
     * 根据ID备注删除原因，执行者，操作时间
     *
     * @return
     */
    @Transactional
    public ServiceResult deleteShopTempTaskById(int shopTaskId) {
        ServiceResult result = new ServiceResult();

        //查询任务对应的临时任务点baseid
        Integer baseId = Integer.valueOf(iShopTaskMapper.queryBaseIdByTaskId(shopTaskId));
        if (baseId==null){
            result.setRetValue(-1);
            result.setDesc("炒店临时任务删除操作失败");
        }

        //将临时任务点置为失效
        iPositionBaseMapper.deletePositionBaseById(baseId);

        //将临时任务置为失效
        int rowsSult = iShopTempTaskMapper.deleteShopTempTaskById(shopTaskId);
        if (rowsSult < 1) {
            result.setRetValue(-1);
            result.setDesc("炒店临时任务删除操作失败");
        }

        return result;
    }

    /**
     * 终止任务
     *
     * @param id
     * @return
     */
    @Transactional
    public ServiceResult pauseItem(Integer id) {
        ServiceResult serviceResult = new ServiceResult();
        try {
            //1从次日起不再生成待办任务
            iShopTaskMapper.updateShopTaskById(id, ShopTaskStatusEnum.TASK_STOP.getValue());
            // 终止现在正在执行的任务
            iShopTaskMapper.updateShopTaskPoolBySystemId(id, ShopTaskStatusEnum.TASK_STOP.getValue());
            iShopTaskMapper.updateShopTaskExecuteBySystemId(id, null, ShopTaskStatusEnum.TASK_STOP.getValue());

            //查询任务对应的临时任务点baseid
            Integer baseId = Integer.valueOf(iShopTaskMapper.queryBaseIdByTaskId(id));
            if (baseId!=null){
                //将临时任务点置为失效
                iPositionBaseMapper.deletePositionBaseById(baseId);
            }
        } catch (Exception e) {
            serviceResult.setRetValue(-1);
            serviceResult.setDesc("终止任务处理失败");
            LOG.error("shop task终止任务功能处理失败", e);
        }
        return serviceResult;
    }
}
