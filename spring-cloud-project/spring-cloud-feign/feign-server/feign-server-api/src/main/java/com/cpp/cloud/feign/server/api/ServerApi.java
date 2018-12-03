package com.cpp.cloud.feign.server.api;

import com.cpp.cloud.feign.server.request.SaveTestRequest;
import com.cpp.cloud.feign.server.response.SaveTestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("${spring-cloud-feign-server.name}")
public interface ServerApi {

    /**
     * 服务端接口
     *
     * @param message
     * @return
     */
    @GetMapping(value = "/say")
    String say(@RequestParam String message);

    /**
     * 测试
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/save")
    SaveTestResponse save(@RequestBody SaveTestRequest request);
}
