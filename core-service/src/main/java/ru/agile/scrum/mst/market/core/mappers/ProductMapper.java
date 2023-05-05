package ru.agile.scrum.mst.market.core.mappers;

import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.core.entities.Product;

@Component
public class ProductMapper {

    public ProductDto mapProductToProductDto(Product product, Integer quantity) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .visible(product.isVisible())
                .categoryTitle(product.getCategory().getTitle())
                .quantityReservation(product.getQuantity() - quantity)
                .quantity(product.getQuantity())
                .imageId(product.getImageId())
                .build();
    }
}
