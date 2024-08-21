package com.madhav.ecommerce.kafka;

import com.madhav.ecommerce.customer.CustomerResponse;
import com.madhav.ecommerce.order.PaymentMethod;
import com.madhav.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
