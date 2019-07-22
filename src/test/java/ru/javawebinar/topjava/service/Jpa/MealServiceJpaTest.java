package ru.javawebinar.topjava.service.Jpa;


import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles({Profiles.JPA})
//@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public class MealServiceJpaTest extends MealServiceTest {



}
