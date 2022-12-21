package hu.acsaifz.vaccinationapp.repositories;

import hu.acsaifz.vaccinationapp.models.Vaccination;
import hu.acsaifz.vaccinationapp.models.VaccinationStatus;
import hu.acsaifz.vaccinationapp.repositories.mapper.VaccinationMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

public class VaccinationRepositoryImpl implements VaccinationRepository{
    private final JdbcTemplate jdbcTemplate;

    public VaccinationRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public List<Vaccination> findSuccessfulVaccinationsByCitizenId(long citizenId) {
        return jdbcTemplate.query(
                "SELECT `vaccinations`.`id`, `vaccinations`.`vaccination_date`, `vaccinations`.`status`, `vaccinations`.`note`, `vaccines`.`id` as vaccine_id, `vaccines`.`name` as vaccine_name " +
                        "FROM `vaccinations` " +
                        "INNER JOIN `vaccines` " +
                        "ON `vaccinations`.`vaccine_id` = `vaccines`.`id` " +
                        "WHERE `vaccinations`.`citizen_id` = ? and `vaccinations`.`status` = ?",
                new VaccinationMapper(),
                citizenId, VaccinationStatus.SUCCESSFUL.toString()
        );
    }

    @Override
    public void save(Vaccination vaccination, long citizenId) {
        if (vaccination.getVaccine() == null){
            jdbcTemplate.update("INSERT INTO `vaccinations`(`citizen_id`, `vaccination_date`, `status`, `note`) VALUES (?,?,?,?)", ps -> {
                ps.setLong(1, citizenId);
                ps.setTimestamp(2, Timestamp.valueOf(vaccination.getVaccinationDate()));
                ps.setString(3, vaccination.getStatus().toString());
                ps.setString(4, vaccination.getNote());
            });
        }else {
            jdbcTemplate.update("INSERT INTO `vaccinations`(`citizen_id`, `vaccination_date`, `status`, `note`, `vaccine_id`) VALUES (?,?,?,?,?)", ps -> {
                ps.setLong(1, citizenId);
                ps.setTimestamp(2, Timestamp.valueOf(vaccination.getVaccinationDate()));
                ps.setString(3, vaccination.getStatus().toString());
                ps.setString(4, vaccination.getNote());
                ps.setLong(5, vaccination.getVaccine().getId());
            });
        }
    }
}
