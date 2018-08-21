package com.axon.market.web.controller.ikeeper;

import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.Table;
import com.axon.market.common.domain.ikeeper.KeeperUserDomain;
import com.axon.market.common.domain.ikeeper.UserMaintainDomain;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.AxonEncryptUtil;
import com.axon.market.common.util.UserUtils;
import com.axon.market.core.service.iflow.OldCustomerService;
import com.axon.market.core.service.ikeeper.DirectionalCustomerBaseService;
import org.apache.commons.lang.StringUtils;
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
 * Created by wangtt on 2017/8/9.
 */
@Controller("directionalCustomerBaseController")
public class DirectionalCustomerBaseController
{

    @Autowired
    @Qualifier("directionalCustomerBaseService")
    DirectionalCustomerBaseService directionalCustomerBaseService;

    AxonEncryptUtil axonEncryptUtil = AxonEncryptUtil.getInstance();

    private static Logger LOG = Logger.getLogger(DirectionalCustomerBaseController.class.getName());


    /**
     * 查询用户维系关系数据
     * @param paras
     * @param session
     * @return
     */
    @RequestMapping(value = "queryDirectonalCustomerBase.view",method = RequestMethod.POST)
    @ResponseBody
    public Table<Map<String,Object>> queryDirectonalCustomerBase(@RequestParam Map<String,Object> paras,HttpSession session){
        // 获取当前登录的用户信息
        UserDomain userDomain = UserUtils.getLoginUser(session);
        int maintainUserId = userDomain.getId();
        int maintainUserAreaId = userDomain.getAreaId();
        int isMaintainUserManage;// 1 : 没有管理权  0 : 有管理权
        if(maintainUserAreaId != 99999){
            // 根据userId查询地市用户是否有管理权
            isMaintainUserManage =  directionalCustomerBaseService.queryUserManageJurisdiction(maintainUserId)?1:0;
        }else{
            // 省级有管理权
            isMaintainUserManage = 1;
        }
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("maintainUserId",maintainUserId);
        paramMap.put("userPhone",axonEncryptUtil.encrypt(String.valueOf(paras.get("userPhone"))));//查询的时候要加密手机号
        paramMap.put("maintainUserPhone",paras.get("maintainPhone"));
        paramMap.put("areaCode",paras.get("areaSelect"));
        paramMap.put("isManage",isMaintainUserManage);
        paramMap.put("maintainUserAreaId", maintainUserAreaId);
        paramMap.put("limit",paras.get("length"));
        paramMap.put("offset",paras.get("start"));
        return directionalCustomerBaseService.queryDirectonalCustomerBase(paramMap);
    }

    /**
     * 新增用户维系关系
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "createUserMaintain.view",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult createUserMaintain(@RequestBody Map<String,Object> param,HttpSession session){
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer maintainUserId = userDomain.getId();
        String userPhone = String.valueOf(param.get("userPhone"));
        if(StringUtils.isEmpty(userPhone)){
            return new ServiceResult(-1,"数据传输异常");
        }
        // 手机号码加密
        String userEncryptPhone = axonEncryptUtil.encrypt(userPhone);
        // 检查是否存在，若存在则覆盖
        int i = directionalCustomerBaseService.checkUserIsExist(userEncryptPhone, maintainUserId);
        if(i != 1 && i != 3){
            return new ServiceResult(-1,"重复覆盖异常");
        }
        // 执行插入操作
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("userPhone",userEncryptPhone);
        paramMap.put("userCode",param.get("userCode"));// 需要校验是否已经存在
        paramMap.put("userName",param.get("userName"));
        paramMap.put("userAreaCode",param.get("userAreaCode"));
        paramMap.put("maintainUserId",param.get("maintainUserId"));
        paramMap.put("userWeiXin",param.get("userWeiXin"));
        paramMap.put("userWeiBo",param.get("userWeiBo"));
        paramMap.put("userQQ",param.get("userQQ"));
        paramMap.put("userWangWang",param.get("userWangWang"));
        paramMap.put("status", 1);
        paramMap.put("createUser",maintainUserId);
        int rs = directionalCustomerBaseService.createUserMaintain(paramMap);
        if(rs<1){
            return  new ServiceResult(-1,"创建失败");
        }
        return new ServiceResult();
    }


    /**
     * 查询用户维系关系明细
     * @param param
     * @return
     */
    @RequestMapping(value = "queryUserMaintainDetail.view",method = RequestMethod.POST)
    @ResponseBody
    public UserMaintainDomain queryUserMaintainDetail(@RequestBody Map<String,Object> param){
        Integer userId = Integer.parseInt(String.valueOf(param.get("userId")));
        UserMaintainDomain userMaintainDomain = directionalCustomerBaseService.queryUserMaintainDetail(userId);
        String userPhone = userMaintainDomain.getUserPhone();
        userMaintainDomain.setUserPhone(axonEncryptUtil.decrypt(userPhone).substring(2));
        return userMaintainDomain;
    }

    /**
     * 更新用户维系关系
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "updateUserMaintain.view",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult updateUserMaintain(@RequestBody Map<String,Object> param,HttpSession session){
        UserDomain userDomain  = UserUtils.getLoginUser(session);
        Integer updateUserId = userDomain.getId();
        Map<String,Object> paraMap = new HashMap<String,Object>();
        String userName = String.valueOf(param.get("userName"));
        String userPhone  = String.valueOf(param.get("userName"));
        if(StringUtils.isEmpty(userName)){
            return new ServiceResult(-1,"数据传输异常");
        }
        paraMap.put("userName",userName);
        paraMap.put("userPhone",userPhone);
        paraMap.put("userCode",param.get("userCode"));
        paraMap.put("userAreaCode",param.get("userAreaCode"));
        paraMap.put("id",param.get("id"));
        paraMap.put("userWeiXin",param.get("userWeiXin"));
        paraMap.put("userQQ",param.get("userQQ"));
        paraMap.put("userWeiBo",param.get("userWeiBo"));
        paraMap.put("userWangWang",param.get("userWangWang"));
        paraMap.put("updateUser",updateUserId);
        return directionalCustomerBaseService.updateUserMaintain(paraMap);
    }

    /**
     * 删除用户维系关系
     * @param param
     * @return
     */
    @RequestMapping(value = "deleteUserMaintain.view",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult deleteUserMaintain(@RequestBody Map<String,Object> param){
        String userId = String.valueOf(param.get("userId"));
        if(StringUtils.isEmpty(userId)){
            return new ServiceResult(-1,"数据传输异常");
        }
        return directionalCustomerBaseService.deleteUserMaintain(userId);
    }

    /**
     * 查询当前的维系员工
     * @param param
     * @param session
     * @return
     */
    @RequestMapping(value = "queryCurrentKeeperUser.view",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryCurrentKeeperUser(@RequestBody Map<String,Object> param,HttpSession session){
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer loginUserId = userDomain.getId();
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("loginUser",directionalCustomerBaseService.queryCurrentKeeperUser(loginUserId));
        return result;
    }

    /**
     * 导入用户维系关系
     * @param request
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "importDirectionalCustomer.view",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> importDirectionalCustomer(HttpServletRequest request,HttpServletResponse response,HttpSession session){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        MultipartFile file = multipartRequest.getFile("file");
        UserDomain userDomain = UserUtils.getLoginUser(session);
        String maintainUserPhone = String.valueOf(userDomain.getTelephone());
        KeeperUserDomain keeperUser = userDomain.getKeeperUser();
        boolean isCanManage = false;
        if(keeperUser != null && keeperUser.getIsCanManage() == 1){
            isCanManage = true;
        }
        Map<String, Object> fileInfo = new HashMap<String, Object>();
        Long fileId = new Date().getTime();
        fileInfo.put("fileId", fileId);
        fileInfo.put("fileName", file.getOriginalFilename());
        fileInfo.put("fileSize", file.getSize());
        fileInfo.put("importUser", userDomain == null ? "admin" : userDomain.getId());
        fileInfo.put("createDate", new Date());
        try
        {
            ServiceResult result = directionalCustomerBaseService.importTempleFile(fileInfo, file.getInputStream(),isCanManage,userDomain);
            resultMap.put("retValue",result.getRetValue());
            resultMap.put("desc",result.getDesc());
            if(result.getRetValue() == 0){
                resultMap.put("fileId",fileId);
            }
        }
        catch (Exception e)
        {
            LOG.error("导入用户维系关系异常",e);
        }
        return resultMap;
    }

    /**
     * 保存上传的用户维系信息
     * @param param
     * @return
     */
    @RequestMapping(value = "saveDirectionalCustomer.view",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult saveDirectionalCustomer(@RequestBody Map<String,Object> param,HttpSession session){
        UserDomain userDomain = UserUtils.getLoginUser(session);
        Integer userId = userDomain.getId();
        String fileId = String.valueOf(param.get("fileId"));
        if(StringUtils.isEmpty(fileId)){
            return new ServiceResult(-1,"数据传输异常");
        }
        return directionalCustomerBaseService.saveDirectionalCustomer(fileId,userId);
    }
}
