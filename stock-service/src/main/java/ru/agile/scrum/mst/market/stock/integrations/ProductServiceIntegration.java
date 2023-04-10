package ru.agile.scrum.mst.market.stock.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.agile.scrum.mst.market.api.ProductStockDto;

@Component
@RequiredArgsConstructor
public class ProductServiceIntegration {
    private final WebClient productServiceWebClient;

//    public void updateProductStockDB(ProductStockDto psd) {
//        System.out.println("test2: " + psd.getId() + ":" + psd.getQuantity());
//        productServiceWebClient.post()
//                .uri("/api/v1/products/updateProductStock")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(psd)
//                .retrieve()
//                .bodyToMono(Void.class)
//                .block();
//        System.out.println("test3");
//    }

    public void updateProductStockDB(ProductStockDto psd){
        productServiceWebClient.get()
                .uri("/api/v1/products/updateProductStock/" +psd.getId())
                .header("quantityStr", psd.getQuantity().toString())
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
