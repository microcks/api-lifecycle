package org.acme.order.api;

import org.acme.order.service.UnavailablePastryException;
import org.acme.order.service.model.Order;
import org.acme.order.service.model.OrderInfo;
import org.acme.order.service.OrderService;
import org.acme.order.service.model.UnavailableProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OrderController is responsible for exposing the REST API for the Order Service. It should take
 * care of serialization, business rules mapping to model types and Http status codes.
 * @author laurent
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

   @Autowired
   OrderService service;

   @PostMapping()
   public ResponseEntity<?> order(@RequestBody OrderInfo info) {
      Order createdOrder = null;
      try {
         createdOrder = service.placeOrder(info);
      } catch (UnavailablePastryException upe) {
         // We have to return a 422 (unprocessable) with correct expected type.
         //return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
         return new ResponseEntity<>(
               new UnavailableProduct(upe.getProduct(), upe.getMessage()),
               HttpStatus.UNPROCESSABLE_ENTITY);
      } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      // We can return a 201 with created entity.
      return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
   }
}
