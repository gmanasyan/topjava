package ru.javawebinar.topjava;


import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;


public class MealTestData {

    public static final List<Meal> MEALS = new ArrayList<>();

    public static final Meal MEAL6 = new Meal(START_SEQ + 2, LocalDateTime.of(2019, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL5 = new Meal(START_SEQ + 3,LocalDateTime.of(2019, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL4 = new Meal(START_SEQ + 4,LocalDateTime.of(2019, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL3 = new Meal(START_SEQ + 5,LocalDateTime.of(2019, Month.MAY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL2 = new Meal(START_SEQ + 6,LocalDateTime.of(2019, Month.MAY, 31, 13, 0), "Обед", 500);
    public static final Meal MEAL1 = new Meal(START_SEQ + 7,LocalDateTime.of(2019, Month.MAY, 31, 20, 0), "Ужин", 510);

    static {
        MEALS.add(MEAL1);
        MEALS.add(MEAL2);
        MEALS.add(MEAL3);
        MEALS.add(MEAL4);
        MEALS.add(MEAL5);
        MEALS.add(MEAL6);

    }


    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
    }
}
