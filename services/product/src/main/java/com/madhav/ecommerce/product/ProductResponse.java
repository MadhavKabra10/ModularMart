package com.madhav.ecommerce.product;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String name,
        String description,
        double availableQuantity,
        BigDecimal price,
        Integer CategoryId,
        String categoryName,
        String categoryDescription
) {
}
