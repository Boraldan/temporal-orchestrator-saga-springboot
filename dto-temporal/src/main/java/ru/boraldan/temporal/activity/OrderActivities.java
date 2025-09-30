package ru.boraldan.temporal.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import ru.boraldan.dto.order.OrderStatus;
import ru.boraldan.dto.order.payload.OrderCancelledPayload;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.order.payload.OrderNewPayload;

import java.util.UUID;

@ActivityInterface
public interface OrderActivities {

    @ActivityMethod
    OrderCreatedPayload createOrder(OrderNewPayload orderNewPayload);

    @ActivityMethod
    OrderCancelledPayload cancelOrder(UUID orderId);

    @ActivityMethod
    void updateOrderStatus(UUID orderId, OrderStatus orderStatus);
}
