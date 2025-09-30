package ru.boraldan.dto.order.payload;




import java.math.BigDecimal;
import java.util.UUID;

public record OrderPaidPayload(

     UUID orderId,
     String customerId,
     BigDecimal amount
){}
