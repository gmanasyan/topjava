package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles({"datajpa"})
public class MealServiceDataJpaTest extends MealServiceTest {

}
