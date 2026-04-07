package project_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project_service.dto.ProjectWithTasksDTO;
import project_service.model.Project;
import project_service.service.ProjectService;


@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<Project> getAll(){
        return projectService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> search(@PathVariable Long id){
        try{
            Project project = projectService.findById(id);
            return ResponseEntity.ok(project);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project project){
        Project status = projectService.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project data) {
        try {
            Project modified = projectService.findById(id);
            modified.setDescription(data.getDescription());
            modified.setProjectStatus(data.getProjectStatus());
            
            Project updated = projectService.save(modified);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            projectService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/with-tasks")
    public ResponseEntity<ProjectWithTasksDTO> getProjectWithTasks(@PathVariable Long id){
        try {
            ProjectWithTasksDTO projectWithTasks = projectService.findProjectWithTasks(id);
            if(projectWithTasks==null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(projectWithTasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
