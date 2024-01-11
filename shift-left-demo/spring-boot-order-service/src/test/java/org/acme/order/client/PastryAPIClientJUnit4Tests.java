package org.acme.order.client;

import io.github.microcks.testcontainers.MicrocksContainer;

import org.acme.order.client.model.Pastry;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PastryAPIClientJUnit4Tests {

   @ClassRule
   public static MicrocksContainer microcksContainer = new MicrocksContainer("quay.io/microcks/microcks-uber:1.8.1");

   @Autowired
   PastryAPIClient client;

   @Before
   public void setup() throws Exception {
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
   }
}
