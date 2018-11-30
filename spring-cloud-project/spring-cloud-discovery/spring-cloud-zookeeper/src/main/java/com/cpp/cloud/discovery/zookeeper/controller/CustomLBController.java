package com.cpp.cloud.discovery.zookeeper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义 loadbalance
 *
 * @author chenjian
 * @date 2018-11-30 10:22
 */
@RestController
@RequestMapping(value = "lb")
public class CustomLBController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    // 定义一个存放所有实例的地址 key：实例名称 value：实例地址集合
    private volatile Map<String, Set<String>> targetUrlsCache = new HashMap<>();

    /**
     * 从注册中心获取所有实例的地址信息，并放入 targetUrlsCache 中
     */
    @Scheduled(fixedRate = 10 * 1000) // 10 秒钟更新一次缓存
    public void updateTargetUrlsCache() {
//        System.out.println("开始更新实例地址....");
        // 获取当前应用的机器列表
        Map<String, Set<String>> newTargetUrlsCache = new HashMap<>();
        discoveryClient.getServices().forEach(serviceName -> {
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
            Set<String> newTargetUrls = serviceInstances
                    .stream()
                    .map(s ->
                            s.isSecure() ?
                                    "https://" + s.getHost() + ":" + s.getPort() :
                                    "http://" + s.getHost() + ":" + s.getPort()
                    ).collect(Collectors.toSet());
            newTargetUrlsCache.put(serviceName, newTargetUrls);
        });

        this.targetUrlsCache = newTargetUrlsCache;
//        System.out.println("更新实例地址结束：\n" + newTargetUrlsCache);
    }

    /**
     * 调用这个接口，通过简单的随机算法，去调用注册中心中的实例地址，达到一种简单 loadbalance 的效果
     *
     * @param serviceName
     * @param message
     * @return
     */
    @GetMapping("/invoke/{serviceName}/say")
    public String invokeSay(@PathVariable String serviceName,
                            @RequestParam String message) {
        // 获取该实例下的url地址
        List<String> targetUrls = new ArrayList<>(targetUrlsCache.get(serviceName));
        int size = targetUrls.size();
        // 随机获取一个地址
        int index = new Random().nextInt(size);
        String targetUrl = targetUrls.get(index);

        return restTemplate.getForObject(targetUrl + "/lb/say?message=" + message, String.class);
    }

    @GetMapping("/say")
    public String say(@RequestParam String message) {
        System.out.printf("接收到消息：message -> %s\n", message);
        return "Hello, " + message;
    }

    /**
     * 使用restTemplate调用
     *
     * @return
     */
    @Bean
    private RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
