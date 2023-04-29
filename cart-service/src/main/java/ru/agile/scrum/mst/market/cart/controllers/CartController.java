package ru.agile.scrum.mst.market.cart.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.agile.scrum.mst.market.api.CartDto;
import ru.agile.scrum.mst.market.api.IntegerResponse;
import ru.agile.scrum.mst.market.api.StringResponse;
import ru.agile.scrum.mst.market.cart.common.SelectorId;
import ru.agile.scrum.mst.market.cart.mappers.CartMapper;
import ru.agile.scrum.mst.market.cart.services.CartService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartMapper cartMapper;
    private final SelectorId selectorId;

    @GetMapping("/generate_id")
    public StringResponse generateGuestCartId() {
        return new StringResponse(UUID.randomUUID().toString());
    }

    @GetMapping("/{guestCartId}")
    public CartDto getCurrentCart(@RequestHeader(required = false) String username, @PathVariable String guestCartId) {
        Runnable copy = () -> cartService.copyCart(username, guestCartId);
        String idCart = selectorId.selectCart(username, guestCartId, copy);
        return cartMapper.mapCartToCartDto(cartService.getUserCart(idCart));
    }

    @GetMapping("/{guestCartId}/add/{productId}")
    public CartDto addProductToCart(@RequestHeader(required = false) String username, @PathVariable String guestCartId,
                                    @PathVariable Long productId) {
        String idCart = selectorId.selectCart(username, guestCartId);
        cartService.addToCart(idCart, productId);
        return cartMapper.mapCartToCartDto(cartService.getUserCart(idCart));
    }

    @GetMapping("/{guestCartId}/clear")
    public void clearCurrentCart(@RequestHeader(required = false) String username, @PathVariable String guestCartId) {
        String idCart = selectorId.selectCart(username, guestCartId);
        cartService.clearCart(idCart);
    }

    @GetMapping("/{guestCartId}/delete/{productId}")
    public void deleteProductToCart(@RequestHeader(required = false) String username, @PathVariable String guestCartId,
                                    @PathVariable Long productId) {
        String idCart = selectorId.selectCart(username, guestCartId);
        cartService.removeFromCart(idCart, productId);
    }

    @GetMapping("/{guestCartId}/decrement/{productId}")
    public void decrementQuantityProduct(@RequestHeader(required = false) String username, @PathVariable String guestCartId,
                                         @PathVariable Long productId) {
        String idCart = selectorId.selectCart(username, guestCartId);
        cartService.decrementQuantity(idCart, productId);
    }

    @GetMapping("/reservation-product/{productId}")
    public IntegerResponse getNumberReservationProduct(@PathVariable Long productId) {
        return new IntegerResponse(cartService.getNumberReservationProduct(productId));
    }
}
