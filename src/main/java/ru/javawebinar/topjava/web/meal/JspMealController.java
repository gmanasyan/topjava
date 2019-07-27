package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

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
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController{

    private int userId = SecurityUtil.authUserId();

    public JspMealController(MealService service) {
        super(service);
    }

    //@PostMapping("/meals")
    @PostMapping("")
    public String updateAndCreateMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");

        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.isEmpty(request.getParameter("id"))) {
            super.create(meal);
        } else {
            super.update(meal, getId(request));
        }

        return "redirect:/meals";
    }

    //@PostMapping("/meals/delete")
    @PostMapping("/delete")
    public String delete(Model model, HttpServletRequest request) {
        int id = getId(request);
        super.delete(id);
        return "redirect:/meals";
    }

    //@PostMapping("/meals/update")
    @PostMapping("/update")
    public String update(Model model, HttpServletRequest request) {
        int id = getId(request);
        model.addAttribute("meal", super.get(id));
        model.addAttribute("action", "update");
        return "mealForm";
    }

    //@PostMapping("/meals/create")
    @PostMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        final Meal meal =  new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        model.addAttribute("action", "create");
        return "mealForm";
    }

    //@GetMapping("/meals")
    @GetMapping("")
    public String meals(Model model, HttpServletRequest request) {
                model.addAttribute("meals", MealsUtil.getWithExcess(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
                return "meals";
    }

    //@GetMapping("/meals/filter")
    //@GetMapping("/filter")
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String mealsFiltered(Model model, HttpServletRequest request) {
                LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
                LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
                LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
                LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

                List<Meal> mealsDateFiltered = service.getBetweenDates(startDate, endDate, userId);
                model.addAttribute("meals", MealsUtil.getFilteredWithExcess(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime));
                return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
