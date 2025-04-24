package com.his.main.configuration.quartzConfig;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

public class QuartzDataSourceConfig {
    @Bean(name = "quartzDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/his-main")
                .username("postgres")
                .password("Arthur@8149")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
