package ru.javawebinar.topjava.web.user;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.sql.SQLException;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            return "profile";
        } else {
            try {
                super.update(userTo, SecurityUtil.authUserId());
            } catch (DataIntegrityViolationException e) {

                Throwable rootCause = ValidationUtil.getRootCause(e);

                log.debug(rootCause.toString());
                if (rootCause.toString().contains("(email)")) {
                    result.rejectValue("email", "error.password");
                }

                return "profile";
            }

            SecurityUtil.get().update(userTo);
            status.setComplete();
            return "redirect:/meals";
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        } else {
            try {
                super.create(userTo);
            } catch (Exception e) {

                // First version to get rootCause
                // Throwable cause = e.getCause();
                // String sqlErrorMessage = cause.getCause().getMessage();

                Throwable rootCause = ValidationUtil.getRootCause(e);

                log.debug(rootCause.toString());
                if (rootCause.toString().contains("(email)")) {
                    result.rejectValue("email", "error.password");
                } else {
                    result.rejectValue("name", "error.generalsqlerror");
                    model.addAttribute("exception", rootCause);
                    model.addAttribute("message", rootCause.toString());
                    return "exception/exception";
                }

                return "profile";
            }

            status.setComplete();
            return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
        }
    }
}