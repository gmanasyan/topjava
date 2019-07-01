package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.List;



@Repository
@Transactional
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Meal save(Meal meal, int userId) {

        if (meal.isNew()) {
            // If LAZY works
            //User ref = em.getReference(User.class, userId);
            //meal.setUser(ref);

            // EAGER
            User user =  em.find(User.class, userId);
            meal.setUser(user);

            meal.setDateTime(LocalDateTime.now().withNano(0));
            em.persist(meal);
            return meal;
        } else if (em.createNamedQuery(Meal.UPDATE)
                .setParameter("id", meal.getId())
                .setParameter("userId", userId)
                .setParameter("description", meal.getDescription())
                .setParameter("calories", meal.getCalories())
                .setParameter("dateTime", meal.getDateTime())
                .executeUpdate() == 0) {
          return null;
        }
        return meal;
    }

    @Override

    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override

    public Meal get(int id, int userId) {

        return em.createNamedQuery(Meal.GET, Meal.class)
                    .setParameter("id", id)
                    .setParameter("userId", userId)
                    .getSingleResult();
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL, Meal.class).setParameter("id", userId).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("id", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}