package com.madhav.ecommerce.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

@FeignClient(
        name = "payment-service",
        url = "http://localhost:8222/api/v1/payments"
)
public interface PaymentClient {
    @PostMapping("/create")
    public Integer requestOrderPayment(PaymentRequest request);
}
