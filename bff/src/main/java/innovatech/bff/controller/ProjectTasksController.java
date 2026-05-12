package innovatech.bff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import innovatech.bff.dto.ProjectWithTasksDTO;
import innovatech.bff.service.ProjectTasksService;

@RestController
@RequestMapping("/bff")
public class ProjectTasksController {
    @Autowired
    private ProjectTasksService projectTasksService;

    @GetMapping("/{id}/tasks")
    public ResponseEntity<ProjectWithTasksDTO> getProjectWithTasks(@PathVariable Long id){
        try {
            ProjectWithTasksDTO projectWithTasks = projectTasksService.findProjectWithTasks(id);
            if(projectWithTasks==null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(projectWithTasks);
        } catch (Exception e) {
            System.err.println("🔥 ERROR CRÍTICO EN BFF: " + e.getMessage());
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
