# 介绍

spring cloud 服务注册中心
* Zookeeper
* Eureka
* Consul

## 注册中心简单对比
| 比较点        | Eureka                                                  | Zookeeper                    | Consul                    |
| ------------- | ------------------------------------------------------- | ---------------------------- | ------------------------- |
| 运维熟悉度    | 相对陌生                                                | 熟悉                         | 更陌生                    |
| 一致性（CAP） | AP（最终一致性）                                        | CP（一致性强）               | AP（最终一致性）          |
| 一致性协议    | HTTP 定时轮训                                           | ZAB                          | RAFT                      |
| 通讯方式      | HTTP REST                                               | 自定义协议                   | HTTP REST                 |
| 更新机制      | Peer 2 Peer（服务器之间） + Scheduler（服务器和客户端） | ZK Watch                     | Agent 监听的方式          |
| 适用规模      | 20 K ~ 30 K 实例（节点）                                | 10K ~ 20K 实例（节点）       | < 3K 实例（节点）         |
| 性能问题      | 简单的更新机制、复杂设计、规模较大时 GC 频繁            | 扩容麻烦、规模较大时 GC 频繁 | 3K 节点以上，更新列表缓慢 |
| dashboard | 有 | 没有，可以自己实现 | 有 |

## Zookeeper
### 注意点
引用依赖的时候，由于原先依赖的是zookeeper3.5.x，这个版本是beta版本，需要排除引用3.4.x，spring官网有说明
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-all</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.12</version>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

需要使用bootstrap.properties，不能使用application.properties（亲测不行），参考[官网](https://cloud.spring.io/spring-cloud-zookeeper/single/spring-cloud-zookeeper.html)

相关博客：
1. [使用Spring Cloud Zookeeper + Feign实现服务发现](https://www.cnblogs.com/karascanvas/p/7521942.html)

## Eureka
使用的是集群部署的模式，注意的是：当第一个eureka server启动的时候会报错，是因为注册不到其它两台server，可忽略

### 重复实例节点问题

由于eureka是通过http心跳检测的方式去判断实例是否存活，当某一个实例进行短时间重启时，会产生重复实例的问题。

这个问题在zookeeper中也会存在，但是由于zookeeper机制的原因（基于watch机制，当session断开时，临时节点会被删除），zookeeper是短时间会有重复实例存在，而eureka则是会一直存在

这会导致一个问题，在做负载均衡时，如果是轮询的方式则会出现某个实例节点访问量比其它节点大的问题

## Consul
不做演示demo

参考博客
1. https://www.cnblogs.com/yjmyzz/p/replace-eureka-with-consul.html
1. 参考spring官方文档