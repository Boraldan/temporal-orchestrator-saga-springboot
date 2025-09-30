package ru.boraldan.shippingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.boraldan.shippingservice.domen.Shipping;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {
}
