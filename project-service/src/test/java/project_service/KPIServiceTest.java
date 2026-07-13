package project_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project_service.model.KPI;
import project_service.repository.KPIRepository;
import project_service.service.KPIService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KPIServiceTest {

    @Mock
    private KPIRepository kpiRepository;

    @InjectMocks
    private KPIService kpiService;

    // ==========================================
    // TEST PARA findAll()
    // ==========================================
    @Test
    void findAll_ShouldReturnListOfKPIs() {
        // Preparar
        //[cite: 8]
        KPI kpi1 = new KPI(1L, "Rendimiento", 85, LocalDate.now(), null);
        KPI kpi2 = new KPI(2L, "Calidad", 90, LocalDate.now(), null);
        when(kpiRepository.findAll()).thenReturn(List.of(kpi1, kpi2));

        // Ejecutar
        //[cite: 9]
        List<KPI> result = kpiService.findAll();

        // Verificar
        assertThat(result).hasSize(2);
        //[cite: 8]
        assertThat(result.get(0).getName()).isEqualTo("Rendimiento");
        verify(kpiRepository).findAll();
    }

    // ==========================================
    // TESTS PARA findById()
    // ==========================================
    @Test
    void findById_WhenKPIExists_ShouldReturnKPI() {
        // Preparar
        //[cite: 8]
        KPI kpi = new KPI(1L, "Cobertura", 100, LocalDate.now(), null);
        when(kpiRepository.findById(1L)).thenReturn(Optional.of(kpi));

        // Ejecutar
        //[cite: 9]
        KPI result = kpiService.findById(1L);

        // Verificar
        assertThat(result).isNotNull();
        //[cite: 8]
        assertThat(result.getKpiId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Cobertura");
        verify(kpiRepository).findById(1L);
    }

    @Test
    void findById_WhenKPIDoesNotExist_ShouldReturnNull() {
        // Preparar
        when(kpiRepository.findById(99L)).thenReturn(Optional.empty());

        // Ejecutar
        //[cite: 9]
        KPI result = kpiService.findById(99L);

        // Verificar
        assertThat(result).isNull();
        verify(kpiRepository).findById(99L);
    }

    // ==========================================
    // TEST PARA save()
    // ==========================================
    @Test
    void save_ShouldSaveAndReturnKPI() {
        // Preparar
        //[cite: 8]
        KPI kpiToSave = new KPI(null, "Eficacia", 75, LocalDate.now(), null);
        KPI savedKpi = new KPI(1L, "Eficacia", 75, LocalDate.now(), null);
        when(kpiRepository.save(any(KPI.class))).thenReturn(savedKpi);

        // Ejecutar
        //[cite: 9]
        KPI result = kpiService.save(kpiToSave);

        // Verificar
        assertThat(result).isNotNull();
        //[cite: 8]
        assertThat(result.getKpiId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Eficacia");
        verify(kpiRepository).save(kpiToSave);
    }

    // ==========================================
    // TEST PARA delete()
    // ==========================================
    @Test
    void delete_ShouldCallRepositoryDelete() {
        // Ejecutar
        //[cite: 9]
        kpiService.delete(1L);

        // Verificar
        verify(kpiRepository).deleteById(1L);
    }
}