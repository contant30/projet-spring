package fr.diginamic.service;

import fr.diginamic.entites.Ville;
import fr.diginamic.exception.VilleApiException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IVilleService {
    @Transactional
    List<Ville> extraireVille();

    @Transactional
    Ville extractVilleID(Integer idVille) throws VilleApiException;

    @Transactional
    List<Ville> ajouterVille(Ville ville) throws VilleApiException;

    @Transactional
    List<Ville> modifierVilleNom(Integer idVille, Ville villeModifiee) throws VilleApiException;

    @Transactional
    void supprimerVille(int id) throws VilleApiException;

    List<Ville> rechercherVillesParCaracteres(String prefix);

    List<Ville> villesPopulationSupMin(Integer min);

    List<Ville> villesPopulationsSupMinInfMax(Integer min, Integer max);

    List<Ville> villesPopulationDepartementSupMin(String codeDep, Integer min);

    List<Ville> villesPopulationDepartementSupMinInfMax(String codeDep, Integer min, Integer max);

    List<Ville> topVillesDepartement(int n, String codeDep);

    Ville rechercheVilleParNom(String nom);
}
