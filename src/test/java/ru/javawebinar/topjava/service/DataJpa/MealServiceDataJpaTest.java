package ru.javawebinar.topjava.service.DataJpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;

import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles({Profiles.DATAJPA})
public class MealServiceDataJpaTest extends MealServiceTest {

}
