package com.cpp.cloud.customized.gateway.servlet;

import com.cpp.cloud.customized.gateway.loadbalancer.ZookeeperLoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * servlet网关实现
 * 实现流程：
 * 1. 请求到达网关服务，网关urlPatterns后一位是服务实例名称（exp：/servlet/gateway/{serviceName}/{service-uri}）
 * 2. 解析出服务实例名称、uri
 * 3. 通过注册中心获取服务实例地址相关信息（这里用的是zookeeper），通过负载均衡算法获取一台实例
 * 4. 组装参数、请求体、请求头，通过 RestTemplate 进行请求
 *
 * @author chenjian
 * @date 2018-12-05 23:29
 */
@WebServlet(name = "ribbonGateway", urlPatterns = "/ribbon/gateway/*")
public class RibbonGatewayServlet extends HttpServlet {

    @Autowired
    private ZookeeperLoadBalancer zookeeperLoadBalancer;

    /**
     * 通过实例名称获取一台实例
     *
     * @param serviceName
     * @return
     */
    private Server chooseServer(String serviceName) {
        return zookeeperLoadBalancer.chooseServer(serviceName);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ${service-name}/${service-uri}
        String pathInfo = request.getPathInfo();
        String[] parts = StringUtils.split(pathInfo.substring(1), "/");
        // 获取服务名称
        String serviceName = parts[0];
        // 获取服务 URI
        String serviceURI = "/" + parts[1];
        // 随机选择一台服务实例
        Server server = chooseServer(serviceName);
        // 构建目标服务 URL -> scheme://ip:port/serviceURI
        String targetURL = buildTargetURL(server, serviceURI, request);

        // 创建转发客户端
        RestTemplate restTemplate = new RestTemplate();

        // 构造 Request 实体
        RequestEntity<byte[]> requestEntity = null;
        try {
            requestEntity = createRequestEntity(request, targetURL);
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(requestEntity, byte[].class);
            writeHeaders(responseEntity, response);
            writeBody(responseEntity, response);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 组装目标URL
     *
     * @param server
     * @param serviceURI
     * @param request
     * @return
     */
    private String buildTargetURL(Server server, String serviceURI, HttpServletRequest request) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(server.getScheme())
                .append(server.getHost()).append(":").append(server.getPort())
                .append(serviceURI);
        String queryString = request.getQueryString();
        if (StringUtils.hasText(queryString)) {
            urlBuilder.append("?").append(queryString);
        }
        return urlBuilder.toString();
    }

    /**
     * 组装请求参数（包含请求体、请求头）
     *
     * @param request
     * @param url
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    private RequestEntity<byte[]> createRequestEntity(HttpServletRequest request, String url) throws URISyntaxException, IOException {
        // 获取当前请求方法
        String method = request.getMethod();
        // 装换 HttpMethod
        HttpMethod httpMethod = HttpMethod.resolve(method);
        byte[] body = createRequestBody(request);
        MultiValueMap<String, String> headers = createRequestHeaders(request);
        return new RequestEntity<>(body, headers, httpMethod, new URI(url));
    }

    /**
     * 组装请求头
     *
     * @param request
     * @return
     */
    private MultiValueMap<String, String> createRequestHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        List<String> headerNames = Collections.list(request.getHeaderNames());
        for (String headerName : headerNames) {
            List<String> headerValues = Collections.list(request.getHeaders(headerName));
            for (String headerValue : headerValues) {
                headers.add(headerName, headerValue);
            }
        }
        return headers;
    }

    /**
     * 组装请求体
     *
     * @param request
     * @return
     * @throws IOException
     */
    private byte[] createRequestBody(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        return StreamUtils.copyToByteArray(inputStream);
    }


    /**
     * 输出 Body 部分
     *
     * @param responseEntity
     * @param response
     * @throws IOException
     */
    private void writeBody(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) throws IOException {
        if (responseEntity.hasBody()) {
            byte[] body = responseEntity.getBody();
            // 输出二进值
            ServletOutputStream outputStream = response.getOutputStream();
            // 输出 ServletOutputStream
            outputStream.write(body);
            outputStream.flush();
        }
    }

    /**
     * 输出 Headers 部分
     *
     * @param responseEntity
     * @param response
     */
    private void writeHeaders(ResponseEntity<byte[]> responseEntity, HttpServletResponse response) {
        // 获取相应头
        HttpHeaders httpHeaders = responseEntity.getHeaders();
        // 输出转发 Response 头
        for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
            String headerName = entry.getKey();
            List<String> headerValues = entry.getValue();
            for (String headerValue : headerValues) {
                response.addHeader(headerName, headerValue);
            }
        }
    }
}
