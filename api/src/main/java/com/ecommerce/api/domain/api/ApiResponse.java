package com.ecommerce.api.domain.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


public record ApiResponse<T>(
        String message,
        @JsonInclude(NON_NULL)
        T result) {

    @ResponseStatus(HttpStatus.OK)
    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>("success", result);
    }
}
