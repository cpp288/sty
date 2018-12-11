package com.cpp.cloud.starter.stream.rocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rocket mq binder configuration
 *
 * @author chenjian
 * @date 2018-12-11 22:51
 */
@Configuration
public class RocketMQMessageChannelBinderConfiguration {

    @Bean
    public RocketMQMessageChannelBinder rocketMQMessageChannelBinder() {
        return new RocketMQMessageChannelBinder();
    }

}
