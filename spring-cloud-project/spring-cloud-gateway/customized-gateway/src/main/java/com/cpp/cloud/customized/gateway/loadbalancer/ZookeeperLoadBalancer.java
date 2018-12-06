package com.cpp.cloud.customized.gateway.loadbalancer;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于ribbon实现负载均衡
 *
 * @author chenjian
 * @date 2018-12-06 09:39
 */
@Component
public class ZookeeperLoadBalancer extends BaseLoadBalancer {

    @Autowired
    private DiscoveryClient discoveryClient;

    private Map<String, BaseLoadBalancer> loadBalancerMap = new ConcurrentHashMap<>();

    @Override
    public Server chooseServer(Object key) {
        if (key instanceof String) {
            String serviceName = String.valueOf(key);
            BaseLoadBalancer baseLoadBalancer = loadBalancerMap.get(serviceName);
            return baseLoadBalancer.chooseServer(serviceName);
        }
        return super.chooseServer(key);
    }

    /**
     * 更新服务实例信息
     * 这里是通过定时的方式，可以基于zookeeper watcher来更新服务
     */
    @Scheduled(fixedRate = 5000)
    public void updateServers() {
        discoveryClient.getServices().stream().forEach(serviceName -> {

            BaseLoadBalancer loadBalancer = new BaseLoadBalancer();

            loadBalancerMap.put(serviceName, loadBalancer);
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
            serviceInstances.forEach(serviceInstance -> {
                Server server = new Server(serviceInstance.isSecure() ? "https://" : "http://",
                        serviceInstance.getHost(), serviceInstance.getPort());
                loadBalancer.addServer(server);
            });
        });
    }
}
