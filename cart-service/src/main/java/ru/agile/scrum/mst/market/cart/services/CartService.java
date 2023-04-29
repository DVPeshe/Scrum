package ru.agile.scrum.mst.market.cart.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.cart.exceptions.ResourceNotFoundException;
import ru.agile.scrum.mst.market.cart.exceptions.TheProductHasNotBeenAddedToTheCartException;
import ru.agile.scrum.mst.market.cart.integrations.ProductServiceIntegration;
import ru.agile.scrum.mst.market.cart.utils.Cart;
import ru.agile.scrum.mst.market.cart.utils.CartItem;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductServiceIntegration productServiceIntegration;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String prefix = "B";

    public Cart getUserCart(String idCart) {
        if (!redisTemplate.hasKey(idCart)) {
            Cart cart = new Cart();
            redisTemplate.opsForValue().set(idCart, cart);
        }
        return (Cart) redisTemplate.opsForValue().get(idCart);
    }

    public void addToCart(String idCart, Long productId) {
        execute(idCart, cart -> {
            ProductDto p = productServiceIntegration.findById(productId);
            reservationProducts(p, 1);
            cart.add(p);
        });
    }

    public void decrementQuantity(String idCart, Long productId) {
        execute(idCart, cart -> {
            ProductDto p = productServiceIntegration.findById(productId);
            cancelReservationProducts(p, 1);
            cart.reduceQuantity(productId);
        });
    }

    public void removeFromCart(String idCart, Long productId) {
        execute(idCart, cart -> {
            String key = prefix + productId;
            redisTemplate.delete(key);
            cart.remove(productId);
        });
    }

    public void clearCart(String idCart) {
        execute(idCart, cart -> {
            cart.getItems().forEach(cartItem -> {
                String key = prefix + cartItem.getProductId();
                redisTemplate.delete(key);
            });
            cart.clear();
        });
    }

    public void copyCart(String userId, String guestId) {
        Cart guestCart = getUserCart(guestId);
        if (!guestCart.getItems().isEmpty()) {
            Consumer<Cart> cartConsumer = cart -> {
                for (CartItem cartItem : guestCart.getItems()) {
                    cart.getItems().add(cartItem);
                }
                cart.getTotalPrice().add(guestCart.getTotalPrice());
            };
            execute(userId, cartConsumer);
            clearCart(guestId);
        }
    }

    private void execute(String idCart, Consumer<Cart> action) {
        Cart cart = getUserCart(idCart);
        action.accept(cart);
        redisTemplate.opsForValue().set(idCart, cart);
    }

    private void reservationProducts(ProductDto productDto, int value) {
        if (productDto.getId() < 1) throw new ResourceNotFoundException("Продукта нет на складе.");
        String key = prefix + productDto.getId();
        Integer newQuantity = 0;
        if (!redisTemplate.hasKey(key)) {
            newQuantity += value;
        } else {
            Integer quantity = (Integer) redisTemplate.opsForValue().get(key);
            newQuantity = quantity + value;
            if (newQuantity > productDto.getQuantity()) throw new TheProductHasNotBeenAddedToTheCartException("Продукт закончился на складе.");
        }
        redisTemplate.opsForValue().set(key, newQuantity);
    }

    private void cancelReservationProducts(ProductDto productDto, int value) {
        String key = prefix + productDto.getId();
        Integer newQuantity = 0;
        Integer quantity = (Integer) redisTemplate.opsForValue().get(key);
        newQuantity = quantity - value;
        if (newQuantity > productDto.getQuantity()) throw new TheProductHasNotBeenAddedToTheCartException("Ошибка клиента.");
        else if (newQuantity == 0) {
            newQuantity = 1;
        }
        redisTemplate.opsForValue().set(key, newQuantity);
    }

    public Integer getNumberReservationProduct(Long productId) {
        String key = prefix + productId;
        if (!redisTemplate.hasKey(key)) {
            return 0;
        }
        return (Integer) redisTemplate.opsForValue().get(key);
    }
}
