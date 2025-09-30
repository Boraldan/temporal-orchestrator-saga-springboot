package ru.boraldan.dto.payment.payload;


import java.util.UUID;

public record PaymentFailedPayload(
        UUID orderId,
        Long paymentId,
        String reason
) {
}
