package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;

@ActiveProfiles({"jpa"})
//@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public class MealServiceJpaTest extends MealServiceTest {

}
