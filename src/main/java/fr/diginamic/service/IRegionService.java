package fr.diginamic.service;

import fr.diginamic.entites.Region;
import fr.diginamic.exception.VilleApiException;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface IRegionService {
    @Transactional
    Region extractRegionCode(String codeRegion);

    Optional<Region> findByCode(String code);

    @Transactional
    Region modifierRegionNom(Long idRegion, Region regionModifiee) throws VilleApiException;

    Region ajouterRegion(Region region) throws VilleApiException;
}
