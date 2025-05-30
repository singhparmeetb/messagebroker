package com.broker.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;

public class RedisConfig {

    private String name;
    private String host;
    private int port;
    private String userName;
    private String password;
    private GenericObjectPoolConfig genericObjectPoolConfig;

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
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

    public void setUserName(String userName) {
        this.userName = userName;
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

}
