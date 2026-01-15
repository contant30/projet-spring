package fr.diginamic.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecuriteConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securiteFilterChain(HttpSecurity http, MemoireService userDetailsService, JwtAuthenticationFilter jwtFilter) throws Exception{

        http.userDetailsService(userDetailsService);
        // 1) Je veux HTTP Basic avec le paramétrage par défaut
        http.httpBasic(Customizer.withDefaults());

        http.cors(Customizer.withDefaults());

        // Supprimer
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/login").permitAll());

        // 2) Règles d'autorisation HTTP pour different rôles
        http.authorizeHttpRequests(auth-> auth
                .requestMatchers(HttpMethod.POST,"/ville/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/ville/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/ville/**").hasRole("USER")
                .requestMatchers(HttpMethod.PUT,"/ville/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Front Angular
        config.setAllowedOriginPatterns(List.of("http://localhost:4200", "http://localhost:*"));

        // Méthodes API
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Headers (JWT, Content-Type)
        config.setAllowedHeaders(List.of("*"));

        // Auth cookies/JWT
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }


}
