package ru.boraldan.temporal.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.payment.payload.PaymentCompletedPayload;
import ru.boraldan.dto.payment.payload.PaymentPendingPayload;
import ru.boraldan.dto.payment.payload.PaymentRefundedPayload;

@ActivityInterface
public interface PaymentActivities {

    @ActivityMethod
    PaymentPendingPayload createPayment(OrderCreatedPayload orderCreatedPayload);

    @ActivityMethod
    PaymentCompletedPayload chargePayment(PaymentPendingPayload paymentPendingPayload);

    @ActivityMethod
    PaymentRefundedPayload cancelPayment(Long paymentId);

}
