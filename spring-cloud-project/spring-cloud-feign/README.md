# 介绍

feign默认集成了ribbon作为负载均衡

* 基于spring cloud feign实现服务调用功能
* 自实现 feign 功能（feign-customized模块）
    * `@EnableRestClient` 注解模块驱动，相当于 Feign 中的 `@EnableFeignClients`
    * `@RestClient` 绑定客户端，指定应用名称，相当于 Feign 中的 `@FeignClient`
    * 客户端接口指定请求映射 `@RequestMapping`
    * 客户端接口指定请求参数 `@RequestParam` `@RequestBody`
    * 使用 `@Autowired` 注入接口，由代理类执行
* feign的配置
    * 使用 requestInterceptors 配置（feign调用之前拦截）统一设置 header

## 服务调用引入背景
`RestTemplate` 限制
* 面向 URL 组件，必须依赖于 主机 + 端口 + URI
    ```java
    restTemplate.getForObject("http://127.0.0.1:8080/say?message=" + message, String.class);
    ```
* 非接口编程

## REST
### REST核心概念
#### 请求映射
`@RequestMapping`
#### 请求参数处理
`@RequestParam`
#### 请求主体处理
`@RequestBody`
#### 响应处理: 
* `@ResponseBody` 
* `@ResponseStatus` 
* `ResponseEntity`
#### 自描述消息
`@RequestMapping(produces="application/widgets+xml")`
#### 内容协商
`ContentNegotiationManager`

理论知识：https://developer.mozilla.org/en-US/docs/Web/HTTP/Content_negotiation

### REST服务框架比较

Spring Cloud Feign 是 OpenFeign 扩展，并且使用 Spring MVC 注解来做 URI 映射，比如 `@RequestMapping` 或 `@GetMapping` 之类

OpenFeign：灵感来自于 JAX-RS（Java REST 标准），[官网](https://github.com/OpenFeign/feign)

JAX-RS：[Java REST 标准](https://github.com/mercyblitz/jsr/tree/master/REST)，可移植性高

|技术栈|HTTP 方法|变量路径|请求参数|自描述消息|
| --- | --- | --- | --- | --- |
|JAX-RS|`@GET`|`@PathParam`|`@FormParam`|`@Produces("application/widgets+xml")`|
|Spring Web MVC|`@GetMapping`|`@PathVariable`|`@RequestParam`|`@RequestMapping(produces="application/widgets+xml")`|
|OpenFeign|`@RequestLine("GET...")`|`@Param`|`@Param`|`@Headers("Content-Type: application/json")`|
|Spring Cloud Feign|`@GetMapping`|`@PathVariable`|`@RequestParam`|`@RequestMapping(produces="application/widgets+xml")`|