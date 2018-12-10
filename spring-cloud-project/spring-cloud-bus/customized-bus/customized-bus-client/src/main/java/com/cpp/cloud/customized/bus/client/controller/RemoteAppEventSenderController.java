package com.cpp.cloud.customized.bus.client.controller;

import com.cpp.cloud.customized.bus.common.RemoteAppEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 远程事件发送端
 *
 * @author chenjian
 * @date 2018-12-10 17:38
 */
@RestController
public class RemoteAppEventSenderController implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @PostMapping(value = "/send/remote/event/{appName}")
    public String sendEvent(@PathVariable String appName, @RequestBody Object body) {
        RemoteAppEvent event = new RemoteAppEvent(body, appName);
        publisher.publishEvent(event);
        return "ok";
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
