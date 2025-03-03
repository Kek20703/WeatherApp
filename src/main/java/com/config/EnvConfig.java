package com.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration

public class EnvConfig {
    private final Dotenv dotenv;


    public EnvConfig() {
        this.dotenv = Dotenv.load();
    }

    public String getDbUser() {
        return dotenv.get("DB_USER");
    }

    public String getDbPassword() {
        return dotenv.get("DB_PASSWORD");
    }

    public String getDbUrl() {
        return dotenv.get("DB_URL");
    }

    public String getDbDriver() {
        return dotenv.get("DB_DRIVER");
    }

    public String getApiKey() {
        return dotenv.get("API_KEY");
    }

}
