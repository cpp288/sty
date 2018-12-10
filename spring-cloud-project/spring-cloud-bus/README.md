# 介绍
官网介绍：
>Spring Cloud Bus links the nodes of a distributed system with a lightweight message broker. This broker can then be used to broadcast state changes (such as configuration changes) or other management instructions. A key idea is that the bus is like a distributed actuator for a Spring Boot application that is scaled out. However, it can also be used as a communication channel between apps. This project provides starters for either an AMQP broker or Kafka as the transport.

## 知识发散
### java 事件/监听者模式

例子：`com.cpp.event.simples.event.GUIEvent`

#### 事件
* 所有事件类型扩展 `java.util.EventObject`
* 每个事件对象都有事件源 `java.util.EventObject.source`

#### 事件监听器
* 事件监听器接口扩展 `java.util.EventListener`
* 方法参数类型 `java.util.EventObject` 对象（子类）
* 监听方法访问限定符 `public`
* 监听方法是没有返回值 `void` (Spring `@EventListener`例外)
* 监听方法不会 `throws Throwable`

### spring事件
#### 事件
Spring 事件基类 `org.springframework.context.ApplicationEvent`
* 相对于 `java.util.EventObject` 增加事件发生时间戳 `timestamp`

##### 内建事件
事件名称 | 调用时机
--- | ---
ContextRefreshedEvent | ConfigurableApplicationContext#refresh()
ContextClosedEvent | ConfigurableApplicationContext#close()
ContextStartedEvent | ConfigurableApplicationContext#start()
ContextStoppedEvent | ConfigurableApplicationContext#stop()
RequestHandledEvent | FrameworkServlet#publishRequestHandledEvent()

#### 事件监听器
所有 Spring 事件监听器实现 `org.springframework.context.ApplicationListener`

## 自实现customized-bus
基于Spring的事件机制，通过http方式进行远程通信