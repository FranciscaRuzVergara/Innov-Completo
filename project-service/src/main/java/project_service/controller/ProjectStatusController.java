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

import project_service.model.ProjectStatus;
import project_service.service.ProjectStatusService;

@RestController
@RequestMapping("/project-status")
public class ProjectStatusController {

    @Autowired
    private ProjectStatusService projectStatusService;

    @GetMapping
    public List<ProjectStatus> getAll(){
        return projectStatusService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectStatus> search(@PathVariable Long id){
        try{
            ProjectStatus projectStatus = projectStatusService.findById(id);
            return ResponseEntity.ok(projectStatus);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProjectStatus> create(@RequestBody ProjectStatus projectStatus){
        ProjectStatus status = projectStatusService.save(projectStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectStatus> update(@PathVariable Long id, @RequestBody ProjectStatus data) {
        try {
            ProjectStatus modified = projectStatusService.findById(id);
            modified.setStatusName(data.getStatusName());
            
            ProjectStatus updated = projectStatusService.save(modified);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            projectStatusService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
