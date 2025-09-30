package ru.boraldan.dto.order;

import lombok.Getter;

@Getter
public enum OrderStatus {

    NEW("OrderNew.v1"),
    CREATED("OrderCreated.v1"),
    PAID("OrderPaid.v1"),
    COMPLETED("OrderCompleted.v1"),
    CANCELLED("OrderCancelled.v1");

    private final String type;

    OrderStatus(String type) {
        this.type = type;
    }
}