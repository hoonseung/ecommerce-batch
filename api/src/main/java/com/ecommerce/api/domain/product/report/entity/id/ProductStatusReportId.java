package com.ecommerce.api.domain.product.report.entity.id;

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
    private String productStatus;
}
