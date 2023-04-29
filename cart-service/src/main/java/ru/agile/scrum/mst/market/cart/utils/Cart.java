package ru.agile.scrum.mst.market.cart.utils;

import lombok.Data;
import ru.agile.scrum.mst.market.api.ProductDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private List<CartItem> items;
    private BigDecimal totalPrice;

    public Cart() {
        this.items = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    public void add(ProductDto p) {
        for (CartItem item : items) {
            if (item.getProductId().equals(p.getId())) {
                item.incrementQuantity();
                recalculate();
                return;
            }
        }
        CartItem cartItem = CartItem.builder()
                .productId(p.getId())
                .productTitle(p.getTitle())
                .quantity(1)
                .pricePerProduct(p.getPrice())
                .price(p.getPrice())
                .build();
        items.add(cartItem);
        recalculate();
    }

    public void remove(Long productId) {
        if (items.removeIf(p -> p.getProductId().equals(productId))) {
            recalculate();
        }
    }

    public void reduceQuantity(Long productId) {
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                item.decrementQuantity();
                recalculate();
                return;
            }
        }
    }

    public void clear() {
        items.clear();
        totalPrice = BigDecimal.ZERO;
    }

    private void recalculate() {
        totalPrice = BigDecimal.ZERO;
        items.forEach(i -> totalPrice = totalPrice.add(i.getPrice()));
    }
}
