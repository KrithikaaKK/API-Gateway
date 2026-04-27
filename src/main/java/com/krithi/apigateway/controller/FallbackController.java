package com.krithi.apigateway.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/product")
    public ResponseEntity<String> productFallback() {
        return ResponseEntity.internalServerError().body("product service is down");
    }

    @RequestMapping("/fallback/user")
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.ok("User service is temporarily unavailable. Please try again later.");
    }


    @RequestMapping("/fallback/rate-limit")
    public ResponseEntity<String> fallbackForUserService() {
        return new ResponseEntity<>("Too many request", HttpStatus.TOO_MANY_REQUESTS);
    }



}
