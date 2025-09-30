package ru.boraldan.dto.order.payload;


import lombok.Builder;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderNewPayload(
        UUID orderId,
        String customerId,
        BigDecimal amount
) {
}
