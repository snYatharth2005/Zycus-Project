package com.stockpulse.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

/** Enables SQLite's writer-friendly settings for the single pooled connection. */
@Configuration
@Profile("!postgres")
public class SqlitePragmaInitializer {
    public SqlitePragmaInitializer(DataSource dataSource) {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        jdbc.execute("PRAGMA journal_mode=WAL");
        jdbc.execute("PRAGMA busy_timeout=5000");
    }
}