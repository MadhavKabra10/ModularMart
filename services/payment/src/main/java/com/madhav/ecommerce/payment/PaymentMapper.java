package com.madhav.ecommerce.payment;

import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public Payment toPayment(PaymentRequest request)
    {
        return Payment.builder()
                .currency(request.currency())
                .description(request.description())
                .orderId(request.orderId())
                .paymentMethod(request.paymentMethod())
                .amount(request.amount())
                .build();
    }
}
