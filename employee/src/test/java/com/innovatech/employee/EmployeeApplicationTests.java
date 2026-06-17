package com.innovatech.employee;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest(properties = "spring.liquibase.enabled=false") 
class EmployeeApplicationTests {
    @Test
    void contextLoads() {
    }
}