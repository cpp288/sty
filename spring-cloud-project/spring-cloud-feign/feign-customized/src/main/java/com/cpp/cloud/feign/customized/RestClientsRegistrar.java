package com.cpp.cloud.feign.customized;

import com.cpp.cloud.feign.customized.annotation.EnableRestClient;
import com.cpp.cloud.feign.customized.annotation.RestClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.stream.Stream;

/**
 * ImportBeanDefinitionRegistrar用法大体和BeanDefinitionRegistryPostProcessor相同，
 * 但是值得注意的是ImportBeanDefinitionRegistrar只能通过由其它类import的方式来加载，通常是主启动类类或者注解。
 * 参考：https://www.jianshu.com/p/caef887b78b5
 *
 * @author chenjian
 * @date 2018-12-03 19:59
 */
public class RestClientsRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanFactoryAware {

    private Environment environment;
    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
                                        BeanDefinitionRegistry registry) {
        // 获取 EnableRestClient 注解的所有属性名称：属性值
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableRestClient.class.getName());
        // 获取clients属性值
        Class<?>[] clientClasses = (Class<?>[]) attributes.get("clients");

        ClassLoader classLoader = metadata.getClass().getClassLoader();

        Stream.of(clientClasses)
                // 获取标注 @RestClient 注解的接口
                .filter(interfaceClass -> AnnotationUtils.findAnnotation(interfaceClass, RestClient.class) != null)
                .forEach(restClientClass -> {
                    Assert.isTrue(restClientClass.isInterface(), "@RestClient can only be specified on an interface");
                    // 获取标注的 @RestClient 注解元信息
                    RestClient restClient = AnnotationUtils.findAnnotation(restClientClass, RestClient.class);
                    // 获取应用名称（占位符：${application.name}）
                    String serviceName = environment.resolvePlaceholders(restClient.name());

                    // 使用代理方式生成代理类，由代理类去执行rest请求，这里使用 JDK 动态代理
                    Object proxyInstance = Proxy.newProxyInstance(classLoader, new Class[]{restClientClass},
                            new RequestMappingMethodInvocationHandler(serviceName, beanFactory));
                    registerBean("RestClient." + serviceName, proxyInstance, restClientClass, registry);
                });
    }

    /**
     * 注册 bean
     *
     * @param beanName
     * @param proxyInstance
     * @param restClientClass
     * @param registry
     */
    private void registerBean(String beanName,
                              Object proxyInstance,
                              Class<?> restClientClass,
                              BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(RestClientClassFactoryBean.class);

        beanDefinitionBuilder.addConstructorArgValue(proxyInstance);
        beanDefinitionBuilder.addConstructorArgValue(restClientClass);

        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    private static class RestClientClassFactoryBean implements FactoryBean {

        private final Object proxyInstance;
        private final Class<?> restClientClass;

        public RestClientClassFactoryBean(Object proxyInstance, Class<?> restClientClass) {
            this.proxyInstance = proxyInstance;
            this.restClientClass = restClientClass;
        }

        @Override
        public Object getObject() throws Exception {
            return proxyInstance;
        }

        @Override
        public Class<?> getObjectType() {
            return restClientClass;
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
