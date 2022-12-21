package hu.acsaifz.vaccinationapp.services;

import com.mysql.cj.jdbc.MysqlDataSource;
import hu.acsaifz.vaccinationapp.models.Citizen;
import hu.acsaifz.vaccinationapp.models.Vaccination;
import hu.acsaifz.vaccinationapp.models.VaccinationStatus;
import hu.acsaifz.vaccinationapp.models.Vaccine;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Test
    void testSaveWhenNoVaccinationAndFindCitizenBySsn(){
        Citizen expected = new Citizen("John Doe", "1069",29,"john.doe@domain.com","123456788");
        citizenService.save(expected);

        Optional<Citizen> result = citizenService.findCitizenBySsn("123456788");

        assertTrue(result.isPresent());
        Citizen citizen = result.get();
        assertEquals(0,citizen.getNumberOfVaccination());
        assertEquals("John Doe", citizen.getName());
    }

    @Test
    void testSaveWhenHasVaccinationAndFindCitizenBySsn(){
        Citizen expected = new Citizen("John Doe", "1069",29,"john.doe@domain.com","123456788");
        List<Vaccination> vaccinations = List.of(
                new Vaccination(
                        LocalDateTime.of(2022,12,21,15,10,0),
                        VaccinationStatus.SUCCESSFUL, "ok",new Vaccine(2, "Moderna"))
        );
        expected.addVaccinations(vaccinations);
        citizenService.save(expected);

        Optional<Citizen> result = citizenService.findCitizenBySsn("123456788");

        assertTrue(result.isPresent());
        Citizen citizen = result.get();
        assertEquals(1,citizen.getNumberOfVaccination());
        assertEquals("John Doe", citizen.getName());
    }
}