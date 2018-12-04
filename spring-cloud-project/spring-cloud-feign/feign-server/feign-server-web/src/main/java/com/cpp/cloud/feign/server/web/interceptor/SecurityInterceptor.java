package com.cpp.cloud.feign.server.web.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 *
 * @author chenjian
 * @date 2018-12-04 14:02
 */
@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization)) {
            return true;
        }
        response.sendError(HttpStatus.FORBIDDEN.value(), "无权限");
        return false;
    }
}
