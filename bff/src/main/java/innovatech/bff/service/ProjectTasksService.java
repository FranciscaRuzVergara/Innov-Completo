package innovatech.bff.service;

import java.util.List;

import org.springframework.stereotype.Service;

import innovatech.bff.client.ProjectClient;
import innovatech.bff.client.TaskClient;
import innovatech.bff.dto.ProjectDTO;
import innovatech.bff.dto.ProjectWithTasksDTO;
import innovatech.bff.dto.TaskDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectTasksService {
    private final ProjectClient projectClient;
    private final TaskClient taskClient;

    public ProjectWithTasksDTO findProjectWithTasks(Long projectId){
        ProjectDTO project = projectClient.getProjectById(projectId);

        if(project == null){
            return null;
        }

        List<TaskDTO> tasks = taskClient.getTasksByProject(projectId);

        return new ProjectWithTasksDTO(
           project.getProjectId(),
            project.getName(),
            project.getDescription(),
            project.getStartDate(),
            project.getEndDate(),
            tasks 
        );
    }
}
