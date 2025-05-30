package com.broker.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import jakarta.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "RedisServer")
public class RedisServers {
    private List<RedisConfig> redisConfigs;
    Map<String, JedisConnectionFactory> redisConnections;

    @PostConstruct
    private void init() {
        Map<String, JedisConnectionFactory> redisConnections = new HashMap<>();
        for (RedisConfig redisConfig : redisConfigs) {
            JedisConnectionFactory serverConnectionFactory = setUpJedisConnectionFactory(redisConfig);
            redisConnections.put(redisConfig.getName(), serverConnectionFactory);
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

    // private StringRedisTemplate getStringRedisTemplate(JedisConnectionFactory
    // connectionFactory) {
    // return new StringRedisTemplate(connectionFactory);
    // }
}
