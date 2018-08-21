package com.axon.market.service.controller;

import com.axon.market.common.domain.iscene.PositionBaseDomain;
import com.axon.market.common.domain.iservice.PositionBaseReturnDo;
import com.axon.market.common.domain.iservice.PositionBaseSyncDomain;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.common.util.ValidateUtil;
import com.axon.market.core.service.iservice.PositionBaseSynService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 位置场景基站站点数据同步服务接口
 * Created by zengcr on 2017/1/20.
 */
@Controller("positionBaseServer")
public class PositionBaseServer
{

    @Qualifier("positionBaseSynService")
    @Autowired
    private PositionBaseSynService positionBaseSynService;
    //客户端请求IP
    private static final String CLIENT_IP = "127.0.0.1";
    //合作方标识
    private static final  String PARTNERID = "bigData";
    //基站类型IDs
    private static final  String BASETYPES = "1,2,3,4,5,6";
    //基站地市
    private static final  String AREAS = "南京,南通,宿迁,常州,徐州,扬州,无锡,泰州,淮安,盐城,苏州,连云港,镇江";
    //新增操作
    private static final  Integer ADD_OPERATE = 0;
    //修改操作
    private static final  Integer DEL_OPERATE = 1;

    /**
     * 基站配置信息同步服务接口
     * @param params 入参对象
     * @param request
     * @return
     */
    @RequestMapping(value = "syncBaseInfoService", method = RequestMethod.POST)
    @ResponseBody
    public String syncBaseInfoService(@RequestBody String params,HttpServletRequest request)
    {
        PositionBaseReturnDo positionBaseReturnDo = new PositionBaseReturnDo();
        Gson gson = new Gson();
        PositionBaseSyncDomain positionBaseSyncDomain = gson.fromJson(params, PositionBaseSyncDomain.class);
        positionBaseReturnDo.setStreamingNo(positionBaseSyncDomain.getStreamingNo() == null ? "" : positionBaseSyncDomain.getStreamingNo());
        positionBaseReturnDo.setTimeStamp(TimeUtil.formatDateToYMDHMS(new Date()));

        //客户端IP鉴权校验
        if(!validateIp(request)){
            positionBaseReturnDo.setResultCode("90006");
            positionBaseReturnDo.setResultDesc("源IP鉴权失败");
            return gson.toJson(positionBaseReturnDo);
        }

        //合作方权限校验
        if(!PARTNERID.equals(positionBaseSyncDomain.getPartnerId())){
            positionBaseReturnDo.setResultCode("90004");
            positionBaseReturnDo.setResultDesc("服务请求没权限");
            return gson.toJson(positionBaseReturnDo);
        }

        //参数正确性校验
        if(!validateObject(positionBaseSyncDomain,positionBaseReturnDo)){
            return gson.toJson(positionBaseReturnDo);
        }

        //解析参数，入库处理
        positionBaseReturnDo = positionBaseSynService.executePositionBase(positionBaseSyncDomain);
        return gson.toJson(positionBaseReturnDo);
    }

    //IP鉴权校验
    private boolean validateIp(HttpServletRequest request){
        boolean flag = true;
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        //不匹配，返回false
        if(!CLIENT_IP.equals(ip)){
             flag = false;
        }
        return flag;
    }

    /**
     * 参数准确性校验
     * @param positionBaseSyncDomain 源入参对象
     * @param positionBaseReturnDo 返回对象
     * @return
     */
    private boolean validateObject(PositionBaseSyncDomain positionBaseSyncDomain,PositionBaseReturnDo positionBaseReturnDo)
    {
        if(ValidateUtil.isStrEmpty(positionBaseSyncDomain.getStreamingNo()))
        {
            positionBaseReturnDo.setResultCode("90007");
            positionBaseReturnDo.setResultDesc("参数streamingNo取值无效");
            return false;
        }
        if(ValidateUtil.isStrEmpty(positionBaseSyncDomain.getTimeStamp())){
            positionBaseReturnDo.setResultCode("90007");
            positionBaseReturnDo.setResultDesc("参数timeStamp取值无效");
            return false;
        }
        if(!ValidateUtil.isEmpty(positionBaseSyncDomain.getBaseInfo())){
            positionBaseReturnDo.setResultCode("90007");
            positionBaseReturnDo.setResultDesc("参数baseInfo取值无效");
            return false;
        }else{
            PositionBaseDomain baseInfo = positionBaseSyncDomain.getBaseInfo();
            if(ValidateUtil.isIntegerEmpty(baseInfo.getOperate()) ){
                positionBaseReturnDo.setResultCode("90007");
                positionBaseReturnDo.setResultDesc("参数operate取值无效");
                return false;
            }
            if(!(ADD_OPERATE == baseInfo.getOperate() || DEL_OPERATE == baseInfo.getOperate())){
                positionBaseReturnDo.setResultCode("90007");
                positionBaseReturnDo.setResultDesc("参数operate取值无效");
                return false;
            }

            //新增位置基站
            if(0 == baseInfo.getOperate() ){
                if(ValidateUtil.isStrEmpty(baseInfo.getBaseName())){
                    positionBaseReturnDo.setResultCode("90007");
                    positionBaseReturnDo.setResultDesc("参数baseName取值无效");
                    return false;
                }
                if(ValidateUtil.isIntegerEmpty(baseInfo.getLocationTypeId())){
                    positionBaseReturnDo.setResultCode("90007");
                    positionBaseReturnDo.setResultDesc("参数locationTypeId取值无效");
                    return false;
                }else{
                    if (BASETYPES.indexOf(Integer.toString(baseInfo.getLocationTypeId())) == -1)
                    {
                        positionBaseReturnDo.setResultCode("90007");
                        positionBaseReturnDo.setResultDesc("参数locationTypeId取值无效");
                        return false;
                    }
                }
                if(ValidateUtil.isStrEmpty(baseInfo.getCityName())){
                    positionBaseReturnDo.setResultCode("90007");
                    positionBaseReturnDo.setResultDesc("参数cityName取值无效");
                    return false;
                }else{
                    if (AREAS.indexOf(baseInfo.getCityName()) == -1)
                    {
                        positionBaseReturnDo.setResultCode("90007");
                        positionBaseReturnDo.setResultDesc("参数cityName取值无效");
                        return false;
                    }
                }
                if(ValidateUtil.isStrEmpty(baseInfo.getLng())){
                    positionBaseReturnDo.setResultCode("90007");
                    positionBaseReturnDo.setResultDesc("参数lng取值无效");
                    return false;
                }
                if(ValidateUtil.isStrEmpty(baseInfo.getLat())){
                    positionBaseReturnDo.setResultCode("90007");
                    positionBaseReturnDo.setResultDesc("参数lat取值无效");
                    return false;
                }
                if(ValidateUtil.isStrEmpty(baseInfo.getRadius())){
                    positionBaseReturnDo.setResultCode("90007");
                    positionBaseReturnDo.setResultDesc("参数radius取值无效");
                    return false;
                }
                if(ValidateUtil.isStrEmpty(baseInfo.getAddress())){
                    positionBaseReturnDo.setResultCode("90007");
                    positionBaseReturnDo.setResultDesc("参数address取值无效");
                    return false;
                }
                if(ValidateUtil.isIntegerEmpty(baseInfo.getCompanyCode())){
                    positionBaseReturnDo.setResultCode("90007");
                    positionBaseReturnDo.setResultDesc("参数companyCode取值无效");
                    return false;
                }

            }
            //删除位置基站
            if(1 == baseInfo.getOperate() ){
                if(ValidateUtil.isIntegerEmpty(baseInfo.getBaseId())){
                    positionBaseReturnDo.setResultCode("90007");
                    positionBaseReturnDo.setResultDesc("参数baseId取值无效");
                    return false;
                }
            }
        }
        return true;
    }



}
