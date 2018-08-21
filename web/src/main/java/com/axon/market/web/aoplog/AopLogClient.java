package com.axon.market.web.aoplog;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by changrong.zeng on 2017/10/12.
 */
public class AopLogClient {
    private static Logger logger = Logger.getLogger(AopLogClient.class.getName());

    /**
     * AOP日志记录入口方法
     * @param pjp
     * @throws Throwable
     */
    public void recordAopLog(JoinPoint pjp) throws Throwable {
        logger.info("Ids AopLog recordAopLog begin...");
        try {
            Object[] args = pjp.getArgs();
            if( null != args && args.length > 0 && args[0] instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest)args[0];
                UserDomain operLogDto = this.getUserInfo(request);
//                Map newParaMap = this.getNewParametersFromRequest(request);
//                Map originParaMap = this.getOriginParametersFromRequest(request);
//                this.operLogService.recordOperLog(operLogDto, newParaMap, originParaMap);
            }
        } catch (Exception var8) {
            logger.error("Ids AopLog recordAopLog Exception is ", var8);
        }
    }

    /**
     * 获取登录用户信息
     * @param request
     * @return
     */
    private UserDomain getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDomain userDomain = UserUtils.getLoginUser(session);
        return userDomain;
    }

    /**
     * 获取变动后的新值
     * @param request
     * @return
     */
    private Map<String, String> getNewParametersFromRequest(HttpServletRequest request) {
        HashMap newParaMap = new HashMap();
        Enumeration parameterNames = request.getParameterNames();

        while(parameterNames.hasMoreElements()) {
            String pName = (String)parameterNames.nextElement();
            String pValueStr = "";
            String[] pValues = request.getParameterValues(pName);
            String[] arr$ = pValues;
            int len$ = pValues.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String value = arr$[i$];
                pValueStr = pValueStr + value + ",";
            }

            if(pValueStr.lastIndexOf(",") >= 0) {
                pValueStr = pValueStr.substring(0, pValueStr.lastIndexOf(","));
            }

            if(!pName.startsWith("origin") && !pName.startsWith("method") && !pName.equals("uniqueKey")) {
                newParaMap.put(pName, pValueStr);
            }
        }
        return newParaMap;
    }

    /**
     * 获取变动前的旧值
     * @param request
     * @return
     */
    private Map<String, String> getOriginParametersFromRequest(HttpServletRequest request) {
        HashMap originParaMap = new HashMap();
        Enumeration parameterNames = request.getParameterNames();

        while(parameterNames.hasMoreElements()) {
            String pName = (String)parameterNames.nextElement();
            String pValue = request.getParameter(pName);
            if(pName.startsWith("origin")) {
                originParaMap.put(StringUtils.substringAfter(pName, "origin"), pValue);
            }
        }
        return originParaMap;
    }

    /**
     * 获取登录用户的登录IP
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                InetAddress inet = null;

                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException var5) {
                    var5.printStackTrace();
                }
            }
        }

        if(ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }


}
