package com.madhav.ecommerce.payment;

public record PaymentRequest(
        String amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customerResponse,
        String description,
        String currency
) {
}
