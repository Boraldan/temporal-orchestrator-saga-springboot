package ru.boraldan.shippingservice.temporal;


import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.shipping.payload.ShippingCancelledPayload;
import ru.boraldan.dto.shipping.payload.ShippingFailurePayload;
import ru.boraldan.dto.shipping.ShippingPayload;
import ru.boraldan.dto.shipping.payload.ShippingPendingPayload;
import ru.boraldan.shippingservice.service.ShippingService;
import ru.boraldan.temporal.activity.ShippingActivities;

@RequiredArgsConstructor
@Component
@ActivityImpl(taskQueues = "shipping-tq")
public class ShippingActivities_v1 implements ShippingActivities {

    private final ShippingService shippingService;

    @Override
    public ShippingPendingPayload createShipping(OrderCreatedPayload orderCreatedPayload) {
        return shippingService.creatShipping(orderCreatedPayload);
    }

    @Override
    public ShippingPayload chargeShipping(ShippingPendingPayload shippingPendingPayload) {
        return shippingService.chargeShipping(shippingPendingPayload);
    }

    @Override
    public ShippingCancelledPayload cancelShipping(ShippingFailurePayload shippingFailurePayload) {
        return shippingService.cancelShipping(shippingFailurePayload);
    }

    @Override
    public ShippingCancelledPayload cancelShippingById(Long shippingId) {
        return shippingService.cancelShippingById(shippingId);
    }
}
