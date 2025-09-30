package ru.boraldan.orderservice.domen.order;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "t_order")
public class Orders {

    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    private String customerId;

    private String orderStatus;

    private BigDecimal amount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
