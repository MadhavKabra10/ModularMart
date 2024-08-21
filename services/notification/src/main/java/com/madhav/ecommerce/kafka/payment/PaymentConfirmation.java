package com.madhav.ecommerce.kafka.payment;

import java.math.BigDecimal;

public record PaymentConfirmation(
        String orderReference,
        BigDecimal amount,
        String customerFirstname,
        String customerLastname,
        String customerEmail
) {
}
