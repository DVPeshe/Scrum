package ru.agile.scrum.mst.market.stock.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.api.JwtResponse;
import ru.agile.scrum.mst.market.api.ProductStockDto;
import ru.agile.scrum.mst.market.stock.integrations.AuthServiceIntegration;
import ru.agile.scrum.mst.market.stock.integrations.ProductServiceIntegration;
import ru.agile.scrum.mst.market.stock.soap.productstocks.ProductStock;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ProductStockService {
    final ProductServiceIntegration productServiceIntegration;
    final AuthServiceIntegration authServiceIntegration;

    public static final Function<ProductStock, ProductStockDto> functionSoapToDto = ps -> {
        ProductStockDto psd = new ProductStockDto();
        psd.setId(ps.getId());
        psd.setQuantity(ps.getQuantity());
        return psd;
    };

    public void updateProductStockDB(ProductStock productStock){
        productServiceIntegration.updateProductStockDB(functionSoapToDto.apply(productStock));
    }

    public String tryAuth(String username, String password){
        JwtResponse jwtResponse = authServiceIntegration.tryAuth(username, password);
        return jwtResponse.getToken();
    }
}
