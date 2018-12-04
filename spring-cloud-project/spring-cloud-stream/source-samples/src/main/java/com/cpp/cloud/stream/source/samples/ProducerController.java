package com.cpp.cloud.stream.source.samples;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 生产者接口
 *
 * @author chenjian
 * @date 2018-11-28 14:45
 */
@RestController
public class ProducerController {

    @Resource
    private SendService sendService;

    @RequestMapping("/send01/{msg}")
    public void send01(@PathVariable String msg) {
        sendService.sendBySource(msg);
    }

    @RequestMapping("/send02/{msg}")
    public void send02(@PathVariable String msg) {
        sendService.sendByMySource(msg);
    }
}
