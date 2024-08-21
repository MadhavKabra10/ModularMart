package com.madhav.ecommerce.order;

import com.madhav.ecommerce.customer.CustomerClient;
import com.madhav.ecommerce.customer.CustomerResponse;
import com.madhav.ecommerce.exception.BusinessException;
import com.madhav.ecommerce.kafka.OrderConfirmation;
import com.madhav.ecommerce.kafka.OrderProducer;
import com.madhav.ecommerce.orderline.OrderLineRequest;
import com.madhav.ecommerce.orderline.OrderLineService;
import com.madhav.ecommerce.payment.PaymentClient;
import com.madhav.ecommerce.payment.PaymentRequest;
import com.madhav.ecommerce.product.ProductClient;
import com.madhav.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;
    public Integer createOrder(OrderRequest request) {
        //check the customer -> OpenFeign
        CustomerResponse customer = this.customerClient.findCustomerById(request.customerId()).orElseThrow(() -> new BusinessException("Cannot create order!No customer exists with provided ID  " + request.customerId()));
        //purchase the products -> product-service using Rest Template
        var purchasedProducts = this.productClient.purchaseProducts(request.products());
        //persist order
        var order = this.repository.save(mapper.toOrder(request));
        //persist order lines
        for(PurchaseRequest purchaseRequest : request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        //start the payment process
        var paymentRequest = new PaymentRequest(
                String.valueOf(request.amount()),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer,
                "Description",
                "USD"
        );
        try {
            paymentClient.requestOrderPayment(paymentRequest);
        }catch (Exception e){
            System.out.println("Exception aagyo bhaiaya");
        }
        //send the order confirmation -> notification-microservice(kafka broker)
        orderProducer.sendOrderConfirmationMessage(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId)
                .map(mapper::toOrderResponse).orElseThrow(() -> new EntityNotFoundException("No order found with the provided ID: " + orderId));
    }
}
