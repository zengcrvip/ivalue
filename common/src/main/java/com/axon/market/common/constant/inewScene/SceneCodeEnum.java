package com.axon.market.common.constant.inewScene;

/**
 * Created by zhuwen on 2017/7/13.
 */
public enum SceneCodeEnum {
    APP_SCENE("R002","apptype","appid"),
    FLOW_SCENE("L001","unit_time", "increase_data");

    private String value;
    private String param1;
    private String param2;

    private SceneCodeEnum(String value, String param1, String param2)
    {
        this.value = value;
        this.param1 = param1;
        this.param2 = param2;
    }

    public String getValue()
    {
        return value;
    }

    public String getParam1()
    {
        return param1;
    }

    public String getParam2()
    {
        return param2;
    }
}
