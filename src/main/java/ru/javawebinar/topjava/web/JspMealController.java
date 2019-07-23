package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class JspMealController {

    @Autowired
    private MealService service;

    private int userId = SecurityUtil.authUserId();

    @PostMapping("/meals")
    public String updateAndCreateMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");

        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.isEmpty(request.getParameter("id"))) {
            checkNew(meal);
            service.create(meal, userId);
        } else {
            assureIdConsistent(meal, getId(request));
            service.update(meal, userId);
        }

        return "redirect:/meals";
    }

    @PostMapping("/meals/delete")
    public String delete(Model model, HttpServletRequest request) {
        int id = getId(request);
        service.delete(id, userId);
        return "redirect:/meals";
    }

    @PostMapping("/meals/update")
    public String update(Model model, HttpServletRequest request) {
        int id = getId(request);
        model.addAttribute("meal", service.get(id, userId));
        model.addAttribute("action", "update");
        return "mealForm";
    }

    @PostMapping("/meals/create")
    public String create(Model model, HttpServletRequest request) {
        final Meal meal =  new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        model.addAttribute("action", "create");
        return "mealForm";
    }

    @GetMapping("/meals")
    public String meals(Model model, HttpServletRequest request) {
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "filter":
                LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
                LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
                LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
                LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

                List<Meal> mealsDateFiltered = service.getBetweenDates(startDate, endDate, userId);
                model.addAttribute("meals", MealsUtil.getFilteredWithExcess(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
                return "meals";
            case "all":
            default:
                model.addAttribute("meals", MealsUtil.getWithExcess(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
                return "meals";
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
