package org.acme.order.service.model;

import java.util.List;
import java.util.UUID;

public class Order {

   private String id;
   private OrderStatus status;
   private String customerId;
   private List<ProductQuantity> productQuantities;
   private Double totalPrice;


   public Order() {
      this.id = String.valueOf(UUID.randomUUID());
      this.status = OrderStatus.CREATED;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public OrderStatus getStatus() {
      return status;
   }

   public void setStatus(OrderStatus status) {
      this.status = status;
   }

   public String getCustomerId() {
      return customerId;
   }

   public void setCustomerId(String customerId) {
      this.customerId = customerId;
   }

   public List<ProductQuantity> getProductQuantities() {
      return productQuantities;
   }

   public void setProductQuantities(List<ProductQuantity> productQuantities) {
      this.productQuantities = productQuantities;
   }

   public Double getTotalPrice() {
      return totalPrice;
   }

   public void setTotalPrice(Double expectedPrice) {
      this.totalPrice = expectedPrice;
   }
}
