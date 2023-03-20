package ru.agile.scrum.mst.market.cart.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.api.ProductDto;
import ru.agile.scrum.mst.market.cart.integrations.ProductServiceIntegration;
import ru.agile.scrum.mst.market.cart.utils.Cart;
import ru.agile.scrum.mst.market.cart.utils.CartItem;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductServiceIntegration productServiceIntegration;
    private final RedisTemplate<String, Object> redisTemplate;

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
            cart.add(p);
        });
    }

    public void decrementQuantity(String idCart, Long productId) {
        execute(idCart, cart -> {
            cart.reduceQuantity(productId);
        });
    }

    public void removeFromCart(String idCart, Long productId) {
        execute(idCart, cart -> cart.remove(productId));
    }

    public void clearCart(String idCart) {
        execute(idCart, Cart::clear);
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
}
