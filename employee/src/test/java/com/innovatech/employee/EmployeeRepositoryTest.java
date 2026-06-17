package com.innovatech.employee;

import com.innovatech.employee.model.Employee;
import com.innovatech.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@DataJpaTest
// Esta línea es la clave para que no ignore tu application-test.properties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void findByEmail_ShouldReturnEmployee_WhenEmailExists() {
        Employee emp = new Employee(null, "12345678", "Juan", "Pérez", "juan@test.com", 40, "9");
        employeeRepository.save(emp);
        
        Optional<Employee> found = employeeRepository.findByEmail("juan@test.com");
        assertThat(found).isPresent();
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        // Cambiamos el 2L por null para que use GenerationType.IDENTITY
        Employee emp = new Employee(null, "87654321", "Maria", "Soto", "maria@test.com", 40, "k");
        employeeRepository.save(emp);

        boolean exists = employeeRepository.existsByEmail("maria@test.com");

        assertThat(exists).isTrue();
    }
}