package assignment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import assignment.model.Assignment;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByEmployeeRut(String employeeRut);

    List<Assignment> findByTaskRoleId(Long taskRoleId);
}
