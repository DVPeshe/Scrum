package ru.agile.scrum.mst.market.stock.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.agile.scrum.mst.market.api.JwtResponse;
import ru.agile.scrum.mst.market.stock.services.ProductStockService;
import ru.agile.scrum.mst.market.stock.soap.productstocks.*;

@Endpoint
@RequiredArgsConstructor
public class ProductStockController {
    private static final String NAMESPACE_URI = "http://www.flamexander.com/spring/ws/productStocks";
    private final ProductStockService productStockService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateProductStocksDBRequest")
    @ResponsePayload
    public UpdateProductStocksDBResponse updateProductStocksDB(@RequestPayload UpdateProductStocksDBRequest request) {
        UpdateProductStocksDBResponse response = new UpdateProductStocksDBResponse();

        request.getProductStocks().forEach(ps -> productStockService.updateProductStockDB(ps));
        return response;
    }
}
