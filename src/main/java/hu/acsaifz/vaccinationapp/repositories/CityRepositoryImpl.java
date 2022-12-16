package hu.acsaifz.vaccinationapp.repositories;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class CityRepositoryImpl implements CityRepository{
    private final JdbcTemplate jdbcTemplate;

    public CityRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<String> findCityByZipCode(String zipCode) {
        return jdbcTemplate.query(
                "SELECT `city` FROM `cities` WHERE `zip` = ?",
                (rs, rowNum) -> rs.getString("city"),
                zipCode
        );
    }
}
