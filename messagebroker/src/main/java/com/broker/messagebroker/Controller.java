// package com.broker.messagebroker;

// import org.springframework.data.redis.core.StringRedisTemplate;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import jakarta.annotation.Resource;
// import lombok.extern.log4j.Log4j2;

// @RestController
// @Log4j2
// public class Controller {

// @Resource
// private StringRedisTemplate stringRedisTemplate;

// @PostMapping("/pushData")
// public void pushAndList(@RequestParam String value) {
// log.debug("Inside Pushing Data to Redis");
// log.debug(stringRedisTemplate.opsForList().leftPush("test", value));

// }

// }
