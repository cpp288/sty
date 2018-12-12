package com.cpp.cloud.discovery.nacos.consumer.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("nacos-service-provider")
public interface ProviderApi {

    @GetMapping("/echo/{message}")
    String echo(@PathVariable String message);
}
