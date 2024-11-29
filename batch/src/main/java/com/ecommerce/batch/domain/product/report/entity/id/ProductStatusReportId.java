package com.ecommerce.batch.domain.product.report.entity.id;

import com.ecommerce.batch.domain.product.ProductStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatusReportId implements Serializable {


    private LocalDate statDate;
    private ProductStatus productStatus;
}
