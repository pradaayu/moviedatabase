package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OmdbConfig {

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    public String getOmdbApiKey() {
        return omdbApiKey;
    }
}