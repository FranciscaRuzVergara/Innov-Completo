package innovatech.bff.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import innovatech.bff.dto.ProjectDTO;

@FeignClient(name="project-service", url = "${project.service.url}")
public interface ProjectClient {
    @GetMapping("/projects/{id}")
    ProjectDTO getProjectById(@PathVariable("id") Long id);

}
