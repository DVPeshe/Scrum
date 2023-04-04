package ru.agile.scrum.mst.market.email.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.agile.scrum.mst.market.api.ProductDto;

@Component
@RequiredArgsConstructor
public class ProductServiceIntegration {


    private final WebClient productServiceWebClient;

    public ProductDto findById(Long id) {
        return productServiceWebClient.get()
                .uri("/api/v1/products/" + id)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }

}
