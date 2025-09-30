package ru.boraldan.dto.shipping;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.boraldan.dto.shipping.payload.ShippingFailurePayload;
import ru.boraldan.dto.shipping.payload.ShippingShippedPayload;

/**
 * Вариант с маркирным интерфейсом.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,            // строковый идентификатор
        include = JsonTypeInfo.As.PROPERTY,    // отдельное поле в JSON
        property = "type"                      // имя поля, где лежит тип
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ShippingShippedPayload.class, name = "ShippingShipped.v1"),
        @JsonSubTypes.Type(value = ShippingFailurePayload.class, name = "ShippingFailure.v1")
})
public interface ShippingPayload {
}
