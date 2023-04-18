package ru.agile.scrum.mst.market.image.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductServiceIntegration {
    private final WebClient productServiceWebClient;

    public void updateImage(Long productId, String imageId) {
        productServiceWebClient.put()
                .uri("api/v1/products/" + productId + "/images")
                .body(Mono.just(imageId), String.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
