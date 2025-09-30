package ru.boraldan.temporal.order;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import ru.boraldan.dto.order.OrderStatus;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.order.payload.OrderNewPayload;
import ru.boraldan.dto.payment.payload.PaymentCompletedPayload;
import ru.boraldan.dto.payment.payload.PaymentPendingPayload;
import ru.boraldan.dto.shipping.ShippingPayload;
import ru.boraldan.dto.shipping.payload.*;
import ru.boraldan.temporal.OrderWorkflow;
import ru.boraldan.temporal.activity.OrderActivities;
import ru.boraldan.temporal.activity.PaymentActivities;
import ru.boraldan.temporal.activity.ShippingActivities;

import java.time.Duration;


@Slf4j
@WorkflowImpl(taskQueues = "workflow-tq")
public class OrderWorkflowProcess_v1 implements OrderWorkflow {

    private String status = "NEW";
//    StartToCloseTimeout (обязательный)
//    Сколько максимум времени Activity может реально выполняться.
//
//    ScheduleToStartTimeout
//    Сколько Activity может лежать в очереди без воркера.
//    Если воркер не успел подобрать за 30s → фейл.
//
//    ScheduleToCloseTimeout
//    Общий потолок "назначили + ждали + выполняли".
//    Обычно ≥ StartToClose + ScheduleToStart.
//
//    HeartbeatTimeout
//    Для длинных Activity (например, загрузка файла, batch-job).
//    Если воркер перестал слать heartbeat дольше чем 30s → Temporal считает его умершим.

    private final OrderActivities orderActivities =
            Workflow.newActivityStub(OrderActivities.class,
                    ActivityOptions.newBuilder()
                            .setTaskQueue("order-tq")
                            .setStartToCloseTimeout(Duration.ofSeconds(10)) // обязательно!
//                            .setScheduleToStartTimeout(Duration.ofSeconds(30))
//                            .setScheduleToCloseTimeout(Duration.ofMinutes(3))
//                            .setHeartbeatTimeout(Duration.ofSeconds(30))
                            .setRetryOptions(RetryOptions.newBuilder()
                                    .setMaximumAttempts(1) // если не выполнит за 1 попытку, значит что-то настроено неправильно
                                    .build())
                            .build());

    private final PaymentActivities paymentActivities =
            Workflow.newActivityStub(PaymentActivities.class,
                    ActivityOptions.newBuilder()
                            .setTaskQueue("payment-tq")
                            .setStartToCloseTimeout(Duration.ofSeconds(10))
                            .setRetryOptions(RetryOptions.newBuilder()
                                    .setMaximumAttempts(1)
                                    .build())
                            .build());


    private final ShippingActivities shippingActivities =
            Workflow.newActivityStub(ShippingActivities.class,
                    ActivityOptions.newBuilder()
                            .setTaskQueue("shipping-tq")
                            .setStartToCloseTimeout(Duration.ofSeconds(10))
                            .setRetryOptions(RetryOptions.newBuilder()
                                    .setMaximumAttempts(1)
                                    .build())
                            .build());

    @Override
    public void processOrder(OrderNewPayload orderNewPayload) {

        Saga saga = new Saga(new Saga.Options.Builder()
                .setParallelCompensation(false)
                .setContinueWithError(true)
                .build());

        try {
            // логика order-service
            OrderCreatedPayload orderCreatedPayload = orderActivities.createOrder(orderNewPayload);
            saga.addCompensation(() -> orderActivities.cancelOrder(orderCreatedPayload.orderId()));

            // логика payment-service
            PaymentPendingPayload paymentPendingPayload = paymentActivities.createPayment(orderCreatedPayload);

            PaymentCompletedPayload okPayment = paymentActivities.chargePayment(paymentPendingPayload);
            saga.addCompensation(() -> paymentActivities.cancelPayment(okPayment.paymentId()));
            orderActivities.updateOrderStatus(okPayment.orderId(), OrderStatus.PAID);

            // логика shipping-service
            ShippingPendingPayload shippingPendingPayload = shippingActivities.createShipping(orderCreatedPayload);

            ShippingPayload shippingPayload = shippingActivities.chargeShipping(shippingPendingPayload);
            if (shippingPayload instanceof ShippingShippedPayload shippingShippedPayload) {
                saga.addCompensation(() -> shippingActivities.cancelShippingById(shippingShippedPayload.shippingId()));
                orderActivities.updateOrderStatus(shippingShippedPayload.orderId(), OrderStatus.COMPLETED);
            } else if (shippingPayload instanceof ShippingFailurePayload shippingFailurePayload) {
                log.error(shippingFailurePayload.reason());
                saga.addCompensation(() -> shippingActivities.cancelShipping(shippingFailurePayload));
                saga.compensate();
            } else {
                throw new IllegalArgumentException("Неизвестный payload: " + shippingPayload);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            saga.compensate();
        }
    }

    @Override
    public void cancelOrderSignal(String orderId) {
        status = "CANCELLED";
        // логика отмены
    }

    @Override
    public String getOrderStatus() {
        return status;
    }
}

