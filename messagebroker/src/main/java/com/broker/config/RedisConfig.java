package com.broker.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.JedisFactory;

@Configuration
@ConfigurationProperties(prefix = "server")
@Getter
@Setter
public class RedisConfig {

    private String host;
    private int port;
    private String password;
    private int maxActive;
    private int maxIdle;
    private int minIdle;
    private Duration maxWait;

    public JedisFactory jedisFactory(){
        return new 
    }

}
