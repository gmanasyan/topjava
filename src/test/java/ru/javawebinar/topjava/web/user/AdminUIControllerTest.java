package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.user.ProfileRestController.REST_URL;

class AdminUIControllerTest extends AbstractControllerTest {

    public static final String AJAX_URL = "/ajax/admin/users";

    @Test
    void activityUpdate() throws Exception {
        User updated = ADMIN;
        updated.setEnabled(false);

        mockMvc.perform(MockMvcRequestBuilders.post(AJAX_URL+"/activity").contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(ADMIN_ID))
                .param("active", String.valueOf(0)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(userService.get(ADMIN_ID), updated);

    }
}