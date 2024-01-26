package org.acme.order.service;

import org.acme.order.ApplicationProperties;
import org.acme.order.service.model.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

   private final KafkaTemplate<String, Object> kafkaTemplate;
   private final ApplicationProperties properties;

   public OrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate, ApplicationProperties properties) {
      this.kafkaTemplate = kafkaTemplate;
      this.properties = properties;
   }

   public void publishOrderCreated(OrderEvent event) {
      kafkaTemplate.send(properties.orderEventsCreatedTopic(), event);
   }
}
