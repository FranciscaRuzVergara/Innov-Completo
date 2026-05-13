package assignment;

import assignment.model.Assignment;
import assignment.repository.AssignmentRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:assignmentdb;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AssignmentRepositoryTest {

    @Autowired
    private AssignmentRepository repository;

    @Test
    void findByEmployeeRut_ShouldReturnList() {
        // Arrange
        Assignment a1 = new Assignment();
        a1.setEmployeeRut("1-1");
        a1.setTaskRoleId(10L);
        repository.save(a1);

        Assignment a2 = new Assignment();
        a2.setEmployeeRut("2-2");
        a2.setTaskRoleId(11L);
        repository.save(a2);

        // Act
        List<Assignment> result = repository.findByEmployeeRut("1-1");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTaskRoleId()).isEqualTo(10L);
    }
}