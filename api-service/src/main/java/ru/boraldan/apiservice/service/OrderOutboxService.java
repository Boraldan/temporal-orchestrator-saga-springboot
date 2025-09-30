package ru.boraldan.apiservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.boraldan.apiservice.domen.OrderOutbox;
import ru.boraldan.apiservice.repository.OrderOutboxRepository;
import ru.boraldan.apiservice.tool.MapperOrderOutbox;
import ru.boraldan.dto.order.payload.OrderNewPayload;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderOutboxService {

    private final OrderOutboxRepository orderOutboxRepo;
    private final MapperOrderOutbox mapper;

    @Transactional
    public OrderOutbox create(OrderNewPayload orderNewPayload) {
        OrderOutbox orderOutbox = mapper.orderNewPayloadToOrderOutbox(orderNewPayload);
        return orderOutboxRepo.save(orderOutbox);
    }

    @Transactional
    public OrderOutbox markStartWorkflow(UUID orderOutboxId, String runId) {
        OrderOutbox orderOutbox = orderOutboxRepo.findById(orderOutboxId)
                .orElseThrow(EntityNotFoundException::new);
        orderOutbox.setStartWorkflow(true);
        orderOutbox.setWorkflowRunId(runId);
        return orderOutbox;
    }

}
