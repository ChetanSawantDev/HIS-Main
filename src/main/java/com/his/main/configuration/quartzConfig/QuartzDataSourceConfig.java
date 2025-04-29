package com.his.main.configuration.quartzConfig;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class QuartzDataSourceConfig {
    @Bean("quartzDataSource")
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource quartzDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/his-main")
                .username("postgres")
                .password("Arthur@8149")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
