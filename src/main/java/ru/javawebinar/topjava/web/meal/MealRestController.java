package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public Meal get(int id) throws NotFoundException {
        return service.get(id, authUserId());
    }

    public List<MealTo> getAll() {
        return MealsUtil.getWithExcess(service.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllByRange(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {

        if (startDate == null) startDate = LocalDate.MIN;
        if (endDate == null) endDate = LocalDate.MAX;

        List<MealTo> mealsWithExcess = MealsUtil.getWithExcess(
                service.getAll(authUserId(), startDate, LocalTime.MIN, endDate, LocalTime.MAX),
                MealsUtil.DEFAULT_CALORIES_PER_DAY);

        log.info("getAllByRange items {}", mealsWithExcess.size());

        if (startTime == null) startTime = LocalTime.MIN;
        if (endTime == null) endTime = LocalTime.MAX;

        LocalTime start = startTime;
        LocalTime end = endTime;

        return mealsWithExcess.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), start, end))
                .collect(Collectors.toList());
    }

    public void delete (int id) throws NotFoundException {
        service.delete(id, authUserId());
    }

    public Meal save (Meal meal) throws NotFoundException {
        return service.save(meal, authUserId());
    }





}