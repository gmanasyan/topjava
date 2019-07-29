package ru.javawebinar.topjava.web.meal;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.web.meal.MealRestController.REST_URL;

class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    protected MealService service;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1));
    }

    @Test
    void getTest() throws Exception {
        mockMvc.perform(get(REST_URL+"/100007"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL6));
    }

    @Test
    void deleteTest() throws Exception {
        mockMvc.perform(delete(REST_URL+"/100007"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL5, MEAL4, MEAL3, MEAL2, MEAL1));
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        mockMvc.perform(put(REST_URL+"/100002").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get(REST_URL+"/100002"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(updated));
    }

    @Test
    void create() throws Exception {
        Meal newMeal = getCreated();
        mockMvc.perform(post(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        newMeal.setId(service.get(100010, USER_ID).getId());

        mockMvc.perform(get(REST_URL+"/100010"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(newMeal));
    }

    @Test
    void getBetween() throws Exception {
        mockMvc.perform(get(REST_URL+"/filter?startDate=2015-05-31T10:15:30"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(contentJson(MEAL6, MEAL5, MEAL4));
    }
}