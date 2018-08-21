package com.axon.market.common.util;

import com.axon.market.common.domain.isystem.UserDomain;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;

/**
 * Created by yangyang on 2016/3/2.
 */
public class UserUtils
{
    public static final String SESSION_USER = "USER";

    public static UserDomain getLoginUser(HttpSession session)
    {
        return (UserDomain) session.getAttribute(SESSION_USER);
    }

    public static String getVisitorIpAdar(HttpServletRequest request)
    {
        String ipString = request.getHeader("x-forwarded-for");

        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString))
        {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString))
        {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString))
        {
            ipString = request.getRemoteAddr();
            //这里主要是获取本机的ip
            if (ipString.equals("127.0.0.1") || ipString.endsWith("0:0:0:0:0:0:1"))
            {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try
                {
                    inet = InetAddress.getLocalHost();
                    ipString = inet.getHostAddress();
                }
                catch (Exception e)
                {
                }
            }
        }
        if (!StringUtils.isBlank(ipString)) //计算失败时候，为unknown
        {
            final String[] arr = ipString.split(",");

            for (final String str : arr)
            {
                if (!"unknown".equalsIgnoreCase(str))
                {
                    ipString = str;
                    break;
                }
            }
            String rexp = "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                    "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                    "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                    "([1-9]|[1-9][0-9]|1\\d\\d|2[0-4]\\d|25[0-5])";

            if(ipString.matches(rexp))
            {
                return ipString;
            }
        }

        return "unknown";
    }

}
