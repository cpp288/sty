# 介绍
主要整理了spring cloud做负载均衡的原理机制

## RestTemplate
Spring 核心 HTTP 消息转换器 `HttpMessageConverter`

REST 自描述消息：媒体类型`MediaType`， text/html;text/xml;application/json

HTTP 协议特点：纯文本协议，自我描述

### `HttpMessageConverter` 分析
判断是否可读可写
```java
public interface HttpMessageConverter<T> {

    boolean canRead(Class<?> clazz, @Nullable MediaType mediaType);


    boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType);
}
```

当前支持的媒体类型
```java
public interface HttpMessageConverter<T> {  
    List<MediaType> getSupportedMediaTypes();
}
```

反序列化
```java
public interface HttpMessageConverter<T> {  
    T read(Class<? extends T> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException;
}
```
上面代码中的`HttpInputMessage`类似于Servlet中的`HttpServletRequest`

### `RestTemplate` 在 `HttpMessageConverter` 设计
`RestTemplate` 利用 `HttpMessageConverter` 对一定媒体类型序列化和反序列化

```java
public class RestTemplate extends InterceptingHttpAccessor implements RestOperations {
    // ...
    // List 形式
    private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    // ...
    public RestTemplate() {
        // 添加内建 HttpMessageConverter 实现
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new ResourceHttpMessageConverter(false));
        this.messageConverters.add(new SourceHttpMessageConverter<>());
        this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());

        // 有条件地添加第三方库HttpMessageConverter 整合实现
        if (romePresent) {
            this.messageConverters.add(new AtomFeedHttpMessageConverter());
            this.messageConverters.add(new RssChannelHttpMessageConverter());
        }

        if (jackson2XmlPresent) {
            this.messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
        }
        else if (jaxb2Present) {
            this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
        }

        if (jackson2Present) {
            this.messageConverters.add(new MappingJackson2HttpMessageConverter());
        }
        else if (gsonPresent) {
            this.messageConverters.add(new GsonHttpMessageConverter());
        }
        else if (jsonbPresent) {
            this.messageConverters.add(new JsonbHttpMessageConverter());
        }

        if (jackson2SmilePresent) {
            this.messageConverters.add(new MappingJackson2SmileHttpMessageConverter());
        }
        if (jackson2CborPresent) {
            this.messageConverters.add(new MappingJackson2CborHttpMessageConverter());
        }
    }
}
```

## ribbon
ribbon就是基于 `RestTemplate` ，通过实现 `ClientHttpRequestInterceptor` 来进行拦截，提供各种负载均衡的能力

可以查看 `RestTemplate` 的父类 `InterceptingHttpAccessor`中：
```java
public abstract class InterceptingHttpAccessor extends HttpAccessor {

	private final List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
	
}
```