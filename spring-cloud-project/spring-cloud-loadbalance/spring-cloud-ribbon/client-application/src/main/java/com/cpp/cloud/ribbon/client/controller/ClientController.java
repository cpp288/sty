package com.cpp.cloud.ribbon.client.controller;

import com.cpp.cloud.ribbon.client.interceptor.LoadBalancedRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    private RestTemplate restTemplate;

    @GetMapping("/invoke/{serviceName}/say")
    public String invokeSay(@PathVariable String serviceName,
                            @RequestParam String message) {
        return restTemplate.getForObject("/" + serviceName + "/say?message=" + message, String.class);
    }

    @Bean
    private ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return new LoadBalancedRequestInterceptor();
    }

    /**
     * 使用restTemplate调用
     *
     * @return
     */
    @Bean
    private RestTemplate restTemplate(@Autowired ClientHttpRequestInterceptor clientHttpRequestInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(clientHttpRequestInterceptor));
        return restTemplate;
    }
}
