package ru.boraldan.dto.payment.payload;


import java.math.BigDecimal;
import java.util.UUID;


public record PaymentCompletedPayload (
     UUID orderId,
     Long paymentId,
     BigDecimal amount
){
}
