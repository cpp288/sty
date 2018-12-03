package com.cpp.cloud.feign.server.web.controller;

import com.cpp.cloud.feign.server.api.ServerApi;
import com.cpp.cloud.feign.server.request.SaveTestRequest;
import com.cpp.cloud.feign.server.response.SaveTestResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ServerController
 *
 * @author chenjian
 * @date 2018-12-03 10:03
 */
@RestController
public class ServerController implements ServerApi {

    @Override
    public String say(@RequestParam String message) {
        System.out.printf("接收到消息：message -> %s\n", message);
        return "Hello, " + message;
    }

    @Override
    public SaveTestResponse save(@RequestBody SaveTestRequest request) {
        System.out.println("接收到请求体 ：" + request);

        return SaveTestResponse.builder().message("服务端接收到请求体：" + request).build();
    }
}
