package ru.boraldan.orderservice.tool;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.boraldan.dto.order.payload.OrderCancelledPayload;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.orderservice.domen.order.Orders;

// !!! ВАЖНО !!!  после внесения изменений в MapperDto всегда надо запускать mvn clean
@Mapper(componentModel = "spring")
public interface MapperDto {

    OrderCreatedPayload orderToCreatedPayload(Orders order);

    @Mapping(target = "reason", source = "reason") // "reason" берётся из параметра метода
    OrderCancelledPayload orderToCancelledPayload(Orders order, String reason);
}
