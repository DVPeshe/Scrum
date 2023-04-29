package ru.agile.scrum.mst.market.core.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.agile.scrum.mst.market.api.ProductCardDto;
import ru.agile.scrum.mst.market.core.entities.Category;
import ru.agile.scrum.mst.market.core.entities.Product;
import ru.agile.scrum.mst.market.core.integrations.CartServiceIntegration;
import ru.agile.scrum.mst.market.core.repositories.ProductRepository;
import ru.agile.scrum.mst.market.core.services.CategoryService;
import ru.agile.scrum.mst.market.core.services.ProductService;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

@SpringBootTest(classes = ProductService.class)
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryService categoryService;
    @MockBean
    private CartServiceIntegration cartServiceIntegration;

    private static Page<Product> productPage;
    private static Category category;
    private static Product product;
    private static ProductCardDto productCardDto ;

    @BeforeAll
    public static void initEntities() {
        category = Category.builder()
                .id(1L)
                .title("Еда")
                .build();
        product = Product.builder()
                .price(new BigDecimal(20))
                .title("Milk")
                .category(category)
                .quantity(3)
                .id(50L)
                .visible(true)
                .build();
        productCardDto = ProductCardDto.builder()
                .price(new BigDecimal(20))
                .title("Milk")
                .categoryTitle("other")
                .quantityReservation(1)
                .quantity(3)
                .id(50L)
                .visible(true)
                .build();
    }


    @Test
    public void createNewProductTest() {
        Mockito.doReturn(Optional.of(category)).when(categoryService).findByTitle("other");
        Mockito.doReturn(false).when(productRepository).existsByTitle(productCardDto.getTitle());

        Product product = productService.createNewProduct(productCardDto);

        Assertions.assertEquals(product.getTitle(), "Milk");
        Mockito.verify(productRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }

    @Test
    public void findAllTest() {
        productPage = new PageImpl<>(List.of(product));
        Specification<Product> spec = Specification.where(null);
        Sort sort = Sort.by("title");
        Mockito.doReturn(productPage).when(productRepository).findAll(spec, PageRequest.of(1, 10, sort));

        Page<Product> page = productService.findAll(1, 10, spec);

        Assertions.assertEquals(1L, page.getTotalElements());
        Assertions.assertEquals(1, page.getTotalPages());
        Assertions.assertEquals("Milk", page.get().findFirst().orElse(new Product()).getTitle());
        Mockito.verify(productRepository, Mockito.times(1)).findAll(spec, PageRequest.of(1, 10, sort));
    }

    @Test
    public void updateProductTest() {
        Mockito.doReturn(Optional.of(category)).when(categoryService).findByTitle("other");
        Mockito.doReturn(product).when(productRepository).getById(productCardDto.getId());

        productService.updateProduct(productCardDto);

        Mockito.verify(productRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }

    @Test
    public void findByIdTest(){
        Mockito.doReturn(Optional.of(product)).when(productRepository).findById(50L);

        Product product = productService.findById(50L).orElse(new Product());

        Assertions.assertEquals(50L, product.getId());
        Mockito.verify(productRepository, Mockito.times(1)).findById(ArgumentMatchers.any());
    }

    @Test
    public void updateVisibleTest(){
        Mockito.doReturn(product).when(productRepository).getById(50L);

        productService.updateVisible(50L, false);

        Mockito.verify(productRepository, Mockito.times(1)).getById(ArgumentMatchers.any());
        Mockito.verify(productRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }

    @Test
    public void updateProductImageTest(){
        Mockito.doReturn(product).when(productRepository).getById(50L);
        String imageId = "imageId";

        productService.updateProductImage(50L, imageId);

        Mockito.verify(productRepository, Mockito.times(1)).getById(ArgumentMatchers.any());
        Mockito.verify(productRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }
}
