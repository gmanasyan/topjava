package ru.javawebinar.topjava.service;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.*;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private static final Logger log = getLogger(MealServiceTest.class);

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Rule
    public final TestName name = new TestName();

    public static List<String> testLog = new ArrayList<>();
    public static List<String> testJLog = new ArrayList<>();

    @Rule
    public TestWatcher watcher = new TestWatcher() {
        private long start;

        @Override
        protected void starting(Description description) {
            start = System.nanoTime();
            log.info("Starting Test");
        }

        @Override
        protected void finished(Description description) {
            String test = String.format("Finished Test: %-20s Duration: %s nano seconds.", description.getMethodName(), (System.nanoTime() - start));
            log.info(test);
            testLog.add(test);
        }
    };


    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        private void logInfo(Description description, long nanos) {
            String test = String.format("JUnit Stopwatch: %-20s Duration: %s nano seconds." , description.getMethodName(), nanos);
            log.info(test);
            testJLog.add(test);
        }

        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, nanos);
        }
    };


    @ClassRule
    public static final ExternalResource resource = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            log.info("Start test class");
        };

        @Override
        protected void after() {
            log.info("End test class");
            log.info("My time test:");
            testLog.forEach(m -> log.info(m));

            log.info("JUnit time test:");
            testJLog.forEach(m -> log.info(m));

        };
    };

    @Autowired
    private MealService service;

    @Test
    @Transactional
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    @Transactional
    public void deleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(1, USER_ID);
    }

    @Test
    @Transactional
    public void deleteNotOwn() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    @Transactional
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    @Transactional
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        log.info("Get {},{}", actual.getDescription(), actual.getUser().toString());
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    @Transactional
    public void getNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(1, USER_ID);
    }

    @Test
    @Transactional
    public void getNotOwn() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    @Transactional
    public void update() throws Exception {
        Meal updated = getUpdated();
        updated.setUser(USER);
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    @Transactional
    public void updateNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    @Transactional
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    @Transactional
    public void getBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}