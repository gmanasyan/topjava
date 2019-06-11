package ru.javawebinar.topjava.crud;

import java.time.LocalDateTime;

public class mealCrudMemory implements mealCrud {

    private static int mealCount = 0;

    @Override
    public boolean createMeal(LocalDateTime dateTime, String description, int calories) {
        return false;
    }

    @Override
    public boolean updateMeal(int id) {
        return false;
    }

    @Override
    public boolean deleteMeal(int id) {
        return false;
    }

    public static int setId() {
        return mealCount++;
    }


}
