package fr.diginamic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Contact contact = new Contact().name("Nom du contact").email("email@exemple.com").url("URL ducontact");
        License license = new License().name("Nom de la licence").url("URL de la licence");
        Info info = new Info()
                .title("API Recensement")
                .version("1.0")
                .description("Cette API fournit des données de recensement de population pour la France.")
                .contact(contact)
                .license(license);

       return new OpenAPI().info(info);

    }
}
