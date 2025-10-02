package __Y2_S1_MTR_02.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Allow access to static resources
                .requestMatchers("/templetes/**", "/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                // Allow access to home page and index
                .requestMatchers("/", "/index", "/health").permitAll()
                // Allow access to API endpoints
                .requestMatchers("/api/**").permitAll()
                // Allow access to login page
                .requestMatchers("/login").permitAll()
                // Require authentication for all other requests
                .anyRequest().permitAll() // Temporarily allow all for testing
            )
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API endpoints
            .formLogin(form -> form.disable()) // Disable default form login
            .httpBasic(basic -> basic.disable()) // Disable HTTP basic auth
            .logout(logout -> logout.disable()); // Disable logout

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
