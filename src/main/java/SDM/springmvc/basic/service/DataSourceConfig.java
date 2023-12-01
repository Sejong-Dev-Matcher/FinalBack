package SDM.springmvc.basic.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://sdm-database.ckkpavqulpya.ap-northeast-2.rds.amazonaws.com:3306/SDM?characterEncoding=UTF-8&serverTimezone=UTC");
        dataSource.setUsername("admin");
        dataSource.setPassword("19980332");
        return dataSource;
    }
}
