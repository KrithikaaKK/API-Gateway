package com.krithi.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
//        return new LettuceConnectionFactory();
//    }
//
//    @Bean
//    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(
//            ReactiveRedisConnectionFactory factory) {
//
//        return new ReactiveRedisTemplate<>(factory, RedisSerializationContext.string());
//    }
//}
