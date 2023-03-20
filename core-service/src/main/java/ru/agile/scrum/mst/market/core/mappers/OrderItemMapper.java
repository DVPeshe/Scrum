package ru.agile.scrum.mst.market.core.mappers;

import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.OrderItemDto;
import ru.agile.scrum.mst.market.core.entities.OrderItem;

@Component
public class OrderItemMapper {

    public OrderItemDto mapOrderItemToOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .productId(orderItem.getId())
                .productTitle(orderItem.getProduct().getTitle())
                .quantity(orderItem.getQuantity())
                .pricePerProduct(orderItem.getPricePerProduct())
                .price(orderItem.getPrice())
                .build();
    }
}
