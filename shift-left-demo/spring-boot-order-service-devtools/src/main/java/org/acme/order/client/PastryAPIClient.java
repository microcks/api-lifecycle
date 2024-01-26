package org.acme.order.client;

import org.acme.order.client.model.Pastry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * PastryAPIClient is responsible for requesting the product/stock management system (aka the Pastry registry)
 * using its REST API. It should take care of serializing entities and Http params as required by the 3rd party API.
 * @author laurent
 */
@Component
public class PastryAPIClient {
   @Autowired
   @Qualifier("pastryRestClient")
   RestClient restClient;

   public Pastry getPastry(String name) {
      return restClient.get().uri("/pastries/{name}", name)
            .retrieve()
            .body(Pastry.class);
   }

   public List<Pastry> listPastries(String size) {
      return restClient.get().uri("/pastries?size=" + size)
            .retrieve()
            .body(new ParameterizedTypeReference<List<Pastry>>() {});
   }
}
