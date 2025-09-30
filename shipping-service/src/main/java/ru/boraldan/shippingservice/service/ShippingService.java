package ru.boraldan.shippingservice.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.boraldan.dto.order.payload.OrderCreatedPayload;
import ru.boraldan.dto.shipping.ShippingPayload;
import ru.boraldan.dto.shipping.ShippingStatus;
import ru.boraldan.dto.shipping.payload.*;
import ru.boraldan.shippingservice.domen.Shipping;
import ru.boraldan.shippingservice.repository.ShippingRepository;
import ru.boraldan.shippingservice.tool.MapperShipping;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShippingService {

    private final ShippingRepository shippingRepo;
    private final MapperShipping mapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public ShippingPendingPayload creatShipping(OrderCreatedPayload orderCreatedPayload) {

        Shipping shipping = mapper.orderCreatedToShipping(orderCreatedPayload);
        shipping.setShippingStatus(ShippingStatus.PENDING.name());
        shipping.setAddress("Spb");
        Shipping savedShipping = shippingRepo.save(shipping);
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void afterCompletion(int status) {
//                if (status == TransactionSynchronization.STATUS_COMMITTED) {
//                }
//            }
//        });
        return mapper.shippingToShippingPendingPayload(savedShipping);
    }

    @Transactional
    public ShippingPayload chargeShipping(ShippingPendingPayload shippingPendingPayload) {
        Shipping shipping = shippingRepo.findById(shippingPendingPayload.shippingId()).orElseThrow(EntityNotFoundException::new);

        // имитация доставки/отмены
        if (shipping.getShippingId() % 3 == 0) {
            log.error("Доставка отменена");
            shipping.setShippingStatus(ShippingStatus.FAILURE.name());
            return mapper.shippingToShippingFailurePayload(shipping, "Перевозчик не смог доставить товар");
        }

        shipping.setShippingStatus(ShippingStatus.SHIPPED.name());
        return mapper.shippingToShippingShippedPayload(shipping);
    }


    @Transactional
    public ShippingCancelledPayload cancelShipping(ShippingFailurePayload shippingFailurePayload) {
        Shipping shipping = shippingRepo.findById(shippingFailurePayload.shippingId()).orElseThrow(EntityNotFoundException::new);
        shipping.setShippingStatus(ShippingStatus.CANCELLED.name());
        return mapper.shippingToShippingCancelledPayload(shipping, shippingFailurePayload.reason());
    }

    @Transactional
    public ShippingCancelledPayload cancelShippingById(Long shippingId) {
        Shipping shipping = shippingRepo.findById(shippingId).orElseThrow(EntityNotFoundException::new);
        shipping.setShippingStatus(ShippingStatus.CANCELLED.name());
        return mapper.shippingToShippingCancelledPayload(shipping, "Отмена доставки товара");
    }
}
