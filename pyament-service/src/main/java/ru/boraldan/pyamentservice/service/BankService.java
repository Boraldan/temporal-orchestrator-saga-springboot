package ru.boraldan.pyamentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.boraldan.dto.bank.BankResponse;
import ru.boraldan.dto.payment.payload.PaymentPendingPayload;


@RequiredArgsConstructor
@Service
public class BankService {

    private int transactionId = 1;

    // имитируем работу платежа, успешного или отклоненного
    public BankResponse sendToBank(PaymentPendingPayload payment) {
        System.out.println("PaymentPendingPayload    " + payment);
        if (payment.paymentId() % 2 == 0) {
            System.out.println("BankService -->  COMPLETED");
            return new BankResponse(transactionId++, "OK", "Платёж совершён");
        } else {
            System.out.println("BankService -->  FAILED");
            return new BankResponse(transactionId++, "CANCEL", "Недостаточно средств");
        }

    }

}
