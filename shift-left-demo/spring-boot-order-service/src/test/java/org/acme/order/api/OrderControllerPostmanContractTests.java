package org.acme.order.api;

import io.github.microcks.testcontainers.MicrocksContainersEnsemble;
import io.github.microcks.testcontainers.model.TestRequest;
import io.github.microcks.testcontainers.model.TestResult;
import io.github.microcks.testcontainers.model.TestRunnerType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.Testcontainers;
import org.testcontainers.junit.jupiter.Container;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@org.testcontainers.junit.jupiter.Testcontainers
public class OrderControllerPostmanContractTests {

   @Container
   public static MicrocksContainersEnsemble microcksEnsemble = new MicrocksContainersEnsemble("quay.io/microcks/microcks-uber:1.8.1")
         .withAccessToHost(true)
         .withPostman();

   @LocalServerPort
   private Integer port;

   @BeforeAll
   public static void setup() throws Exception {
      // Load the System Under Test OpenAPI contract into Microcks.
      microcksEnsemble.getMicrocksContainer().importAsMainArtifact(new File("target/classes/order-service-openapi.yaml"));
      microcksEnsemble.getMicrocksContainer().importAsSecondaryArtifact(new File("target/classes/order-service-postman-collection.json"));
      // Load also the third-party ones so that RestTemplate will also use Microcks mocks 👻
      microcksEnsemble.getMicrocksContainer().importAsMainArtifact(new File("target/test-classes/third-parties/apipastries-openapi.yaml"));
      microcksEnsemble.getMicrocksContainer().importAsSecondaryArtifact(new File("target/test-classes/third-parties/apipastries-postman-collection.json"));
   }

   @BeforeEach
   public void setupPort() {
      // Host port exposition should be done here.
      Testcontainers.exposeHostPorts(port);
   }

   @DynamicPropertySource
   static void configureProperties(DynamicPropertyRegistry registry) {
      String url = microcksEnsemble.getMicrocksContainer().getRestMockEndpoint("API Pastries", "0.0.1");
      registry.add("pastries.baseUrl", () -> url);
   }

   @Test
   public void testPostmanCollectionContract() throws Exception {
      // Ask for a Postman Collection script conformance to be launched.
      TestRequest testRequest = new TestRequest.Builder()
            .serviceId("Order Service API:0.1.0")
            .runnerType(TestRunnerType.POSTMAN.name())
            .testEndpoint("http://host.testcontainers.internal:" + port + "/api")
            .build();

      TestResult testResult = microcksEnsemble.getMicrocksContainer().testEndpoint(testRequest);

      // You may inspect complete response object with following:
      System.err.println(microcksEnsemble.getMicrocksContainer().getLogs());
      System.err.println(microcksEnsemble.getPostmanContainer().getLogs());
      ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
      System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(testResult));

      assertTrue(testResult.isSuccess());
      assertEquals(1, testResult.getTestCaseResults().size());
   }
}
