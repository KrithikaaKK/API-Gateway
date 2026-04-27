package com.krithi.apigateway.config;

import com.krithi.apigateway.model.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private static List<String> EXCLUDED_ROUTES = List.of("/user-service/user/register","/user-service/user/login");
    private final WebClient.Builder webBuilder;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request =  exchange.getRequest();
        if(EXCLUDED_ROUTES.contains(request.getURI().getPath())){
            return chain.filter(exchange); //bypass those
        }
        String token = request.getHeaders().getFirst("Authorization");
        if(token==null || !token.contains("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(HttpStatus.SC_UNAUTHORIZED));
            return exchange.getResponse().setComplete(); //tell its complete to mono
        }
        String bearerToken = token.substring(7);

        return webBuilder.build()
                .post()
                .uri("lb://USER-SERVICE/user/validate")
                .headers(h -> h.setBearerAuth(bearerToken)) // to remove the bearer cause it get added while setting header
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(
                                        new RuntimeException("user service error :" + resp.statusCode()+body)
                                )))
                .bodyToMono(TokenValidationResponse.class)
                .flatMap(res -> {
                    if(Boolean.TRUE.equals(res.getIsValid())){
                        request.mutate()
                                .header("username",res.getUsername())
                                .build();
                        return chain.filter(exchange);
                    }
                    else {
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatusCode.valueOf(HttpStatus.SC_UNAUTHORIZED));
                        return response.setComplete();

                    }
                });



    }

    @Override
    public int getOrder() {
        return -1; // filter with lowest number run early
    }
}
