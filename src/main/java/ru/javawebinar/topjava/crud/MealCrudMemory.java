package ru.javawebinar.topjava.crud;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class MealCrudMemory implements MealCrud {

    private static int mealCount = 0;

    private static MealCrudMemory singleton;
    private List<Meal> meals = new ArrayList<>();

    private MealCrudMemory() {
        meals.add(new Meal(MealCrudMemory.setId(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(MealCrudMemory.setId(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(MealCrudMemory.setId(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(MealCrudMemory.setId(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(MealCrudMemory.setId(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(MealCrudMemory.setId(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    public static MealCrudMemory getInstance() {
        if (singleton == null) singleton = new MealCrudMemory();
        return singleton;
    }


    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return meals;
    }

    @Override
    public boolean createMeal(Meal meal) {
        return false;
    }

    @Override
    public Meal updateMeal(int id) {
        return null;
    }

    @Override
    public boolean deleteMeal(int id) {

        boolean success = false;
        try {
            meals.remove(findMealById(id));
            success = true;
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    private Meal findMealById(int id) {

        for (Meal item: meals) {
            if (item.getId() == id) return item;
        }

        return null;
    }


    public static int setId() {
        return mealCount++;
    }



}
