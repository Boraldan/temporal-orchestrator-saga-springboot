package ru.boraldan.dto.payment;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("PaymentPending.v1"),
    COMPLETED("PaymentCompleted.v1"),
    FAILED("PaymentFailed.v1"),
    REFUNDED("PaymentRefunded.v1");

    private final String type;

    PaymentStatus(String type) {
        this.type = type;
    }
}