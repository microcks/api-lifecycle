package org.acme.order.api;

import org.acme.order.service.OrderService;
import org.acme.order.service.UnavailablePastryException;
import org.acme.order.service.model.Order;
import org.acme.order.service.model.OrderInfo;
import org.acme.order.service.model.UnavailableProduct;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

/**
 * OrderResource is responsible for exposing the REST API for the Order Service. It should take
 * care of serialization, business rules mapping to model types and Http status codes.
 * @author laurent
 */
@Path("/api/orders")
public class OrderResource {
   @Inject
   OrderService service;

   @POST
   public RestResponse<?> order(OrderInfo info) {
      Order createdOrder = null;
      try {
         createdOrder = service.placeOrder(info);
      } catch (UnavailablePastryException upe) {
         // We have to return a 422 (unprocessable) with correct expected type.
         //return ResponseBuilder.create(422).build();
         return ResponseBuilder.create(422)
               .entity(new UnavailableProduct(upe.getProduct(), upe.getMessage()))
               .build();
      } catch (Exception e) {
         return ResponseBuilder.serverError().build();
      }
      // We can return a 201 with created entity.
      return ResponseBuilder.create(Status.CREATED.getStatusCode())
            .entity(createdOrder)
            .build();
   }
}
