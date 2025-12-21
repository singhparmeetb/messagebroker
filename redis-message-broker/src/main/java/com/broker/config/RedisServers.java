package com.broker.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConfigurationProperties(prefix = "redis-servers")
public class RedisServers {
    private List<RedisConfig> redisConfigs;
    private Map<String, JedisConnectionFactory> redisConnections;

    @PostConstruct
    private void init() {
        redisConnections = new HashMap<String, JedisConnectionFactory>();
        for (RedisConfig redisConfig : redisConfigs) {
            JedisConnectionFactory serverConnectionFactory = setUpJedisConnectionFactory(redisConfig);
            redisConnections.put(redisConfig.getName(), serverConnectionFactory);
            serverConnectionFactory.afterPropertiesSet();
            log.debug(redisConfig.toString());
        }
    }

    private JedisConnectionFactory setUpJedisConnectionFactory(RedisConfig redisConfig) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
                redisConfig.getHost(), redisConfig.getPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisConfig.getPassword()));

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigBuilder = JedisClientConfiguration
                .builder();
        jedisClientConfigBuilder.usePooling().poolConfig(redisConfig.getGenericObjectPoolConfig());

        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfigBuilder.build());
    }

    public StringRedisTemplate getStringRedisTemplate(String serverName) {
        return new StringRedisTemplate(redisConnections.get(serverName));
    }

    public void setRedisConfigs(List<RedisConfig> redisConfigs) {
    }

    @PreDestroy
    public void closeRedisConnections() {
        for (Map.Entry<String, JedisConnectionFactory> entry : redisConnections.entrySet()) {
            entry.getValue().stop();
        }
    }

    // private StringRedisTemplate getStringRedisTemplate(JedisConnectionFactory
    // connectionFactory) {
    // return new StringRedisTemplate(connectionFactory);
    // }

}
