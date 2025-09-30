package ru.boraldan.dto.payment.payload;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentPendingPayload   (
        UUID orderId,
        Long paymentId,
        BigDecimal amount) {
}
