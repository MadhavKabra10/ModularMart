package com.madhav.ecommerce.payment;

import com.madhav.ecommerce.customer.CustomerResponse;
import com.madhav.ecommerce.order.PaymentMethod;

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
