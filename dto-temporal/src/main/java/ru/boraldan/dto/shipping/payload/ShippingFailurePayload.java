package ru.boraldan.dto.shipping.payload;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ru.boraldan.dto.shipping.ShippingPayload;

import java.util.UUID;

@JsonTypeName("ShippingFailure.v1")
public record ShippingFailurePayload(UUID orderId, Long shippingId, String reason) implements ShippingPayload {
}
