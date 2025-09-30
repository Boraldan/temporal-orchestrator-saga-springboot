package ru.boraldan.apiservice.service;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import ru.boraldan.apiservice.domen.OrderOutbox;
import ru.boraldan.apiservice.tool.MapperOrderOutbox;
import ru.boraldan.temporal.OrderWorkflow;

@RequiredArgsConstructor
@Service
public class WorkflowRetryService {

    private final MapperOrderOutbox mapper;
    private final OrderOutboxService orderOutboxService;

    @Retryable(
            retryFor = WorkflowServiceException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000)
    )
    public void startWorkflow(OrderWorkflow workflow, OrderOutbox orderOutbox) {
        WorkflowExecution exec = WorkflowClient.start(workflow::processOrder,
                mapper.OrderOutboxToOrderNewPayload(orderOutbox));

        orderOutboxService.markStartWorkflow(orderOutbox.getOrderOutboxId(), exec.getRunId());
    }

}
