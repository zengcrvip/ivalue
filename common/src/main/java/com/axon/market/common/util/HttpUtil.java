package com.axon.market.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.WebRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by chenyu on 2016/8/2.
 */
public class HttpUtil
{
    private static final Logger LOG = Logger.getLogger(HttpUtil.class.getName());

    private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(15000)
            .setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000)
            .build();

    private static final HttpUtil instance = new HttpUtil();

    public static HttpUtil getInstance()
    {
        return instance;
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public String sendHttpPost(String httpUrl)
    {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数(格式:key1=value1&key2=value2)
     */
    public String sendHttpPost(String httpUrl, String params)
    {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        //设置参数
        StringEntity stringEntity = new StringEntity(params, "UTF-8");
        stringEntity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(stringEntity);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps)
    {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet())
        {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        }
        catch (Exception e)
        {
            LOG.error("Create UrlEncodedFormEntity Object error. ", e);
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param jsonStr 参数(格式:key1=value1&key2=value2)
     */
    public String sendHttpPostByJson(String httpUrl, String jsonStr)
    {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        //设置参数
        StringEntity stringEntity = new StringEntity(jsonStr, "UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        return sendHttpPost(httpPost);
    }


    /**
     * 发送 post请求（带文件）
     *
     * @param httpUrl   地址
     * @param maps      参数
     * @param fileLists 附件
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists)
    {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
        for (String key : maps.keySet())
        {
            meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
        }
        for (File file : fileLists)
        {
            FileBody fileBody = new FileBody(file);
            meBuilder.addPart("files", fileBody);
        }
        HttpEntity reqEntity = meBuilder.build();
        httpPost.setEntity(reqEntity);
        return sendHttpPost(httpPost);
    }

    public String sendHttpGetByHeader(String url, Map<String, String> headers)
    {
        // 创建httpDelete
        HttpGet httpGet = new HttpGet(url);
        // json 处理
        for (Map.Entry<String, String> entry : headers.entrySet())
        {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }
        return sendHttpGet(httpGet);
    }

    public String sendHttpPostByHeader(String url, Map<String, String> headers)
    {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(url);
        // json 处理
        for (Map.Entry<String, String> entry : headers.entrySet())
        {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private String sendHttpPost(HttpPost httpPost)
    {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try
        {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        }
        catch (Exception e)
        {
            LOG.error("Send Http Post Request error. ", e);
        }
        finally
        {
            try
            {
                // 关闭连接,释放资源
                if (response != null)
                {
                    response.close();
                }
                if (httpClient != null)
                {
                    httpClient.close();
                }
            }
            catch (IOException e)
            {
                LOG.error("Http Post Close Resource error. ", e);
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public String sendHttpGet(String httpUrl)
    {
        // 创建get请求
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpGet(httpGet);
    }

    /**
     * 发送 get请求Https
     *
     * @param httpUrl
     */
    public String sendHttpsGet(String httpUrl)
    {
        // 创建get请求
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpsGet(httpGet);
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @return
     */
    private String sendHttpGet(HttpGet httpGet)
    {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try
        {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        }
        catch (Exception e)
        {
            LOG.error("Send Http Get Request error. ", e);
        }
        finally
        {
            try
            {
                // 关闭连接,释放资源
                if (response != null)
                {
                    response.close();
                }
                if (httpClient != null)
                {
                    httpClient.close();
                }
            }
            catch (IOException e)
            {
                LOG.error("Http Get Close Resource error. ");
            }
        }
        return responseContent;
    }

    /**
     * 发送Get请求Https
     *
     * @param httpGet
     * @return
     */
    private String sendHttpsGet(HttpGet httpGet)
    {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try
        {
            // 创建默认的httpClient实例.
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        }
        catch (Exception e)
        {
            LOG.error("Send Https Get Request error. ", e);
        }
        finally
        {
            try
            {
                // 关闭连接,释放资源
                if (response != null)
                {
                    response.close();
                }
                if (httpClient != null)
                {
                    httpClient.close();
                }
            }
            catch (IOException e)
            {
                LOG.error("Https Get Close Resource error. ", e);
            }
        }
        return responseContent;
    }

//    /**
//     * 发送 delete请求
//     *
//     * @param httpUrl 地址
//     * @param jsonStr  参数(格式:key1=value1&key2=value2)
//     */
//    public String sendHttpDeleteByJson(String httpUrl, String jsonStr)
//    {
//        // 创建httpDelete
//        HttpDelete httpDelete = new HttpDelete(httpUrl);
//        // json 处理
//
//        httpDelete.setHeader("Content-Type", "application/json; charset=UTF-8");
//        httpDelete.setHeader("X-Requested-With", "XMLHttpRequest");
//        return sendHttpDelete(httpDelete);
//    }


    /**
     * 发送httpDelete请求
     *
     * @param httpDelete
     * @return
     */
    public String sendHttpDelete(HttpEntityEnclosingRequestBase httpDelete)
    {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try
        {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpDelete.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpDelete);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOG.error("Send Http Delete Request error. ", e);
        }
        finally
        {
            try
            {
                // 关闭连接,释放资源
                if (response != null)
                {
                    response.close();
                }
                if (httpClient != null)
                {
                    httpClient.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                LOG.error("Http Delete Close Resource error. ");
            }
        }
        return responseContent;
    }

}
