package ru.boraldan.temporal;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import ru.boraldan.dto.order.payload.OrderNewPayload;

@WorkflowInterface
public interface OrderWorkflow {
    @WorkflowMethod
    void processOrder(OrderNewPayload orderNewPayload);

    @SignalMethod
    void cancelOrderSignal(String orderId);

    @QueryMethod
    String getOrderStatus();
}
