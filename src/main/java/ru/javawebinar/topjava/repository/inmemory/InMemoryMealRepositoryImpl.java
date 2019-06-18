package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import org.springframework.stereotype.Repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private static final Logger log = getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private Map<Integer, Integer> userOfMeal = new ConcurrentHashMap<>();   // Map(mealId, userId)

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
            repository.put(meal.getId(), meal);
            userOfMeal.put(meal.getId(), userId);
            return meal;
        }

        // user try update not his meal
        if (userOfMeal.get(meal.getId()) != userId) {
            log.error("Access denaid to update not user meal {}", userId, meal);
            return null;
        }

        log.info("Update meal {}", meal);
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
        log.info("Get meal {}", id);
        if (userOfMeal.get(id) == userId) {
            return repository.get(id);
        }
        else
            return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.entrySet().stream()
                .filter(setItem -> userOfMeal.get(setItem.getKey()).equals(userId))
                .map(setItem -> setItem.getValue())
                //.sorted(Comparator.comparing(meal -> meal.getDate()))
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime())) //reverse
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {

        return repository.entrySet().stream()
                .filter(setItem -> userOfMeal.get(setItem.getKey()).equals(userId))
                .map(setItem -> setItem.getValue())
                .filter(meal -> (meal.getDate().isEqual(startDate)
                        || meal.getDate().isEqual(endDate)
                        || (meal.getDate().isAfter(startDate) && meal.getDate().isBefore(endDate))
                ))
                .filter(meal -> (meal.getTime().compareTo(startTime) >= 0)
                        && (meal.getTime().compareTo(endTime) <= 0)
                )
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime())) //reverse
                .collect(Collectors.toList());
    }


}

