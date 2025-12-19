package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.entites.Ville;
import fr.diginamic.hello.exception.VilleApiException;
import fr.diginamic.hello.service.VilleService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ville")
public class VilleControleur {


@Autowired
private final VilleService villeService;

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
    public List<Ville> getVille() {
        return villeService.extraireVille();
    }

    /**
     * @param ville le nom de la ville à ajouter
     * @param result
     * @return une liste de ville
     */
    @PostMapping
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody Ville ville, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<FieldError> errors = result.getFieldErrors();
                String message = errors.stream()
                        .map(e -> e.getField() + " : " + e.getDefaultMessage())
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(message);
            }

            List<Ville> villes = villeService.ajouterVille(ville);
            String noms = villes.stream()
                    .map(Ville::getNom)
                    .collect(Collectors.joining(", "));
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
    public List<Ville> getVilleChaine(@PathVariable String chaine) throws VilleApiException {
        System.out.println("Recherche par nom = " + chaine);
        return villeService.rechercherVilleParNom(chaine);
    }


    /**
     * Modifie une ville par son id
     * @param id
     * @param villeModifiee
     * @param result
     * @return
     * @throws VilleApiException
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody Ville villeModifiee, BindingResult result) throws VilleApiException {
        System.out.println("Modification de la ville id = " + id);

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            String message = errors.stream()
                    .map(e -> e.getField() + " : " + e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new VilleApiException(message);
        }
        villeService.modifierVilleNom(id, villeModifiee);
        return ResponseEntity.ok("La ville " + villeModifiee.getNom() + " a été modifiée");
    }


    /**
     * recherche par son ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Optional<Ville> getVilleId(@PathVariable int id) {
        System.out.println("Recherche par id = " + id);
        return villeService.extraireVille().stream().filter(v -> v.getId() == id).findFirst();
    }

    /**
     * Recherche par nom
     * @param nom
     * @return
     * @throws VilleApiException
     */
    @GetMapping("/nom/{nom}")
    public Ville getVilleNom(@PathVariable String nom) throws VilleApiException {
        System.out.println("Recherche par nom = " + nom);

        // Vérifie si le nom est null ou vide
        if (nom == null || nom.trim().isEmpty()) {
            throw new VilleApiException("La recherche ne peut être vide ou null");
        }

        // Recherche la ville
        Optional<Ville> ville = villeService.extraireVille().stream()
                .filter(v -> v.getNom().equalsIgnoreCase(nom))
                .findFirst();

        // Si la ville n'est pas trouvée, lever une exception
        if (ville.isEmpty()) {
            throw new VilleApiException("Aucune ville trouvée avec le nom : " + nom);
        }
        return ville.get();
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





    /*
     * recherche par minimum de population
     */
    @GetMapping("/population/{min}")
    public List<Ville> getVillePopulation (@PathVariable int min) throws VilleApiException {
        System.out.println("Recherche par nom = " + min);

        // Vérifie si la chaine est null ou vide
        if (min < 0) {
            throw new VilleApiException("La recherche ne peut être vide ou null");
        }

        // Recherche les villes en fonction de la population
        List<Ville> villes = villeService.extraireVille().stream()
                .filter(ville -> ville.getPopulation() > min)
                .collect(java.util.stream.Collectors.toList());

        // Si pas de ville trouvée, lève une exception
        if (villes.isEmpty()) {
            throw new VilleApiException("Aucune ville trouvée avec une population supérieure à " + min);
        }

        return villes;
    }


}


