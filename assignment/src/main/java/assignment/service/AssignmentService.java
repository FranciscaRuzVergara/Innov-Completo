package assignment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import assignment.model.Assignment;
import assignment.repository.AssignmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    public Assignment createAssignment(Assignment assignment) {
        //guarda en la bd
        Assignment saved = assignmentRepository.save(assignment);
        
        //aviso a kafka
        try {
            String eventJson = objectMapper.writeValueAsString(saved);
            kafkaTemplate.send("topic-asignaciones", eventJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return saved;
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public void deleteById(Long id) {
    if (assignmentRepository.existsById(id)) {
        assignmentRepository.deleteById(id);
    } else {
        throw new RuntimeException("No se encontró la asignación con ID: " + id);
    }
}
}
