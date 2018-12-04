package com.cpp.base.difference.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * filter
 *
 * @author chenjian
 * @date 2018-12-04 14:44
 */
@Component
@WebFilter(filterName = "urlFilter", urlPatterns = "/test")// 配置拦截路径
public class UrlFilter implements Filter {

    /**
     * filter初始化的时候调用，即web容器启动时调用
     * web容器启动时根据web.xml文件，依次加载ServletContext -> listener -> filter -> servlet
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("UrlFilter init...");
    }

    /**
     * filter执行功能，根据参数来看，可以对request,response和chain（是否放行）进行操作
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("UrlFilter doFilter before...");
        chain.doFilter(request, response);
        System.out.println("UrlFilter doFilter after...");
    }

    /**
     * filter在服务器正常关闭(比如System.exit(0))等情况下会调用
     */
    @Override
    public void destroy() {
        System.out.println("UrlFilter destroy...");
    }
}
