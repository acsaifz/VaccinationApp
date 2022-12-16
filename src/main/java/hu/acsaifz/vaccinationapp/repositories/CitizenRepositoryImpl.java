package hu.acsaifz.vaccinationapp.repositories;

import hu.acsaifz.vaccinationapp.models.Citizen;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class CitizenRepositoryImpl implements CitizenRepository{
    private final JdbcTemplate jdbcTemplate;

    public CitizenRepositoryImpl(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public void save(Citizen citizen) {
        jdbcTemplate.update("INSERT INTO `citizens`(`name`, `zip`, `age`, `email`, `ssn`) VALUES (?,?,?,?,?) ", ps -> {
            ps.setString(1, citizen.getName());
            ps.setString(2, citizen.getZipCode());
            ps.setInt(3, citizen.getAge());
            ps.setString(4, citizen.getEmail());
            ps.setString(5, citizen.getSsn());
        });
    }

    @Override
    public List<Citizen> findAll() {
        return jdbcTemplate.query("SELECT * FROM citizens ORDER BY `id`",
                (rs, rowNum) -> new Citizen(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("zip"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("ssn")
                )
        );
    }
}
