package ru.boraldan.pyamentservice.domen;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private UUID orderId;

    private String paymentStatus;

    private BigDecimal amount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Payment(UUID orderId, String paymentStatus, BigDecimal amount) {
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }
}
