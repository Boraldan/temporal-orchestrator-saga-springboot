package ru.boraldan.dto.order.payload;


import java.util.UUID;

public record OrderCancelledPayload(
        UUID orderId,
        String customerId,
        String reason
) {
}
