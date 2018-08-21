package com.axon.market.web.filter;

import com.axon.market.web.constants.UserConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 页面请求拦截 filter
 * Created by yuanfei on 2017/01/22.
 */
public class AuthPageFilter implements Filter {
    private FilterConfig config;

    /**
     * 过滤器初始化
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();

        int startIndex = uri.lastIndexOf("/") == -1 ? 0 : uri.lastIndexOf("/") + 1;
        //只处理菜单请求(包含.requestHtml)
        if (uri.contains(UserConstants.HTML_REQUEST)) {
            String urlPermission = uri.substring(startIndex, uri.indexOf(".requestHtml"));
            List<String> menuUrls = (List<String>) session.getAttribute(UserConstants.SESSION_M_PERMISSION);
            if (!menuUrls.contains(urlPermission) && !"personalInfo".equals(urlPermission) && !"cityAllotStall".equals(urlPermission) && !"allotStall".equals(urlPermission) && !"parnterList".equals(urlPermission)) {
                response.sendRedirect("pages/icommon/error");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     *
     */
    @Override
    public void destroy() {
        config = null;
    }

}
