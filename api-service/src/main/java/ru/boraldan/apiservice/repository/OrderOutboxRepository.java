package ru.boraldan.apiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.boraldan.apiservice.domen.OrderOutbox;

import java.util.UUID;

@Repository
public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, UUID> {
}
