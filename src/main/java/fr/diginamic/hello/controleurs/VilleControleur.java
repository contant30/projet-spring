package fr.diginamic.hello.controleurs;

import fr.diginamic.hello.entite.Ville;
import fr.diginamic.hello.service.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ville")
public class VilleControleur {


    @Autowired
    private final VilleService villeService;

    // Constructeur
    public VilleControleur(VilleService villeService) {
        this.villeService = villeService;
    }

    // retourne la liste des villes
    @GetMapping//(path = "/ville")
    public List<Ville> getVille() {
        return villeService.extraireVille();
    }

    // Ajout d'une ville
    @PostMapping//(path = "/ville")
    public ResponseEntity<String> ajouterVille (@RequestBody Ville ville){
        System.out.println(ville);

        // recherche si une ville est déjà present
        for (Ville v : villeService.extraireVille()){
            if (v.getNom().equals(ville.getNom())){
                return ResponseEntity.badRequest().body("La ville existe déjà");
            }
        }
        villeService.ajouterVille(ville);
        return ResponseEntity.ok(" La ville "+ ville.getNom()+ " est bien ajouté avec "+ ville.getPopulation() + " population");
    }



    // Recherche par id
    @GetMapping("/{id}")
    public Optional<Ville> getVilleId(@PathVariable int id){
        System.out.println("Recherche par id = "+ id);
        return villeService.extraireVille().stream().filter(v->v.getId()==id).findFirst();
    }


    // Modifi une ville par son ID
    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille (@PathVariable int id,@RequestBody Ville villeModifiee){
        System.out.println("Modification de la ville id = "+ id);

        // Recherche de la ville
        Optional<Ville> villeExist = villeService.extraireVille().stream().filter(v -> v.getId()==id).findFirst();

        // Si la ville n'existe pas, une exception
        if (villeExist.isEmpty()){
            return ResponseEntity.badRequest().body("La ville avec l'id " + id + " n'existe pas");
        }

        // mise à jours des infos
        Ville exsite = villeExist.get();
        exsite.setNom(villeModifiee.getNom());
        exsite.setPopulation(villeModifiee.getPopulation());

        return ResponseEntity.ok("La ville " + exsite.getNom() + " a été modifiée");

    }


}
