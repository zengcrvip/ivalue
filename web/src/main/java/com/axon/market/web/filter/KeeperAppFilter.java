package com.axon.market.web.filter;

import com.axon.market.common.bean.ResultVo;
import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.common.util.JsonUtil;
import com.axon.market.core.service.isystem.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 用户登录拦截 filter
 * Created by chenyu on 2016/4/26.
 */
public class KeeperAppFilter implements Filter
{
    private FilterConfig config;

    private static UserService userService = UserService.getInstance();

    /**
     * 过滤器初始化
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        config = filterConfig;
    }

    /**
     * 过滤器执行操作
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String uri = request.getRequestURI();
        if(uri.indexOf("loginForApp") > 0 || uri.indexOf("validationCodeService") > 0)
        {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        ResultVo result = new ResultVo();
        ServletRequest requestWrapper = new MAPIHttpServletRequestWrapper(request);
        //获取参数
        String body = HttpHelper.getBodyString(requestWrapper);
        if (StringUtils.isEmpty(body))
        {
            filterChain.doFilter(requestWrapper, response);
            return;
        }

        String token = (String)JsonUtil.stringToObject(body, Map.class).get("token");
        if (StringUtils.isEmpty(token))
        {
            result.setResultCode("2222");
            result.setResultMsg("缺少token信息");
            response.getWriter().write(JsonUtil.objectToString(result));
            return;
        }

        UserDomain user = userService.queryUserByToken(token);
        if (null == user)
        {
            result.setResultCode("9999");
            result.setResultMsg("登录已失效，请重新登录");
            response.getWriter().write(JsonUtil.objectToString(result));
        }
        else
        {
            filterChain.doFilter(requestWrapper, response);
        }
    }

    /**
     *
     */
    @Override
    public void destroy()
    {
        config = null;
    }

}
