package ru.boraldan.shippingservice.tool;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.shipping.payload.ShippingCancelledPayload;
import ru.boraldan.dto.shipping.payload.ShippingFailurePayload;
import ru.boraldan.dto.shipping.payload.ShippingPendingPayload;
import ru.boraldan.dto.shipping.payload.ShippingShippedPayload;
import ru.boraldan.shippingservice.domen.Shipping;

@Mapper(componentModel = "spring")
public interface MapperShipping {

    Shipping orderCreatedToShipping(OrderCreatedPayload orderCreatedPayload);

    ShippingPendingPayload shippingToShippingPendingPayload(Shipping shipping);

    ShippingShippedPayload shippingToShippingShippedPayload(Shipping shipping );

    @Mapping(target = "reason",  source = "reason")
    ShippingFailurePayload shippingToShippingFailurePayload(Shipping shipping, String reason );

    @Mapping(target = "reason",  source = "reason")
    ShippingCancelledPayload shippingToShippingCancelledPayload(Shipping shipping, String reason );
}
