package ru.boraldan.dto.shipping.payload;


import com.fasterxml.jackson.annotation.JsonTypeName;
import ru.boraldan.dto.shipping.ShippingPayload;
import ru.boraldan.dto.shipping.ShippingStatus;

import java.util.UUID;
@JsonTypeName("ShippingCancelled.v1")
public record ShippingCancelledPayload(UUID orderId, Long shippingId, String reason) implements ShippingPayload {
}
