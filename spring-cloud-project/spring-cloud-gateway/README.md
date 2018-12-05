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