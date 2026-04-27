package com.krithi.apigateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GeneralController {
    private final ReactiveStringRedisTemplate redisTemplate;
    private final ReactiveRedisConnectionFactory connectionFactory;


    @PostMapping("/get")
    public String test(){
        return "hai";
    }



    @GetMapping("/test-redis")
    public Mono<String> testRedis() {
        return redisTemplate.opsForValue()
                .set("test-key", "test-value")
                .then(redisTemplate.opsForValue().get("test-key"))
                .map(value -> "Redis is working! Value: " + value)
                .defaultIfEmpty("Redis connection failed");
    }

    @GetMapping("/check-rate-limit-keys")
    public Flux<String> checkKeys() {
        return redisTemplate.keys("request_rate_limiter*")
                .map(key -> "Found key: " + key);
    }


    @GetMapping("/manual-rate-limit-test")
    public Mono<String> manualTest() {
        String key = "request_rate_limiter.127.0.0.1.tokens";

        return redisTemplate.opsForValue()
                .set(key, "3", Duration.ofMinutes(1))
                .then(redisTemplate.opsForValue().get(key))
                .map(value -> "Manually created key: " + key + ", value: " + value);
    }

    @GetMapping("/redis-info")
    public Mono<Map<String, Object>> redisInfo() {
        return connectionFactory.getReactiveConnection()
                .serverCommands()
                .info("keyspace")
                .map(props -> {
                    Map<String, Object> info = new HashMap<>();
                    info.put("keyspace", props.toString());
                    return info;
                })
                .doFinally(signal ->
                        connectionFactory.getReactiveConnection().closeLater().subscribe()
                );
    }
}
