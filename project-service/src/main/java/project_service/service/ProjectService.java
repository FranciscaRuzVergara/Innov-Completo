package project_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project_service.model.KPI;
import project_service.repository.KPIRepository;

import jakarta.transaction.Transactional;
import project_service.client.TaskClient;
import project_service.dto.ProjectWithTasksDTO;
import project_service.dto.TaskDTO;
import project_service.model.Project;
import project_service.model.ProjectStatus;
import project_service.repository.ProjectRepository;
import project_service.repository.ProjectStatusRepository; 

@Service
@Transactional
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectStatusRepository projectStatusRepository; 

    @Autowired
    private KPIRepository kpiRepository;

    @Autowired
    private TaskClient taskClient;

    @Autowired
    private EmailService emailService;

    public List<Project> findAll(){
        return projectRepository.findAll();
    }

    public Project findById(Long id){
        return projectRepository.findById(id).orElse(null);
    }

    public Project save(Project project){
        if (project.getProjectStatus() != null && project.getProjectStatus().getProjectStatusId() != null) {
            Long statusId = project.getProjectStatus().getProjectStatusId();
            ProjectStatus persistentStatus = projectStatusRepository.findById(statusId).orElse(null);
            if (persistentStatus != null) {
                project.setProjectStatus(persistentStatus);
            } else {
                throw new IllegalArgumentException("El ID de Estado de Proyecto suministrado no existe en el sistema.");
            }
        }

        if (project.getKpis() != null && !project.getKpis().isEmpty()) {
            List<KPI> managedKpis = new java.util.ArrayList<>();
            
            for (KPI kpiReq : project.getKpis()) {
                if (kpiReq.getKpiId() != null) {
                    KPI persistentKpi = kpiRepository.findById(kpiReq.getKpiId()).orElse(null);
                    
                    if (persistentKpi != null) {
                        persistentKpi.setProject(project);
                        managedKpis.add(persistentKpi);
                    } else {
                        throw new IllegalArgumentException("El KPI con ID " + kpiReq.getKpiId() + " no existe.");
                    }
                } else {
                    kpiReq.setProject(project);
                    managedKpis.add(kpiReq);
                }
            }
            project.setKpis(managedKpis);
        }

        Project savedProject = projectRepository.save(project);

        if (savedProject.getEndDate() != null && !savedProject.getEndDate().isAfter(LocalDate.now())) {
            emailService.sendProjectDelayAlert(
                savedProject.getName(), 
                savedProject.getEndDate().toString()
            );
        }

        return savedProject;
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