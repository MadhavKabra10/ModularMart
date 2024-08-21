package com.madhav.ecommerce.payment;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService service;
    @PostMapping("/create")
    public String createPayment(
            @RequestBody @Valid PaymentRequest request)
    {
        try {
            String cancelUrl = "http://localhost:8060/api/v1/payments/cancel";
            String successUrl = "http://localhost:8060/api/v1/payments/success";
            Payment payment = service.createPayment(request, cancelUrl, successUrl);
            for (Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    System.out.println(links.getHref());
                    return "Payment created!";
                }
            }
        }
        catch (PayPalRESTException e) {
            log.error("PayPalRest Error occurred :: " + e);
        }
        return "error";
    }
    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ){
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "Payment Success!";
            }
        }catch (PayPalRESTException e){
            log.error("PayPalRest Error occurred :: " + e);
        }
        return "Payment error!";
    }
    @GetMapping("/error")
    public String paymentError(){
        return "Payment error!";
    }
    @GetMapping("/cancel")
    public String paymentCancel(){
        return "Payment canceled!";
    }
}
