package com.axon.market.core.service.iscene;


import com.axon.market.common.bean.HttpResult;
import com.axon.market.common.bean.ServiceResult;
import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.domain.ischeduling.MarketJobDomain;
import com.axon.market.common.domain.ishop.ShopTaskDomain;
import com.axon.market.common.domain.isystem.PositionDataSynDomain;
import com.axon.market.common.util.HttpUtil;
import com.axon.market.dao.mapper.iscene.IPositionLocalSynDataMapper;
import com.axon.market.dao.scheduling.IPositionSynDataMapper;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 位置场景数据同步服务类
 * Created by zengcr on 2016/12/8.
 */
@Component("positionSynDataService")
public class PositionSynDataService
{
    private static final Logger LOG = Logger.getLogger(PositionSynDataService.class.getName());

    @Autowired
    @Qualifier("positionSynDataDao")
    private IPositionSynDataMapper iPositionSynDataMapper;

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    private HttpUtil httpUtil = HttpUtil.getInstance();

//    @Autowired
//    @Qualifier("positionLocalSynDataDao")
//    private IPositionLocalSynDataMapper iPositionSynDataMapper;

    /**
     * 将lines的号码清单插入到目标表tableName
     */
    public int syncPhone(String tableName,List<String> lines){
        return iPositionSynDataMapper.syncPhone(tableName, lines);
    }

    /**
     * 将lines的号码清单插入到目标表tableName
     */
    public int syncPhone2(String tableName,List<Map<String,Object>> lines){
        return iPositionSynDataMapper.syncPhone2(tableName, lines);
    }

    /**
     * 创建目标表
     * @param tableName
     * @return
     */
    public int createPhoneTable(String tableName){
        return iPositionSynDataMapper.createPhoneTable(tableName);
    }

    public String getConfSegmentId(String areaCode){
        if(StringUtils.isNotEmpty(areaCode)){
            return iPositionSynDataMapper.getConfSegmentId(areaCode);
        }
        return null;
    }

    public int insertPTask(PositionDataSynDomain positionDataSynDomain){
        return iPositionSynDataMapper.insertPTask(positionDataSynDomain);
    }

    public int updatePTask(PositionDataSynDomain positionDataSynDomain){
        return iPositionSynDataMapper.updatePTask(positionDataSynDomain);
    }

    /**
     * 位置营销活动删除
     */
    public ServiceResult deleteMarket(MarketJobDomain marketJobDomain) throws Exception{
        ServiceResult result = new ServiceResult();
        Integer pTaskId = marketJobDomain.getLastTaskId();
        int num = -1;
        if(null != pTaskId){
            //删除远程位置任务
            String data = "{\"taskIds\":[" + pTaskId + "]}";
            String res = deleteJson(systemConfigBean.getPositionTaskUrl(),data);
            Gson gson = new Gson();
            HttpResult httpResult = gson.fromJson(res, HttpResult.class);
            if("true".equals(httpResult.getSuccess())){
                //修改任务的配置状态
                num = iPositionSynDataMapper.deleteSynData(pTaskId);
            }else {
                LOG.error(httpResult.getMessage());
                throw new Exception(httpResult.getMessage());
            }
            if(num < 0){
                result = new ServiceResult(-1,"位置营销删除失败！");
            }
        }

        return result;
    }

    /**
     * 位置场景暂停
     * @param marketJobDomain
     * @return
     */
    public ServiceResult pauseMarket(MarketJobDomain marketJobDomain) throws Exception{
        ServiceResult result = new ServiceResult();
        Integer pTaskId = marketJobDomain.getLastTaskId();
        int num = 0;
        if(null != pTaskId){
            //删除远程位置任务
            String data = "{\"taskIds\":[" + pTaskId + "]}";
            String res = deleteJson(systemConfigBean.getPositionTaskUrl(), data);
            //修改任务的配置状态
            num = iPositionSynDataMapper.pauseSynData(pTaskId);
        }
        if(num < 0){
            result = new ServiceResult(-1,"位置营销暂停失败！");
        }
        return result;
    }

    /**
     * 炒店任务删除
     * @param pTaskIdList
     * @return
     */
    public void deleteShopTask(List<Map<String,Object>> pTaskIdList){
        StringBuffer pTaskIdBuffer = new StringBuffer();
        for(int i = 0;i<pTaskIdList.size();i++){
            String pTaskId = String.valueOf(pTaskIdList.get(i).get("lastTaskId"));
            pTaskIdBuffer.append(",").append(pTaskId);
        }
        String pTaskIds = pTaskIdBuffer.substring(1);
        try
        {
            if(null != pTaskIds && !"".equals(pTaskIds)){
                //删除远程位置任务
                String data = "{\"taskIds\":[" + pTaskIds + "]}";
                String res = deleteJson(systemConfigBean.getPositionTaskUrl(),data);
                Gson gson = new Gson();
                HttpResult httpResult = gson.fromJson(res, HttpResult.class);
                if("true".equals(httpResult.getSuccess())){
                    //修改任务的配置状态
                    iPositionSynDataMapper.deletePTask(pTaskIds);
                }else {
                    LOG.error("shop task终止任务删除正在执行的实时处理任务接口调用失败"+httpResult.getMessage());
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("shop task终止任务删除正在执行的实时处理任务失败", e);
        }
    }

    /**
     * 位置场景重启
     * @param marketJobDomain
     * @return
     */
    public ServiceResult resumeMarket(MarketJobDomain marketJobDomain){
        ServiceResult result = new ServiceResult();
        Integer pTaskId = marketJobDomain.getLastTaskId();
        int num = 0;
        if(null != pTaskId){
            //添加远程任务
//            String data = "{\"taskIds\":[" + pTaskId + "]}";
//            httpUtil.sendHttpPostByJson(systemConfigBean.getPositionTaskUrl(), data);
            //修改任务的配置状态,状态修改为6，交给定时处理器处理
            num = iPositionSynDataMapper.resumeSynData(pTaskId);
        }
        if(num < 0){
            result = new ServiceResult(-1,"位置营销启动失败！");
        }
        return result;
    }


    private String deleteData(String url,String data) throws URISyntaxException, UnsupportedEncodingException
    {

        HttpEntityEnclosingRequestBase http = new HttpEntityEnclosingRequestBase()
        {
            @Override
            public String getMethod()
            {
                return "DELETE";
            }
        };

        http.setURI(new URI(url));

        StringEntity stringEntity = new StringEntity(data, "UTF-8");
        stringEntity.setContentType("application/json");
        http.setEntity(stringEntity);

//        List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("taskIds",data));
//        http.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        return HttpUtil.getInstance().sendHttpDelete(http);
    }

    @SuppressWarnings("resource")
    public static String deleteJson(String url, String json) throws IOException {
        /**
         * 没有现成的delete可以带json的，自己实现一个，参考HttpPost的实现
         */
        class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
            public static final String METHOD_NAME = "DELETE";

            @SuppressWarnings("unused")
            public HttpDeleteWithBody() {
            }

            @SuppressWarnings("unused")
            public HttpDeleteWithBody(URI uri) {
                setURI(uri);
            }

            public HttpDeleteWithBody(String uri) {
                setURI(URI.create(uri));
            }

            public String getMethod() {
                return METHOD_NAME;
            }
        }

        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setIntParameter("http.socket.timeout", 15000);
        httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String retVal = "";
        try {
            HttpDeleteWithBody httpdelete = new HttpDeleteWithBody(url);
            StringEntity params = new StringEntity(json, "UTF-8");
            httpdelete.addHeader("content-type", "application/json");
            httpdelete.setEntity(params);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            retVal = new String(httpclient.execute(httpdelete, responseHandler).getBytes(), HTTP.UTF_8);

        } catch (IOException e) {
            throw e;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        LOG.info("================================================retVal"+retVal);
        return retVal;
    }





}
