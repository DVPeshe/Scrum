package ru.agile.scrum.mst.market.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.agile.scrum.mst.market.api.CategoryDto;
import ru.agile.scrum.mst.market.core.entities.Category;
import ru.agile.scrum.mst.market.core.mappers.CategoryMapper;
import ru.agile.scrum.mst.market.core.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Optional<Category> findByTitle(String title) {
        return categoryRepository.findByTitle(title);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<String> getTitleCategory() {
        return getAllCategories().stream().map(Category::getTitle).collect(Collectors.toList());
    }
}
