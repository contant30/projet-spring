package fr.diginamic.repository;

import fr.diginamic.entites.Ville;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VilleRepository extends CrudRepository<Ville, Integer> {


    // Recherche une ville par son nom
    Ville findByNomIgnoreCase(String nom);


    // Recherche de toutes les villes dont le nom commence par une chaine de caractères
    //donnée
    List<Ville> findByNomStartingWithIgnoreCase(String prefix);

    // Recherche de toutes les villes dont la population est supérieure à min (paramètre de
    //type int). Les villes sont retournées par population descendante.

    List<Ville> findByPopulationGreaterThanOrderByPopulationDesc(Integer min);

    //Recherche de toutes les villes dont la population est supérieure à min et inférieure à
    //max. Les villes sont retournées par population descendante.

    List<Ville> findByPopulationBetweenOrderByPopulationDesc(Integer min, Integer max);

    //Recherche de toutes les villes d’un département dont la population est supérieure à
    //min (paramètre de type int). Les villes sont retournées par population descendante.

    List<Ville> findByDepartementCodePostaleAndPopulationGreaterThanOrderByPopulationDesc(
            String codeDepartement, Integer min);

    //Recherche de toutes les villes d’un département dont la population est supérieure à
    //min et inférieure à max. Les villes sont retournées par population descendante.

    List<Ville> findByDepartementCodePostaleAndPopulationBetweenOrderByPopulationDesc(
            String codeDepartement, Integer min, Integer max);

    //Recherche des n villes les plus peuplées d’un département donné (n est aussi un
    //paramètre)
    List<Ville> findTopByDepartementCodePostaleOrderByPopulationDesc(
            String codeDepartement, int n);

    List<Ville> findByDepartementCodePostaleStartingWith(String codeDepartement);

}
