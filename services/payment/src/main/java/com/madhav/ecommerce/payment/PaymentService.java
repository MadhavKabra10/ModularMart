package com.madhav.ecommerce.payment;

import com.madhav.ecommerce.notification.NotificationProducer;
import com.madhav.ecommerce.notification.PaymentNotificationRequest;
import com.paypal.api.payments.*;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;
    private final APIContext apiContext;
    public Payment createPayment(
            PaymentRequest request,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(request.currency());
        amount.setTotal(String.format(Locale.forLanguageTag(request.currency()),"%.2f",Double.valueOf(request.amount())));
        Transaction transaction = new Transaction();
        transaction.setDescription(request.description());
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Payer payer = new Payer();
        //If String.valueOf(request.paymentMethod() doesn't work write PAYPAL directly
        payer.setPaymentMethod(String.valueOf(request.paymentMethod()));
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);
        var paymentToSaveInDB = mapper.toPayment(request);
        repository.save(paymentToSaveInDB);
        BigDecimal paymentAmount = new BigDecimal(request.amount());
        notificationProducer.sendNotification(
                new PaymentNotificationRequest(
                        request.orderReference(),
                        paymentAmount,
                        request.paymentMethod(),
                        request.customerResponse().firstname(),
                        request.customerResponse().lastname(),
                        request.customerResponse().email()
                )
        );
        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId)throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext,paymentExecution);
    }
}
