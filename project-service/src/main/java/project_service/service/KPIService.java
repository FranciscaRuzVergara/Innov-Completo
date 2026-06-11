package project_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import project_service.model.KPI;
import project_service.repository.KPIRepository;

@Service
@Transactional
public class KPIService {
    @Autowired
    private KPIRepository kpiRepository;

    public List<KPI> findAll(){
        return kpiRepository.findAll();
    }

    public KPI findById(Long id){
        return kpiRepository.findById(id).orElse(null);
    }

    public KPI save(KPI kpi){
        return kpiRepository.save(kpi);
    }
    
    public void delete(Long id){
        kpiRepository.deleteById(id);
    }
}
