package fr.diginamic.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecuriteConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securiteFilterChain(HttpSecurity http, MemoireService userDetailsService) throws Exception{

        http.userDetailsService(userDetailsService);
        // 1) Je veux HTTP Basic avec le paramétrage par défaut
        http.httpBasic(Customizer.withDefaults());

        // Supprimer
        http.csrf(csrf -> csrf.disable());

        // 2) Règles d'autorisation HTTP pour different rôles
        http.authorizeHttpRequests(auth-> auth
                .requestMatchers(HttpMethod.POST,"/ville/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/ville/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/ville/**").hasRole("USER")
                .requestMatchers(HttpMethod.PUT,"/ville/**").hasRole("ADMIN")
                .anyRequest().authenticated());
        return http.build();
    }
}
