package ru.boraldan.pyamentservice.temporal;

import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.payment.payload.PaymentCompletedPayload;
import ru.boraldan.dto.payment.payload.PaymentPendingPayload;
import ru.boraldan.dto.payment.payload.PaymentRefundedPayload;
import ru.boraldan.pyamentservice.service.PaymentService;
import ru.boraldan.temporal.activity.PaymentActivities;

@RequiredArgsConstructor
@Component
@ActivityImpl(taskQueues = "payment-tq")
public class PaymentActivities_v1 implements PaymentActivities {

    private final PaymentService paymentService;

    @Override
    public PaymentPendingPayload createPayment(OrderCreatedPayload orderCreatedPayload) {
        return paymentService.creatPayment(orderCreatedPayload);
    }

    @Override
    public PaymentCompletedPayload chargePayment(PaymentPendingPayload paymentPendingPayload) {
        return paymentService.sentPaymentToBank(paymentPendingPayload);
    }

    @Override
    public PaymentRefundedPayload cancelPayment(Long paymentId) {
        return paymentService.cancelPayment(paymentId);
    }


}
