package ru.javawebinar.topjava.service;


import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.MealTestData.*;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles({"datajpa"})
public class UserServiceDataJpaTest extends UserServiceTest {

    @Test
    @Override
    public void get() throws Exception {
        User user = service.get(ADMIN_ID);

        List<Meal> meals = new ArrayList<>();
        meals.add(ADMIN_MEAL2);
        meals.add(ADMIN_MEAL1);
        ADMIN.setMeals(meals);

        assertMatch(user, ADMIN);
    }
}
