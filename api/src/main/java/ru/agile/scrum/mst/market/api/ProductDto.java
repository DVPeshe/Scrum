package ru.agile.scrum.mst.market.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "Модель продукта")
public class ProductDto {
    @Schema(description = "ID продукта", required = true, example = "1")
    private Long id;

    @Schema(description = "Название продукта", required = true, maxLength = 255, minLength = 3, example = "Коробка конфет")
    private String title;

    @Schema(description = "Цена продукта", required = true, example = "120.00")
    private BigDecimal price;

    @Schema(description = "Видимость продукта", required = true, example = "true")
    private boolean visible;

    @Schema(description = "Категория продукта", required = true, example = "Еда")
    private String categoryTitle;
}
