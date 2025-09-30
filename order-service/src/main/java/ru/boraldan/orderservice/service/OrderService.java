package ru.boraldan.orderservice.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.boraldan.dto.order.OrderStatus;
import ru.boraldan.dto.order.payload.OrderCancelledPayload;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.order.payload.OrderNewPayload;
import ru.boraldan.orderservice.domen.order.Orders;
import ru.boraldan.orderservice.repository.OrdersRepository;
import ru.boraldan.orderservice.tool.MapperDto;

import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrdersRepository ordersRepo;
    private final MapperDto mapperDto;

    @Transactional
    public OrderCreatedPayload creatOrder(OrderNewPayload orderNewPayload) {
        Orders savedOrder = ordersRepo.save(Orders.builder()
                .orderId(orderNewPayload.orderId())
                .customerId(orderNewPayload.customerId())
                .orderStatus(OrderStatus.CREATED.name())
                .amount(orderNewPayload.amount())
                .build());

        OrderCreatedPayload orderCreatedPayload = mapperDto.orderToCreatedPayload(savedOrder);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                Level level = status == TransactionSynchronization.STATUS_COMMITTED ? Level.INFO : Level.ERROR;
                log.atLevel(level).log("creatOrder");
            }
        });
        return orderCreatedPayload;
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, OrderStatus orderStatus) {
        Orders order = ordersRepo.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.setOrderStatus(orderStatus.name());
    }


    @Transactional
    public OrderCancelledPayload cancelOrder(UUID orderId) {
        Orders order = ordersRepo.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.setOrderStatus(OrderStatus.CANCELLED.name());
        return mapperDto.orderToCancelledPayload(order, "Ордер отменен: %s".formatted(orderId));
    }
}
