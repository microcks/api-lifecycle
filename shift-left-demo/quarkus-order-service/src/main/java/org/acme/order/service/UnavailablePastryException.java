package org.acme.order.service;

public class UnavailablePastryException extends Exception {

   private String product;

   public UnavailablePastryException(String product, String message) {
      super(message);
      this.product = product;
   }

   public String getProduct() {
      return this.product;
   }
}
