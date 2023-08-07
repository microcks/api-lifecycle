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

/**
 * OrderService is responsible for checking business rules/constraints on Orders.
 * @author laurent
 */
@Service
public class OrderService {

   @Autowired
   PastryAPIClient pastryRepository;

   /**
    * This method will check that an Order can be actually placed and persisted. A full implementation
    * will probably check stocks, customer loyalty, payment methods, shipping details, etc... For sake
    * of simplicity, we'll just check that products (here pastries) are all available.
    * @param info The order informations.
    * @return
    * @throws UnavailablePastryException
    * @throws Exception
    */
   public Order placeOrder(OrderInfo info) throws UnavailablePastryException, Exception {
      // For all products in order, check the avaibility calling the Pastry API.
      Map<CompletableFuture<Boolean>, String> availabilityFutures = new HashMap<>();
      for (ProductQuantity productQuantity : info.productQuantities()) {
         availabilityFutures.put(checkPastryAvailability(productQuantity.productName()), productQuantity.productName());
      }

      // Wait for all completable future to finish.
      CompletableFuture.allOf(availabilityFutures.keySet().toArray(new CompletableFuture[0])).join();

      try {
         // If one pastry is marked as unavailable, throw a business exception.
         for (CompletableFuture<Boolean> availabilityFuture : availabilityFutures.keySet()) {
            if (!availabilityFuture.get()) {
               String pastryName = availabilityFutures.get(availabilityFuture);
               throw new UnavailablePastryException(pastryName, "Pastry " + pastryName + " is not available");
            }
         }
      } catch (InterruptedException | ExecutionException e) {
         throw new Exception("Unexpected exception: " + e.getMessage());
      }

      // Everything is available! Create (and probably persist ;-) a new order.
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
