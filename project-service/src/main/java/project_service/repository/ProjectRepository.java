package project_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project_service.model.Project;

public interface ProjectRepository extends JpaRepository<Project,Long>{

}
