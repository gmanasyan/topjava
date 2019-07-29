package ru.javawebinar.topjava.web;

import jdk.jfr.ContentType;
import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.MealTestData.assertMatch;

public class ResourceControllerTest extends AbstractControllerTest{

    @Test
    void testMeals() throws Exception {
        mockMvc.perform(get("/resources/css/style.css"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/css"));
    }

}
