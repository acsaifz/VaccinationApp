package hu.acsaifz.vaccinationapp.repositories.mapper;

import hu.acsaifz.vaccinationapp.models.Vaccination;
import hu.acsaifz.vaccinationapp.models.VaccinationStatus;
import hu.acsaifz.vaccinationapp.models.Vaccine;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VaccinationMapper implements RowMapper<Vaccination> {
    @Override
    public Vaccination mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Vaccination(
                rs.getLong("id"),
                rs.getTimestamp("vaccination_date").toLocalDateTime(),
                VaccinationStatus.valueOf(rs.getString("status")),
                rs.getString("note"),
                new Vaccine(
                        rs.getInt("vaccine_id"),
                        rs.getString("vaccine_name")
                )
        );
    }
}
