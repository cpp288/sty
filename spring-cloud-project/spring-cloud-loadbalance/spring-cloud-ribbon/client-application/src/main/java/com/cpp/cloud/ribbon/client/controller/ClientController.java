package com.cpp.cloud.ribbon.client.controller;

import com.cpp.cloud.ribbon.client.annotation.CustomizedLoadBalanced;
import com.cpp.cloud.ribbon.client.interceptor.LoadBalancedRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;

/**
 * client controller
 *
 * @author chenjian
 * @date 2018-11-30 14:10
 */
@RestController
public class ClientController {

    @Autowired
    @CustomizedLoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    @LoadBalanced
    private RestTemplate lbRestTemplate;

    /**
     * 使用自定义 RestTemplate Bean 进行调用
     *
     * @param serviceName
     * @param message
     * @return
     */
    @GetMapping("/invoke/{serviceName}/say")
    public String invokeSay(@PathVariable String serviceName,
                            @RequestParam String message) {
        return restTemplate.getForObject("/" + serviceName + "/say?message=" + message, String.class);
    }

    /**
     * 使用 Ribbon RestTemplate Bean
     *
     * @param serviceName
     * @param message
     * @return
     */
    @GetMapping("/lb/invoke/{serviceName}/say")
    public String lbInvokeSay(@PathVariable String serviceName,
                              @RequestParam String message) {
        return lbRestTemplate.getForObject("http://" + serviceName + "/say?message=" + message, String.class);
    }

    /**
     * Ribbon RestTemplate Bean
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate lbRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 自定义的 RestTemplate Bean，增加手动实现的 ClientHttpRequestInterceptor
     *
     * @return
     */
    @Bean
    @CustomizedLoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return new LoadBalancedRequestInterceptor();
    }

    @Bean
    public Object customizer(@CustomizedLoadBalanced Collection<RestTemplate> restTemplates,
                             @Autowired ClientHttpRequestInterceptor clientHttpRequestInterceptor) {
        restTemplates.forEach(r -> r.setInterceptors(Collections.singletonList(clientHttpRequestInterceptor)));
        return new Object();
    }
}
