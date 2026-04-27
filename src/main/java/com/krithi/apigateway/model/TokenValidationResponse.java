package com.krithi.apigateway.model;

import lombok.Data;

@Data
public class TokenValidationResponse {
    String username;
    Boolean isValid;
}