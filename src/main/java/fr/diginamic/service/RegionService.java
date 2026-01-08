package fr.diginamic.service;

import fr.diginamic.entites.Departement;
import fr.diginamic.entites.Region;
import fr.diginamic.exception.VilleApiException;
import fr.diginamic.repository.RegionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegionService implements IRegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Transactional
    @Override
    public Region extractRegionCode(String codeRegion) {
        return regionRepository.findByCode(codeRegion)
                .orElseThrow(() -> new EntityNotFoundException("Région non trouvée: " + codeRegion));
    }

    @Override
    public Optional<Region> findByCode(String code) {
        return regionRepository.findByCode(code);
    }

    @Transactional
    @Override
    public Region modifierRegionNom(Long idRegion, Region regionModifiee) throws VilleApiException {
        Region regExiste = regionRepository.findById(idRegion)
                .orElseThrow(() -> new VilleApiException("Région #" + idRegion + " introuvable"));

        regExiste.setNom(regionModifiee.getNom());
        regExiste.setCode(regionModifiee.getCode());  // code aussi ?
        return regionRepository.save(regExiste);
    }

    @Override
    public Region ajouterRegion(Region region) throws VilleApiException {
        return regionRepository.save(region);
    }






}
