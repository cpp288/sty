package com.cpp.cloud.feign.server.api;

import com.cpp.cloud.feign.customized.annotation.RestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 自定义 @RestClient 注解api接口
 *
 * @author chenjian
 * @date 2018-12-03 22:25
 */
@RestClient(name = "${spring-cloud-feign-server.name}")
public interface CustomizedServerApi {

    /**
     * 服务端接口
     *
     * @param message
     * @return
     */
    @GetMapping(value = "/rest/say")
    String say(@RequestParam String message);
}
