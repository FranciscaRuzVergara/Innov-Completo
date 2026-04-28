package project_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project_service.model.ProjectStatus;

public interface ProjectStatusRepository extends JpaRepository<ProjectStatus,Long> {

}
