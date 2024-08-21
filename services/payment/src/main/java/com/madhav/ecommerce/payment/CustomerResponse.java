package com.madhav.ecommerce.payment;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email
) {
}
