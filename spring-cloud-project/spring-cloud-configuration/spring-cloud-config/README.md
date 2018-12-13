# spring cloud config
## server原理分析
启动 spring cloud config 注解 `@EnableConfigServer`

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ConfigServerConfiguration.class)
public @interface EnableConfigServer {

}
```

会发现Import配置类`ConfigServerConfiguration`

```java
@Configuration
public class ConfigServerConfiguration {
	class Marker {}

	@Bean
	public Marker enableConfigServerMarker() {
		return new Marker();
	}
}
```

这里只定义了一个 `Marker` 的 bean，是一个空类，这是什么意思呢？

我们找一下这个类被使用的地方：

```java
@Configuration
@ConditionalOnBean(ConfigServerConfiguration.Marker.class)
@EnableConfigurationProperties(ConfigServerProperties.class)
@Import({ EnvironmentRepositoryConfiguration.class, CompositeConfiguration.class, ResourceRepositoryConfiguration.class,
		ConfigServerEncryptionConfiguration.class, ConfigServerMvcConfiguration.class })
public class ConfigServerAutoConfiguration {

}
```

从上面的代码，我们可以看到 `@ConditionalOnBean(ConfigServerConfiguration.Marker.class)`，说明这个类主要是用来加载 `ConfigServerAutoConfiguration` 的条件之一

继续看 `EnvironmentRepositoryConfiguration` :

```java
@Configuration
@EnableConfigurationProperties({ SvnKitEnvironmentProperties.class,
		JdbcEnvironmentProperties.class, NativeEnvironmentProperties.class, VaultEnvironmentProperties.class })
@Import({ CompositeRepositoryConfiguration.class, JdbcRepositoryConfiguration.class, VaultRepositoryConfiguration.class,
		SvnRepositoryConfiguration.class, NativeRepositoryConfiguration.class, GitRepositoryConfiguration.class,
		DefaultRepositoryConfiguration.class })
public class EnvironmentRepositoryConfiguration {
	// ...
}
```

看到有很多的 `RepositoryConfiguration`，这和官网上说的，spring cloud config 配置源可以支持 Git、JDBC、SVN、本地文件等等

我们再看下刚才 `ConfigServerAutoConfiguration` 类上的 `ConfigServerEncryptionConfiguration` 类，它其中有一个内部类：

```java
@Configuration
@ConditionalOnMissingBean(value = EnvironmentRepository.class, search = SearchStrategy.CURRENT)
class DefaultRepositoryConfiguration {
	@Autowired
	private ConfigurableEnvironment environment;

	@Autowired
	private ConfigServerProperties server;

	@Autowired(required = false)
	private TransportConfigCallback customTransportConfigCallback;

	@Bean
	public MultipleJGitEnvironmentRepository defaultEnvironmentRepository(
	        MultipleJGitEnvironmentRepositoryFactory gitEnvironmentRepositoryFactory,
			MultipleJGitEnvironmentProperties environmentProperties) throws Exception {
		return gitEnvironmentRepositoryFactory.build(environmentProperties);
	}
}
```

从上面的代码可以看到：

> 当 Spring 应用上下文没有出现 `EnvironmentRepository` Bean 的时候，那么，默认激活 `DefaultRepositoryConfiguration` (Git 实现)，否则采用自定义实现。

这也能解释了为什么spring cloud config 默认使用的是 Git 来做配置源

这样我们就可以通过自定义 `EnvironmentRepository` Bean来自定义实现配置源，具体看代码

通过http请求：/${application}/${profile}/${label}

