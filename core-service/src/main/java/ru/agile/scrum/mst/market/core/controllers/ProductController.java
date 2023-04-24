package ru.agile.scrum.mst.market.core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.ProductCardDto;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.api.StringResponse;
import ru.agile.scrum.mst.market.core.entities.Product;
import ru.agile.scrum.mst.market.core.exceptions.AppError;
import ru.agile.scrum.mst.market.core.exceptions.ResourceNotFoundException;
import ru.agile.scrum.mst.market.core.mappers.ProductCardMapper;
import ru.agile.scrum.mst.market.core.mappers.ProductMapper;
import ru.agile.scrum.mst.market.core.repositories.specifications.ProductsSpecifications;
import ru.agile.scrum.mst.market.core.services.CategoryService;
import ru.agile.scrum.mst.market.core.services.ProductService;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Продукты", description = "Методы работы с продуктами")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final ProductCardMapper productCardMapper;

    @GetMapping
    public Page<ProductDto> getAllProducts(
            @RequestParam(name = "p", defaultValue = "1") @Parameter(description = "Номер страницы", required = true) Integer page,
            @RequestParam(name = "page_size", defaultValue = "10") @Parameter(description = "Номер страницы", required = false) Integer pageSize,
            @RequestParam(name = "title_part", required = false) @Parameter(description = "Фильтр части названия продукта", required = false) String titlePart,
            @RequestParam(name = "min_price", required = false) @Parameter(description = "Фильтр по мин цене продукта", required = false) Integer minPrice,
            @RequestParam(name = "max_price", required = false) @Parameter(description = "Фильтр по макс цене продукта", required = false) Integer maxPrice,
            @RequestParam(name = "category_title", required = false) @Parameter(description = "Фильтр по категории продукта", required = false) String categoryTitle
    ) {
        if (page < 1) {
            page = 1;
        }
        Specification<Product> spec = Specification.where(null);
        if (titlePart != null) {
            spec = spec.and(ProductsSpecifications.titleLike(titlePart));
        }
        if (minPrice != null) {
            spec = spec.and(ProductsSpecifications.priceGreaterOrEqualsThan(BigDecimal.valueOf(minPrice)));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductsSpecifications.priceLessThanOrEqualsThan(BigDecimal.valueOf(maxPrice)));
        }
        if (categoryTitle != null) {
            spec = spec.and(ProductsSpecifications.titleCategoryLike(categoryTitle));
        }
        spec = spec.and(ProductsSpecifications.visibleLike());
        return productService.findAll(page - 1, pageSize, spec).map(productMapper::mapProductToProductDto);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/cards")
    public Page<ProductCardDto> getProductListEdit(
            @RequestParam(name = "p", defaultValue = "1") Integer page,
            @RequestParam(name = "page_size", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "title_part", required = false) String titlePart
    ) {
        if (page < 1) {
            page = 1;
        }
        Specification<Product> spec = Specification.where(null);
        if (titlePart != null) {
            spec = spec.and(ProductsSpecifications.titleLike(titlePart));
        }
        return productService.findAll(page - 1, pageSize, spec).map(productCardMapper::mapProductToProductCardDto);
    }

    @Operation(
            summary = "Запрос на получение продукта по id",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ProductDto.class))
                    ),
                    @ApiResponse(
                            description = "Продукт не найден", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable @Parameter(description = "Идентификатор продукта", required = true) Long id) {
        return productMapper.mapProductToProductDto(productService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Продукт с id: " + id + " не найден")));
    }

    @Operation(
            summary = "Запрос на создание нового продукта",
            responses = {
                    @ApiResponse(
                            description = "Продукт успешно создан", responseCode = "201"
                    )
            }
    )

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNewProducts(@RequestBody ProductCardDto productCardDto) {
        productService.createNewProduct(productCardDto);
        StringResponse stringResponse = new StringResponse(String.format("Продукт %s успешно создан", productCardDto.getTitle()));
        return ResponseEntity.ok(stringResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDataProduct(@PathVariable Long id, @RequestBody ProductCardDto productCardDto) {
        productService.updateProduct(productCardDto);
        StringResponse stringResponse = new StringResponse(String.format("Продукт %s успешно обновлен", productCardDto.getTitle()));
        return ResponseEntity.ok(stringResponse);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{id}/visualize")
    public void visualizeProduct(@PathVariable Long id ) {
        productService.updateVisible(id, true);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{id}/unvisualize")
    public void unvisualizeProduct(@PathVariable Long id ) {
        productService.updateVisible(id, false);
    }

    @GetMapping("/card/{id}")
    public ProductCardDto getProductCardById(@PathVariable @Parameter(description = "Идентификатор продукта", required = true) Long id) {
        return productCardMapper.mapProductToProductCardDto(productService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Продукт с id: " + id + " не найден")));
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{id}/images/{imageId}")
    public void updateProductImage(@PathVariable Long id, @PathVariable String imageId) {
        productService.updateProductImage(id, imageId);
    }
}
