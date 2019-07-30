package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    public static final String REST_URL = "/rest/meals";

    @Autowired
    private MealService mealService;

    @GetMapping("")
    public List<MealTo> getAll() {
        return MealsUtil.getWithExcess(mealService.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Meal create(@RequestBody Meal meal) {
        return super.create(meal);
    }


    /*
        First version - string parameters from request
    */
//    @GetMapping("/filter")
//    public List<MealTo> getBetween(
//            @RequestParam(required=false) String startDate,
//            @RequestParam(required=false) String endDate,
//            @RequestParam(required=false) String startTime,
//            @RequestParam(required=false) String endTime ) {
//        LocalDate sDate = parseLocalDate(startDate);
//        LocalDate eDate = parseLocalDate(endDate);
//        LocalTime sTime = parseLocalTime(startTime);
//        LocalTime eTime = parseLocalTime(endTime);
//        return super.getBetween(sDate, sTime, eDate, eTime);
//    }
//
//
//

    /*
        Second version - with @DateTimeFormat
    */
//    @GetMapping("/filter")
//    public List<MealTo> getBetween(
//            @Nullable @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @Nullable @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
//            @Nullable @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
//            @Nullable @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
//
//        return super.getBetween(
//               (startDate != null) ? startDate.toLocalDate() : null,
//               (startTime != null) ? startTime.toLocalTime() : null,
//               (endDate != null) ? endDate.toLocalDate() : null,
//               (endTime != null) ? endTime.toLocalTime() : null);
//
//    }


    /*
        Third version - optional
    */
    @GetMapping("/filter")
    public List<MealTo> getBetween(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        final class StringToLocalDate implements Converter<String, LocalDate> {
            public LocalDate convert(String source) {
                return parseLocalDate(source);
            }
        }

        final class StringToLocalTime implements Converter<String, LocalTime> {
            public LocalTime convert(String source) {
                return parseLocalTime(source);
            }
        }

        StringToLocalDate stringToLocalDate = new StringToLocalDate();
        StringToLocalTime stringToLocalTime = new StringToLocalTime();

        return super.getBetween(
                stringToLocalDate.convert(startDate),
                stringToLocalTime.convert(startTime),
                stringToLocalDate.convert(endDate),
                stringToLocalTime.convert(endTime));

    }
}