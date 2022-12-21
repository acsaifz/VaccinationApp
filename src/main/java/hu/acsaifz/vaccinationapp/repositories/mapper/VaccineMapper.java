package hu.acsaifz.vaccinationapp.repositories.mapper;

import hu.acsaifz.vaccinationapp.models.Vaccine;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VaccineMapper implements RowMapper<Vaccine> {
    @Override
    public Vaccine mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Vaccine(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
