package com.axon.market.web.controller.ishop;

import com.axon.market.common.bean.MarketingAuditUsersModel;
import com.axon.market.common.bean.SmsConfigBean;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.icommon.MenuDomain;
import com.axon.market.common.domain.ishop.BusinessHallPortraitDomain;
import com.axon.market.common.domain.ishop.ShopListDomain;
import com.axon.market.common.domain.ishop.ShopReviewDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.util.SearchConditionUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.icommon.MenuService;
import com.axon.market.core.service.icommon.SendSmsService;
import com.axon.market.core.service.ishop.ShopListService;
import com.axon.market.dao.mapper.isystem.IRoleMapper;
import com.axon.market.dao.mapper.isystem.IUserMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的炒店控制器
 * Created by gloomysw on 2017/02/20.
 */
@Controller("myShopListController")
public class MyShopListController
{
    @Autowired
    @Qualifier("shopListService")
    private ShopListService shopListService;

    @Autowired
    @Qualifier("sendSmsService")
    private SendSmsService sendSmsService;

    @Autowired
    @Qualifier("smsConfigBean")
    private SmsConfigBean smsConfigBean;

    @Autowired
    @Qualifier("menuService")
    private MenuService menuService;

    @Autowired
    @Qualifier("userDao")
    private IUserMapper userDao;

    @Autowired
    @Qualifier("roleDao")
    private IRoleMapper roleDao;

    /**
 * 获取我的炒店列表
 * 创建人:邵炜
 * 创建时间:2017年2月20日11:10:33
 *
 * @return 我的炒店实体集合
 */
@RequestMapping(value = "selectMyShopList.view", method = RequestMethod.POST)
@ResponseBody
private Table<ShopListDomain> selectMyShopList(@RequestParam Map<String, Object> paras, HttpSession session)
{
    UserDomain userDomain = UserUtils.getLoginUser(session);
    Integer createUserId = userDomain.getId();
    String cityCode = "", baseIdArray = userDomain.getBusinessHallIds();
    if (StringUtils.isBlank(baseIdArray))
    {
        cityCode= String.valueOf(userDomain.getAreaCode());
        baseIdArray="";
    }
    Map<String, Object> result = new HashMap<String, Object>();
    int pageIndex = Integer.valueOf((String) paras.get("pageIndex"));
    int  itemCounts = shopListService.selectPositionBaseTotal("","","",createUserId,baseIdArray,cityCode,"");
    List<ShopListDomain> positionBaseDomainList = shopListService.selectPositionBaseByPage(12, pageIndex*12, "", "", "",createUserId,baseIdArray,cityCode,"");

    result.put("itemCounts", itemCounts);
    result.put("items", positionBaseDomainList);
    return new Table(positionBaseDomainList, itemCounts);
}

    /**
     * 新增我的炒店
     * 创建人:邵炜
     * 创建时间:2017年2月20日19:46:21
     *
     * @param model 我的炒店实体
     * @return 受影响行数
     */
    @RequestMapping(value = "insertMyShopList.view", method = RequestMethod.POST)
    @ResponseBody
    private long insertMyShopList(@RequestBody ShopListDomain model, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);

        UserDomain userDomaind = userDao.queryUserById(userDomain.getId());
        List<MenuDomain> menuList = menuService.getMenus(userDomaind.getId(), session);
        session.setAttribute("USER", userDomaind);
        //session存放数据权限
        session.setAttribute("D_PERMISSION", getDataPermissions(userDomaind));
        session.setAttribute("MENU", menuList);

        String cityCode = "", baseIdArray = userDomain.getBusinessHallIds();
        if (StringUtils.isBlank(baseIdArray))
        {
            cityCode= String.valueOf(userDomain.getAreaCode());
            baseIdArray="";
        }
        return shopListService.insertMyShopList(model, userDomain.getId(),cityCode,baseIdArray);
    }

    /**
     * 修改我的炒店
     * 创建人:邵炜
     * 创建时间:2017年2月20日19:46:21
     *
     * @param model 我的炒店实体
     * @return 受影响行数
     */
    @RequestMapping(value = "updateMyShopList.view", method = RequestMethod.POST)
    @ResponseBody
    private int updateMyShopList(@RequestBody ShopListDomain model, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String cityCode = "", baseIdArray = userDomain.getBusinessHallIds();
        if (StringUtils.isBlank(baseIdArray))
        {
            cityCode= String.valueOf(userDomain.getAreaCode());
            baseIdArray="";
        }
        return shopListService.updateMyShopList(model, userDomain.getId(), baseIdArray, cityCode);
    }

    /**
     * 修改或新增我的炒店
     *
     * @param model 我的炒店对象
     * @return 受影响行数
     */
    @RequestMapping(value = "addOrUpdateMyShopList.view", method = RequestMethod.POST)
    @ResponseBody
    private long addOrUpdateMyShopList(@RequestBody ShopListDomain model,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String cityCode = "", baseIdArray = userDomain.getBusinessHallIds();
        if (StringUtils.isBlank(baseIdArray))
        {
            cityCode= String.valueOf(userDomain.getAreaCode());
            baseIdArray="";
        }
        model.setStatus("2");
        model.setReviewUserId(0);
        model.setReviewUserIdFrom(0);
        try {
            List<MarketingAuditUsersModel> list= JsonUtil.stringToObject(userDomain.getMarketingAuditUsers(),new TypeReference<List<MarketingAuditUsersModel>>()
            {
            });
            if (list.size()>0){
                model.setReviewUserId(Integer.valueOf(list.get(0).getAuditUser()));
                model.setReviewUserIdFrom(userDomain.getId());
            }
        } catch (Exception e) {

        }
        if (model.getBaseId() > 0){
            return shopListService.updateMyShopList(model,userDomain.getId(),baseIdArray,cityCode);
        }else{
            long baseid=shopListService.insertMyShopList(model,userDomain.getId(),cityCode,baseIdArray);
            UserDomain userDomaind = userDao.queryUserById(userDomain.getId());
            List<MenuDomain> menuList = menuService.getMenus(userDomaind.getId(), session);
            session.setAttribute("USER", userDomaind);
            //session存放数据权限
            session.setAttribute("D_PERMISSION", getDataPermissions(userDomaind));
            session.setAttribute("MENU", menuList);
            return baseid;
        }
    }

    /**
     * 获取用户的数据权限
     *
     * @param userDomain
     * @return
     */
    private Map<String, Boolean> getDataPermissions(UserDomain userDomain)
    {
        Map<String, Boolean> permissionResult = new HashMap<>();

        List<Map<String, Object>> permissions = roleDao.queryUserDataPermissionList(userDomain.getId());
        for (Map<String, Object> permission : permissions)
        {
            //用于后端数据鉴权控制
            permissionResult.put(String.valueOf(permission.get("url")) + "_u", "1".equals(String.valueOf(permission.get("isContain"))));
            //用于前端鉴权控制(显示隐藏等)
            permissionResult.put(String.valueOf(permission.get("permission")) + "_p", "1".equals(String.valueOf(permission.get("isContain"))));
        }
        return permissionResult;
    }

    /**
     * 修改或新增我的炒店
     *
     * @param model 我的炒店对象
     * @return 受影响行数
     */
    @RequestMapping(value = "adminAddOrUpdateMyShopList.view", method = RequestMethod.POST)
    @ResponseBody
    private long adminAddOrUpdateMyShopList(@RequestBody ShopListDomain model,HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer createUserId = userDomain.getId();
        String cityCode = "", baseIdArray = userDomain.getBusinessHallIds();
        if (StringUtils.isBlank(baseIdArray))
        {
            cityCode= String.valueOf(userDomain.getAreaCode());
            baseIdArray="";
        }
        model.setReviewUserId(0);
        model.setReviewUserIdFrom(0);
        try {
            List<MarketingAuditUsersModel> list= JsonUtil.stringToObject(userDomain.getMarketingAuditUsers(),new TypeReference<List<MarketingAuditUsersModel>>()
            {

            });
            if (list.size()>0){
                model.setReviewUserId(Integer.valueOf(list.get(0).getAuditUser()));
                model.setReviewUserIdFrom(userDomain.getId());
            }
        } catch (Exception e) {

        }
        if (model.getReviewUserId()==0){
            model.setStatus("1");
        }else{
            model.setStatus("2");
        }
        if (model.getBaseId() > 0){
            return shopListService.updateMyShopList(model,createUserId,baseIdArray,cityCode);
        }else{
            long baseIds=shopListService.insertMyShopList(model,createUserId,cityCode,baseIdArray);
            UserDomain userDomaind = userDao.queryUserById(userDomain.getId());
            List<MenuDomain> menuList = menuService.getMenus(userDomaind.getId(), session);
            session.setAttribute("USER", userDomaind);
            //session存放数据权限
            session.setAttribute("D_PERMISSION", getDataPermissions(userDomaind));
            session.setAttribute("MENU", menuList);
            return baseIds;
        }
    }

    /**
     * 位置场景基站配置分页展示
     *
     * @param paras   基站ID，基站名称
     * @param session
     * @return
     */
    @RequestMapping(value = "selectBaseInfoByPage.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<ShopListDomain> selectBaseInfoByPage(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String) paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //基站ＩＤ
        String baseId = (String) (paras.get("baseId"));
        //基站名称
        String baseName = SearchConditionUtil.optimizeCondition((String) (paras.get("baseName")));
        //所属地市
        String baseArea = (String) (paras.get("baseArea"));
        //营业厅编码
        String busCoding= SearchConditionUtil.optimizeCondition((String)(paras.get("buscoding")));
        Integer itemCounts = 0;
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer createUserId = userDomain.getId();
        String cityCode = "", baseIdArray = userDomain.getBusinessHallIds();
        if (StringUtils.isBlank(baseIdArray))
        {
            cityCode= String.valueOf(userDomain.getAreaCode());
            baseIdArray="";
        }

        itemCounts = shopListService.selectPositionBaseTotal(baseId,baseName,baseArea,createUserId,baseIdArray,cityCode,busCoding);
        List<ShopListDomain> positionBaseDomainList = shopListService.selectPositionBaseByPage(pageSize,curPageIndex, baseId, baseName, baseArea,createUserId,baseIdArray,cityCode,busCoding);

        result.put("itemCounts", itemCounts);
        result.put("items", positionBaseDomainList);
        return new Table(positionBaseDomainList, itemCounts);
    }

    /**
     * 获取待审核的店面信息
     * 创建人:邵炜
     * 创建时间:2017年2月22日19:57:04
     *
     * @param paras   基站ID，基站名称
     * @param session
     * @return
     */
    @RequestMapping(value = "selectBaseInfoByPageStatus.view", method = RequestMethod.POST)
    @ResponseBody
    public Table<ShopListDomain> selectBaseInfoByPageStatus(@RequestParam Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer reviewUserId = userDomain.getId();

        String cityCode = "", baseIdArray = userDomain.getBusinessHallIds();
        if (StringUtils.isBlank(baseIdArray))
        {
            cityCode= String.valueOf(userDomain.getAreaCode());
            baseIdArray="";
        }
        Map<String, Object> result = new HashMap<String, Object>();
        int curPageIndex = Integer.valueOf((String) paras.get("start"));
        int pageSize = Integer.valueOf((String) paras.get("length"));
        //基站ＩＤ
        String baseId = (String) (paras.get("baseId"));
        //基站名称
        String baseName = SearchConditionUtil.optimizeCondition((String) (paras.get("baseName")));
        Integer itemCounts = 0;

        itemCounts = shopListService.selectBaseInfoByPageStatusTotal(baseId, baseName, reviewUserId);
        List<ShopListDomain> positionBaseDomainList = shopListService.selectBaseInfoByPageStatus(pageSize, curPageIndex, baseId, baseName, reviewUserId);

        result.put("itemCounts", itemCounts);
        result.put("items", positionBaseDomainList);
        return new Table(positionBaseDomainList, itemCounts);
    }

    /**
     * 发送催单短信给审核人员
     * 创建人:邵炜
     * 创建人:邵炜
     * 创建时间:2017年3月11日17:21:48
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "sendPreviewUserMessage.view", method = RequestMethod.POST)
    @ResponseBody
    public int sendPreviewUserMessage(@RequestBody Map<String,Object> paras,HttpSession session){
        Integer baseId= (Integer) paras.get("baseId");
        String baseName= (String) paras.get("baseName");
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer createUserId = userDomain.getId();
        String cityCode = "", baseIdArray = userDomain.getBusinessHallIds();
        if (StringUtils.isBlank(baseIdArray))
        {
            cityCode= String.valueOf(userDomain.getAreaCode());
            baseIdArray="";
        }
        UserDomain model=shopListService.selectReviewUserPhoneByBaseId(createUserId, cityCode, String.valueOf(baseId), baseIdArray);
        if (model==null){
            return 0;
        }
        // String message= MessageFormat.format("【智能营销】尊敬的{0}用户您好，炒店配置审批[{1}]待您审核，请登录江苏智能营销平台处理",model.getName(),baseName);
        String message = MessageFormat.format(smsConfigBean.getReminderNoticeSmsContent(),model.getName(),baseName);
        return "0".equals(sendSmsService.sendReminderNoticeSms(model.getTelephone(),message))?1:0;
    }

    /**
     * 炒店审核
     * 创建人:邵炜
     * 创建时间:2017年2月22日22:33:24
     *
     * @param paras   接入参数
     * @param session 登录人信息
     * @return
     */
    @RequestMapping(value = "updateShopStatusByBaseId.view", method = RequestMethod.POST)
    @ResponseBody
    public int updateShopStatusByBaseId(@RequestBody Map<String, Object> paras, HttpSession session)
    {
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer reviewUserId = userDomain.getId();
        String cityCode = "", baseIdArray = userDomain.getBusinessHallIds();
        if (StringUtils.isBlank(baseIdArray))
        {
            cityCode= String.valueOf(userDomain.getAreaCode());
        }else{
            baseIdArray="";
        }
        String baseId = (String) paras.get("baseId");
        String status = (String) paras.get("status");
        String remark = (String) paras.get("remark");
        String baseName=(String)paras.get("baseName");
        String cityCodef=String.valueOf((int)paras.get("cityCode"));
        String updateReviewUserId="0";
        Integer updateReviewFrom=0;

        ShopReviewDomain dbProcessModel=shopListService.selectShopByBaseId(baseId,reviewUserId);
        if (dbProcessModel!=null){
            try {
                List<MarketingAuditUsersModel> list= JsonUtil.stringToObject(dbProcessModel.getMarketingAuditUsers(),new TypeReference<List<MarketingAuditUsersModel>>()
                {
                });
                for (int index=0;index<list.size();index++){
                    if (Integer.getInteger(list.get(index).getAuditUser()) ==userDomain.getId()&&index+1<=list.size()){
                        updateReviewUserId=list.get(index+1).getAuditUser();
                        updateReviewFrom=dbProcessModel.getReviewUserIdFrom();
                    }
                }
            } catch (IOException e) {

            }
        }

//        if (updateReviewUserId=="0"){
//            try {
//                List<marketingAuditUsersModel> list= JsonUtil.stringToObject(userDomain.getMarketingAuditUsers(),new TypeReference<List<marketingAuditUsersModel>>()
//                {
//                });
//                if (list.size()>0){
//                    updateReviewUserId=list.get(0).getAuditUser();
//                    updateReviewFrom=userDomain.getId();
//                }
//            } catch (IOException e) {
//
//            }
//        }

        if (updateReviewUserId!="0"){
            status="2";
        }

        int resultCode = shopListService.updateShopStatusByBaseId(baseId, status, reviewUserId,updateReviewUserId,updateReviewFrom,cityCodef,baseName);
        resultCode += shopListService.insertShopAuditThrough(baseId, remark, reviewUserId);
        return resultCode;
    }

    /**
     * @param paras
     * @return
     */
    @RequestMapping(value = "queryBaseInfoById.view", method = RequestMethod.POST)
    @ResponseBody
    public ShopListDomain queryBaseInfoById(@RequestBody Map<String, Object> paras)
    {
        String baseId = String.valueOf(paras.get("baseId"));
        return shopListService.queryBaseInfoById(baseId);
    }


    // 根据营业厅编码查询营业厅画像
    @RequestMapping("queryBusinessHallPortraitById.view")
    @ResponseBody
    public Map<String,Object> queryBusinessHallPortraitById(@RequestBody Map<String,Object> param){
        String businessHallCoding = String.valueOf(param.get("businessHallCoding"));
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(businessHallCoding)){
            return map;
        }
        BusinessHallPortraitDomain domain =  shopListService.queryBusinessHallPortraitById(businessHallCoding);
        map.put("data",domain);
        return map;
    }
}
