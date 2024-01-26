package org.acme.order.service.model;

public class UnavailableProduct {
   private String productName;
   private String details;

   public UnavailableProduct(String productName, String details) {
      this.productName = productName;
      this.details = details;
   }

   public String getProductName() {
      return productName;
   }

   public String getDetails() {
      return details;
   }
}
