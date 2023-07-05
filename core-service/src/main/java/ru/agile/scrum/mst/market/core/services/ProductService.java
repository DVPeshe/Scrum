package ru.agile.scrum.mst.market.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.agile.scrum.mst.market.api.CartItemDto;
import ru.agile.scrum.mst.market.api.IntegerResponse;
import ru.agile.scrum.mst.market.api.ProductCardDto;
import ru.agile.scrum.mst.market.api.ProductStockDto;
import ru.agile.scrum.mst.market.core.entities.Category;
import ru.agile.scrum.mst.market.core.entities.Product;
import ru.agile.scrum.mst.market.core.exceptions.FieldsNotNullException;
import ru.agile.scrum.mst.market.core.exceptions.ResourceNotFoundException;
import ru.agile.scrum.mst.market.core.exceptions.TheProductExistsException;
import ru.agile.scrum.mst.market.core.integrations.CartServiceIntegration;
import ru.agile.scrum.mst.market.core.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    private final CartServiceIntegration cartServiceIntegration;

    public Page<Product> findAll(int page, int pageSize, Specification<Product> specification) {
        Sort sort = Sort.by("title");
        return productRepository.findAll(specification, PageRequest.of(page, pageSize, sort));
    }

    @Transactional
    public void updateProduct(ProductCardDto productCardDto) {
        Product product = productRepository.getById(productCardDto.getId());
        if (productCardDto.getTitle() != null) {
            product.setTitle(productCardDto.getTitle());
        }
        if (productCardDto.getPrice() != null) {
            product.setPrice(productCardDto.getPrice());
        }
        if (productCardDto.getCategoryTitle() != null) {
            Category category = categoryService.findByTitle(productCardDto.getCategoryTitle())
                    .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена"));
            product.setCategory(category);
        }
        if (productCardDto.getQuantity() != null) {
            product.setQuantity(productCardDto.getQuantity());
        }
        if (productCardDto.getDescription() != null) {
            product.setDescription(productCardDto.getDescription());
        }
        productRepository.save(product);
    }

    @Transactional
    public void updateProductStock(ProductStockDto productStockDto) {
        Product product = productRepository.getById(productStockDto.getId());
        if (productStockDto.getQuantity() != null) {
            product.setQuantity(productStockDto.getQuantity());
        }
        productRepository.save(product);
    }

    public Product createNewProduct(ProductCardDto productCardDto) {
        if (productCardDto.getTitle() == null || productCardDto.getCategoryTitle() == null
                || productCardDto.getPrice() == null) {
            throw new FieldsNotNullException("Все поля формы должны быть заполнены");
        }
        Product product = new Product();
        product.setTitle(productCardDto.getTitle());
        product.setPrice(productCardDto.getPrice());
        product.setVisible(true);
        product.setCategory(categoryService.findByTitle(productCardDto.getCategoryTitle()).orElseThrow(
                () -> new ResourceNotFoundException("Категория с названием: " +
                        productCardDto.getCategoryTitle() + " не найдена")));
        product.setQuantity(productCardDto.getQuantity());
        product.setDescription((productCardDto.getDescription()));
        if (productRepository.existsByTitle(productCardDto.getTitle())) {
            throw new TheProductExistsException("Такой продукт уже существует");
        }
        productRepository.save(product);
        return product;
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void updateVisible(Long id, Boolean visible) {
        Product byId = productRepository.getById(id);
        byId.setVisible(visible);
        productRepository.save(byId);
    }

    @Transactional
    public void updateProductImage(Long id, String imageId) {
        Product byId = productRepository.getById(id);
        byId.setImageId(imageId);
        productRepository.save(byId);
    }

    public Integer getNumberReservationProduct(Long productId) {
        IntegerResponse numberReservationProduct = cartServiceIntegration.getNumberReservationProduct(productId);
        return numberReservationProduct.getValue();
    }
    @Transactional
    public void updateProductsStorage(List<CartItemDto> items) {
        items.forEach(item -> {
            Long productId = item.getProductId();
            Product byId = productRepository.getById(productId);
            int quantity = byId.getQuantity();
            byId.setQuantity(quantity - item.getQuantity());
            productRepository.save(byId);
        });
    }
}
