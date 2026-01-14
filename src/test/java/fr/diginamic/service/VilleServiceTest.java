package fr.diginamic.service;

import fr.diginamic.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes= Application.class)
@ActiveProfiles("test")
public class VilleServiceTest {

    @Test
    void contextLoads() {

    }
}
