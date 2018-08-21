package com.axon.market.core.service.icommon;

import com.axon.market.common.bean.PhonePlusConfigBean;
import com.axon.market.common.bean.PhonePlusResult;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.util.JsonUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Duzm on 2017/8/9.
 */
@Component("phonePlusService")
public class PhonePlusService {

    private static final Logger LOG = Logger.getLogger(PhonePlusService.class);

    private static final HttpUtil HTTP_UTIL = HttpUtil.getInstance();

    private static final PhonePlusConfigBean CONFIG_BEAN = PhonePlusConfigBean.getInstance();

    public Map<String,String> initCallToPhonePlus(String callingNumber, String calledNumber, String displayNumber) throws Exception
    {
        Map<String, String> maps = new HashMap<>();
        maps.put("agent_id", CONFIG_BEAN.getAgentId());
        maps.put("staff_id",callingNumber);
        maps.put("name",callingNumber);
        maps.put("calling_number",callingNumber);
        maps.put("called_number", calledNumber);
        maps.put("display_number", displayNumber);

        LOG.info("initCall send message: " + getSendMessage(maps));

        String result = HTTP_UTIL.sendHttpPost(CONFIG_BEAN.getInitialcallUrl(), maps);
        LOG.info("initCall receive message: " + result);

         return  JsonUtil.stringToObject(result, Map.class);
    }

    public Map<String,String> submitResultToPhonePlus(String serialId, String result) throws Exception
    {
        HttpUtil httpUtil = HttpUtil.getInstance();
        Map<String, String> maps = new HashMap<>();
        maps.put("agent_id",CONFIG_BEAN.getAgentId());
        maps.put("serial_id",serialId);
        maps.put("result", result);

        LOG.debug("submitResult send message: " + getSendMessage(maps));
        //{"result_desc":"调用成功","result_code":"0"}
        String submitResult = HTTP_UTIL.sendHttpPost(CONFIG_BEAN.getSubmitResultUrl(), maps);
        LOG.debug("submitResult receive message: " + submitResult);
        return  JsonUtil.stringToObject(submitResult, Map.class);
    }

    public PhonePlusResult getCallHistoryFromPhonePlus(String serialId) throws Exception{
        HttpUtil httpUtil = HttpUtil.getInstance();
        Map<String, String> maps = new HashMap<>();
        maps.put("agent_id",CONFIG_BEAN.getAgentId());
        maps.put("serial_id", serialId);

        LOG.debug("CallHistory send message: " + getSendMessage(maps));
        //{"start_time":null,"calling_number":"15950585292","result_desc":"查询成功","end_time":null,"result_code":"0","display_number":"112086","called_number":"15950585292","call_duration":0,"call_result":"沟通成功"}
        String result = HTTP_UTIL.sendHttpPost(CONFIG_BEAN.getGetCallHistoryUrl(), maps);
        LOG.debug("CallHistory receive message: " + result);
        return  JsonUtil.stringToObject(result, PhonePlusResult.class);
    }

    public PhonePlusResult getCallRecordFromPhonePlus(String serialId) throws Exception{
        HttpUtil httpUtil = HttpUtil.getInstance();
        Map<String, String> maps = new HashMap<>();
        maps.put("agent_id",CONFIG_BEAN.getAgentId());
        maps.put("serial_id", serialId);

        LOG.debug("CallRecord send message: " + getSendMessage(maps));
        //{"result_desc":"数据不存在","result_code":"40008"}
        String result = HTTP_UTIL.sendHttpPost(CONFIG_BEAN.getGetCallRecordUrl(), maps);
        LOG.debug("CallRecord receive message: " + result);
        return  JsonUtil.stringToObject(result, PhonePlusResult.class);
    }

    private String getSendMessage(Map<String, String> param) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : param.keySet()){
            stringBuilder.append(key).append("=").append(param.get(key)).append(";");
        }
        return stringBuilder.toString();
    }
}
