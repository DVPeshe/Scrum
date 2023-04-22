package ru.agile.scrum.mst.market.image.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class ProductServiceIntegration {
    private final WebClient productServiceWebClient;

    public void updateImage(Long productId, String imageId, Principal principal) {
        productServiceWebClient.put()
                .uri("api/v1/products/" + productId + "/images")
                .header("username", principal.getName())
                .header("roles", getRolesStringFromTokenSecurity(principal))
                .body(Mono.just(imageId), String.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private String getRolesStringFromTokenSecurity(Principal principal) {
        String tokenSecurity = principal.toString();
        int start = tokenSecurity.indexOf("Granted Authorities");
        String str = tokenSecurity.substring(start);
        int end = str.indexOf("]") + 1;
        return str.substring(0, end).replace("Granted Authorities=", "");
    }
}
