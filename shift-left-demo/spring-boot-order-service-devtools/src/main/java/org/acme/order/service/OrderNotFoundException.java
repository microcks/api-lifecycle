package org.acme.order.service;

public class OrderNotFoundException extends Exception {

   public String orderId;

   public OrderNotFoundException(String orderId) {
      super();
      this.orderId = orderId;
   }

   public String getOrderId() {
      return orderId;
   }
}
