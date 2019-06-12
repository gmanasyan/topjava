package ru.javawebinar.topjava.crud;

import ru.javawebinar.topjava.model.Meal;
import java.util.List;

public interface MealCrud {

    public boolean createMeal(Meal meal);
    public Meal updateMeal(int id);
    public boolean deleteMeal(int id);
    public Meal get(int id);
    public List<Meal> getAll();

}