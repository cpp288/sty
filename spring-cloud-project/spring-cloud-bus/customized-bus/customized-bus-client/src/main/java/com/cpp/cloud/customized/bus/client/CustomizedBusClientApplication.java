package com.cpp.cloud.customized.bus.client;

import com.cpp.cloud.customized.bus.common.event.RemoteAppEventListener;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 *
 * @author chenjian
 * @date 2018-12-10 17:35
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CustomizedBusClientApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CustomizedBusClientApplication.class)
                .web(WebApplicationType.SERVLET)
                .listeners(new RemoteAppEventListener())
                .run(args);
    }
}
