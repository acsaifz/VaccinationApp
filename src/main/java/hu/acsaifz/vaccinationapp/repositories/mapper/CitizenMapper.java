package hu.acsaifz.vaccinationapp.repositories.mapper;

import hu.acsaifz.vaccinationapp.models.Citizen;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CitizenMapper implements RowMapper<Citizen> {

    @Override
    public Citizen mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Citizen(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("zip"),
                rs.getInt("age"),
                rs.getString("email"),
                rs.getString("ssn")
        );
    }
}
