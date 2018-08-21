package com.axon.market.core.service.iscene;

import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.constant.iscene.ReturnMessage;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 2016/12/20.
 */
@Service("commandService")
public class CommandService
{
    Gson gs = new Gson();

    private static final Logger LOG = Logger.getLogger(CommandService.class.getName());

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    /**
     * 封装的调用接口方法
     * @param url
     * @param map
     * @return
     */
    private String sendPost(String url, Map<String, String> map)
    {
        try
        {
            String result = HttpUtil.getInstance().sendHttpPost(url, map);
            return result;
        }
        catch (Exception e)
        {
            LOG.error("控制中心命令接口 异常：" + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发送命令
     * @param command
     * @return
     */
    public Operation sendCommand(String command)
    {
        if (!StringUtils.isEmpty(command))
        {
            Map<String, String> map = new HashMap<String, String>();
            Map<String, String> txtMap = new HashMap<String, String>();
            txtMap.put("command", command);
            map.put("message", gs.toJson(txtMap));
            String message = sendPost(interfaceBean.getCommandUrl(), map);
            if (StringUtils.isEmpty(message))
            {
                return new Operation(false, ReturnMessage.API_ERROR);
            }
            return new Operation(true, message);
        }
        else
        {
            return new Operation(false, ReturnMessage.EMPTY_CONTENT);
        }
    }
}
