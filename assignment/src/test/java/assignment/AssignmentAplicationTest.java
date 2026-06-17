package assignment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Añadimos estas propiedades para simular el origen de datos si se cae por JPA
@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class AssignmentApplicationTests {

    @Test
    void contextLoads() {
        // Si compila y levanta con la BD de prueba, este test pasará automáticamente
    }
}