package com.innovatech.employee;

import com.innovatech.employee.model.Employee;
import com.innovatech.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void findByEmail_ShouldReturnEmployee_WhenEmailExists() {
        // Given
        Employee emp = new Employee("12345678", "Juan", "Pérez", "juan@test.com", 40, "9");
        employeeRepository.save(emp);

        // When
        Optional<Employee> found = employeeRepository.findByEmail("juan@test.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getRut()).isEqualTo("12345678");
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        Employee emp = new Employee("87654321", "Maria", "Soto", "maria@test.com", 40, "k");
        employeeRepository.save(emp);

        boolean exists = employeeRepository.existsByEmail("maria@test.com");

        assertThat(exists).isTrue();
    }
}