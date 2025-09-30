package ru.boraldan.dto.bank;

public record BankResponse(Integer transactionId, String status, String message) {
}
