package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import org.springframework.stereotype.Repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private static final Logger log = getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();   // Map(userId, Map<mealId, meal>)

    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    private void save(Meal meal) {
        save(meal, 1);
    }

    @Override
    public Meal save(Meal meal, int userId) {

        if (meal.isNew()) {
            log.info("New meal {}", meal);
            meal.setId(counter.incrementAndGet());

            Map<Integer, Meal> meals = repository.getOrDefault(userId, new HashMap<>());

            meals.put(meal.getId(), meal);

            repository.merge(userId, meals, (oldMeals, newMeals) -> newMeals);
            return meal;
        }

        if ((repository.get(userId) != null) && (repository.get(userId).get(meal.getId()) == null )) {
            log.error("Access denaid to update not user meal {}", userId, meal);
            return null;
        }

        log.info("Update meal {}", meal);
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if ((repository.get(userId) != null) && (repository.get(userId).get(id) != null)) {
            return repository.get(userId).remove(id) != null;
        }
        else
            return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("Get meal {}", id);
        if ((repository.get(userId) != null) && (repository.get(userId).get(id) != null))
            return repository.get(userId).get(id);
        else
            return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.get(userId).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return repository.get(userId).values().stream()
                .filter(meal -> DateTimeUtil.isBetweenDate(meal.getDate(), startDate, endDate))
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)
                )
                .collect(Collectors.toList());
    }

}

