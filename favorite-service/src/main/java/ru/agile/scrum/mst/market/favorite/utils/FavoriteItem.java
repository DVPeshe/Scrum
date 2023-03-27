package ru.agile.scrum.mst.market.favorite.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteItem {
    private Long productId;
    private String productTitle;
    private BigDecimal price;

}
