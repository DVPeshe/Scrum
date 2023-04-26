package ru.agile.scrum.mst.market.core.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.agile.scrum.mst.market.api.OrderDto;
import ru.agile.scrum.mst.market.core.controllers.OrderController;
import ru.agile.scrum.mst.market.core.entities.Order;
import ru.agile.scrum.mst.market.core.mappers.OrderMapper;
import ru.agile.scrum.mst.market.core.services.OrderService;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    private static Order order1;
    private static Order order2;
    private static OrderDto orderDto1;
    private static OrderDto orderDto2;
    private static Principal principal;


    @Test
    public void getUserOrdersTest() throws Exception {
        order1 = Order.builder()
                .id(1L)
                .username("ethan")
                .totalPrice(BigDecimal.valueOf(10))
                .build();
        order2 = Order.builder()
                .id(2L)
                .username("ethan")
                .totalPrice(BigDecimal.valueOf(20))
                .build();
        orderDto1 = OrderDto.builder()
                .id(1L)
                .totalPrice(BigDecimal.valueOf(10))
                .build();
        orderDto2 = OrderDto.builder()
                .id(2L)
                .totalPrice(BigDecimal.valueOf(20))
                .build();

        List<Order> list = new ArrayList<Order>();
        list.add(order1);
        list.add(order2);
        given(orderService.findUserOrders("ethan")).willReturn(list);
        given(orderMapper.mapOrderToOrderDto(order1)).willReturn(orderDto1);
        given(orderMapper.mapOrderToOrderDto(order2)).willReturn(orderDto2);
        mvc
                .perform(
                        get("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "")
                                .header("username", "ethan")
                                .header("roles", "ROLE_USER")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.value[0].totalPrice", CoreMatchers.is(10)));
    }

    @Test
    public void clearUserOrdersTest() throws Exception {
        mvc
                .perform(
                        delete("/api/v1/orders/my")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "")
                                .header("username", "ethan")
                                .header("roles", "ROLE_USER")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void createNewOrderTest() throws Exception{
        mvc
                .perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "")
                                .header("username", "ethan")
                                .header("roles", "ROLE_USER")
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

}
