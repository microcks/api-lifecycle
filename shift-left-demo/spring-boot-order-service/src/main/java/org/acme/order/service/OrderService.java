package org.acme.order.service;

import org.acme.order.client.PastryAPIClient;
import org.acme.order.client.model.Pastry;
import org.acme.order.service.model.Order;
import org.acme.order.service.model.OrderInfo;
import org.acme.order.service.model.ProductQuantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class OrderService {

   @Autowired
   PastryAPIClient pastryRepository;

   public Order placeOrder(OrderInfo info) throws UnavailablePastryException, Exception {
      Map<CompletableFuture<Boolean>, String> availabilityFutures = new HashMap<>();
      for (ProductQuantity productQuantity : info.productQuantities()) {
         availabilityFutures.put(checkPastryAvailability(productQuantity.productName()), productQuantity.productName());
      }

      // Wait for all completable future to finish.
      CompletableFuture.allOf(availabilityFutures.keySet().toArray(new CompletableFuture[0])).join();

      try {
         for (CompletableFuture<Boolean> availabilityFuture : availabilityFutures.keySet()) {
            if (!availabilityFuture.get()) {
               String pastryName = availabilityFutures.get(availabilityFuture);
               throw new UnavailablePastryException(pastryName, "Pastry " + pastryName + " is not available");
            }
         }
      } catch (InterruptedException | ExecutionException e) {
         throw new Exception("Unexpected exception: " + e.getMessage());
      }

      Order result = new Order();
      result.setCustomerId(info.customerId());
      result.setProductQuantities(info.productQuantities());
      result.setTotalPrice(info.totalPrice());
      return result;
   }

   private CompletableFuture<Boolean> checkPastryAvailability(String pastryName) {
      try {
         Pastry pastry = pastryRepository.getPastry(pastryName);
         return CompletableFuture.completedFuture("available".equals(pastry.status()));
      } catch (Exception e) {
         return CompletableFuture.completedFuture(false);
      }
   }
}
