package fr.diginamic.controleurs;

import fr.diginamic.DTO.DepartementDto;
import fr.diginamic.DTO.VilleDto;
import fr.diginamic.entites.Departement;
import fr.diginamic.entites.Ville;
import fr.diginamic.exception.VilleApiException;
import fr.diginamic.service.DepartementService;
import fr.diginamic.service.VilleService;
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
public class VilleControleur {


@Autowired
private final VilleService villeService;

    @Autowired
    private VilleMapper villeMapper;

    @Autowired
    private DepartementService departementService;

    /**
     * Constructeur
     * @param villeService
     */
    public VilleControleur(VilleService villeService) {
        this.villeService = villeService;
    }

    /**
     * @return une liste de ville
     */
    @GetMapping
    public List<VilleDto> getVille() {
        List<Ville> villes = villeService.extraireVille();
        return villes.stream().map(villeMapper::toDto).collect(Collectors.toList());
    }

    /**
     * @param villeDto le nom de la ville à ajouter
     * @param result
     * @return une liste de ville
     */

    @PostMapping
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody VilleDto villeDto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                String message = result.getFieldErrors().stream().map(e -> e.getField() + " : " + e.getDefaultMessage()).collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(message);
            }

            // Au moins code doit être présent
            DepartementDto depDto = villeDto.getDepartementDto();
            if (depDto == null ||(depDto.getId() == null &&(depDto.getCodePostale() == null || depDto.getCodePostale().isBlank()))) {
                return ResponseEntity.badRequest().body("Un département doit être associé");
            }

            Departement departement = null;
            // Recherche par id
            if (depDto.getId() != null) {
                departement = departementService.extractDepartementID(depDto.getId());
            }

            if (departement == null &&depDto.getCodePostale() != null && !depDto.getCodePostale().isEmpty()) {

                String code = depDto.getCodePostale();
                departement = departementService.extractDepartementCode(code);

                if (departement == null) {
                    Departement nouveau = new Departement();
                    nouveau.setNom(depDto.getNom());
                    nouveau.setCodePostale(depDto.getCodePostale());
                    departementService.saveDepartement(nouveau);
                    departement = nouveau;
                }
            }

            //  Si toujours null → exception "département inconnu"
            if (departement == null) {
                throw new VilleApiException("Département inconnu : aucun id ou code valide fourni");
            }

            Ville ville = villeMapper.toBean(villeDto);
            ville.setDepartement(departement);

            List<Ville> villes = villeService.ajouterVille(ville);
            String noms = villes.stream().map(Ville::getNom).collect(Collectors.joining(", "));
            return ResponseEntity.ok("Les villes suivantes sont maintenant en base : " + noms);

        } catch (VilleApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Recherche ville par caractère
     * @param chaine
     * @return
     * @throws VilleApiException
     */
    @GetMapping("/chaine/{chaine}")
    public List<VilleDto> getVilleChaine(@PathVariable String chaine) throws VilleApiException {
        System.out.println("Recherche par nom = " + chaine);
        List<Ville> villes = villeService.rechercherVilleParNom(chaine);
        return villes.stream().map(villeMapper::toDto).collect(Collectors.toList());
    }

    /**
     *
     * @param id
     * @param villeDto
     * @param result
     * @return
     * @throws VilleApiException
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody VilleDto villeDto, BindingResult result) throws VilleApiException {
        System.out.println("Modification de la ville id = " + id);

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            String message = errors.stream().map(e -> e.getField() + " : " + e.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new VilleApiException(message);
        }

        Ville ville = villeMapper.toBean(villeDto);

        villeService.modifierVilleNom(id, ville);
        return ResponseEntity.ok("La ville " + villeDto.getNom() + " a été modifiée");
    }

    /**
     * recherche par son ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Optional<VilleDto> getVilleId(@PathVariable int id) {
        System.out.println("Recherche par id = " + id);
        Optional<Ville> ville = villeService.extraireVille().stream().filter(v -> v.getId() == id).findFirst();
        return ville.map(villeMapper::toDto);
    }

    /**
     * Recherche par nom
     * @param nom
     * @return
     * @throws VilleApiException
     */
    @GetMapping("/nom/{nom}")
    public VilleDto getVilleNom(@PathVariable String nom) throws VilleApiException {
        System.out.println("Recherche par nom = " + nom);

        if (nom == null || nom.trim().isEmpty()) {
            throw new VilleApiException("La recherche ne peut être vide ou null");
        }

        Optional<Ville> ville = villeService.extraireVille().stream().filter(v -> v.getNom().equalsIgnoreCase(nom)).findFirst();

        if (ville.isEmpty()) {
            throw new VilleApiException("Aucune ville trouvée avec le nom : " + nom);
        }
        return villeMapper.toDto(ville.get());
    }

    /**
     * Supprime une ville par rapport à son Id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerVille(@PathVariable int id) {
        System.out.println("Suppression de la ville id = " + id);

        try {
            villeService.supprimerVille(id);
            return ResponseEntity.ok("La ville avec l'id " + id + " a été supprimée");
        } catch (VilleApiException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/population/{min}")
    public List<VilleDto> getVillePopulation(@PathVariable int min) throws VilleApiException {
        System.out.println("Recherche par nom = " + min);

        if (min < 0) {
            throw new VilleApiException("La recherche ne peut être vide ou null");
        }

        List<Ville> villes = villeService.extraireVille().stream().filter(ville -> ville.getPopulation() > min).collect(Collectors.toList());

        if (villes.isEmpty()) {
            throw new VilleApiException("Aucune ville trouvée avec une population supérieure à " + min);
        }
        return villes.stream().map(villeMapper::toDto).collect(Collectors.toList());
    }
}


