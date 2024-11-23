package com.ecommerce.api.domain.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public record ApiResponse<T>(String message, T result) {

    @ResponseStatus(HttpStatus.OK)
    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>("success", result);
    }
}
