package fr.diginamic;

import fr.diginamic.DTO.DepartementDto;
import fr.diginamic.entites.Departement;
import fr.diginamic.exception.VilleApiException;
import fr.diginamic.service.DepartementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@SpringBootApplication
public class TraitementFichier implements CommandLineRunner {

    @Autowired
    private DepartementService departementService;

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(TraitementFichier.class)
                .web(WebApplicationType.NONE);
        builder.run(args);
    }


    @Override
    public void run(String... args) throws Exception {

        RestTemplate template = new RestTemplate();
        DepartementDto[] liste = template.getForObject("https://geo.api.gouv.fr/departements", DepartementDto[].class);

        int miseAJour = 0, cree = 0;

        for (DepartementDto depDto : liste) {
            Optional<Departement> optDep = departementService.findByCode(depDto.getCode());
            if (optDep.isPresent()) {
                Departement dep = optDep.get();
                dep.setNom(depDto.getNom());
                dep.setCode(depDto.getCode());

                try {
                    departementService.modifierDepartementNom(optDep.get().getId(), dep);
                    miseAJour++;
                } catch (VilleApiException e) {
                    System.err.println("Erreur update " + depDto.getCode() + ": " + e.getMessage());
                }

                System.out.printf("Mise à jour: %d, Créés: %d%n", miseAJour, cree);
            }
        }
    }
}


