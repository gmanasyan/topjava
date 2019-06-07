package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.security.KeyStore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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

        List<UserMealWithExceed> userMealWithExceeds = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> userMealWithExceedsOpt1 = getFilteredWithExceededOptional1(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> userMealWithExceedsOpt2 = getFilteredWithExceededOptional2(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> userMealWithExceedsOpt4 = getFilteredWithExceededV4(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> userMealWithExceedsOpt5 = getFilteredWithExceededV5(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);





        userMealWithExceedsOpt4.forEach(System.out::println);
        userMealWithExceedsOpt5.forEach(System.out::println);


        //userMealWithExceeds.forEach(System.out::println);

        System.out.println("Base task");
        printUserMeal(userMealWithExceeds);

        System.out.println("Optional task 1");
        printUserMeal(userMealWithExceedsOpt1);

        System.out.println("Optional task 2");
        printUserMeal(userMealWithExceedsOpt2);

        System.out.println("Optional task 2");
        printUserMeal(userMealWithExceedsOpt2);

    }

    public static void printUserMeal(List<UserMealWithExceed> userMealWithExceeds) {
        if (userMealWithExceeds != null)
            for (UserMealWithExceed item : userMealWithExceeds)
                System.out.println(item.getDateTime() + " " + item.getDescription() + " " + item.getCalories() + " " + item.getExceed());
    }



    // getFilteredWithExceeded v5
    public static List<UserMealWithExceed> getFilteredWithExceededV5(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Collection<List<UserMeal>> list = mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate())).values();

        return list.stream().flatMap(dayMeals -> {
            boolean exceed = dayMeals.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;

            return dayMeals.stream().filter(meal ->
                    TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                    .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed));
        })
                .collect((Collectors.toList()));
    }


        // getFilteredWithExceeded v4
    public static List<UserMealWithExceed> getFilteredWithExceededV4(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {



        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        final Map<LocalDate, AtomicBoolean> exceededMap = new HashMap<>();

        final List<UserMealWithExceed> mealsWithExceeded = new ArrayList<>();

        mealList.forEach(meal -> {
            AtomicBoolean wrapBoolean = exceededMap.computeIfAbsent(meal.getDateTime().toLocalDate(), date -> new AtomicBoolean());
            Integer dailyCalories = caloriesSumByDate.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
            // map.merge("question", " Blabla", (oldVal, newVal) -> oldVal + newVal); складываются значения в лямбде.

            // Atomic Boolean will modify and automatically modified all elements in mailsWithExceede
            // Need to change in class UserMealWithExceeded the exeed value to Atomic Boolean

            if (dailyCalories > caloriesPerDay) {
                wrapBoolean.set(true);
            }
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealsWithExceeded.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        wrapBoolean.get()));
            }
        });

        return mealsWithExceeded;
    }


    // getFilteredWithExceeded v3
    public static List<UserMealWithExceed> getFilteredWithExceededV3(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> coloriesSumByDate = mealList.stream().collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .filter(um -> TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(),
                         coloriesSumByDate.get(um.getDateTime().toLocalDate())>caloriesPerDay))
                .collect(Collectors.toList());
    }


    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> datesList = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            LocalDate currentDate = userMeal.getDateTime().toLocalDate();
            datesList.put(userMeal.getDateTime().toLocalDate(), datesList.getOrDefault(currentDate, 0) + userMeal.getCalories());
        }

        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExceeds.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        datesList.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
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

        List<UserMealWithExceed> userMealWithExceeds = mealList
                .stream()
                .filter(s -> TimeUtil.isBetween(s.getDateTime().toLocalTime(), startTime, endTime))
                .map(s -> new UserMealWithExceed(s.getDateTime(), s.getDescription(), s.getCalories(), datesList.get(s.getDateTime().toLocalDate()) > caloriesPerDay ? true : false))
                .collect(Collectors.toList());

        return userMealWithExceeds;
    }

    // Optional 2
    public static List<UserMealWithExceed> getFilteredWithExceededOptional2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        Map<LocalDate, Integer> datesList = new HashMap<>();

        for (UserMeal userMeal : mealList) {
            LocalDate currentDate = userMeal.getDateTime().toLocalDate();
            datesList.put(userMeal.getDateTime().toLocalDate(), datesList.getOrDefault(currentDate, 0) + userMeal.getCalories());
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                userMealWithExceeds.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false));
        }

        for (UserMealWithExceed user : userMealWithExceeds) {
            if (datesList.get(user.getDateTime().toLocalDate()) > caloriesPerDay) {
                userMealWithExceeds.remove(userMealWithExceeds.indexOf(user));
                userMealWithExceeds.add(new UserMealWithExceed(user.getDateTime(), user.getDescription(), user.getCalories(), true));
            }
        }

        return userMealWithExceeds;
    }

}
