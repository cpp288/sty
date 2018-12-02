# 介绍
* 使用 hystrix 的方式进行熔断
* 使用自己写代码的方式

## hystrix方式
主要通过注解 `@HystrixCommand` 方式实现，相关实现可以参考官网或者博客

## 自实现
主要通过从简单版本 -> 中级版本 -> 高级版本

### 简单版本
通过线程池设置超时时间的方式，简单实现了超时熔断

### 中级版本
通过抛异常，然后在 `CustomizedControllerAdvice` 或者自实现 `HandlerInterceptor`的方式进行异常拦截来实现

### 高级版本
使用了注解的方式来设置超时时间、并发量等，通过aspectJ aop的方式切面处理请求，并通过 `Semaphore` 信号量的方式来实现限流

# 总结
自实现部分也是hystrix的一些实现思路，通过这些可以更好的了解hystrix的实现方式