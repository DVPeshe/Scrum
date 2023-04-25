package ru.agile.scrum.mst.market.core.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.agile.scrum.mst.market.api.CategoryDto;
import ru.agile.scrum.mst.market.core.entities.Category;
import ru.agile.scrum.mst.market.core.mappers.CategoryMapper;
import ru.agile.scrum.mst.market.core.repositories.CategoryRepository;
import ru.agile.scrum.mst.market.core.services.CategoryService;
import java.util.*;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryMapper categoryMapper;

    @MockBean
    private CategoryRepository categoryRepository;

    private static Category category;
    private static Category category2;
    private static CategoryDto categoryDto;
    private static CategoryDto categoryDto2;

    @BeforeAll
    public static void initEntities() {
        category = Category.builder()
                .id(1L)
                .title("Еда")
                .build();

        category2 = Category.builder()
                .id(2L)
                .title("Ноутбуки")
                .build();

        categoryDto = CategoryDto.builder()
                .id(1L)
                .title("Еда")
                .build();

        categoryDto2 = CategoryDto.builder()
                .id(2L)
                .title("Ноутбуки")
                .build();
    }

    @Test
    public void findCategoryByTitleTest(){
        Mockito.doReturn(Optional.of(category)).when(categoryRepository).findByTitle("Еда");
        String title = "Еда";

        Category newCategory = categoryService.findByTitle(title).orElse(new Category());

        Assertions.assertEquals(1L, newCategory.getId());
        Mockito.verify(categoryRepository, Mockito.times(1)).findByTitle(ArgumentMatchers.any());
    }

    @Test
    public void getAllCategoriesTest(){
        List<Category> list = new ArrayList<Category>();
        list.add(category);
        list.add(category2);
        Mockito.doReturn(list).when(categoryRepository).findAll();

        List<Category> list1 = categoryService.getAllCategories();

        Assertions.assertEquals(list1.get(1).getTitle(), "Ноутбуки");
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getTitleCategoryTest(){
        List<Category> list = new ArrayList<Category>();
        list.add(category);
        list.add(category2);
        Mockito.doReturn(list).when(categoryRepository).findAll();

        List<String> list1 = categoryService.getTitleCategory();

        Assertions.assertEquals(list1.get(1), "Ноутбуки");
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
    }
}
