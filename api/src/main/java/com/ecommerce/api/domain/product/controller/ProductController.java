package com.ecommerce.api.domain.product.controller;

import com.ecommerce.api.domain.api.ApiResponse;
import com.ecommerce.api.domain.product.dto.ProductResponse;
import com.ecommerce.api.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> findOneProduct(@PathVariable("productId") String productId) {
        ProductResponse productResponse = ProductResponse.from(productService.findOneProduct(productId));
        return ApiResponse.success(productResponse);
    }

    @GetMapping("")
    public ApiResponse<Page<ProductResponse>> findAllProduct(
            @PageableDefault(page = 0, size = 10, sort = "productId", direction = Sort.Direction.ASC)
            Pageable pageable) {
        Page<ProductResponse> productResponses = productService.findAllProduct(pageable)
                .map(ProductResponse::from);
        return ApiResponse.success(productResponses);
    }
}
