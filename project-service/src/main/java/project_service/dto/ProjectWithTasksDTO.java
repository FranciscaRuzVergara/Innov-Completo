package project_service.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project_service.model.KPI;
import project_service.model.ProjectStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWithTasksDTO {
    private Long projectId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String statusName;
    private List<KPI> kpis;

    private List<TaskDTO> tasks;

}
