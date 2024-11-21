package com.ecommerce.api.domain.product;

import com.ecommerce.api.domain.product.entity.Product;
import com.ecommerce.api.domain.product.exeption.InsufficientStockQuantityException;
import com.ecommerce.api.domain.product.exeption.InvalidStockQuantityException;
import com.ecommerce.api.domain.product.exeption.StockQuantityArithmeticException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.of(
                "PRODUCT-1",
                1L,
                "Electronics",
                "SampleProduct",
                now().toLocalDate(),
                now().toLocalDate(),
                ProductStatus.AVAILABLE,
                "brand",
                "manufacturer",
                10,
                100,
                now(),
                now()
        );
    }

    @Test
    void testIncreaseStockQuantity() {
        product.increaseStockQuantity(50);
        assertThat(product.getStockQuantity()).isEqualTo(150);
    }

    @Test
    void testIncreaseStockQuantity_GivenNegativeValue_WhenIncreaseStockQuantity_ThenReturnThrowEx() {
        assertThatThrownBy(() -> product.increaseStockQuantity(Integer.MAX_VALUE))
                .isExactlyInstanceOf(StockQuantityArithmeticException.class);
    }

    @ValueSource(ints = {-1, -100, 0})
    @ParameterizedTest
    void testIncreaseStockQuantity_GivenInvalidValue_ThenReturnThrowEx(int negativeStockQuantity) {
        assertThatThrownBy(() -> product.increaseStockQuantity(negativeStockQuantity))
                .isExactlyInstanceOf(InvalidStockQuantityException.class);
    }

    @Test
    void testDecreaseStockQuantity() {
        product.decreaseStockQuantity(50);
        assertThat(product.getStockQuantity()).isEqualTo(50);
    }

    @Test
    void testDecreaseStockQuantity_GivenMoreThenCurrentStockQuantity_WhenDecreaseStockQuantity_ThenReturnThrowEx() {
        assertThatThrownBy(() -> product.decreaseStockQuantity(101))
                .isExactlyInstanceOf(InsufficientStockQuantityException.class);
    }

    @ValueSource(ints = {-1, -100, 0})
    @ParameterizedTest
    void testDecreaseStockQuantity_GivenInvalidValue_ThenReturnThrowEx(int negativeStockQuantity) {
        assertThatThrownBy(() -> product.decreaseStockQuantity(negativeStockQuantity))
                .isExactlyInstanceOf(InvalidStockQuantityException.class);
    }


}