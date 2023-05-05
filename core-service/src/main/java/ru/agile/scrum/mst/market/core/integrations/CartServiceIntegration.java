package ru.agile.scrum.mst.market.core.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.agile.scrum.mst.market.api.CartDto;
import ru.agile.scrum.mst.market.api.IntegerResponse;

@Component
@RequiredArgsConstructor
public class CartServiceIntegration {
    private final WebClient cartServiceWebClient;

    public CartDto getCurrentUserCart(String username, String tokenSecurity) {
        return cartServiceWebClient.get()
                .uri("/api/v1/cart/0")
                .header("username", username)
                .header("roles", getRolesStringFromTokenSecurity(tokenSecurity))
                .retrieve()
                .bodyToMono(CartDto.class)
                .block();
    }

    public void clearCart(String username, String tokenSecurity) {
        cartServiceWebClient.get()
                .uri("/api/v1/cart/0/clear")
                .header("username", username)
                .header("roles", getRolesStringFromTokenSecurity(tokenSecurity))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public IntegerResponse getNumberReservationProduct(Long productId) {
        return cartServiceWebClient.get()
                .uri("/api/v1/cart/reservation-product/" + productId)
                .retrieve()
                .bodyToMono(IntegerResponse.class)
                .block();
    }

    private String getRolesStringFromTokenSecurity(String tokenSecurity) {
        int start = tokenSecurity.indexOf("Granted Authorities");
        String str = tokenSecurity.substring(start);
        int end = str.indexOf("]") + 1;
        return str.substring(0, end).replace("Granted Authorities=", "");
    }
}
