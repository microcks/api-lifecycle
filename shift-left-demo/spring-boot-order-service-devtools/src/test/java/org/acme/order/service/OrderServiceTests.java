package org.acme.order.service;

import io.github.microcks.testcontainers.model.TestRequest;
import io.github.microcks.testcontainers.model.TestResult;
import io.github.microcks.testcontainers.model.TestRunnerType;

import org.acme.order.BaseIntegrationTest;
import org.acme.order.service.model.Order;
import org.acme.order.service.model.OrderInfo;
import org.acme.order.service.model.ProductQuantity;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.KafkaContainer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

public class OrderServiceTests extends BaseIntegrationTest {

   @Autowired
   KafkaContainer kafkaContainer;

   @Autowired
   OrderService service;

   @Test
   public void testEventIsPublishedWhenOrderIsCreated() {
      ensureTopicExists("orders-created");

      // Prepare a Microcks test.
      TestRequest kafkaTest = new TestRequest.Builder()
            .serviceId("Order Events API:0.1.0")
            .filteredOperations(List.of("SUBSCRIBE orders-created"))
            .runnerType(TestRunnerType.ASYNC_API_SCHEMA.name())
            .testEndpoint("kafka://kafka:19092/orders-created")
            .timeout(2000l)
            .build();

      // Prepare an application Order.
      OrderInfo info = new OrderInfo("123-456-789", List.of(
            new ProductQuantity("Millefeuille", 1),
            new ProductQuantity("Paris-Brest", 1)
      ), 8.4);

      try {
         // Launch the Microcks test and wait a bit to be sure it actually connects to Kafka.
         CompletableFuture<TestResult> testRequestFuture = microcksEnsemble.getMicrocksContainer().testEndpointAsync(kafkaTest);
         await().pollDelay(750, TimeUnit.MILLISECONDS).untilAsserted(() -> assertTrue(true));

         // Stimulate the application to create an order.
         Order createdOrder = service.placeOrder(info);

         // You may check additional stuff on createdOrder...

         // Get the Microcks test result.
         TestResult testResult = testRequestFuture.get();

         // Check success and that we read 1 valid message on the topic.
         assertTrue(testResult.isSuccess());
         assertFalse(testResult.getTestCaseResults().isEmpty());
         assertEquals(1, testResult.getTestCaseResults().get(0).getTestStepResults().size());

         System.err.println(microcksEnsemble.getAsyncMinionContainer().getLogs());
      } catch (Exception e) {
         fail("No exception should be thrown when testing Kafka publication", e);
      }
   }

   private void ensureTopicExists(String topic) {
      Properties properties = new Properties();
      properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
      AdminClient adminClient = AdminClient.create(properties);
      adminClient.createTopics(List.of(new NewTopic(topic, 1, Short.valueOf("1"))));
   }
}
