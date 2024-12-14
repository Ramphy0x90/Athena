package r16a.Athena.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import r16a.Athena.config.Jwt.JwtFilter;
import r16a.Athena.config.Jwt.JwtUtil;
import r16a.Athena.filters.IpRequestFilter;
import r16a.Athena.services.IpRequestTrackingService;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.function.Supplier;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final IpRequestTrackingService ipRequestTrackingService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    Supplier<String> secretGenerator() {
        return () -> {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[32];
            random.nextBytes(bytes);
            return Base64.getEncoder().encodeToString(bytes);
        };
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // APIs
                        .requestMatchers("/*/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/client/**").permitAll()
                        .requestMatchers("/api/v1/admin/authenticate").permitAll()
                        .requestMatchers("/api/v1/**").authenticated()

                        // Assets & templates
                        .requestMatchers(HttpMethod.GET, "/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.GET, "/css/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/js/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()

                        // Templates
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public FilterRegistrationBean<IpRequestFilter> ipRequestFilterRegistration() {
        FilterRegistrationBean<IpRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new IpRequestFilter(ipRequestTrackingService));
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:8080/", "http://auth.r16a.ch/", "https://cloud.r16a.ch/"));
        corsConfiguration.setAllowedHeaders(List.of(
                "Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"
        ));
        corsConfiguration.setExposedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }
}
