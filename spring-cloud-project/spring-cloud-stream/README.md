# 介绍

[官网文档](https://spring.io/docs/reference)

[官网文档2](https://docs.spring.io/spring-cloud-stream/docs/Chelsea.BUILD-SNAPSHOT/reference/htmlsingle/index.html#_spring_cloud_stream_core)

相关博客：
1. https://www.cnblogs.com/yangzhilong/p/7904461.html
2. https://www.cnblogs.com/huangjuncong/p/9102843.html
3. https://stackoverflow.com/questions/43778809/how-to-manage-manual-ack-with-spring-cloud-stream-rabbit
4. https://www.cnblogs.com/yangzhilong/p/7904461.html

## 为什么需要SpringCloud Stream消息驱动呢
比方说我们用到了RabbitMQ和Kafka，由于这两个消息中间件的架构上的不同，像RabbitMQ有exchange，kafka有Topic，partitions分区，这些中间件的差异性导致我们实际项目开发给我们造成了一定的困扰，我们如果用了两个消息队列的其中一种，后面的业务需求，我想往另外一种消息队列进行迁移，这时候无疑就是一个灾难性的，一大堆东西都要重新推倒重新做，因为它跟我们的系统耦合了，这时候springcloud Stream给我们提供了一种解耦合的方式。

如下是官方文档提供的架构图所示：

![spring-cloud-stream架构](image/spring-cloud-stream架构.png)

Spring Cloud Stream由一个中间件中立的核组成。应用通过Spring Cloud Stream插入的input(相当于消费者consumer，它是从队列中接收消息的)和output(相当于生产者producer，它是从队列中发送消息的。)通道与外界交流。

通道通过指定中间件的Binder实现与外部代理连接。业务开发者不再关注具体消息中间件，只需关注Binder对应用程序提供的抽象概念来使用消息中间件实现业务即可。

## 各类stream类比

![stream](image/stream.png)

## Spring cloud stream 整合 RocketMQ

![stream整合](image/stream整合.png)

### 实现步骤
参考官网：
* A class that implements the Binder interface; （实现 Binder  接口）
* A Spring @Configuration class that creates a bean of type Binder along with the middleware connection infrastructure.（Binder 实现类上标注 @Configuration 注解）
* A META-INF/spring.binders file found on the classpath containing one or more binder definitions, as shown in the following example:（META-INF/spring.binders  配置 Binder 名称和 Binder 实现自动装配类映射）