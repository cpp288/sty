package com.cpp.base.difference;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author chenjian
 * @date 2018-12-04 14:55
 */
@RestController
public class TestController {

    @GetMapping(value = "/test")
    public String test() {
        System.out.println("do test...");
        return "ok";
    }
}
