package hu.acsaifz.vaccinationapp.repositories;

import hu.acsaifz.vaccinationapp.models.Vaccine;
import hu.acsaifz.vaccinationapp.repositories.mapper.VaccineMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class VaccineRepositoryImpl implements VaccineRepository{
    private final JdbcTemplate jdbcTemplate;

    public VaccineRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Vaccine> findAll() {
        return jdbcTemplate.query("SELECT * FROM `vaccines` ORDER BY `id`", new VaccineMapper());
    }

    @Override
    public Optional<Vaccine> findById(long id) {
        List<Vaccine> result = jdbcTemplate.query("SELECT * FROM `vaccines` WHERE `id` = ?", new VaccineMapper(),id);

        Optional<Vaccine> vaccine;

        if (result.isEmpty()){
            vaccine = Optional.empty();
        } else {
            vaccine = Optional.of(result.get(0));
        }

        return vaccine;
    }
}
