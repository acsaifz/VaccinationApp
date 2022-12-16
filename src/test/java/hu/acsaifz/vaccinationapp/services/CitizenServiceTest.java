package hu.acsaifz.vaccinationapp.services;

import com.mysql.cj.jdbc.MysqlDataSource;
import hu.acsaifz.vaccinationapp.models.Citizen;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CitizenServiceTest {
    private CitizenService citizenService;

    @BeforeEach
    void init(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(System.getenv("DB_URL"));
        dataSource.setUser(System.getenv("DB_USER"));
        dataSource.setPassword(System.getenv("DB_PASSWORD"));

        Flyway flyway = Flyway.configure()
                .cleanDisabled(false)
                .dataSource(dataSource)
                .load();

        flyway.clean();
        flyway.migrate();

        citizenService = new CitizenService(dataSource);
    }

    @Test
    void testMassRegistration(){
        Path path = Paths.get("src/test/resources/citizens.csv");
        citizenService.massRegistration(path);

        List<Citizen> result = citizenService.findAll();

        assertEquals(10,result.size());
        assertEquals("Ganny Ogdahl", result.get(4).getName());
    }

    @Test
    void testMassRegistrationWhenFileNotFound(){
        Path path = Paths.get("src/test/resources/xyz.csv");

        Exception exception = assertThrows(IllegalStateException.class, () -> citizenService.massRegistration(path));

        List<Citizen> result = citizenService.findAll();

        assertEquals("File cannot found!", exception.getMessage());
        assertTrue(result.isEmpty());
    }
}