package com.cpp.cloud.ribbon.client.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通过实现 ClientHttpRequestInterceptor 接口，可以对 RestTemplate 调用进行拦截处理
 * 1. 通过定时的方式，从注册中心获取服务的名称、地址信息
 * 2. 当客户端通过 RestTemplate 方式请求时（如：/spring-cloud-ribbon-server/say?message=hello），进行拦截
 * 3. 获取请求地址，将服务名称替换成具体的url地址
 * 4. 请求得到具体的响应，并返回
 *
 * @author chenjian
 * @date 2018-11-30 14:12
 */
public class LoadBalancedRequestInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    private DiscoveryClient discoveryClient;

    // 定义一个存放所有实例的地址 key：实例名称 value：实例地址集合
    private volatile Map<String, Set<String>> targetUrlsCache = new HashMap<>();

    /**
     * 从注册中心获取所有实例的地址信息，并放入 targetUrlsCache 中
     */
    @Scheduled(fixedRate = 10 * 1000) // 10 秒钟更新一次缓存
    public void updateTargetUrlsCache() {
//        System.out.println("开始更新实例地址....");
        // 获取当前应用的机器列表
        Map<String, Set<String>> newTargetUrlsCache = new HashMap<>();
        discoveryClient.getServices().forEach(serviceName -> {
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
            Set<String> newTargetUrls = serviceInstances
                    .stream()
                    .map(s ->
                            s.isSecure() ?
                                    "https://" + s.getHost() + ":" + s.getPort() :
                                    "http://" + s.getHost() + ":" + s.getPort()
                    ).collect(Collectors.toSet());
            newTargetUrlsCache.put(serviceName, newTargetUrls);
        });

        this.targetUrlsCache = newTargetUrlsCache;
//        System.out.println("更新实例地址结束：\n" + newTargetUrlsCache);
    }

    /**
     * 实现一个简单的随机负载均衡算法，并通过解析出来的url和参数进行请求相应
     * 这只是一个简单的demo
     *
     * @param request
     * @param body
     * @param execution
     * @return
     * @throws IOException
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        URI requestURI = request.getURI();
        String path = requestURI.getPath();
        String[] parts = StringUtils.split(path.substring(1), "/");
        String serviceName = parts[0]; // serviceName
        String uri = parts[1];  // "/say?message="
        // 服务器列表快照
        List<String> targetUrls = new LinkedList<>(targetUrlsCache.get(serviceName));
        int size = targetUrls.size();
        // size =3 , index =0 -2
        int index = new Random().nextInt(size);
        // 选择其中一台服务器
        String targetURL = targetUrls.get(index);
        // 最终服务器 URL
        String actualURL = targetURL + "/" + uri + "?" + requestURI.getQuery();

        // 执行请求
        System.out.println("本次请求的 URL : " + actualURL);

        URL url = new URL(actualURL);

        URLConnection urlConnection = url.openConnection();

        // 响应头
        HttpHeaders httpHeaders = new HttpHeaders();
        // 响应主体
        InputStream responseBody = urlConnection.getInputStream();

        return new SimpleClientHttpResponse(httpHeaders, responseBody);
    }

    private static class SimpleClientHttpResponse implements ClientHttpResponse {

        private HttpHeaders headers;

        private InputStream body;

        public SimpleClientHttpResponse(HttpHeaders headers, InputStream body) {
            this.headers = headers;
            this.body = body;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.OK;
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return 200;
        }

        @Override
        public String getStatusText() throws IOException {
            return "OK";
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}
