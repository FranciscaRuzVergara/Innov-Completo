package assignment;

import assignment.controller.AssignmentController;
import assignment.model.Assignment;
import assignment.service.AssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AssignmentController.class)
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssignmentService assignmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAssignment_ShouldReturnCreated() throws Exception {
        Assignment assignment = new Assignment();
        assignment.setEmployeeRut("11.222.333-4");
        assignment.setTaskRoleId(5L);

        when(assignmentService.createAssignment(any(Assignment.class))).thenReturn(assignment);

        mockMvc.perform(post("/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assignment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeRut").value("11.222.333-4"));
    }
}