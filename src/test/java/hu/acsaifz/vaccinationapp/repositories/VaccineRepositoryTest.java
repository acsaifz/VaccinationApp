package hu.acsaifz.vaccinationapp.repositories;

import com.mysql.cj.jdbc.MysqlDataSource;
import hu.acsaifz.vaccinationapp.models.Vaccine;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VaccineRepositoryTest {
    private VaccineRepository vaccineRepository;

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

        vaccineRepository = new VaccineRepositoryImpl(dataSource);
    }

    @Test
    void testFindAll(){
        List<Vaccine> result = vaccineRepository.findAll();

        assertEquals(7, result.size());
        assertEquals("Janssen", result.get(3).getName());
        assertEquals("Sinopharm", result.get(6).getName());
    }

    @Test
    void testFindById(){
        Optional<Vaccine> result = vaccineRepository.findById(2);

        assertTrue(result.isPresent());
        assertEquals("Moderna", result.get().getName());
    }

}