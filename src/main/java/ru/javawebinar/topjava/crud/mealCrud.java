package ru.javawebinar.topjava.crud;

import java.time.LocalDateTime;

public interface mealCrud {

    public boolean createMeal(LocalDateTime dateTime, String description, int calories);
    public boolean updateMeal(int id);
    public boolean deleteMeal(int id);

}
