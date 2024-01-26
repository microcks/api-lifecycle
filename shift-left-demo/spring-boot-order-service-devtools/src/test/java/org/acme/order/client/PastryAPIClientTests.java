package org.acme.order.client;

import org.acme.order.BaseIntegrationTest;
import org.acme.order.client.model.Pastry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PastryAPIClientTests extends BaseIntegrationTest {

   @Autowired
   PastryAPIClient client;

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
