package com.axon.market.web.controller.iservice;

import com.axon.market.common.domain.iflow.OldCustomerDomain;
import com.axon.market.common.domain.iservice.OldCustomerResultDomain;
import com.axon.market.common.domain.iservice.OldCustomerResult;
import com.axon.market.common.util.RedisUtil;
import com.axon.market.core.service.iflow.OldCustomerService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 老用户专享对外提供的API接口
 * Created by zengcr on 2017/7/29.
 */
@RestController
@RequestMapping("preferentialTask")
public class OldCustomerAPIController {
    private static final  String SHOP_AREA_WHITE_PHONE_LIST  = "shopWhitePhoneList";

    private  static  Logger LOG = Logger.getLogger(OldCustomerAPIController.class.getName());

    @Autowired
    @Qualifier("oldCustomerService")
    OldCustomerService oldCustomerService;



    /**
     * 根据手机号码获取该用户参与的老用户专享优惠活动
     * @param phone
     * @return
     */
    @RequestMapping(value = "/{phone}", method = RequestMethod.GET)
    public OldCustomerResult queryPreferentialTask(@PathVariable String phone){
        OldCustomerResult result = new OldCustomerResult();
        // 判断手机号是否格式正确
        if(isPhoneFormat(phone)){
            List<OldCustomerResultDomain> preferentialTasks = new ArrayList<OldCustomerResultDomain>();
            OldCustomerResultDomain domain = new OldCustomerResultDomain();//返回的相关活动信息
            List<Integer> matchedTaskId = new ArrayList<Integer>();
            //1、找出当前已上线的任务
            //2、遍历任务
            //3、根据目标地区或者是目标号码匹配任务
            //4、匹配到的任务添加到 List<OldCustomerResultDomain> preferentialTasks

            // 1 遍历所有已经上线的任务

            try
            {
                List<OldCustomerDomain> oldCustomerList = oldCustomerService.queryAllOnlineTask();
                Integer userAreaCode = getUserAreaCode(phone);
                if(userAreaCode == -1){
                    result.setReturnValue(-3);
                    result.setReturnMsg("不是江苏省的联通手机号");
                    return result;
                }
                for(OldCustomerDomain oldCustomerDomain :oldCustomerList){
                    Integer taskId = oldCustomerDomain.getTaskId();
                    String blackUsers = oldCustomerDomain.getBlackUsers();
                    String appointUsers = oldCustomerDomain.getAppointUsers();
                    String areaCode = oldCustomerDomain.getMarketAreaCode();
                    // 用户是否存在于黑名单中
                    if(!StringUtils.isEmpty(blackUsers)){
                        if(oldCustomerService.checkUsers(phone,blackUsers,"black")){
                           continue;
                        }
                    }
                    // 用户是否存在于营销白名单中
                    if(!StringUtils.isEmpty(appointUsers)){
                        if(oldCustomerService.checkUsers(phone,appointUsers,"appoint")){
                            matchedTaskId.add(taskId);
                            continue;
                        }
                    }
                    // 任务区域是否覆盖用户所在区域
                    // 获取号段对应的地区
                    if(StringUtils.isEmpty(areaCode)){
                        continue;
                    }
                    if(userAreaCode != -1 && "99999".equals(areaCode)){
                        matchedTaskId.add(taskId);
                    }else if(userAreaCode != -1 && areaCode.indexOf(String.valueOf(userAreaCode)) > 0){
                        matchedTaskId.add(taskId);
                    }
//                    else{
//                        result.setReturnValue(-3);
//                        result.setReturnMsg("不是江苏省的联通手机号");
//                        return result;
//                    }
                }
            }
            catch (Exception e)
            {
                LOG.error("根据手机号码获取该用户参与的老用户专享优惠活动 error",e);
                result.setReturnValue(-4);
                result.setReturnMsg("系统异常");
                return result;
            }
            //遍历匹配到的任务，组装信息
            for(Integer taskId : matchedTaskId){
                domain = oldCustomerService.queryOldCustomerResult(taskId);
                if(domain != null){
                    preferentialTasks.add(domain);
                }
            }
//            domain.setMarketName("汤总要审的任务哦");
//            domain.setBaseName("南京市秦淮区洪家园沃店营业厅");
//            domain.setMarketContent("优惠活动开始啦？快来快来！");
//            domain.setTelePhone("025-88889999");
//            domain.setOnlineLink("www.baidu.com");
//            domain.setMarketType(3);
//            preferentialTasks.add(domain);
            result.setPreferentialTasks(preferentialTasks);
        }else{
            //不满足格式
            result.setReturnValue(-2);
            result.setReturnMsg("手机号码格式不对");
        }
        return result;
    }

    /**
     * 获取用户手机号码所在区域
     * @param phone
     * @return
     */
    private Integer getUserAreaCode(String phone){
        String destPhone = "86"+ phone.substring(0,7);
        Integer marketAreaCode = -1;
        List<Integer> areaCodeList = oldCustomerService.queryAllAreaCode();
        for(int areaCode : areaCodeList){
            final String phoneSegment = RedisUtil.getInstance().hget(SHOP_AREA_WHITE_PHONE_LIST,String.valueOf(areaCode));
            if(phoneSegment.indexOf(destPhone) > 0 ){
                marketAreaCode = areaCode;
                break;
            }
        }
        return marketAreaCode;
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
}




