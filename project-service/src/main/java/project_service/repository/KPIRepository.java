package project_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project_service.model.KPI;

public interface KPIRepository extends JpaRepository<KPI,Long>{

}
