package ru.boraldan.apiservice.controller;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.boraldan.apiservice.domen.OrderOutbox;
import ru.boraldan.apiservice.service.OrderOutboxService;
import ru.boraldan.apiservice.service.WorkflowRetryService;
import ru.boraldan.apiservice.tool.BigDecimalUtils;
import ru.boraldan.apiservice.tool.MapperOrderOutbox;
import ru.boraldan.dto.order.OrderStatus;
import ru.boraldan.dto.order.payload.OrderNewPayload;
import ru.boraldan.temporal.OrderWorkflow;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/new-order")
public class InboxOrderController {

    private final OrderOutboxService orderOutboxService;
    private final WorkflowClient workflowClient;
    private final WorkflowRetryService workflowRetryService;
    private final MapperOrderOutbox mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String createOrder(@RequestBody OrderNewPayload order) {

        if (BigDecimalUtils.isZeroOrNegative(order.amount())) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        OrderOutbox orderOutbox = orderOutboxService.create(order);

//    Запуск workflow
//    WorkflowExecutionTimeout
//    Общий потолок жизни workflow (даже с рестартами и ретраями).
//    Обычно выставляют часы/дни, а не минуты.
//    Например, заказ в e-commerce может жить до 24ч.
//
//    WorkflowRunTimeout
//    Таймаут для одной конкретной попытки исполнения workflow.
//    Обычно меньше, чем ExecutionTimeout.
//    Пример: 10m (одна транзакция/обработка).
//
//    WorkflowTaskTimeout
//    Сколько времени дано на обработку одного workflow task (шаг из истории).
//    Обычно короткий: 10–30 секунд.

        OrderWorkflow workflow = workflowClient.newWorkflowStub(
                OrderWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId("order-" + orderOutbox.getOrderOutboxId())   // связываем workflowId = orderId
                        .setTaskQueue("workflow-tq")
                        .setWorkflowExecutionTimeout(Duration.ofMinutes(1)) // полный потолок
//                        .setWorkflowRunTimeout(Duration.ofMinutes(10))    // одна попытка
//                        .setWorkflowTaskTimeout(Duration.ofSeconds(10))   // один тик
//                        .setRetryOptions(RetryOptions.newBuilder()
//                                        .setMaximumAttempts(1)            // повтор, это создание нового RunId()
//                                        .setInitialInterval(Duration.ofSeconds(3))
//                                        .build()             )
                        .build());

        // стартуем асинхронно и в случае успеха, помечаем в бд
        // WorkflowClient.start(...) возвращает объект WorkflowExecution, в котором есть:
        // getWorkflowId()
        // getRunId()

        String runId = null;

        try {
            WorkflowExecution exec = WorkflowClient.start(workflow::processOrder,
                    mapper.OrderOutboxToOrderNewPayload(orderOutbox));
            runId = exec.getRunId();
            log.info("Workflow started: workflowId={}, runId={}", exec.getWorkflowId(), exec.getRunId());
            orderOutboxService.markStartWorkflow(orderOutbox.getOrderOutboxId(), exec.getRunId());

        } catch (WorkflowExecutionAlreadyStarted e) {
            log.warn("Workflow уже запущен: workflowId={}, runId={}",
                    e.getExecution().getWorkflowId(), e.getExecution().getRunId());
            orderOutboxService.markStartWorkflow(orderOutbox.getOrderOutboxId(), runId);
        } catch (WorkflowServiceException e) {
            log.error("WorkflowServiceException при старте workflow: {}", e.getMessage(), e);
            workflowRetryService.startWorkflow(workflow, orderOutbox);
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при старте workflow: {}", e.getMessage(), e);
            throw e;
        }

        return "Order создан : %s, статус : %s".formatted(orderOutbox.getOrderOutboxId(), OrderStatus.NEW.name());
    }

}
