package com.axon.market.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.domain.inewScene.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyang on 2016/3/10.
 */
public class JsonUtil
{
    private static ObjectMapper mapper  = new ObjectMapper();

    public static String objectToString(Object object) throws JsonProcessingException
    {
        return mapper.writeValueAsString(object);
    }

    public static <T> T stringToObject(String value, TypeReference<T> valueTypeRef) throws IOException
    {
        return mapper.readValue(value, valueTypeRef);
    }

    public static  <T> T stringToObject(String content, Class<T> beanClass) throws IOException
    {
        return mapper.readValue(content, beanClass);
    }

    public static void main(String[] args){
        ResultVo  result = new ResultVo();
        result.setResultCode("haha");
        result.setResultMsg("bibibib");

        try{
        System.out.println(objectToString(result));
    }catch(Exception e){
        }

        NewSceneAppDomain test = new NewSceneAppDomain();
        NewSceneAppDomain test1 = new NewSceneAppDomain();
        test.setAppid("123");
        test.setApptype("type");
        test1.setAppid("321");
        test1.setApptype("type2");
        List<NewSceneAppDomain> list1 = new ArrayList<>();
        list1.add(test);
        list1.add(test1);
        try{
            System.out.println(objectToString(test));
            System.out.println(objectToString(list1));
        }catch(Exception e){
        }

        System.out.println("in1");
        String testDomain = "[{\"apptype\":\"1\",\"appid\",\"500\"}]";

        try {
            List<Map<String,String>> list = stringToObject(testDomain, List.class);

            System.out.println("in2");
            for (Map<String,String> dom:list)
            {
                System.out.println("apptype value is:"+dom.get("apptype"));
                System.out.println("apptype id is:"+dom.get("appid"));
            }
        }catch (Exception e){
            System.out.println("error");
        }





    }



}
