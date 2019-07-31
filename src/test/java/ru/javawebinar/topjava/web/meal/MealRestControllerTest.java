package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.contentJson;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.meal.MealRestController.REST_URL;

class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    protected MealService service;

    @Test
    void getAllTest() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(getMealTo(MEAL6), getMealTo(MEAL5), getMealTo(MEAL4), getMealTo(MEAL3), getMealTo(MEAL2), getMealTo(MEAL1)));
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
        assertMatch(service.getAll(USER_ID), MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void updateTest() throws Exception {
        Meal updated = getUpdated();
        mockMvc.perform(put(REST_URL+"/" + MEAL1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void createTest() throws Exception {
        Meal newMeal = getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andDo(print())
                .andExpect(status().isCreated());

        Meal returned = readFromJson(action, Meal.class);
        newMeal.setId(returned.getId());

        assertMatch(returned, newMeal);
    }

    /* Test for Second version */
//    @Test
//    void getBetween() throws Exception {
//        mockMvc.perform(get(REST_URL+"/filter?startDate=2015-05-31T10:15:30"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(contentJson(MEAL6, MEAL5, MEAL4));
//    }


    /* Test for third version */
    @Test
    void getBetweenTest() throws Exception {
        mockMvc.perform(get(REST_URL+"/filter?startDate=2015-05-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(contentJson(getMealTo(MEAL6), getMealTo(MEAL5), getMealTo(MEAL4)));
    }

}