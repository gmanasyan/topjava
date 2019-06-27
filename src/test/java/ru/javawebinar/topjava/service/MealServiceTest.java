package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))

public class MealServiceTest {


    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL6.getId(), USER_ID);
        assertMatch(meal, MEAL6);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        Meal meal = service.get(MEAL6.getId(), ADMIN_ID);
    }

    @Test
    public void delete() throws Exception  {
        service.delete(MEAL6.getId(), USER_ID);
        List<Meal> meals = new ArrayList<>(MEALS);
        meals.remove(5);
        assertMatch(service.getAll(USER_ID), meals);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(MEAL6.getId(), ADMIN_ID);

    }

    @Test
    public void getBetweenDates() {
        List<Meal> all = service.getBetweenDates(LocalDate.of(2019, Month.MAY, 1), LocalDate.of(2019, Month.MAY, 30),  USER_ID);
        assertMatch(all, MEALS.subList(3, 6));
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> all = service.getBetweenDateTimes(LocalDateTime.of(2019, Month.MAY, 1, 10, 0), LocalDateTime.of(2019, Month.MAY, 31,12, 0 ),  USER_ID);
        assertMatch(all,  MEALS.subList(2, 6));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, MEALS);
    }

    @Test
    public void update() {
        Meal updated = new Meal(MEAL1);
        updated.setCalories(3000);
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        Meal updated = new Meal(MEAL1);
        updated.setCalories(3000);
        service.update(updated, ADMIN_ID);

    }

    @Test
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2019, Month.JUNE, 24, 10, 0), "Завтрак", 1500);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.get(created.getId(), USER_ID), newMeal);

    }
}