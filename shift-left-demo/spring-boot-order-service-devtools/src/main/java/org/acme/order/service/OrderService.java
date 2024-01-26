package org.acme.order.service;

import org.acme.order.client.PastryAPIClient;
import org.acme.order.client.model.Pastry;
import org.acme.order.service.model.Order;
import org.acme.order.service.model.OrderEvent;
import org.acme.order.service.model.OrderInfo;
import org.acme.order.service.model.OrderStatus;
import org.acme.order.service.model.ProductQuantity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * OrderService is responsible for checking business rules/constraints on Orders.
 * @author laurent
 */
@Service
public class OrderService {

   // This is a dumb implementation of an event sourcing repository. Don't use this in production!
   private final Map<String, List<OrderEvent>> orderEventsRepository = new HashMap<>();

   private final PastryAPIClient pastryRepository;

   private final OrderEventPublisher eventPublisher;

   OrderService(PastryAPIClient pastryRepository, OrderEventPublisher eventPublisher) {
      this.pastryRepository = pastryRepository;
      this.eventPublisher = eventPublisher;
   }

   /**
    * This method will check that an Order can be actually placed and persisted. A full implementation
    * will probably check stocks, customer loyalty, payment methods, shipping details, etc... For sake
    * of simplicity, we'll just check that products (here pastries) are all available.
    * @param info The order information.
    * @return A created Order with incoming info, new unique identifier and created status.
    * @throws UnavailablePastryException
    * @throws Exception
    */
   public Order placeOrder(OrderInfo info) throws UnavailablePastryException, Exception {
      // For all products in order, check the availability calling the Pastry API.
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

      // Everything is available! Create a new order.
      Order result = new Order();
      result.setCustomerId(info.customerId());
      result.setProductQuantities(info.productQuantities());
      result.setTotalPrice(info.totalPrice());

      // Persist and publish creation event.
      OrderEvent orderCreated = new OrderEvent(System.currentTimeMillis(), result, "Creation");
      persistOrderEvent(orderCreated);
      eventPublisher.publishOrderCreated(orderCreated);

      return result;
   }

   /**
    *
    * @param reviewedOrderEvent
    */
   public void updateReviewedOrder(OrderEvent reviewedOrderEvent) {
      persistOrderEvent(reviewedOrderEvent);
   }

   /**
    *
    * @param id
    * @return
    */
   public Order getOrder(String id) throws OrderNotFoundException {
      List<OrderEvent> orderEvents = orderEventsRepository.get(id);
      if (orderEvents != null) {
         return orderEvents.get(orderEvents.size() - 1).order();
      }
      throw new OrderNotFoundException(id);
   }

   /**
    *
    * @param id
    * @return
    * @throws OrderNotFoundException
    */
   public List<OrderEvent> getOrderEvents(String id) throws OrderNotFoundException {
      List<OrderEvent> orderEvents = orderEventsRepository.get(id);
      if (orderEvents == null) {
         throw new OrderNotFoundException(id);
      }
      return orderEvents;
   }

   private CompletableFuture<Boolean> checkPastryAvailability(String pastryName) {
      try {
         Pastry pastry = pastryRepository.getPastry(pastryName);
         return CompletableFuture.completedFuture("available".equals(pastry.status()));
      } catch (Exception e) {
         return CompletableFuture.completedFuture(false);
      }
   }

   private void persistOrderEvent(OrderEvent event) {
      List<OrderEvent> orderEvents = orderEventsRepository.get(event.order().getId());
      if (orderEvents == null) {
         orderEvents = new ArrayList<>();
      }
      orderEvents.add(event);
      orderEventsRepository.put(event.order().getId(), orderEvents);
   }
}
