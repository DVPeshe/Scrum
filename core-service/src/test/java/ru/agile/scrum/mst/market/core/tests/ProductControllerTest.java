package ru.agile.scrum.mst.market.core.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.agile.scrum.mst.market.api.ProductCardDto;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.core.controllers.ProductController;
import ru.agile.scrum.mst.market.core.entities.Category;
import ru.agile.scrum.mst.market.core.entities.Product;
import ru.agile.scrum.mst.market.core.mappers.ProductCardMapper;
import ru.agile.scrum.mst.market.core.mappers.ProductMapper;
import ru.agile.scrum.mst.market.core.services.CategoryService;
import ru.agile.scrum.mst.market.core.services.ProductService;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductCardMapper productCardMapper;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private CategoryService categoryService;

    private static Product product;
    private static ProductCardDto productCardDto;
    private static ProductDto productDto;
    private static Page<Product> productPage;
    private static Category category;

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
                .quantity(3)
                .id(50L)
                .visible(true)
                .build();
        productDto = ProductDto.builder()
                .price(new BigDecimal(20))
                .title("Milk")
                .categoryTitle("other")
                .quantity(3)
                .id(50L)
                .visible(true)
                .build();
    }

    @Test
    public void getProductListEditTest() throws Exception{
        productPage = new PageImpl<>(List.of(product));
        Specification<Product> spec = Specification.where(null);
        given(productService.findAll(0, 5, spec)).willReturn(productPage);
        given(productCardMapper.mapProductToProductCardDto(product)).willReturn(productCardDto);
        mvc
                .perform(
                        get("/api/v1/products/cards")
                                .param("p", String.valueOf(1))
                                .param("page_size", String.valueOf(5))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "")
                                .header("username", "ethan")
                                .header("roles", "ROLE_MANAGER")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("Milk")));
    }

    @Test
    public void getProductByIdTest() throws Exception{
        given(productService.findById(1L)).willReturn(Optional.of(product));
        given(productMapper.mapProductToProductDto(product)).willReturn(productDto);
        mvc
                .perform(
                        get("/api/v1/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Milk")))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    public void createNewProductTest() throws Exception {
        given(productService.createNewProduct(productCardDto)).willReturn(product);
        mvc
                .perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productCardDto))
                                .header("Authorization", "")
                                .header("username", "ethan")
                                .header("roles", "ROLE_MANAGER")
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void updateDataProductTest() throws Exception {
        MockMultipartHttpServletRequestBuilder putMultipart = (MockMultipartHttpServletRequestBuilder)
                MockMvcRequestBuilders.multipart("/api/v1/products/1")
                        .with(rq -> { rq.setMethod("PUT"); return rq; });
        mvc
                .perform(
                        putMultipart
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(productCardDto))
                                .header("Authorization", "")
                                .header("username", "ethan")
                                .header("roles", "ROLE_MANAGER")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void visualizeProductTest() throws Exception{
        MockMultipartHttpServletRequestBuilder putMultipart = (MockMultipartHttpServletRequestBuilder)
                MockMvcRequestBuilders.multipart("/api/v1/products/1/visualize")
                        .with(rq -> { rq.setMethod("PUT"); return rq; });
        mvc
                .perform(
                        putMultipart
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "")
                                .header("username", "ethan")
                                .header("roles", "ROLE_MANAGER")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void unvisualizeProductTest() throws Exception{
        MockMultipartHttpServletRequestBuilder putMultipart = (MockMultipartHttpServletRequestBuilder)
                MockMvcRequestBuilders.multipart("/api/v1/products/1/unvisualize")
                        .with(rq -> { rq.setMethod("PUT"); return rq; });
        mvc
                .perform(
                        putMultipart
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "")
                                .header("username", "ethan")
                                .header("roles", "ROLE_MANAGER")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getProductCardByIdTest() throws Exception{
        given(productService.findById(1L)).willReturn(Optional.of(product));
        given(productCardMapper.mapProductToProductCardDto(product)).willReturn(productCardDto);
        mvc
                .perform(
                        get("/api/v1/products/card/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Milk")))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    public void updateProductImageTest() throws Exception{
        MockMultipartHttpServletRequestBuilder putMultipart = (MockMultipartHttpServletRequestBuilder)
                MockMvcRequestBuilders.multipart("/api/v1/products/1/images/imageId")
                        .with(rq -> {
                            rq.setMethod("PUT");
                            rq.addHeader("username", "MyName");
                            rq.addHeader("roles", "ROLE_MANAGER");
                            return rq; });
        mvc
                .perform(
                        putMultipart
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
