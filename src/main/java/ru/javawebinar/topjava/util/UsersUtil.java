package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.*;

public class UsersUtil {

    public static final List<User> USERS = Arrays.asList(
            new User(null, "john", "john123@gmai.com", "dsSfwfw234247f", 2000, true, EnumSet.of(Role.ROLE_USER)),
            new User(null, "mike", "mike123@gmai.com", "shdfjh35435", 2000, true, EnumSet.of(Role.ROLE_USER)),
            new User(null, "ann", "mike123@gmai.com", "shdfjh35435", 2000, true, EnumSet.of(Role.ROLE_USER))
    );


}
