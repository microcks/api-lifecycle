package org.acme.order.service;

import org.acme.order.BaseIntegrationTest;
import org.acme.order.service.model.Order;
import org.acme.order.service.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.awaitility.core.ConditionTimeoutException;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

public class OrderEventListenerTests extends BaseIntegrationTest {

   @Autowired
   OrderService service;

   @Test
   public void testEventIsConsumedAndProcessedByService() throws Exception {
      try {
         await().atMost(4, TimeUnit.SECONDS)
               .pollDelay(400, TimeUnit.MILLISECONDS)
               .pollInterval(400, TimeUnit.MILLISECONDS)
               .until(() -> {
                  try {
                     Order order = service.getOrder("123-456-789");
                     assertEquals("lbroudoux", order.getCustomerId());
                     assertEquals(OrderStatus.VALIDATED, order.getStatus());
                     assertEquals(2, order.getProductQuantities().size());
                     return true;
                  } catch (OrderNotFoundException onfe) {
                     // Continue until ConditionTimeoutException.
                  }
                  return false;
               });
      } catch (ConditionTimeoutException timeoutException) {
         fail("The expected Order was not received/processed in expected delay");
      }
   }
}
