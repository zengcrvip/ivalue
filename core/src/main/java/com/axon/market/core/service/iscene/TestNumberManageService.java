package com.axon.market.core.service.iscene;


import com.axon.market.common.bean.InterfaceBean;
import com.axon.market.common.domain.iscene.TestNumberManageDomain;
import com.axon.market.common.bean.Operation;
import com.axon.market.common.bean.Table;

import com.axon.market.common.util.HttpUtil;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.common.constant.iscene.ReturnMessage;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by DELL on 2016/12/13.
 */
@Service("testNumberManageService")
public class TestNumberManageService
{
    Gson gs = new Gson();
    private static final String SUCCESS = "00000";

    private static final Logger LOG = Logger.getLogger(TestNumberManageService.class.getName());

    @Autowired
    @Qualifier("interfaceBean")
    private InterfaceBean interfaceBean;

    /**
     * 封装一个调用接口获取数据的方法
     *
     * @param url
     * @return
     */
    public JsonTestNumber getUrlData(String url) throws IOException,NullPointerException
    {
        String result = HttpUtil.getInstance().sendHttpGet(url);
        JsonTestNumber jtn = gs.fromJson(result, JsonTestNumber.class);
        return jtn;
    }

    /**
     * 内部类用于接受数据
     */
    private class JsonTestNumber
    {
        private String ResultCode;

        private String Message;

        private String Data;

        public String getResultCode()
        {
            return ResultCode;
        }

        public void setResultCode(String resultCode)
        {
            ResultCode = resultCode;
        }

        public String getMessage()
        {
            return Message;
        }

        public void setMessage(String message)
        {
            Message = message;
        }

        public String getData()
        {
            return Data;
        }

        public void setData(String data)
        {
            Data = data;
        }
    }

    /**
     * 获取测试号码数据
     *
     * @param limit
     * @param offset
     * @return
     */
    public Table<TestNumberManageDomain> getMobList(Integer limit, Integer offset)
    {
        try
        {
            JsonTestNumber jtn = getUrlData(interfaceBean.getGetTestNumberUrl());
            String jsonMsg = jtn.getMessage();
            Map<String, String> resMap = JsonUtil.stringToObject(jsonMsg, Map.class);
            List<TestNumberManageDomain> list = new ArrayList<TestNumberManageDomain>();
            if (resMap.size() <= 0)
            {
                return new Table(list, 0);
            }
            else
            {
                TestNumberManageDomain testNumber = null;
                for (Map.Entry<String, String> entry : resMap.entrySet())
                {
                    testNumber = new TestNumberManageDomain();
                    testNumber.setMob(entry.getKey());
                    testNumber.setTaskId(entry.getValue());
                    list.add(testNumber);
                }
                return new Table<TestNumberManageDomain>(list, list.size());
            }
        }
        catch (Exception e)
        {
            LOG.error("查询测试号码列表异常:" + e.toString());
            e.printStackTrace();
            return new Table(new ArrayList<TestNumberManageDomain>(), 0);
        }
    }

    /**
     * 新增测试号码
     *
     * @param mob
     * @param taskId
     * @return
     */
    public Operation addTestNumber(String mob, String taskId)
    {
        try
        {
            JsonTestNumber jtn = getUrlData(interfaceBean.getAddTestNumberUrl() + "?mob=" + mob + "&taskId=" + taskId);
            if (SUCCESS.equals(jtn.getResultCode()))
            {
                return new Operation(true, jtn.getMessage());
            }
            else
            {
                return new Operation(false, jtn.getMessage());
            }
        }
        catch (Exception e)
        {
            LOG.error("新增测试号码异常：" + e.toString());
            e.printStackTrace();
            return new Operation(false, ReturnMessage.ERROR);
        }
    }

    /**
     * 删除测试号码
     *
     * @param mob
     * @return
     */
    public Operation delTestNumber(String mob)
    {
        try
        {
            JsonTestNumber jtn = getUrlData(interfaceBean.getDelTestNumberUrl() + "?mob=" + mob);
            if (SUCCESS.equals(jtn.getResultCode()))
            {
                return new Operation(true, jtn.getMessage());

            }
            else
            {
                return new Operation(false, jtn.getMessage());
            }
        }
        catch (Exception e)
        {
            LOG.error("删除测试号码异常:" + e.toString());
            e.printStackTrace();
            return new Operation(false, ReturnMessage.ERROR);
        }
    }

}
