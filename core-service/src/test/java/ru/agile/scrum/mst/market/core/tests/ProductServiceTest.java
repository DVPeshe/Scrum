package ru.agile.scrum.mst.market.core.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.agile.scrum.mst.market.api.ProductCardDto;
import ru.agile.scrum.mst.market.core.entities.Category;
import ru.agile.scrum.mst.market.core.entities.Product;
import ru.agile.scrum.mst.market.core.repositories.ProductRepository;
import ru.agile.scrum.mst.market.core.services.CategoryService;
import ru.agile.scrum.mst.market.core.services.ProductService;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest(classes = ProductService.class)
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void createOrderTest() {
        ProductCardDto productCardDto = ProductCardDto.builder()
                .price(new BigDecimal(20))
                .title("Milk")
                .categoryTitle("other")
                .quantity(3)
                .id(50L)
                .build();

        Category category = Category.builder()
                .id(4L)
                .title("Other")
                .build();

        Mockito.doReturn(Optional.of(category)).when(categoryService).findByTitle("other");
        Mockito.doReturn(false).when(productRepository).existsByTitle(productCardDto.getTitle());

        Product product = productService.createNewProduct(productCardDto);
        Assertions.assertEquals(product.getTitle(), "Milk");
        Mockito.verify(productRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }
}
