package fr.diginamic.controleurs;

import fr.diginamic.DTO.DepartementDto;
import fr.diginamic.DTO.VilleDto;
import fr.diginamic.entites.Departement;
import fr.diginamic.entites.Ville;
import fr.diginamic.exception.VilleApiException;
import fr.diginamic.repository.VilleRepository;
import fr.diginamic.service.IVilleService;
import fr.diginamic.service.iDepartementService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fr.diginamic.mapper.VilleMapper;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ville")
public class VilleControleur implements IVilleControleur {


    @Autowired
    private IVilleService IVilleService;

    @Autowired
    private VilleMapper villeMapper;

    @Autowired
    private iDepartementService iDepartementService;

    @Autowired
    private VilleRepository villeRepository;



    /**
     * Constructeur
     * @param IVilleService
     */
    public VilleControleur(IVilleService IVilleService) {
        this.IVilleService = IVilleService;
    }


    /**
     * Affiche toutes les villes
     * @return une liste de ville
     */
    @GetMapping
    @Override
    public List<VilleDto> getVille() {
        List<Ville> villes = IVilleService.extraireVille();
        return villes.stream().map(villeMapper::toDto).collect(Collectors.toList());
    }


    /**
     * Ajouter une ville
     * @param villeDto le nom de la ville à ajouter
     * @param result
     * @return une liste de ville
     */

    @PostMapping
    @Override
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody VilleDto villeDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                String message = result.getFieldErrors().stream().map(e -> e.getField() + " : " + e.getDefaultMessage()).collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(message);
            }

            // code doit être présent
            DepartementDto depDto = villeDto.getDepartementDto();
            if (depDto == null ||(depDto.getId() == null &&(depDto.getCode() == null || depDto.getCode().isBlank()))) {
                return ResponseEntity.badRequest().body("Un département doit être associé");
            }

            Departement departement = null;
            // Recherche par id
            if (depDto.getId() != null) {
                departement = iDepartementService.extractDepartementID(depDto.getId())
                        .orElseThrow(() -> new VilleApiException("Département ID " + depDto.getId() + " introuvable"));
            }

            if (departement == null &&depDto.getCode() != null && !depDto.getCode().isEmpty()) {

                String code = depDto.getCode();
                departement = iDepartementService.extractDepartementCode(code);

                if (departement == null) {
                    Departement nouveau = new Departement();
                    nouveau.setNom(depDto.getNom());
                    nouveau.setCode(depDto.getCode());
                    iDepartementService.saveDepartement(nouveau);
                    departement = nouveau;
                }
            }

            //  Si toujours null → exception "département inconnu"
            if (departement == null) {
                throw new VilleApiException("Département inconnu : aucun id ou code valide fourni");
            }

            Ville ville = villeMapper.toBean(villeDto);
            ville.setDepartement(departement);

            List<Ville> villes = IVilleService.ajouterVille(ville);
            String noms = villes.stream().map(Ville::getNom).collect(Collectors.joining(", "));
            return ResponseEntity.ok("Les villes suivantes sont maintenant en base : " + noms);

        } catch (VilleApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    /**
     * Modifier une ville par son id
     * @param id
     * @param villeDto
     * @param result
     * @return
     * @throws VilleApiException
     */
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody VilleDto villeDto, BindingResult result) throws VilleApiException {
        System.out.println("Modification de la ville id = " + id);

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            String message = errors.stream().map(e -> e.getField() + " : " + e.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new VilleApiException(message);
        }

        Ville ville = villeMapper.toBean(villeDto);

        IVilleService.modifierVilleNom(id, ville);
        return ResponseEntity.ok("La ville " + villeDto.getNom() + " a été modifiée");
    }



//    /**
//     * Recherche par nom
//     * @param nom
//     * @return
//     * @throws VilleApiException
//     */
//    @GetMapping("/nom/{nom}")
//    public VilleDto getVilleNom(@PathVariable String nom) throws VilleApiException {
//        System.out.println("Recherche par nom = " + nom);
//
//        if (nom == null || nom.trim().isEmpty()) {
//            throw new VilleApiException("La recherche ne peut être vide ou null");
//        }
//
//        Optional<Ville> ville = villeService.extraireVille().stream().filter(v -> v.getNom().equalsIgnoreCase(nom)).findFirst();
//
//        if (ville.isEmpty()) {
//            throw new VilleApiException("Aucune ville trouvée avec le nom : " + nom);
//        }
//        return villeMapper.toDto(ville.get());
//    }

//    /**
//     *
//     * @param min
//     * @return
//     * @throws VilleApiException
//     */
//    @GetMapping("/population/{min}")
//    public List<VilleDto> getVillePopulation(@PathVariable int min) throws VilleApiException {
//        System.out.println("Recherche par nom = " + min);
//
//        if (min < 0) {
//            throw new VilleApiException("La recherche ne peut être vide ou null");
//        }
//
//        List<Ville> villes = villeService.extraireVille().stream().filter(ville -> ville.getPopulation() > min).collect(Collectors.toList());
//
//        if (villes.isEmpty()) {
//            throw new VilleApiException("Aucune ville trouvée avec une population supérieure à " + min);
//        }
//        return villes.stream().map(villeMapper::toDto).collect(Collectors.toList());
//    }

    /**
     * recherche par son ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Override
    public Optional<VilleDto> getVilleId(@PathVariable int id) {
        System.out.println("Recherche par id = " + id);
        Optional<Ville> ville = IVilleService.extraireVille().stream().filter(v -> v.getId() == id).findFirst();
        return ville.map(villeMapper::toDto);
    }

//    /**
//     * Recherche par nom
//     * @param nom
//     * @return
//     * @throws VilleApiException
//     */
//    @GetMapping("/nom/{nom}")
//    public VilleDto getVilleNom(@PathVariable String nom) throws VilleApiException {
//        System.out.println("Recherche par nom = " + nom);
//
//        if (nom == null || nom.trim().isEmpty()) {
//            throw new VilleApiException("La recherche ne peut être vide ou null");
//        }
//
//        Optional<Ville> ville = villeService.extraireVille().stream().filter(v -> v.getNom().equalsIgnoreCase(nom)).findFirst();
//
//        if (ville.isEmpty()) {
//            throw new VilleApiException("Aucune ville trouvée avec le nom : " + nom);
//        }
//        return villeMapper.toDto(ville.get());
//    }

    /**
     * Supprime une ville par rapport à son Id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> supprimerVille(@PathVariable int id) {
        System.out.println("Suppression de la ville id = " + id);

        try {
            IVilleService.supprimerVille(id);
            return ResponseEntity.ok("La ville avec l'id " + id + " a été supprimée");
        } catch (VilleApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Recherche une ville par nom
     * @param nom
     * @return
     * @throws VilleApiException
     */
    @GetMapping("/nom/{nom}")
    @Override
    public VilleDto getVillePopulationNom(@PathVariable String nom) throws VilleApiException {
        Ville ville = IVilleService.rechercheVilleParNom(nom);  // Retourne Ville unique
        if (ville == null) {
            throw new VilleApiException("Ville non trouvée : " + nom);
        }
        return villeMapper.toDto(ville);  // Mapper vers DTO
    }

    /**
     * Recherche toutes les villes dont la population est supérieure à min
     * @param min
     * @return
     */
    @GetMapping("/population/{min}")
    @Override
    public List<VilleDto> getVillePopulation(@PathVariable Integer min) {
        return IVilleService.villesPopulationSupMin(min)
                .stream().map(villeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Recherche toutes les villes dont la population est supérieure à Min et inférieur à Max
     * @param min
     * @param max
     * @return
     */
    @GetMapping("/population/{min}/{max}")
    @Override
    public List<VilleDto> getVillePopulation(@PathVariable Integer min, @PathVariable Integer max){
        return IVilleService.villesPopulationsSupMinInfMax(min, max)
                .stream().map(villeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Recherche toutes les villes d'un département dont la population est supérieure à Min
     * @param code
     * @param min
     * @return
     */
    @GetMapping("/departement/{code}/population/{min}")
    @Override
    public List<VilleDto> getVillesSupMinDep(@PathVariable String code, @PathVariable Integer min){
        return IVilleService.villesPopulationDepartementSupMin(code, min)
                .stream().map(villeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Recherche toutes les villes d'un département dont la population est supérieure à Min et inférieur à Max
     * @param code
     * @param min
     * @return
     */
    @GetMapping("/departement/{code}/population/{min}/{max}")
    @Override
    public List<VilleDto> getVillesSupMinInfMaxDep(@PathVariable String code, @PathVariable Integer min, @PathVariable Integer max){
     return IVilleService.villesPopulationDepartementSupMinInfMax(code, min, max)
             .stream().map(villeMapper::toDto)
             .collect(Collectors.toList());
    }

    /**
     * Recherche les n villes les plus peuplées d'un département
     * @param code
     * @param min
     * @return
     */
    @GetMapping("/departement/{code}/top/{n}")
    @Override
    public List<VilleDto> getTopVillesDepartement(@PathVariable String code, @PathVariable int n){
        return IVilleService.topVillesDepartement(n, code)
                .stream().map(villeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Recherche ville par caractère
     * @param chaine
     * @return
     * @throws VilleApiException
     */
    @GetMapping("/chaine/{chaine}")
    @Override
    public List<VilleDto> getVilleChaine(@PathVariable String chaine) throws VilleApiException {
        System.out.println("Recherche par nom = " + chaine);
        List<Ville> villes = IVilleService.rechercherVillesParCaracteres(chaine);
        return villes.stream().map(villeMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Mise en place export csv pour recherche de villes population sup à min
     * @param min
     * @param response
     * @throws Exception
     */
    @GetMapping("/population/{min}/fiche")
    public void ficheVille(@PathVariable Integer min, HttpServletResponse response) throws Exception{

        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"villes_min_" + min + ".csv\"");

        response.getWriter().append("Nom;habitant;code département");
        List<VilleDto> villes = IVilleService.villesPopulationSupMin(min)
                .stream().map(villeMapper::toDto)
                .collect(Collectors.toList());

        for (VilleDto v : villes){
            response.getWriter().append(v.getNom()+";"+v.getPopulation()+";"+v.getCodeDepartement());
        }
        response.flushBuffer();
    }

}


