package hu.acsaifz.vaccinationapp.repositories;

import hu.acsaifz.vaccinationapp.models.Citizen;
import hu.acsaifz.vaccinationapp.models.Report;
import hu.acsaifz.vaccinationapp.models.VaccinationStatus;
import hu.acsaifz.vaccinationapp.repositories.mapper.CitizenMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CitizenRepositoryImpl implements CitizenRepository{
    private final JdbcTemplate jdbcTemplate;

    public CitizenRepositoryImpl(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public long save(Citizen citizen) {
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO `citizens`(`name`, `zip`, `age`, `email`, `ssn`) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, citizen.getName());
            ps.setString(2, citizen.getZipCode());
            ps.setInt(3, citizen.getAge());
            ps.setString(4, citizen.getEmail());
            ps.setString(5, citizen.getSsn());

            return ps;
        }, holder);

        return holder.getKey().longValue();
    }

    @Override
    public void saveAll(List<Citizen> citizens) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(jdbcTemplate.getDataSource());
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        template.execute(new TransactionCallbackWithoutResult(){

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                jdbcTemplate.batchUpdate("INSERT INTO `citizens` (`name`, `zip`, `age`, `email`, `ssn`) VALUES(?,?,?,?,?)", citizens,50,(ps, citizen) -> {
                    ps.setString(1, citizen.getName());
                    ps.setString(2, citizen.getZipCode());
                    ps.setInt(3,citizen.getAge());
                    ps.setString(4, citizen.getEmail());
                    ps.setString(5, citizen.getSsn());
                });
            }
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

    @Override
    public List<Citizen> findCitizensByZipCodeForDailyVaccinations(String zipCode) {
        return jdbcTemplate.query(
                "SELECT `citizens`.`id`, `citizens`.`name`, `citizens`.`zip`, `citizens`.`age`, `citizens`.`email`, `citizens`.`ssn` " +
                    "FROM `citizens` " +
                    "LEFT JOIN ( " +
                        "SELECT `vaccinations`.`citizen_id`, `vaccinations`.`vaccination_date` " +
                        "FROM `vaccinations` " +
                        "WHERE `vaccinations`.`status` = ? " +
                    ") AS `vaccinations` " +
                    "ON `citizens`.`id` = `vaccinations`.`citizen_id` " +                    "WHERE `citizens`.`zip` = ? " +
                    "GROUP BY `citizens`.`id`, `citizens`.`age`, `citizens`.`name` " +
                    "HAVING COUNT(`vaccinations`.`citizen_id`) < 2 AND (" +
                        "DATEDIFF(CURRENT_TIMESTAMP(), MAX(`vaccinations`.`vaccination_date`)) >= 15 " +
                        "OR " +
                        "max(`vaccinations`.`vaccination_date`) is null) " +
                    "ORDER BY `citizens`.`age` DESC, `citizens`.`name` ASC " +
                    "LIMIT 16", new CitizenMapper(), VaccinationStatus.SUCCESSFUL.toString(), zipCode
        );
    }

    @Override
    public Optional<Citizen> findCitizenBySsn(String ssn) {
        List<Citizen> result = jdbcTemplate.query("SELECT * FROM `citizens` WHERE `ssn` = ?", new CitizenMapper(), ssn);

        if (result.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(result.get(0));
    }

    @Override
    public Map<String, Report> getAllVaccinatedCitizensCountCategorizedByVaccinationsCount() {
        return jdbcTemplate.query("SELECT `citizens`.`zip`, " +
                "ifnull(`not_vaccinated`.`count_of_not_vaccinated`,0) as `count_of_not_vaccinated`, " +
                "ifnull(`once_vaccinated`.`count_of_once_vaccinated`,0) as `count_of_once_vaccinated`, " +
                "ifnull(`twice_vaccinated`.`count_of_twice_vaccinated`,0) as `count_of_twice_vaccinated` " +
                "FROM `citizens` " +
                "LEFT JOIN `not_vaccinated` " +
                "ON `citizens`.`zip` = `not_vaccinated`.`zip` " +
                "LEFT JOIN `once_vaccinated` " +
                "ON `citizens`.`zip` = `once_vaccinated`.`zip` " +
                "LEFT JOIN `twice_vaccinated` " +
                "ON `citizens`.`zip` = `twice_vaccinated`.`zip` " +
                "GROUP BY `citizens`.`zip` ", (ResultSetExtractor<Map<String, Report>>) rs -> {

                    HashMap<String, Report> result = new HashMap<>();

                    while (rs.next()){
                        Report report = new Report(
                                rs.getInt("count_of_not_vaccinated"),
                                rs.getInt("count_of_once_vaccinated"),
                                rs.getInt("count_of_twice_vaccinated")
                        );

                        result.put(rs.getString("zip"),report);
                    }

                    return result;
        });
    }
}
