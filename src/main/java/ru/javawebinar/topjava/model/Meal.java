package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.UPDATE, query = "UPDATE Meal m SET m.dateTime =:dateTime, m.description =:description, " +
                "m.calories =:calories WHERE m.id=:id AND m.user.id =:userId"),
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal u WHERE u.id=:id AND u.user.id =:userId"),
        @NamedQuery(name = Meal.GET, query = "SELECT u FROM Meal u WHERE u.id=:id AND u.user.id = :userId"),
        @NamedQuery(name = Meal.ALL_FILTERED, query = "SELECT u FROM Meal u  WHERE u.user.id=:id " +
                "AND u.dateTime >= :startDate AND u.dateTime <= :endDate ORDER BY u.dateTime DESC"),
        @NamedQuery(name = Meal.ALL, query = "SELECT u FROM Meal u WHERE u.user.id=:id ORDER BY u.dateTime DESC")
})

@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "meals_unique_user_datetime_idx")})
public class Meal extends AbstractBaseEntity {


    public static final String UPDATE = "Meal.update";
    public static final String DELETE = "Meal.delete";
    public static final String GET = "Meal.get";
    public static final String ALL_FILTERED = "Meal.getAllFiltered";
    public static final String ALL = "Meal.getAll";

    @Column(name = "date_time", nullable = false )
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    private String description;

    @Column(name = "calories", nullable = false)
    @Range(min = 10, max = 5000)
    private int calories;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
