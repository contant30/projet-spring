//package fr.diginamic;
//
//import fr.diginamic.DTO.RegionDto;
//import fr.diginamic.entites.Region;
//import fr.diginamic.exception.VilleApiException;
//import fr.diginamic.service.IRegionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Optional;
//
//@SpringBootApplication
//
//public class TraitementFichierRegion implements CommandLineRunner {
//
//
//    @Autowired
//    private IRegionService IRegionService;
//
//    public static void main(String[] args) {
//        SpringApplicationBuilder builder = new SpringApplicationBuilder(TraitementFichierRegion.class)
//                .web(WebApplicationType.NONE);
//        builder.run(args);
//    }
//
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        RestTemplate template = new RestTemplate();
//        RegionDto[] liste = template.getForObject("https://geo.api.gouv.fr/regions", RegionDto[].class);
//
//        for (RegionDto regDto : liste) {
//            Optional<Region> optReg = IRegionService.findByCode(regDto.getCode());
//
//            if (optReg.isPresent()) {
//                Region reg = optReg.get();
//                reg.setNom(regDto.getNom());
//
//                try {
//                    IRegionService.modifierRegionNom(optReg.get().getId(), reg);
//                } catch (VilleApiException e) {
//                    System.err.println("Erreur update " + regDto.getCode() + ": " + e.getMessage());
//                }
//            } else {
//
//                Region reg = new Region();
//                reg.setCode(regDto.getCode());
//                reg.setNom(regDto.getNom());
//                try {
//                    IRegionService.ajouterRegion(reg);
//
//            } catch (VilleApiException e) {
//
//                }
//            }
//        }
//    }
//}

