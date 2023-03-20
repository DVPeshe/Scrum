package ru.agile.scrum.mst.market.core.mappers;

import org.springframework.stereotype.Component;
import ru.agile.scrum.mst.market.api.CategoryDto;
import ru.agile.scrum.mst.market.core.entities.Category;

@Component
public class CategoryMapper {

    public CategoryDto mapCategoryToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .build();
    }
}
