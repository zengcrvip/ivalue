package com.axon.market.web.filter;

import com.axon.market.common.domain.isystem.UserDomain;
import com.axon.market.web.constants.UserConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 数据权限拦截 filter
 * Created by yuanfei on 2017/01/22.
 */
public class AuthDataFilter implements Filter
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

        //经过一级过滤后，数据权限类型只处理用户存在的情况，其余情况直接下发
        UserDomain user = (UserDomain) session.getAttribute(UserConstants.SESSION_USER);
        if (null != user)
        {
            int startIndex = uri.lastIndexOf("/") == -1 ? 0 : uri.lastIndexOf("/") + 1;
            Map<String, Boolean> dataPermissions = (Map<String, Boolean>) session.getAttribute(UserConstants.SESSION_D_PERMISSION);
            String urlPermission = uri.substring(startIndex, uri.indexOf(".view"));
            if (!UserConstants.hasPermission(dataPermissions, urlPermission + ".view_u"))
            {
                response.setStatus(755);
                response.setHeader("sessionStatus", "no-permission");
                return;
            }
        }

        filterChain.doFilter(request, response);
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
