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

import project_service.model.KPI;
import project_service.service.KPIService;

@RestController
@RequestMapping("/kpis")
public class KPIController {
    @Autowired
    private KPIService kpiService;

    @GetMapping
    public List<KPI> getAll(){
        return kpiService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<KPI> search(@PathVariable Long id){
        try{
            KPI kpi = kpiService.findById(id);
            return ResponseEntity.ok(kpi);
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<KPI> create(@RequestBody KPI kpi){
        KPI status = kpiService.save(kpi);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KPI> update(@PathVariable Long id, @RequestBody KPI data) {
        try {
            KPI modified = kpiService.findById(id);
            modified.setName(data.getName());
            modified.setPercentage(data.getPercentage());
            
            KPI updated = kpiService.save(modified);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            kpiService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
