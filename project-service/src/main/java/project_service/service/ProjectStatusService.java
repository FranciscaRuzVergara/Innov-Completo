package project_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import project_service.model.ProjectStatus;
import project_service.repository.ProjectStatusRepository;

@Service
@Transactional
public class ProjectStatusService {
    @Autowired
    private ProjectStatusRepository projectStatusRepository;

    public List<ProjectStatus> findAll(){
        return projectStatusRepository.findAll();
    }

    public ProjectStatus findById(Long id){
        return projectStatusRepository.findById(id).orElse(null);
    }

    public ProjectStatus save(ProjectStatus status){
        return projectStatusRepository.save(status);
    }

    public void delete(Long id){
        projectStatusRepository.deleteById(id);
    }

}
