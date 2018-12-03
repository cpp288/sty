package com.cpp.cloud.feign.customized;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 代理 InvocationHandler，使用invoke方法执行具体业务
 *
 * @author chenjian
 * @date 2018-12-03 22:14
 */
public class RequestMappingMethodInvocationHandler implements InvocationHandler {

    private final String serviceName;
    private final BeanFactory beanFactory;

    public RequestMappingMethodInvocationHandler(String serviceName, BeanFactory beanFactory) {
        this.serviceName = serviceName;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 这里只实现 GetMapping
        GetMapping getMapping = AnnotationUtils.getAnnotation(method, GetMapping.class);
        if (getMapping != null) {
            String url = getMappingUrlBuilder(method, args, getMapping);
            RestTemplate restTemplate = beanFactory.getBean(RestTemplate.class);
            return restTemplate.getForObject(url, method.getReturnType());
        }

        return null;
    }

    /**
     * GetMapping 请求 url 组装
     *
     * @param method
     * @param args
     * @param getMapping
     * @return
     */
    private String getMappingUrlBuilder(Method method, Object[] args, GetMapping getMapping) {
        String[] uri = getMapping.value();
        // 拼接url路径：http://${serviceName}/${uri}
        StringBuffer urlBuffer = new StringBuffer("http://").append(serviceName).append("/").append(uri[0]);
        // 获取请求参数

        int parameterCount = method.getParameterCount();
        // 方法参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        // 获取方法参数属性
        Parameter[] parameters = method.getParameters();
        // 方法参数注解（exp：@RequestParam）
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        // 用于组装请求参数（?k1=v1&k2=v2）
        StringBuilder queryStringBuilder = new StringBuilder();
        for (int i = 0; i < parameterCount; i++) {
            Annotation[] paramAnnotations = parameterAnnotations[i];
            // 只接收一个注解
            Assert.isTrue(paramAnnotations.length == 1, "just only one annotation");
            Class<?> paramType = parameterTypes[i];

            Annotation paramAnnotation = paramAnnotations[0];
            // 注解必须是 @RequestParam
            Assert.isTrue(RequestParam.class.equals(paramAnnotation.annotationType()), "just @RequestParam");

            RequestParam requestParam = (RequestParam) paramAnnotation;

            // 获取方法参数名称，即请求参数名称（如果注解 @RequestParam 执行value则取，否则就是方法参数名称）
            String paramName = parameters[i].getName();
            String requestParamName = StringUtils.hasText(requestParam.value()) ? requestParam.value() : paramName;
            // 获取方法参数值，即请求参数值
            String requestParamValue = String.class.equals(paramType) ? (String) args[i] : String.valueOf(args[i]);

            queryStringBuilder.append("&")
                    .append(requestParamName).append("=").append(requestParamValue);
        }

        String queryString = queryStringBuilder.toString();
        if (StringUtils.hasText(queryString)) {
            urlBuffer.append("?").append(queryString);
        }

        return urlBuffer.toString();
    }
}
