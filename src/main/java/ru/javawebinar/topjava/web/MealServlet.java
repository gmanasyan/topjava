package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsInit;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


public class MealServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        MealsInit mealsInit = MealsInit.getInstance();

        int caloriesPerDay = 2000;
        List<MealTo> mealsWithExcess = new ArrayList<>();
        mealsWithExcess = MealsUtil.getFilteredWithExcess(mealsInit.meals, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);

        req.setAttribute("list", mealsWithExcess);
        req.setAttribute("caloriesPerDay", caloriesPerDay);

        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        //resp.sendRedirect("meals.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("delete-id");
        req.setAttribute("delete-id", id);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);

      //  System.out.println(req.getParameterMap());
    }
}
