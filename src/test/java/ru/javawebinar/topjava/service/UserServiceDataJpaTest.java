package ru.javawebinar.topjava.service;


import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"postgres","datajpa"})
public class UserServiceDataJpaTest extends UserServiceTest {
}
