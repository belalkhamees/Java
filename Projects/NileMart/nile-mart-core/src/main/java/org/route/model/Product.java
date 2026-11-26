package org.route.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Product {

    @EqualsAndHashCode.Include
    private String sku;
    private String name;
    private BigDecimal price;
    private Category category;
    @Setter private int stockQuantity;

}
