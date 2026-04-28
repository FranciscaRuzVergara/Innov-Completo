package project_service.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long idTask;
    private String name;
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateCreated;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateFinished;
    private Long projectId;

}
