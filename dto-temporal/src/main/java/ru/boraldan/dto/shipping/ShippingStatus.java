package ru.boraldan.dto.shipping;

import lombok.Getter;

@Getter
public enum ShippingStatus {
    PENDING("ShippingPending.v1"),
    SCHEDULED("ShippingScheduled.v1"),
    SHIPPED("ShippingShipped.v1"),
    DELIVERED("ShippingDelivered.v1"),
    CANCELLED("ShippingCancelled.v1"),
    FAILURE("ShippingFailure.v1");

    private final String type;

    ShippingStatus(String type) {
        this.type = type;
    }
}
