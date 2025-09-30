package ru.boraldan.apiservice.tool;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Primary;
import ru.boraldan.apiservice.domen.OrderOutbox;
import ru.boraldan.dto.order.payload.OrderNewPayload;

// ВАЖНО! после внесения изменений в Mapper всегда надо запускать mvn clean
@Primary
@Mapper(componentModel = "spring")
public interface MapperOrderOutbox {

    OrderOutbox orderNewPayloadToOrderOutbox(OrderNewPayload orderNewPayload);

    @Mapping(target = "orderId", source = "orderOutboxId")
    OrderNewPayload OrderOutboxToOrderNewPayload(OrderOutbox orderOutbox);


}
