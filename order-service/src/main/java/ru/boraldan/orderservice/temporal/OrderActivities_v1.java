package ru.boraldan.orderservice.temporal;

import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.boraldan.dto.order.OrderStatus;
import ru.boraldan.dto.order.payload.OrderCancelledPayload;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.order.payload.OrderNewPayload;
import ru.boraldan.orderservice.service.OrderService;
import ru.boraldan.temporal.activity.OrderActivities;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@ActivityImpl(taskQueues = "order-tq")
public class OrderActivities_v1 implements OrderActivities {

    private final OrderService orderService;

    @Override
    public OrderCreatedPayload createOrder(OrderNewPayload orderNewPayload) {
        return orderService.creatOrder(orderNewPayload);
    }

    @Override
    public OrderCancelledPayload cancelOrder(UUID orderId) {
        return orderService.cancelOrder(orderId);
    }

    @Override
    public void updateOrderStatus(UUID orderId, OrderStatus orderStatus) {
        try {
            orderService.updateOrderStatus(orderId, orderStatus);
        } catch (Exception e) {
            log.error("Не удалось обновить статус ордера: {}", orderId);
        }
    }


}