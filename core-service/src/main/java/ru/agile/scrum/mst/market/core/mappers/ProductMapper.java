package ru.agile.scrum.mst.market.core.mappers;

import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.core.entities.Product;

@Component
public class ProductMapper {

    public ProductDto mapProductToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .visible(product.isVisible())
                .categoryTitle(product.getCategory().getTitle())
                .imageId(product.getImageId())
                .build();
    }
}
