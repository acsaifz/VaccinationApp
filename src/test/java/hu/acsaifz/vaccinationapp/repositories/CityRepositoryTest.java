package hu.acsaifz.vaccinationapp.repositories;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityRepositoryTest {
    private CityRepository cityRepository;

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

        //flyway.clean();
        flyway.migrate();

        cityRepository = new CityRepositoryImpl(dataSource);
    }

    @Test
    void testFindCityByZipCode(){
        List<String> cities = cityRepository.findCityByZipCode("2243");

        assertEquals(1, cities.size());
        assertEquals("Kóka", cities.get(0));
    }

    @Test
    void testFindCityByZipCodeNotAvailable(){
        List<String> cities = cityRepository.findCityByZipCode("1000");

        assertTrue(cities.isEmpty());
    }

}