# 介绍
spring cloud 网关技术

## 自实现网关
### servlet方式
spring cloud zuul是基于servlet的方式实现的

实现流程：
1. 请求到达网关服务，网关urlPatterns后一位是服务实例名称（exp：/servlet/gateway/{serviceName}/{service-uri}）
2. 解析出服务实例名称、uri
3. 通过注册中心获取服务实例地址相关信息（这里用的是zookeeper），通过负载均衡算法获取一台实例
4. 组装参数、请求体、请求头，通过 RestTemplate 进行请求

具体实现代码：GatewayServlet

依赖组件：
* 模块装配（`@EnableZuulProxy` 配合注解：`@Import`）
* 服务发现（zookeeper、eureka）
* 服务路由（URI映射目的服务）
* 负载均衡（ribbon）
* 服务熔断（hystrix，可选）

自实现负载均衡：
1. 通过实现ribbon的 `ILoadBalancer` 接口，或者其子类（如： `BaseLoadBalancer` ）；
2. 通过实现ribbon的 `IRule` 来定义负载均衡规则；

## zuul
相关代码查看zuul模块

官方文档：https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.0.M3/single/spring-cloud-netflix.html#netflix-zuul-starter