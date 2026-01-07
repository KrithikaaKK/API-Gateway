package com.krithi.apigateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebclientConfig {


    private static final Logger logger = LoggerFactory.getLogger(WebclientConfig.class);

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes().build();
    }


    @Bean
    @LoadBalanced
    public WebClient.Builder lbWebClient(){
        return WebClient.builder();
    }

}
