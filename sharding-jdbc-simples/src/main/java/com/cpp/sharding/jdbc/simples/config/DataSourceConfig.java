package com.cpp.sharding.jdbc.simples.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import io.shardingsphere.core.rule.ShardingRule;
import io.shardingsphere.core.yaml.sharding.YamlShardingConfiguration;
import io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;

/**
 * 数据源配置
 *
 * @author chenjian
 * @date 2018-12-06 19:40
 */
@Configuration
public class DataSourceConfig {

//    @Autowired
//    private Filter statFilter;
//
//    private static final String SHARDING_YML_PATH = "sharding/dataSource.yml";
//
//    /**
//     * 构建dataSource
//     * 这里没有使用ShardingDataSourceFactory
//     * 因为要为druid数据源配置监听Filter
//     *
//     * @return
//     * @throws SQLException
//     * @throws IOException
//     */
//    @Bean
//    public DataSource dataSource() throws SQLException, IOException {
//        YamlShardingConfiguration config = parse();
//        ShardingRule rule = config.getShardingRule(Collections.<String, DataSource>emptyMap());
//        rule.getDataSourceMap().forEach((k, v) -> {
//            DruidDataSource d = (DruidDataSource) v;
//            d.setProxyFilters(Lists.newArrayList(statFilter));
//        });
//
//        return new ShardingDataSource(new HashMap<String, DataSource>(),
//                rule, config.getShardingRule().getConfigMap(), config.getShardingRule().getProps());
//    }
//
//    /**
//     * 解析yml
//     *
//     * @return
//     * @throws IOException
//     * @throws FileNotFoundException
//     * @throws UnsupportedEncodingException
//     */
//    private YamlShardingConfiguration parse() throws IOException, FileNotFoundException, UnsupportedEncodingException {
//        Resource certResource = new ClassPathResource(SHARDING_YML_PATH);
//        try (
//                InputStreamReader inputStreamReader = new InputStreamReader(certResource.getInputStream(), "UTF-8")
//        ) {
//            return new Yaml(new Constructor(YamlShardingConfiguration.class)).loadAs(inputStreamReader, YamlShardingConfiguration.class);
//        }
//    }

//    @Bean(name = "shardingDataSource")
//    public DataSource dataSource() throws Exception {
//        Resource resource = new ClassPathResource("sharding.yml");
//        File file = resource.getFile();
//        DataSource dataSource = new YamlShardingDataSource(file);
//        return dataSource;
//    }
}
