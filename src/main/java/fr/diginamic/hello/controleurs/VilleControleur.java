package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.entite.Ville;
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

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ville")
public class VilleControleur {


@Autowired
private final VilleService villeService;

    /*
     * Constructeur
     */
    public VilleControleur(VilleService villeService) {
        this.villeService = villeService;
    }

    /*
     * retourne la liste des villes
     */
    @GetMapping//(path = "/ville")
    public List<Ville> getVille() {
        return villeService.extraireVille();
    }

    /*
     * Ajout d'une ville
     */
    @PostMapping//(path = "/ville")
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody Ville ville, BindingResult result) throws VilleApiException {
        System.out.println(ville);

        if (result.hasErrors()){
            List<FieldError> errors = result.getFieldErrors();
            String message = errors.stream()
                    .map(e -> e.getField() + " : " + e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new VilleApiException(message);
        }

//        if (ville.getPopulation() < 10) {
//            throw new VilleApiException(" La ville doit avoir au moins 10 habitant");
//        }
//        if (ville.getNom().length() < 2) {
//            throw new VilleApiException(" La ville doit avoir au moins 2 lettres");
//        }
        // recherche si une ville est déjà present
        for (Ville v : villeService.extraireVille()) {
            if (v.getNom().equals(ville.getNom())) {
                throw new VilleApiException(" La ville existe déjà");
            }
        }
        villeService.ajouterVille(ville);
        return ResponseEntity.ok(" La ville " + ville.getNom() + " est bien ajouté avec " + ville.getPopulation() + " population");
    }

    /*
     * Recherche par id
     */
    @GetMapping("/{id}")
    public Optional<Ville> getVilleId(@PathVariable int id) {
        System.out.println("Recherche par id = " + id);
        return villeService.extraireVille().stream().filter(v -> v.getId() == id).findFirst();
    }

    /*
     * recherche par nom
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
     /*
     * recherche par chaine de caractère
     */
    @GetMapping("/chaine/{chaine}")
    public List<Ville> getVilleChaine (@PathVariable String chaine) throws VilleApiException {
         System.out.println("Recherche par nom = " + chaine);

            // Vérifie si la chaine est null ou vide
         if (chaine == null || chaine.trim().isEmpty()) {
                throw new VilleApiException("La recherche ne peut être vide ou null");
            }
            // Recherche les villes dont le nom contient la chaîne
         return villeService.extraireVille().stream()
                    .filter(ville -> ville.getNom().toLowerCase().startsWith(chaine.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
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



    /*
     * Modifier une ville par son ID
     */
    @PutMapping("/{id}")

    public ResponseEntity<String> modifierVille( @PathVariable int id, @Valid @RequestBody Ville villeModifiee,BindingResult result) throws
            VilleApiException {
        System.out.println("Modification de la ville id = " + id);

        if (result.hasErrors()){
            List<FieldError> errors = result.getFieldErrors();
            String message = errors.stream()
                    .map(e -> e.getField() + " : " + e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new VilleApiException(message);
        }

        // Recherche de la ville
        Optional<Ville> villeExist = villeService.extraireVille().stream().filter(v -> v.getId() == id).findFirst();

        // Si la ville n'existe pas par rapport à l'id, une exception
        if (villeExist.isEmpty()) {
            throw new VilleApiException("La ville avec l'id " + id + " n'existe pas");
        }

        // Vérification que la population est > 10 et que le nom a au moins 2 lettres
//        if (villeModifiee.getPopulation() < 10) {
//            throw new VilleApiException(" La ville doit avoir au moins 10 habitant");
//        }
//        if (villeModifiee.getNom().length() < 2) {
//            throw new VilleApiException("La ville doit avoir au moins 2 lettres");
//        }

        // mise à jours des infos
        Ville exsite = villeExist.get();
        exsite.setNom(villeModifiee.getNom());
        exsite.setPopulation(villeModifiee.getPopulation());

        return ResponseEntity.ok("La ville " + exsite.getNom() + " a été modifiée");
    }

    /*
     *Supprimer une ville par son ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerVille(@PathVariable int id) {
        System.out.println("Suppression de la ville id = " + id);

        // Recherche de la ville
        Optional<Ville> villeExist = villeService.extraireVille().stream().filter(v -> v.getId() == id).findFirst();

        // Si la ville n'existe pas par rapport à l'id, retourne une erreur
        if (villeExist.isEmpty()) {
            return ResponseEntity.badRequest().body("La ville avec l'id " + id + " n'existe pas");
        }

        // Suppression de la ville
        villeService.supprimerVille(id);

        return ResponseEntity.ok("La ville avec l'id " + id + " a été supprimée");
    }

}


