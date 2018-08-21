package com.axon.market.web.controller.ishop;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.constant.ischeduling.ShopTaskStatusEnum;
import com.axon.market.common.domain.iresource.SmsContentDomain;
import com.axon.market.common.domain.iscene.SmsSendConfigDomain;
import com.axon.market.common.domain.ishop.ShopListDomain;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.domain.ishop.ShopTemporaryTaskDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import com.axon.market.common.util.excel.ExportUtils;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.core.service.ishop.ShopTemporaryTaskService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.axon.market.core.service.icommon.AreaService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 炒店任务管理Controller
 * Created by zengcr on 2017/2/14.
 */
@Controller("ShopTemporaryTaskController")
public class ShopTemporaryTaskController {
    /**
     * 导出EXECL数据字段
     */
    private static String[] fields = new String[]{"phone"};
    private static final Logger LOG = Logger.getLogger(ShopTemporaryTaskController.class.getName());

    @Qualifier("shopTemporaryTaskService")
    @Autowired
    private ShopTemporaryTaskService shopTemporaryTaskService;

    @Qualifier("areaService")
    @Autowired
    private AreaService areaService;

    @Qualifier("shopTaskService")
    @Autowired
    private ShopTaskService shopTaskService;

    /**
     * 炒店临时任务分页展示
     *
     * @param paras   炒店ID，炒店名称，状态
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopTempTaskByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<ShopTaskDomain> queryShopTempTaskByPage(@RequestParam Map<String, Object> paras, HttpSession session) {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        parasMap.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        parasMap.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());

        Map<String, Object> result = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String) paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //炒店任务ID
        String shopTaskId = (String) (paras.get("shopTaskId"));
        //炒店任务名
        String shopTaskName = StringUtils.isNotEmpty(String.valueOf(paras.get("projectName"))) ? SearchConditionUtil.optimizeCondition(String.valueOf(paras.get("projectName"))) : null;
        //炒店任务类型
        String taskType = !"".equals(String.valueOf(paras.get("taskType"))) ? String.valueOf(paras.get("taskType")) : "";
        //地区
        String baseArea = !"".equals(String.valueOf(paras.get("baseArea"))) ? String.valueOf(paras.get("baseArea")) : "";
        //开始时间
        String beginTime = !"".equals(String.valueOf(paras.get("beginTime"))) ? String.valueOf(paras.get("beginTime")) : "";

        Integer itemCounts = 0;

        parasMap.put("shopTaskId", shopTaskId);
        parasMap.put("shopTaskName", shopTaskName);
        parasMap.put("taskType", taskType);
        parasMap.put("baseArea", baseArea);
        parasMap.put("beginTime", beginTime);
        parasMap.put("offset", curPageIndex);
        parasMap.put("limit", pageSize);
        List<ShopTemporaryTaskDomain> shopTaskList = null;
        itemCounts = shopTemporaryTaskService.queryShopTempTaskTotal(parasMap);
        shopTaskList = shopTemporaryTaskService.queryShopTempTaskByPage(parasMap);

        result.put("itemCounts", itemCounts);
        result.put("items", shopTaskList);
        return new Table(shopTaskList, itemCounts);
    }

    /**
     * 新建炒店临时任务
     */
    @RequestMapping(value = "createShopTempTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult createShopTempTask(@RequestBody ShopTemporaryTaskDomain shopTaskDomain, HttpSession session) {

        UserDomain userDomain = UserUtils.getLoginUser(session);
        ServiceResult result = doJudge(shopTaskDomain, userDomain);
        if (-1 == result.getRetValue()) {
            return result;
        }

        //判断该用户是否需要审批，如果不需要审批，直接设置状态为审批通过
        if (StringUtils.isEmpty(userDomain.getMarketingAuditUsers())) {
            shopTaskDomain.setStatus(ShopTaskStatusEnum.TASK_READY.getValue());
        } else {
            shopTaskDomain.setStatus(ShopTaskStatusEnum.TASK_AUDITING.getValue());
        }

        return shopTemporaryTaskService.insertShopTempTask(shopTaskDomain, userDomain);
    }

    /**
     * 编辑炒店临时任务
     */
    @RequestMapping(value = "updateShopTempTask.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateShopTempTask(@RequestBody ShopTemporaryTaskDomain shopTaskDomain, HttpSession session) {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        ServiceResult result = doJudge(shopTaskDomain, userDomain);
        if (-1 == result.getRetValue()) {
            return result;
        }

        //判断该用户是否需要审批，如果不需要审批，直接设置状态为审批通过
        if (StringUtils.isEmpty(userDomain.getMarketingAuditUsers())) {
            shopTaskDomain.setStatus(ShopTaskStatusEnum.TASK_READY.getValue());
        } else {
            shopTaskDomain.setStatus(ShopTaskStatusEnum.TASK_AUDITING.getValue());
        }

        return shopTemporaryTaskService.updateShopTempTask(shopTaskDomain, userDomain);
    }

    //判断是否允许创建
    ServiceResult doJudge(ShopTemporaryTaskDomain shopTaskDomain, UserDomain userDomain) {
        ServiceResult result = new ServiceResult();
        shopTaskDomain.setCreateUser(String.valueOf(userDomain.getId()));
        Integer areaCode = userDomain.getAreaCode();

        String businessHallIds = userDomain.getBusinessHallIds();
        //if (1 == userDomain.getUserType()) {
        if (99999 == areaCode) {
            shopTaskDomain.setTaskType(1); //省级任务
            if (!areaService.belongArea(userDomain.getAreaCode(), shopTaskDomain.getCityCode())) {
                result.setRetValue(-1);
                result.setDesc("请选择本省地址");
                return result;
            }
        } else if (null == businessHallIds || "".equals(businessHallIds)){
            shopTaskDomain.setTaskType(2); //地市级任务
            //市级用户只能选择本市地点
            if (!shopTaskDomain.getCityCode().equals(userDomain.getAreaCode())) {
                result.setRetValue(-1);
                result.setDesc("请选择本市地址");
                return result;
            }
        } else {
            shopTaskDomain.setTaskType(3); //营业厅级任务
            result.setRetValue(-1);
            result.setDesc("没有权限创建该任务");
            return result;
        }
        return result;
    }

    /**
     * 根据ID查询删除炒店临时任务
     *
     * @param paras   主键ID
     * @param session
     * @return
     */
    @RequestMapping(value = "deleteShopTempTaskById.view")
    @ResponseBody
    public ServiceResult deleteShopTempTaskById(@RequestBody Map<String, Object> paras, HttpSession session) {
        Integer shopTaskId = Integer.valueOf(String.valueOf(paras.get("shopTaskId")));
        ServiceResult result = shopTemporaryTaskService.deleteShopTempTaskById(shopTaskId);
        return result;
    }

    /**
     * 查询临时促销接入号
     *
     * @return
     */
    @RequestMapping(value = "queryAccessNum.view")
    @ResponseBody
    public List<Map<String, String>> queryAccessNum(HttpSession session) {
        Map<String, Object> parasMap = new HashMap<String, Object>();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        parasMap.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        parasMap.put("type", "create");
        List<Map<String, String>> result = shopTaskService.querFixedAccessNum(parasMap);
        return result;
    }

    /**
     * 终止任务
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "pauseTempItem.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult pauseTempItem(@RequestBody Map<String, Object> paras, HttpSession session) {
        String id = String.valueOf(paras.get("id"));
        String taskName = (String) paras.get("taskName");
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return shopTemporaryTaskService.pauseItem(Integer.valueOf(id));
    }
}
