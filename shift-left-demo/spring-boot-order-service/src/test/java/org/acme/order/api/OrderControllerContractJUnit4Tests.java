package org.acme.order.api;

import io.github.microcks.testcontainers.MicrocksContainer;
import io.github.microcks.testcontainers.model.TestRequest;
import io.github.microcks.testcontainers.model.TestResult;
import io.github.microcks.testcontainers.model.TestRunnerType;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.Testcontainers;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerContractJUnit4Tests {

   @ClassRule
   public static MicrocksContainer microcksContainer = new MicrocksContainer("quay.io/microcks/microcks-uber:nightly")
         .withAccessToHost(true);

   @LocalServerPort
   private Integer port;

   @Before
   public void setup() throws Exception {
      Testcontainers.exposeHostPorts(port);
      // Load the System Under Test OpenAPI contract into Microcks.
      microcksContainer.importAsMainArtifact(new File("target/classes/order-service-openapi.yaml"));
      // Load also the third-party ones so that RestTemplate will also use Microcks mocks 👻
      microcksContainer.importAsMainArtifact(new File("target/test-classes/third-parties/apipastries-openapi.yaml"));
      microcksContainer.importAsSecondaryArtifact(new File("target/test-classes/third-parties/apipastries-postman-collection.json"));
   }

   @DynamicPropertySource
   static void configureProperties(DynamicPropertyRegistry registry) {
      String url = microcksContainer.getRestMockEndpoint("API Pastries", "0.0.1");
      registry.add("pastries.baseUrl", () -> url);
   }

   @Test
   public void testOpenAPIContract() throws Exception {
      // Ask for an Open API conformance to be launched.
      TestRequest testRequest = new TestRequest.Builder()
            .serviceId("Order Service API:0.1.0")
            .runnerType(TestRunnerType.OPEN_API_SCHEMA.name())
            .testEndpoint("http://host.testcontainers.internal:" + port + "/api")
            .build();

      TestResult testResult = microcksContainer.testEndpoint(testRequest);

      /*
      // You may inspect complete response object with following:
      ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
      System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(testResult));
      */

      assertTrue(testResult.isSuccess());
      assertEquals(1, testResult.getTestCaseResults().size());
   }
}
