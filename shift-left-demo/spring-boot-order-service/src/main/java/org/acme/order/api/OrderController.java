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
         //return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
         return new ResponseEntity<>(
               new UnavailableProduct(upe.getProduct(), upe.getMessage()),
               HttpStatus.UNPROCESSABLE_ENTITY);
      } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
   }
}
