package com.cpp.cloud.feign.client.controller;

import com.cpp.cloud.feign.server.api.ServerApi;
import com.cpp.cloud.feign.server.request.SaveTestRequest;
import com.cpp.cloud.feign.server.response.SaveTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * feign调用
 *
 * @author chenjian
 * @date 2018-12-03 10:59
 */
@RestController
public class FeignController {

    @Autowired
    private ServerApi serverApi;

    @GetMapping(value = "/feign/say")
    public String say(@RequestParam String message) {
        return serverApi.say(message);
    }

    @PostMapping(value = "/feign/save")
    public SaveTestResponse save(@RequestBody SaveTestRequest request) {
        return serverApi.save(request);
    }
}
