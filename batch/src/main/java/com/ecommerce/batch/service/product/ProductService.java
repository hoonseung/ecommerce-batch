package com.ecommerce.batch.service.product;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class ProductService {

    private final JdbcTemplate jdbcTemplate;

    public ProductService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public Long getCountsForProduct() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products", Long.class);
    }
}
