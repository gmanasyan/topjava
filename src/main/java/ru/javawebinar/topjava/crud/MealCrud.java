package ru.javawebinar.topjava.crud;

import ru.javawebinar.topjava.model.Meal;
import java.util.List;

public interface MealCrud {

    Meal create(Meal meal);
    Meal update(int id);
    boolean delete(int id);
    Meal get(int id);
    List<Meal> getAll();

}