package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private Map<Integer, Integer> userOfMeal = new ConcurrentHashMap<>();   // Map(mealId, userId)

    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    private void save(Meal meal) {
        save(meal, 0);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            userOfMeal.put(meal.getId(), userId);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (userOfMeal.get(id).equals(userId)) {
            userOfMeal.remove(id);
            return repository.remove(id) != null;
        }
        else
            return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (userOfMeal.get(id).equals(userId))
            return repository.get(id);
        else
            return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.entrySet().stream()
                .filter(setItem -> userOfMeal.get(setItem.getKey()).equals(userId))
                .map(setItem -> setItem.getValue())
                //.sorted(Comparator.comparing(meal -> meal.getDate()))
                .sorted((meal1, meal2) -> meal2.getDate().compareTo(meal1.getDate())) //reverse
                .collect(Collectors.toList());
    }

}

