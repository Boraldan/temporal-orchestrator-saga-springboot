package ru.boraldan.dto.shipping.payload;


import com.fasterxml.jackson.annotation.JsonTypeName;
import ru.boraldan.dto.shipping.ShippingPayload;

import java.util.UUID;

@JsonTypeName("ShippingScheduled.v1")
public record ShippingScheduledPayload(UUID orderId, Long shippingId, String address) implements ShippingPayload {
}
