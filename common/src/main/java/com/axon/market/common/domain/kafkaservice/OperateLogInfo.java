package com.axon.market.common.domain.kafkaservice;

import com.axon.market.common.util.JsonUtil;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Zhuwen on 2017/9/13.
 */
public class OperateLogInfo {
    private String module;
    private String webname;
    private String operatetype;
    private String operatorid;
    private String operatetime;
    private String desc;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getWebname() {
        return webname;
    }

    public void setWebname(String webname) {
        this.webname = webname;
    }

    public String getOperatetype() {
        return operatetype;
    }

    public void setOperatetype(String operatetype) {
        this.operatetype = operatetype;
    }

    public String getOperatorid() {
        return operatorid;
    }

    public void setOperatorid(String operatorid) {
        this.operatorid = operatorid;
    }

    public String getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(String operatetime) {
        this.operatetime = operatetime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public OperateLogInfo(String value){
        if (StringUtils.isEmpty(value))
        {
            throw new IllegalArgumentException("value cannot empty!");
        }
        try{
            Map<String, String> map = JsonUtil.stringToObject(value, Map.class);

            this.module = map.get("module") == null? "":map.get("module");
            this.webname = map.get("webname") == null? "":map.get("webname");
            this.operatetype = map.get("operatetype") == null? "":map.get("operatetype");
            this.operatorid = map.get("operatorid") == null? "":map.get("operatorid");
            this.operatetime = map.get("operatetime") == null? "": map.get("operatetime");
            this.desc = map.get("desc") == null? "": map.get("desc");
        }catch (IOException e){
//            throw new IllegalArgumentException("value cannot be converted!");

        }
    }

    @Override
    public String toString(){
        return "OperateLogInfo [module=" + module + ", webname=" + webname + ", operatetype="
                + operatetype + ", operatorid=" + operatorid + ", operatetime=" + operatetime + ", desc=" + desc + "]";
    }
}
