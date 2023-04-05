package ru.agile.scrum.mst.market.comment.mappers;

import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.ProductCommentDto;
import ru.agile.scrum.mst.market.comment.entity.Product;
import ru.agile.scrum.mst.market.comment.repository.ProductRepository;

@Component
public class ProductMapper {
    private final ProductRepository productRepository;

    public ProductMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product mapProductCommentDtoToProduct(ProductCommentDto productCommentDto) {
        Product product = Product.builder()
                .id(productCommentDto.getId())
                .title(productCommentDto.getTitle())
                .build();
        return product;
    }

    public ProductCommentDto mapProductToProductCommentDto(Product product) {
        ProductCommentDto productCommentDto = ProductCommentDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .build();
        return productCommentDto;
    }


}
