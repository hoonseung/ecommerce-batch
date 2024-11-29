package com.ecommerce.batch.domain.product.report.entity.id;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BrandReportId implements Serializable {


    private LocalDate statDate;
    private String brand;
}
