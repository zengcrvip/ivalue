package com.axon.market.web.filter;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.web.constants.UserConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户登录拦截 filter
 * Created by chenyu on 2016/4/26.
 */
public class LoginFilter implements Filter
{
    private FilterConfig config;

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
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();

        UserDomain user = (UserDomain) session.getAttribute(UserConstants.SESSION_USER);
        ((HttpServletResponse) servletResponse).setHeader("Pragma", "No-cache");
        ((HttpServletResponse) servletResponse).setHeader("Cache-Control", "no-cache");
        //拦截非登录
        if (uri.toLowerCase().contains(UserConstants.LOGIN))
        {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else
        {
            if (null == user)
            {
                if (request.getHeader("x-requested-with") != null
                        && "XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with")))
                {
                    response.setStatus(911);
                    response.setHeader("sessionStatus", "timeout");
                    response.addHeader("redirectUrl", config.getInitParameter("redirectUrl"));
                    return;
                }
                response.sendRedirect(config.getInitParameter("redirectUrl"));
                return;
            }
            else
            {
                filterChain.doFilter(request, response);
            }
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
