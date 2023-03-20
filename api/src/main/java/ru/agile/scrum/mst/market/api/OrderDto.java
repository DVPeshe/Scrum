package ru.agile.scrum.mst.market.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderDto {
    private Long id;
    private List<OrderItemDto> items;
    private BigDecimal totalPrice;
    private String createdAtStr;
}
