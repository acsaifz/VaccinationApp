package hu.acsaifz.vaccinationapp.repositories;

import com.mysql.cj.jdbc.MysqlDataSource;
import hu.acsaifz.vaccinationapp.models.Citizen;
import hu.acsaifz.vaccinationapp.models.Vaccination;
import hu.acsaifz.vaccinationapp.models.VaccinationStatus;
import hu.acsaifz.vaccinationapp.models.Vaccine;
import hu.acsaifz.vaccinationapp.services.CitizenService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VaccinationRepositoryTest {
    private VaccinationRepository vaccinationRepository;


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

        vaccinationRepository = new VaccinationRepositoryImpl(dataSource);

        CitizenService citizenService = new CitizenService(dataSource);
        Citizen citizen = new Citizen("John Doe", "1069", 29, "john.doe@domain.com","123456788");
        citizenService.save(citizen);
    }

    @Test
    void testFindSuccessfulVaccinationsByCitizenIdAndSave(){
        LocalDateTime vaccinationTime = LocalDateTime.parse("2022-12-20T20:29:35");
        Vaccination vaccination = new Vaccination(vaccinationTime, VaccinationStatus.SUCCESSFUL,"ok",new Vaccine(1, "Pfizer-BioNtech"));
        Vaccination vaccination2 = new Vaccination(vaccinationTime.minusDays(1), VaccinationStatus.FAILED,"Patient was sick.");

        vaccinationRepository.save(vaccination, 1);
        vaccinationRepository.save(vaccination2, 1);

        List<Vaccination> vaccinations = vaccinationRepository.findSuccessfulVaccinationsByCitizenId(1);

        assertEquals(1, vaccinations.size());
        assertEquals(vaccinationTime, vaccinations.get(0).getVaccinationDate());
    }
}