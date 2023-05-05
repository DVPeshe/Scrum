package ru.agile.scrum.mst.market.core.tests;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.agile.scrum.mst.market.core.controllers.CategoryController;
import ru.agile.scrum.mst.market.core.entities.Category;
import ru.agile.scrum.mst.market.core.services.CategoryService;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryService categoryService;

    private static Category category;
    private static Category category2;

    @Test
    public void getAllCategoryTest() throws Exception {
        category = Category.builder()
                .id(1L)
                .title("Еда")
                .build();

        category2 = Category.builder()
                .id(2L)
                .title("Ноутбуки")
                .build();
        List<String> list = new ArrayList<String>();
        list.add(category.getTitle());
        list.add(category2.getTitle());

        given(categoryService.getTitleCategory()).willReturn(list);
        mvc
                .perform(
                        get("/api/v1/categories/titles")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.value", CoreMatchers.is(list)));
    }

}
