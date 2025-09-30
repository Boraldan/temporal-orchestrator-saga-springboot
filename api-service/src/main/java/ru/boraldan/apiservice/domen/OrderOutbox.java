package ru.boraldan.apiservice.domen;

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
@Table(name = "t_order_outbox")
public class OrderOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_outbox_id")
    private UUID orderOutboxId;

    @Column(name = "workflow_run_id")
    private String workflowRunId;

    private String customerId;

    private BigDecimal amount;

    @Column(name = "start_workflow")
    private Boolean startWorkflow;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;



}

