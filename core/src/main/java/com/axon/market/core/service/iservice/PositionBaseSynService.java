package com.axon.market.core.service.iservice;

import com.axon.market.common.domain.iscene.PositionBaseDomain;
import com.axon.market.common.domain.iservice.BaseInfo;
import com.axon.market.common.domain.iservice.PositionBaseReturnDo;
import com.axon.market.common.domain.iservice.PositionBaseSyncDomain;
import com.axon.market.common.util.TimeUtil;
import com.axon.market.dao.mapper.iscene.IPositionBaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 位置基站信息接口同步service
 * Created by zengcr on 2017/1/21.
 */
@Component("positionBaseSynService")
public class PositionBaseSynService
{

    @Qualifier("positionBaseDao")
    @Autowired
    private IPositionBaseMapper iPositionBaseMapper;

    /**
     * 对基站对象进行入库处理
     * @param positionBaseSyncDomain
     * @return
     */
    public PositionBaseReturnDo executePositionBase(PositionBaseSyncDomain positionBaseSyncDomain){
        PositionBaseReturnDo positionBaseReturnDo = new PositionBaseReturnDo();
        int flag = 0;
        //设置回参
        positionBaseReturnDo.setStreamingNo(positionBaseSyncDomain.getStreamingNo());
        positionBaseReturnDo.setTimeStamp(TimeUtil.formatDateToYMDHMS(new Date()));

        PositionBaseDomain positionBaseDomain = positionBaseSyncDomain.getBaseInfo();
        Integer operate =  positionBaseDomain.getOperate();
        //新增操作
        if(0 == operate){
            positionBaseDomain.setLocationType(getBaseType().get(positionBaseDomain.getLocationTypeId()));
            positionBaseDomain.setCityCode(getBaseAreaCode().get(positionBaseDomain.getCityName()));
            positionBaseDomain.setStatus(1);
            flag = iPositionBaseMapper.createPositionBase(positionBaseDomain);
            BaseInfo baseInfo = new BaseInfo();
            baseInfo.setOperate(operate);
            baseInfo.setBaseId(positionBaseDomain.getBaseId());
            baseInfo.setBaseName(positionBaseDomain.getBaseName());
            positionBaseReturnDo.setBaseInfo(baseInfo);
        }
        //删除操作
        if(1 == operate){
            Integer baseId = positionBaseDomain.getBaseId();
            flag = iPositionBaseMapper.deletePositionBaseById(baseId);
        }
        //异常错误
        if (flag != 1)
        {
            positionBaseReturnDo.setResultCode("99999");
            positionBaseReturnDo.setResultDesc("未知错误");
        }
        return positionBaseReturnDo;
    }

    /**
     * 获取地区编码
     * @return
     */
    private Map<String,Integer> getBaseAreaCode(){
        Map<String,Integer> areaMap = new HashMap<String,Integer>();
        areaMap.put("南京",25);
        areaMap.put("南通",513);
        areaMap.put("宿迁",527);
        areaMap.put("常州",519);
        areaMap.put("徐州", 516);
        areaMap.put("扬州", 514);
        areaMap.put("无锡",510);
        areaMap.put("泰州",523);
        areaMap.put("淮安",517);
        areaMap.put("盐城",515);
        areaMap.put("苏州",512);
        areaMap.put("连云港",518);
        areaMap.put("镇江",511);
        return areaMap;
    }

    /**
     * 获取基站类型
     * @return
     */
    private Map<Integer,String> getBaseType(){
        Map<Integer,String> baseTypeMap = new HashMap<Integer,String>();
        baseTypeMap.put(1,"自营厅");
        baseTypeMap.put(2,"合作厅");
        baseTypeMap.put(3,"临促点");
        baseTypeMap.put(4,"第三方");
        baseTypeMap.put(5, "公共设施");
        baseTypeMap.put(6, "其他");
        return baseTypeMap;
    }
}
