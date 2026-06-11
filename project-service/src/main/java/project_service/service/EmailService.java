package project_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.email.from}")
    private String fromEmail;

    @Value("${resend.email.to}")
    private String toEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendProjectDelayAlert(String projectName, String endDate) {
        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("from", fromEmail);
        body.put("to", new String[]{toEmail});
        body.put("subject", "🚨 ALERTA OPERACIONAL: Proyecto con Atraso Detectado");
        
        String htmlContent = String.format(
            "<div style='font-family: sans-serif; padding: 20px; color: #333;'>" +
            "<h2>Notificación de Desfase de Cronograma</h2>" +
            "<p>Se ha registrado un proyecto en el sistema cuyas fechas operacionales presentan anomalías estructurales.</p>" +
            "<ul>" +
            "<li><strong>Proyecto:</strong> %s</li>" +
            "<li><strong>Fecha estimada de término:</strong> %s</li>" +
            "</ul>" +
            "<p style='color: red;'><strong>Estado:</strong> ATRASADO / PLAZO VENCIDO</p>" +
            "</div>", 
            projectName, endDate
        );
        body.put("html", htmlContent);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("📧 Alerta de correo enviada exitosamente a través de Resend.");
            }
        } catch (Exception e) {
            System.err.println("❌ Fallo crítico al despachar correo en Resend: " + e.getMessage());
        }
    }
}
