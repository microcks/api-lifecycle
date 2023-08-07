package org.acme.order.client;

import org.acme.order.client.model.Pastry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * PastryAPIClient is responsible for requesting the product/stock management system (aka the Pastry registry)
 * using its REST API. It should take care of serializing entities and Http params as required by the 3rd party API.
 * @author laurent
 */
@Component
public class PastryAPIClient {

   RestTemplate restTemplate = new RestTemplate();

   @Value("${pastries.baseUrl}")
   private final String resourceUrl = null;

   public Pastry getPastry(String name) {
      return restTemplate.getForObject(resourceUrl + "/pastries/" + name, Pastry.class);
   }

   public List<Pastry> listPastries(String size) {
      /*
      // At first, I thought that uriVariables were working that way...
      ResponseEntity<List<Pastry>> responseEntity =
            restTemplate.exchange(
                  resourceUrl + "/pastries",
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<Pastry>>() {},
                  Map.of("size", size)
            );
      */

      // ... but then realized it was that way! Thanks to unit test.
      ResponseEntity<List<Pastry>> responseEntity =
            restTemplate.exchange(
                  resourceUrl + "/pastries?size={size}",
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<Pastry>>() {},
                  size
            );
      return responseEntity.getBody();
   }
}
