package com.cpp.cloud.customized.bus.server.controller;

import com.cpp.cloud.customized.bus.common.RemoteAppData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 远程事件接收器
 *
 * @author chenjian
 * @date 2018-12-10 17:42
 */
@RestController
@Slf4j
public class RemoteAppEventReceiverController implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @PostMapping("/receive/remote/event/")
    public String receive(@RequestBody RemoteAppData data) { //  REST 请求不需要具体类型

        // 事件的发送者
        String sender = data.getSender();
        // 事件的数据内容
        Object value = data.getValue();
        // 事件类型
        String type = data.getEventType();

        // 接受到对象内容，同样也要发送事件到本地，做处理
        publisher.publishEvent(new SenderRemoteAppEvent(sender, value));
        return "received";
    }

    private static class SenderRemoteAppEvent extends ApplicationEvent {

        private final String sender;

        private SenderRemoteAppEvent(String sender, Object value) {
            super(value);
            this.sender = sender;
        }

        public String getSender() {
            return sender;
        }
    }

    @EventListener
    @Async
    public void onEvent(SenderRemoteAppEvent event) {
        log.info("接受到事件源：" + event.getClass().getSimpleName() + " , 来自应用 ： " + event.getSender());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
