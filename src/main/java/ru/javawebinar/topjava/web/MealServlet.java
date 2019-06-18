package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.format.datetime.DateFormatter;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRepository repository;

    private ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //repository = new InMemoryMealRepositoryImpl();

        try {
            appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
        } catch (Exception e) {

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        MealRestController mealRestController = appCtx.getBean(MealRestController.class);

        String id = request.getParameter("id");

        if (id != null) {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);

            mealRestController.save(meal);
            response.sendRedirect("meals");

        } else {

            LocalDate startDate = null;
            LocalDate endDate = null;
            LocalTime startTime = null;
            LocalTime endTime = null;

            try {
                startDate = LocalDate.parse(request.getParameter("startDate"));
            } catch (Exception e) {
            }

            try {
                endDate = LocalDate.parse(request.getParameter("endDate"));
            } catch (Exception e) {
            }

            try {
                startTime = LocalTime.parse(request.getParameter("startTime"));
            } catch (Exception e) {
            }

            try {
                endTime = LocalTime.parse(request.getParameter("endTime"));
            } catch (Exception e) {
            }

            if ((startDate != null) || (endDate != null) || (startTime != null) || (endTime != null)) {
                log.info("getByRange");
                request.setAttribute("meals", mealRestController.getAllByRange(startDate, startTime, endDate, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
            } else {
                response.sendRedirect("meals");
            }
        }


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        MealRestController mealRestController = appCtx.getBean(MealRestController.class);

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        mealRestController.save(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000)) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
