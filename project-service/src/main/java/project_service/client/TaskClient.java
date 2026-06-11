package project_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import project_service.dto.TaskDTO;

@FeignClient(name = "task-service", url = "${task.service.url}")
public interface TaskClient {
    @GetMapping("/tasks/project/{projectId}")
    List<TaskDTO> getTasksByProject(@PathVariable("projectId") Long projectId);

}
