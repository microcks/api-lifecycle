package org.acme.order.service;

import org.acme.order.service.model.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

   private final OrderService orderService;

   OrderEventListener(OrderService orderService) {
      this.orderService = orderService;
   }

   @KafkaListener(topics = "${application.order-events-reviewed-topic}", groupId = "order-service")
   public void handleReviewedOrder(OrderEvent event) {
      orderService.updateReviewedOrder(event);
   }
}
