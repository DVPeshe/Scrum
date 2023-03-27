package ru.agile.scrum.mst.market.favorite.mappers;

import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.FavoriteItemDto;
import ru.agile.scrum.mst.market.favorite.utils.FavoriteItem;

@Component
public class FavoriteItemMapper {
    public FavoriteItemDto mapFavoriteItemToFavoriteItemDto(FavoriteItem favoriteItem) {
        return FavoriteItemDto.builder()
                .productId(favoriteItem.getProductId())
                .productTitle(favoriteItem.getProductTitle())
                .price(favoriteItem.getPrice())
                .build();
    }
}
