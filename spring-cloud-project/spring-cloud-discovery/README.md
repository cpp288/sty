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
