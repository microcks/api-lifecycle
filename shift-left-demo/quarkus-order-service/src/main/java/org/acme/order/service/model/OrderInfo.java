package org.acme.order.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderInfo(String customerId, List<ProductQuantity> productQuantities, Double totalPrice) {
}
