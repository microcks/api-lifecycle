package org.acme.order.service.model;

public record OrderEvent(long timestamp, Order order, String changeReason) {}
