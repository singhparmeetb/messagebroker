package com.broker.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class RedisConfig {

    private String name;
    private String host;
    private int port;
    private String password;
    private GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig<>();

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        return genericObjectPoolConfig;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaxIdle(int maxIdle) {
        this.genericObjectPoolConfig.setMaxIdle(maxIdle);
    }

    public void setMaxTotal(int maxTotal) {
        this.genericObjectPoolConfig.setMaxTotal(maxTotal);
    }

    @Override
    public String toString() {
        return "RedisConfig [name=" + name + ", host=" + host + ", port=" + port + ", password=" + password + "]";
    }

}
