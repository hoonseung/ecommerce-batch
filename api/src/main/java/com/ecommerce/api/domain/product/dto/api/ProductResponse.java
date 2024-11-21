package com.ecommerce.api.domain.product.dto.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public record ProductResponse<T>(String message, T result) {

    @ResponseStatus(HttpStatus.OK)
    public static <T> ProductResponse<T> success(T result) {
        return new ProductResponse<>("success", result);
    }
}
