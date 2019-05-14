package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.security.KeyStore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        // List<UserMealWithExceed> userMealWithExceeds = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        // .toLocalDate();
        // .toLocalTime();

        List<UserMealWithExceed> userMealWithExceeds = getFilteredWithExceededOptional1(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        if (userMealWithExceeds != null)
            for (UserMealWithExceed item : userMealWithExceeds) {
                System.out.println(item.getDateTime() + " " + item.getDescription() + " " + item.getCalories() + " " + item.getExceed());
            }
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        Map<LocalDate, Integer> datesList = new HashMap<>();

        for (UserMeal userMeal : mealList) {
            LocalDate currentDate = userMeal.getDateTime().toLocalDate();
            int caloriesDate = 0;
            try {
                caloriesDate = datesList.get(currentDate);
            } catch (Exception e) {
            }
            datesList.put(userMeal.getDateTime().toLocalDate(), caloriesDate + userMeal.getCalories());
        }

        for (UserMeal userMeal : mealList) {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                if (datesList.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay)
                    userMealWithExceeds.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
                else
                    userMealWithExceeds.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false));
            }
        }

        return userMealWithExceeds;
    }

    // Optional 1
    public static List<UserMealWithExceed> getFilteredWithExceededOptional1(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> datesList = mealList
                .stream()
                .map(s -> new UserMeal(s.getDateTime(), s.getDescription(), s.getCalories()))
                .collect(Collectors.groupingBy(s -> s.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));

        datesList.forEach((k, v) -> System.out.println(k + " " + v));

        List<UserMealWithExceed> userMealWithExceeds = mealList
                .stream()
                .filter(s -> TimeUtil.isBetween(s.getDateTime().toLocalTime(), startTime, endTime))
                .map(s -> new UserMealWithExceed(s.getDateTime(), s.getDescription(), s.getCalories(), datesList.get(s.getDateTime().toLocalDate()) > caloriesPerDay ? true : false))
                .collect(Collectors.toList());

        return userMealWithExceeds;
    }

}
