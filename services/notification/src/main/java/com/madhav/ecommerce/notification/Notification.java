package com.madhav.ecommerce.notification;

import com.madhav.ecommerce.kafka.order.OrderConfirmation;
import com.madhav.ecommerce.kafka.payment.PaymentConfirmation;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

//Interview mai puche ki baki sab ke liye postgres sql database aur Notification ke liye NoSQL kyu
//to bolna ki isme apan do objects include kar rahe hai attributes mai OrderConfirmation and
//PaymentConfirmation aur SQL mai aur usko relational mapping vagaira karna padta jiski as such koi
//requirement nahi hai so isliye NoSQL database ka fayda uthate hue usse hi choose kar liya😎
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {
    @Id
    private String id;
    private NotificationType type;
    private LocalDateTime notificationDate;
    private OrderConfirmation orderConfirmation;
    private PaymentConfirmation paymentConfirmation;
}
