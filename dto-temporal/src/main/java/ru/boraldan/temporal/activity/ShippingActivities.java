package ru.boraldan.temporal.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.shipping.ShippingPayload;
import ru.boraldan.dto.shipping.payload.*;

@ActivityInterface
public interface ShippingActivities {

    @ActivityMethod
    ShippingPendingPayload createShipping(OrderCreatedPayload orderCreatedPayload);

    @ActivityMethod
    ShippingPayload chargeShipping(ShippingPendingPayload shippingPendingPayload);

    @ActivityMethod
    ShippingCancelledPayload cancelShipping(ShippingFailurePayload shippingFailurePayload);

    @ActivityMethod
    ShippingCancelledPayload cancelShippingById(Long shippingId);

}
