package hu.acsaifz.vaccinationapp.repositories;

import com.mysql.cj.jdbc.MysqlDataSource;
import hu.acsaifz.vaccinationapp.models.Citizen;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CitizenRepositoryTest {
    private CitizenRepository citizenRepository;

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

        citizenRepository = new CitizenRepositoryImpl(dataSource);
    }

    @Test
    void testSaveAndFind(){
        Citizen expected = new Citizen("John Doe", "1069", 29, "john.doe@domain.com","123456788");
        Citizen expected2 = new Citizen("Jane Doe", "1111",21, "jane.doe@domain.com","925862035");

        citizenRepository.save(expected);
        citizenRepository.save(expected2);

        List<Citizen> result = citizenRepository.findAll();

        assertEquals(2, result.size());
        assertEquals(expected.getSsn(),result.get(0).getSsn());
        assertEquals(expected2.getSsn(), result.get(1).getSsn());
    }

}