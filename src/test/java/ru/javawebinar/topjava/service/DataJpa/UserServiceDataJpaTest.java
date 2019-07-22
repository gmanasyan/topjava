package ru.javawebinar.topjava.service.DataJpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import static ru.javawebinar.topjava.MealTestData.*;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles({Profiles.DATAJPA})
public class UserServiceDataJpaTest extends UserServiceTest {

    @Test
    public void getWithMeals() throws Exception {
        User user = service.getWithMeals(ADMIN_ID);

        List<Meal> mealsAdmin = List.of(ADMIN_MEAL2, ADMIN_MEAL1);
        List<Meal> mealsUser = user.getMeals();

        user.setMeals(null);

        assertMatch(user, ADMIN);
        assertMatch(mealsUser, mealsAdmin);
    }
}
