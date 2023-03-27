package ru.agile.scrum.mst.market.core.mappers;

import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.ProductCardDto;
import ru.agile.scrum.mst.market.core.entities.Product;

@Component
public class ProductCardMapper {

    public ProductCardDto mapProductToProductCardDto(Product product) {
        return ProductCardDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .visible(product.isVisible())
                .categoryTitle(product.getCategory().getTitle())
                .description(product.getDescription())
                .build();
    }
}
