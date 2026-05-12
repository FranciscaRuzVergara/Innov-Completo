package project_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import project_service.client.TaskClient;
import project_service.dto.ProjectWithTasksDTO;
import project_service.dto.TaskDTO;
import project_service.model.Project;
import project_service.repository.ProjectRepository;

@Service
@Transactional
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskClient taskClient;

    public List<Project> findAll(){
        return projectRepository.findAll();
    }

    public Project findById(Long id){
        return projectRepository.findById(id).orElse(null);
    }

    public Project save(Project project){
        return projectRepository.save(project);
    }

    public void delete(Long id){
        projectRepository.deleteById(id);
    }

    public ProjectWithTasksDTO findProjectWithTasks(Long projectId){
        Project project = projectRepository.findById(projectId).orElse(null);
        if(project==null){
            return null;
        }
        List<TaskDTO> tasks = taskClient.getTasksByProject(projectId);

        return new ProjectWithTasksDTO(
            project.getProjectId(),
            project.getName(),
            project.getDescription(),
            project.getStartDate(),
            project.getEndDate(),
            project.getProjectStatus().getStatusName(),
            project.getKpis(),
            tasks
        );
    }
}
