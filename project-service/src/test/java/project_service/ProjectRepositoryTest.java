package project_service;

import project_service.model.ProjectStatus;
import project_service.repository.ProjectStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectStatusRepository repository;

    @Test
    void saveStatus_ShouldGenerateId() {
        ProjectStatus status = new ProjectStatus(null, "Completado", new ArrayList<>());
        
        ProjectStatus saved = repository.save(status);
        
        assertThat(saved.getProjectStatusId()).isNotNull();
        assertThat(saved.getStatusName()).isEqualTo("Completado");
    }
}