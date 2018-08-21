package com.axon.market.web.constants;

import java.util.Map;

/**
 * Created by yuanfei on 2017/1/18.
 */
public class UserConstants
{
    public static final String LOGIN = "login";

    public static final String SESSION_USER = "USER";

    public static final String SESSION_MENU = "MENU";

    public static final String HTML_REQUEST = ".requestHtml";

    //数据控制权限
    public static final String SESSION_D_PERMISSION = "D_PERMISSION";

    //菜单控制权限
    public static final String SESSION_M_PERMISSION = "M_PERMISSION";




    public static Boolean hasPermission(Map<String,Boolean> permissions,String value)
    {
        if(permissions == null || null == permissions.get(value) || permissions.get(value))
        {
            return true;
        }
        return false;
    }
}
