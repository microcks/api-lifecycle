package org.acme.order.client;

import io.github.microcks.testcontainers.MicrocksContainer;

import org.acme.order.client.model.Pastry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class PastryAPIClientTests {

   @Container
   public static MicrocksContainer microcksContainer = new MicrocksContainer("quay.io/microcks/microcks-uber:1.8.1");

   @Autowired
   PastryAPIClient client;

   @BeforeAll
   public static void setup() throws Exception {
      microcksContainer.importAsMainArtifact(new File("target/test-classes/third-parties/apipastries-openapi.yaml"));
      microcksContainer.importAsSecondaryArtifact(new File("target/test-classes/third-parties/apipastries-postman-collection.json"));
   }

   @DynamicPropertySource
   static void configureProperties(DynamicPropertyRegistry registry) {
      String url = microcksContainer.getRestMockEndpoint("API Pastries", "0.0.1");
      registry.add("pastries.baseUrl", () -> url);
   }

   @Test
   public void testGetPastries() {
      // Test our API client and check that arguments and responses are correctly serialized.
      List<Pastry> pastries = client.listPastries("S");
      assertEquals(1, pastries.size());

      pastries = client.listPastries("M");
      assertEquals(2, pastries.size());

      pastries = client.listPastries("L");
      assertEquals(2, pastries.size());
   }

   @Test
   public void testGetPastry() {
      // Test our API client and check that arguments and responses are correctly serialized.
      Pastry pastry = client.getPastry("Millefeuille");
      assertEquals("Millefeuille", pastry.name());
      assertEquals("available", pastry.status());

      pastry = client.getPastry("Eclair Cafe");
      assertEquals("Eclair Cafe", pastry.name());
      assertEquals("available", pastry.status());

      pastry = client.getPastry("Eclair Chocolat");
      assertEquals("Eclair Chocolat", pastry.name());
      assertEquals("unknown", pastry.status());
   }
}
