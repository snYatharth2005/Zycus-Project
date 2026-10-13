package com.stockpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.stockpulse.ai.LlmProperties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(LlmProperties.class)
public class StockPulseApplication {
    public static void main(String[] args) {
        try {
            Files.createDirectories(Path.of("data"));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to create SQLite data directory", exception);
        }
        SpringApplication.run(StockPulseApplication.class, args);
    }
}