package ru.boraldan.pyamentservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import ru.boraldan.dto.bank.BankResponse;
import ru.boraldan.dto.order.payload.OrderCancelledPayload;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.payment.PaymentStatus;
import ru.boraldan.dto.payment.payload.PaymentCompletedPayload;
import ru.boraldan.dto.payment.payload.PaymentFailedPayload;
import ru.boraldan.dto.payment.payload.PaymentPendingPayload;
import ru.boraldan.dto.payment.payload.PaymentRefundedPayload;
import ru.boraldan.pyamentservice.domen.Payment;
import ru.boraldan.pyamentservice.repository.PaymentRepository;


import java.util.concurrent.Executor;

@Log4j2
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final TransactionTemplate transactionTemplate;
    private final BankService bankService;

    @Transactional
    public PaymentPendingPayload creatPayment(OrderCreatedPayload order) {

        Payment payment = new Payment(order.orderId(), PaymentStatus.PENDING.name(), order.amount());
        Payment savedPayment = paymentRepo.save(payment);

        PaymentPendingPayload paymentPendingPayload = new PaymentPendingPayload(
                savedPayment.getOrderId(),
                savedPayment.getPaymentId(),
                savedPayment.getAmount());
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void afterCompletion(int status) {
//                if (status == TransactionSynchronization.STATUS_COMMITTED) {
//                    executor.execute(() -> sentPaymentToBank(paymentPendingPayload));
//                }
//            }
//        });
        return paymentPendingPayload;
    }

    public PaymentCompletedPayload sentPaymentToBank(PaymentPendingPayload payment) {
        BankResponse flag = bankService.sendToBank(payment);

        if (flag.status().equals("CANCEL")) {
            PaymentFailedPayload paymentFailedPayload = new PaymentFailedPayload(
                    payment.orderId(),
                    payment.paymentId(),
                    "Ошибка при оплате: недостаточно средств");
            updateFailedPayment(paymentFailedPayload);
            throw new IllegalArgumentException("Ошибка при оплате: недостаточно средств");
        }

        PaymentCompletedPayload paymentCompletedPayload = new PaymentCompletedPayload(
                payment.orderId(),
                payment.paymentId(),
                payment.amount());
        updateCompletedPayment(paymentCompletedPayload);

        return paymentCompletedPayload;
    }


    public void updateCompletedPayment(PaymentCompletedPayload paymentIn) {
        transactionTemplate.executeWithoutResult(transaction -> {
            Payment payment = paymentRepo.findById(paymentIn.paymentId())
                    .orElseThrow(EntityNotFoundException::new);
            payment.setPaymentStatus(PaymentStatus.COMPLETED.name());

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                //                @Override
//                public void afterCommit() {   paymentEventProducer.sendPaymentKafka(savedOutbox); }
                @Override
                public void afterCompletion(int status) {
                    if (status == TransactionSynchronization.STATUS_COMMITTED) {

                    }
                }
            });
        });
    }

    public void updateFailedPayment(PaymentFailedPayload event) {
        transactionTemplate.executeWithoutResult(transaction -> {
            Payment payment = paymentRepo.findById(event.paymentId())
                    .orElseThrow(EntityNotFoundException::new);
            payment.setPaymentStatus(PaymentStatus.FAILED.name());
        });
    }

    @Transactional
    public PaymentRefundedPayload cancelPayment(Long paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(EntityNotFoundException::new);
        payment.setPaymentStatus(PaymentStatus.REFUNDED.name());

        return new PaymentRefundedPayload(payment.getOrderId(), payment.getPaymentId(), payment.getAmount());
    }
}


 
