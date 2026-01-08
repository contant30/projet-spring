package fr.diginamic.service;

//import fr.diginamic.dao.DepartementDao;
import fr.diginamic.entites.Departement;
import fr.diginamic.entites.Region;
import fr.diginamic.exception.VilleApiException;
import fr.diginamic.repository.DepartementRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartementService implements iDepartementService {

//    @Autowired
//    private DepartementDao departementDao;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private IRegionService IRegionService;


    @Transactional
    @Override
    public void saveDepartement(Departement departement) {
        entityManager.persist(departement);
    }

    /**
     * @return une liste de département
     */
    @Transactional
    @Override
    public List<Departement> extraireDepartement() {
        return departementRepository.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Optional<Departement> extractDepartementID(Long id) {
        return departementRepository.findById(id);
    }

    @Transactional
    @Override
    public Departement extractDepartementCode(String codeDepartement) {
        Optional<Departement> optional = departementRepository.findByCode(codeDepartement);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new EntityNotFoundException("Département non trouvé pour code: " + codeDepartement);
        }
    }

    /**
     * @param nomDepartement le nom de la ville recherchée
     * @return une ville par son nom
     */
    @Transactional
    @Override
    public Departement extractDepartementNom(String nomDepartement){
        return departementRepository.findByNomIgnoreCase(nomDepartement)
                .orElseThrow(() -> new EntityNotFoundException("Département '" + nomDepartement + "' introuvable"));
    }

    /**
     * @param departement le nom du département ajouté
     * @return une liste de departement
     * @throws VilleApiException
     */
    @Transactional
    @Override
    public Departement ajouterDepartement(Departement departement) throws VilleApiException {
        if (departementRepository.findByNomIgnoreCase(departement.getNom()).isPresent()) {
            throw new VilleApiException("Département '" + departement.getNom() + "' existe déjà");
        }
        return departementRepository.save(departement);
    }

    /**
     * Recherche un département par rapport à son nom
     * @param chaine
     * @return
     * @throws VilleApiException
     */
    @Transactional
    @Override
    public List<Departement> rechercherDepartementNom(String chaine) throws VilleApiException {
        if (chaine == null || chaine.trim().isEmpty()) {
            throw new VilleApiException("La recherche ne peut être vide ou null");
        }
        return extraireDepartement().stream().filter(departement -> departement.getNom().toLowerCase().startsWith(chaine.toLowerCase())).collect(Collectors.toList());
    }

    /**
     *
     * @param id
     * @param depModifiee
     * @return
     * @throws VilleApiException
     */
    @Transactional
    @Override
    public Departement modifierDepartementNom(Long idDepartement, Departement departementModifiee) throws VilleApiException {
        Departement depExiste = departementRepository.findById(idDepartement)
                .orElseThrow(() -> new VilleApiException("Département " + idDepartement + " introuvable"));


        String codeRegionInput = departementModifiee.getCodeRegion();  // Direct DTO
        if (codeRegionInput != null && !codeRegionInput.isEmpty()) {
            Region region = IRegionService.extractRegionCode(codeRegionInput);
            depExiste.setRegion(region);
        }

        depExiste.setNom(departementModifiee.getNom());
        depExiste.setCode(departementModifiee.getCode());

        return departementRepository.saveAndFlush(depExiste);
    }

    public Optional<Departement> findByCode(String code) {
        return departementRepository.findByCode(code);
    }

}
