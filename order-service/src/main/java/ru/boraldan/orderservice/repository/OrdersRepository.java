package ru.boraldan.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.boraldan.orderservice.domen.order.Orders;

import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, UUID> {
}
