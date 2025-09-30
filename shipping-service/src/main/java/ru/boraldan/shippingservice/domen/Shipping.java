package ru.boraldan.shippingservice.domen;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@Entity
@Table(name = "t_shipping")
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shippingId;

    private UUID orderId;

    private String shippingStatus;

    private String address;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public Shipping(UUID orderId, String shippingStatus, String address) {
        this.orderId = orderId;
        this.shippingStatus = shippingStatus;
        this.address = address;
    }
}