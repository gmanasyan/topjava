package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.crud.MealCrudMemory;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class MealServlet extends HttpServlet {

    private MealCrudMemory crud = MealCrudMemory.getInstance();
    public int caloriesPerDay = 2000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<MealTo> mealsWithExcess = new ArrayList<>();
        mealsWithExcess = MealsUtil.getFilteredWithExcess(crud.getAll(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay);

        req.setAttribute("list", mealsWithExcess);
        req.setAttribute("caloriesPerDay", caloriesPerDay);

        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        //resp.sendRedirect("meals.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.valueOf(req.getParameter("deleteId"));
        if (id >= 0) crud.deleteMeal(id);

        resp.sendRedirect("meals");
    }
}
