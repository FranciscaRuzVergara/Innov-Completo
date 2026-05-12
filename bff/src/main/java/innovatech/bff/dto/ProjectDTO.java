package innovatech.bff.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long projectId;
    private String name;
    private String description;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;
}
