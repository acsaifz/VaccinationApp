package hu.acsaifz.vaccinationapp.services;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

public class DataSourceService {
    private static DataSourceService instance;
    private DataSource dataSource;

    private DataSourceService(){
        this.createDataSource();
    }

    private void createDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(System.getenv("DB_URL"));
        dataSource.setUser(System.getenv("DB_USER"));
        dataSource.setPassword(System.getenv("DB_PASSWORD"));

        Flyway flyway = Flyway.configure()
                .cleanDisabled(false)
                .dataSource(dataSource)
                .load();

        flyway.migrate();

        this.dataSource = dataSource;
    }

    public static DataSource getDataSource(){
        if (instance == null){
            instance = new DataSourceService();
        }
        return instance.dataSource;
    }
}
