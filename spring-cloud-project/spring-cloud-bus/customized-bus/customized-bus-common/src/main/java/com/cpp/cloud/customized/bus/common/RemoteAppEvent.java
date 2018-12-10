package com.cpp.cloud.customized.bus.common;

import com.cpp.cloud.customized.bus.common.enums.RemoteType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 远程事件
 *
 * @author chenjian
 * @date 2018-12-10 16:32
 */
public class RemoteAppEvent extends ApplicationEvent {

    /**
     * 远程类型
     */
    @Getter
    @Setter
    private RemoteType remoteType;

    /**
     * 调用的应用名称
     */
    @Getter
    private final String appName;

    @Getter
    @Setter
    private List<HostAndPort> hostAndPorts;

    public RemoteAppEvent(Object source, String appName) {
        super(source);
        this.appName = appName;
    }

    @Getter
    public static class HostAndPort {

        private final String host;

        private final int port;

        private final boolean secure;

        public HostAndPort(String host, int port, boolean secure) {
            this.host = host;
            this.port = port;
            this.secure = secure;
        }
    }
}
