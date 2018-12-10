package com.cpp.cloud.customized.bus.common.event;

import com.cpp.cloud.customized.bus.common.RemoteAppData;
import com.cpp.cloud.customized.bus.common.RemoteAppEvent;
import com.cpp.cloud.customized.bus.common.enums.RemoteType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 远程调用事件监听器
 *
 * @author chenjian
 * @date 2018-12-10 16:41
 */
@Slf4j
public class RemoteAppEventListener implements SmartApplicationListener {

    private RestTemplate restTemplate = new RestTemplate();

    private DiscoveryClient discoveryClient;

    private String currentAppName;

    /**
     * 监听的事件类型，SmartApplicationListener 可以监听多个事件
     *
     * @param eventType
     * @return
     */
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return RemoteAppEvent.class.isAssignableFrom(eventType)
                || ContextRefreshedEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof RemoteAppEvent) {
            onRemoteAppEvent((RemoteAppEvent) event);
        } else if (event instanceof ContextRefreshedEvent) {
            onContextRefreshedEvent((ContextRefreshedEvent) event);
        }
    }

    private void onRemoteAppEvent(RemoteAppEvent event) {
        if (event.getRemoteType() == null || event.getRemoteType().equals(RemoteType.HTTP)) {
            doHttpRemote(event);
        } else {
            // TODO 其它类型的
        }
    }

    /**
     * http类型远程
     *
     * @param event
     */
    private void doHttpRemote(RemoteAppEvent event) {
        Object source = event.getSource();
        String appName = event.getAppName();
        List<RemoteAppEvent.HostAndPort> hostAndPorts = event.getHostAndPorts();

        List<ServiceInstance> serviceInstances;
        // 如果没有指定则广播所有实例
        if (CollectionUtils.isEmpty(hostAndPorts)) {
            serviceInstances = discoveryClient.getInstances(appName);
        } else {
            serviceInstances = new ArrayList<>();
            for (RemoteAppEvent.HostAndPort hostAndPort : hostAndPorts) {
                serviceInstances.add(new DefaultServiceInstance(
                        appName, hostAndPort.getHost(), hostAndPort.getPort(), hostAndPort.isSecure()));
            }
        }
        if (CollectionUtils.isEmpty(serviceInstances)) {
            log.warn("{0} 没有可用的服务实例", appName);
            return;
        }

        // 开始发送
        for (ServiceInstance s : serviceInstances) {
            String rootURL = s.isSecure() ?
                    "https://" + s.getHost() + ":" + s.getPort() :
                    "http://" + s.getHost() + ":" + s.getPort();

            // 调用的url地址
            String url = rootURL + "/receive/remote/event/";

            RemoteAppData data = new RemoteAppData();
            data.setSender(currentAppName);
            data.setValue(source);
            data.setEventType(RemoteAppEvent.class.getName());
            // 发送 HTTP 请求
            log.info(restTemplate.postForObject(url, data, String.class));
        }
    }

    /**
     * 监听 ContextRefreshedEvent 事件，从而注入相关参数
     *
     * @param event
     */
    private void onContextRefreshedEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        this.discoveryClient = applicationContext.getBean(DiscoveryClient.class);
        this.currentAppName = applicationContext.getEnvironment().getProperty("spring.application.name");
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
