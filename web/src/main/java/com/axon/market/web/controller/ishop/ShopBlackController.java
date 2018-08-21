package com.axon.market.web.controller.ishop;

import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.ishop.ShopBlackDomain;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.ishop.ShopBlackService;
import com.axon.market.core.service.ishop.ShopTaskService;
import com.axon.market.core.service.isystem.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 炒店用户黑名单
 * Created by zengcr on 2017/4/12.
 */
@Controller("shopBlackController")
public class ShopBlackController
{
    private static final Logger LOG = Logger.getLogger(ShopBlackController.class.getName());

    @Autowired
    @Qualifier("shopBlackService")
    private ShopBlackService shopBlackService;

    @Qualifier("userService")
    @Autowired
    private UserService userService;

    @Qualifier("shopTaskService")
    @Autowired
    private ShopTaskService shopTaskService;

    /**
     * 炒店黑名单分页显示
     *
     * @param paras   phone用户号码
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopBlackPhoneByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<ShopTaskDomain> queryShopBlackPhoneByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> parasMap = new HashMap<String, Object>();

        UserDomain loginUserDomain = UserUtils.getLoginUser(session);
        //从数据库读取
        UserDomain userDomain = userService.queryUserById(loginUserDomain.getId());
        parasMap.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());


        Map<String, Object> result = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf(String.valueOf(paras.get("start")));
        int pageSize = Integer.valueOf(String.valueOf(paras.get("length")));
        //手机号码
        String phone = String.valueOf(paras.get("phone"));
        String baseAreas = String.valueOf(paras.get("baseAreas"));
        Integer itemCounts = 0;
        parasMap.put("phone", phone);
        parasMap.put("baseAreas", baseAreas);
        parasMap.put("offset", curPageIndex);
        parasMap.put("limit", pageSize);
        List<ShopBlackDomain> shopBlackList = null;
        itemCounts = shopBlackService.queryShopBlackPhoneTotal(parasMap);
        shopBlackList = shopBlackService.queryShopBlackPhoneByPage(parasMap);

        result.put("itemCounts", itemCounts);
        result.put("items", shopBlackList);
        return new Table(shopBlackList, itemCounts);
    }

    @RequestMapping(value = "queryShopBlackBaseAreas.view", method = RequestMethod.POST)
    @ResponseBody
    public String queryShopBlackBaseAreas(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        String areaIds = String.valueOf(paras.get("AREA_IDS"));

        UserDomain loginUserDomain = UserUtils.getLoginUser(session);
        UserDomain userDomain = userService.queryUserById(loginUserDomain.getId());
        String businessCodes = userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds();
        return shopBlackService.queryShopBlackBaseAreas(areaIds, businessCodes);
    }

    /**
     * 查询基站站点
     *
     * @param paras   baseAreaId:地区编码，baseId:基站ID，baseName:基站名称
     * @param session
     * @return
     */
    @RequestMapping(value = "queryShopBlockBases.view", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> queryShopBlockBases(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        List<Map<String, String>> list = null;
        Map<String, Object> parasMap = new HashMap<String, Object>();

        UserDomain loginUserDomain = UserUtils.getLoginUser(session);
        //从数据库读取
        UserDomain userDomain = userService.queryUserById(loginUserDomain.getId());
        parasMap.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        parasMap.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());

        Map<String, Object> result = new HashMap<String, Object>();
        String baseAreaId = String.valueOf(paras.get("baseAreaId"));   //地区编码ID
        String baseName = String.valueOf(paras.get("baseName"));   //基站名称
        parasMap.put("baseAreaId", baseAreaId);
        parasMap.put("baseName", baseName);

        list = shopBlackService.queryShopBlockBases(parasMap);
        result.put("items", list);
        return result;
    }

    /**
     * 新增炒店用户黑名单
     *
     * @param shopBlackDomain
     * @param session
     * @return
     */
    @RequestMapping(value = "createShopUserBlackList.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation createShopUserBlackList(@RequestBody ShopBlackDomain shopBlackDomain, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        shopBlackDomain.setCreateUser(userDomain.getId());
        return shopBlackService.createShopUserBlackList(shopBlackDomain);
    }

    /**
     * 批量导入 炒店用户黑名单
     *
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "createMoreShopUserBlackList.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation createMoreShopUserBlackList(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        paras.put("createUser", userDomain.getId());
        return shopBlackService.createMoreShopUserBlackList(paras);
    }

    /**
     * @param paras
     * @return
     */
    @RequestMapping(value = "deleteShopUserBlackList.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteShopUserBlackList(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        paras.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        return shopBlackService.deleteShopUserBlackList(paras);
    }

    /**
     * @param paras
     * @return
     */
    @RequestMapping(value = "deleteAllShopUserBlackList.view", method = RequestMethod.POST)
    @ResponseBody
    public Operation deleteAllShopUserBlackList(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        //从数据库读取
        paras.put("businessCodes", userDomain.getBusinessHallIds() == null ? "" : userDomain.getBusinessHallIds());
        paras.put("areaCode", userDomain.getAreaCode() == null ? "" : userDomain.getAreaCode());
        return shopBlackService.deleteAllShopUserBlackList(paras);
    }

    /**
     * 批量导入炒店黑名单用户文件
     *
     * @param request
     * @return
     */
    @RequestMapping("importShopUserBlackListFile.view")
    @ResponseBody
    public Map<String, Object> importShopUserBlackListFile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
    {
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile file = multipartRequest.getFile("file");
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Map<String, Object> fileInfo = new HashMap<String, Object>();
        Long fileId = new Date().getTime();
        fileInfo.put("fileId", fileId);
        fileInfo.put("fileName", file.getOriginalFilename());
        fileInfo.put("fileSize", file.getSize());
        fileInfo.put("taskType", "炒店用户黑名单文件导入");
        fileInfo.put("createUser", userDomain == null ? "admin" : userDomain.getId());
        fileInfo.put("createDate", new Date());
        fileInfo.put("targetTable", "shop_black_users");

        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            ServiceResult returnResult = shopTaskService.storeFile(fileInfo, file.getInputStream(), "shopUserBlackList");
            result.put("retValue", returnResult.getRetValue());
            result.put("desc", returnResult.getDesc());
            result.put("fileId", fileId);
        }
        catch (IOException e)
        {
            LOG.error("importShopUserBlackListFile error." + e);
            result.put("retValue", "-1");
            result.put("desc", "文件导入失败");
        }
        return result;
    }

    /**
     * 刷新黑名单进GP
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "flushShopBlack.view", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult flushShopBlack(HttpSession session)
    {
        UserDomain loginUser = UserUtils.getLoginUser(session);
        return shopBlackService.flushShopBlack();
    }
}
