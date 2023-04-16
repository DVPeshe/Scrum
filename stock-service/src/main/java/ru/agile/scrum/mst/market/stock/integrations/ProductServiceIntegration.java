package ru.agile.scrum.mst.market.stock.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import ru.agile.scrum.mst.market.api.ProductStockDto;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class ProductServiceIntegration {
    private final WebClient productServiceWebClient;

    public void updateProductStockDB(ProductStockDto psd) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnR1ciIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdLCJleHAiOjE2ODE1MjMwNzIsImlhdCI6MTY4MTQ4NzA3Mn0.AkiP8gcSjMepPyyybVbft0R5q8A2KqkRyU585VwTvp0";
        LinkedMultiValueMap mvmap = new LinkedMultiValueMap<>();
        mvmap.add("Authorization", "Bearer " + token);
        mvmap.add("username", "artur");
        mvmap.add("roles", "ROLE_MANAGER");
        Consumer<HttpHeaders> consumer = it -> it.addAll(mvmap);

        productServiceWebClient.post()
                .uri("/api/v1/products/updateProductStock")
                .headers(consumer)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(psd)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }



}
