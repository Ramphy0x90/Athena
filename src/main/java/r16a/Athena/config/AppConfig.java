package r16a.Athena.config;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${app.base-url}")
    private String appBaseUrl;

    @Value("${server.port}")
    private int serverPort;

    @Value("${app.is-production}")
    private boolean isProduction;

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
