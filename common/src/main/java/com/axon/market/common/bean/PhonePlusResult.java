package com.axon.market.common.bean;

import com.axon.market.common.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Created by Duzm on 2017/8/12.
 */
public class PhonePlusResult {
    //String s = "{\"result_desc\":\"调用成功\",\"result_code\":\"0\"}";
    
    public static final String SUCCESS = "0";
    
    private String result_desc;
    
    private String result_code;
    
    private String serial_id;
    
    private String start_time;

    private String calling_number;
    
    private String end_time;
    
    private String display_number;
    
    private String called_number;
    
    private String call_duration;
    
    private String call_result;
    
    public String getComments() {
        try {
            return JsonUtil.objectToString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public String getResult_desc() {
        return result_desc;
    }

    public String getResult_code() {
        return result_code;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getCalling_number() {
        return calling_number;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getDisplay_number() {
        return display_number;
    }

    public String getCalled_number() {
        return called_number;
    }

    public String getCall_duration() {
        return call_duration;
    }

    public String getCall_result() {
        return call_result;
    }

    public void setResult_desc(String result_desc) {
        this.result_desc = result_desc;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setCalling_number(String calling_number) {
        this.calling_number = calling_number;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setDisplay_number(String display_number) {
        this.display_number = display_number;
    }

    public void setCalled_number(String called_number) {
        this.called_number = called_number;
    }

    public void setCall_duration(String call_duration) {
        this.call_duration = call_duration;
    }

    public void setCall_result(String call_result) {
        this.call_result = call_result;
    }
}
