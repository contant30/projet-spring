package fr.diginamic.service;

import fr.diginamic.entites.Departement;
import fr.diginamic.exception.VilleApiException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface iDepartementService {
    @Transactional
    void saveDepartement(Departement departement);

    @Transactional
    List<Departement> extraireDepartement();

    @Transactional
    Optional<Departement> extractDepartementID(Long id);

    @Transactional
    Departement extractDepartementCode(String codeDepartement);

    @Transactional
    Departement extractDepartementNom(String nomDepartement);

    @Transactional
    Departement ajouterDepartement(Departement departement) throws VilleApiException;

    @Transactional
    List<Departement> rechercherDepartementNom(String chaine) throws VilleApiException;

    @Transactional
    Departement modifierDepartementNom(Long idDepartement, Departement departementModifiee) throws VilleApiException;
}
