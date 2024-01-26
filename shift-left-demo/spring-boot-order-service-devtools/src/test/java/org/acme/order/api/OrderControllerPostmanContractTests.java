package org.acme.order.api;

import io.github.microcks.testcontainers.model.TestRequest;
import io.github.microcks.testcontainers.model.TestResult;
import io.github.microcks.testcontainers.model.TestRunnerType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.acme.order.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderControllerPostmanContractTests extends BaseIntegrationTest {

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
      //System.err.println(microcksEnsemble.getMicrocksContainer().getLogs());
      //System.err.println(microcksEnsemble.getPostmanContainer().getLogs());
      ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
      System.err.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(testResult));

      assertTrue(testResult.isSuccess());
      assertEquals(1, testResult.getTestCaseResults().size());
   }
}
