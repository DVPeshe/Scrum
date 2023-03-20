package ru.agile.scrum.mst.market.core.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.OrderDto;
import ru.agile.scrum.mst.market.core.entities.Order;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;

    public OrderDto mapOrderToOrderDto(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.MEDIUM);
        String localDateTimeStr = order.getCreatedAt().format(formatter);
        return OrderDto.builder()
                .id(order.getId())
                .items(order.getItems()
                        .stream()
                        .map(orderItemMapper::mapOrderItemToOrderItemDto)
                        .collect(Collectors.toList()))
                .totalPrice(order.getTotalPrice())
                .createdAtStr(localDateTimeStr)
                .build();
    }
}
