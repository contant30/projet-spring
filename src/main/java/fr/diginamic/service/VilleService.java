package fr.diginamic.service;

//import fr.diginamic.dao.VilleDao;
import fr.diginamic.entites.Ville;
import fr.diginamic.exception.VilleApiException;
import fr.diginamic.repository.VilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import java.util.List;

@Service
public class VilleService implements IVilleService {

//    @Autowired
//    private VilleDao villeDao;

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private EntityManager entityManager;

//    @Transactional
//    public void saveVille(Ville ville) {
//        entityManager.persist(ville);
//    }

    /**
     * @return une liste de ville
     */
    @Transactional
    @Override
    public List<Ville> extraireVille() {
        return (List<Ville>) villeRepository.findAll();
    }

    /**
     * Recherche une ville par id
     * @param idVille l'identifiant de la ville recherchée
     * @return une ville par son id
     */
    @Transactional
    @Override
    public Ville extractVilleID(Integer idVille) throws VilleApiException {
        return villeRepository.findById(idVille)
                .orElseThrow(() -> new VilleApiException("Ville " + idVille + " introuvable"));    }

//    /**
//     * @param nomVille le nom de la ville recherchée
//     * @return une ville par son nom
//     */
//    @Transactional
//    public Ville extractVilleNom(String nomVille){
//        return villeDao.getVilleNom(nomVille);
//    }

//    /**
//     * Recherche une ville par rapport à son nom
//     * @param chaine
//     * @return
//     * @throws VilleApiException
//     */
//    @Transactional
//    public List<Ville> rechercherVilleParNom(String chaine) throws VilleApiException {
//        if (chaine == null || chaine.trim().isEmpty()) {
//            throw new VilleApiException("La recherche ne peut être vide ou null");
//        }
//        return extraireVille().stream()
//                .filter(ville -> ville.getNom().toLowerCase().startsWith(chaine.toLowerCase()))
//                .collect(Collectors.toList());
//    }


    /**
     * Ajoute une ville
     * @param ville le nom de la ville ajouté
     * @return une liste de ville
     * @throws VilleApiException
     */
    @Transactional
    @Override
    public List<Ville> ajouterVille(Ville ville) throws VilleApiException {

        for (Ville v : extraireVille()){
            if ((v.getNom().equals(ville.getNom()))){
                throw new VilleApiException("la ville existe déjà");
            }
        }
        villeRepository.save(ville);

        return extraireVille();
    }

    /**
     * Modifie une ville par rapport à son id
     * @param idVille
     * @param villeModifiee
     * @return une liste de ville
     * @throws VilleApiException
     */
    @Transactional
    @Override
    public List<Ville> modifierVilleNom(Integer idVille, Ville villeModifiee) throws VilleApiException{

        Ville villeExiste = villeRepository.findById(idVille).orElseThrow(()
                -> new VilleApiException("Ville " + idVille + " introuvable"));

        if (villeExiste == null) {
            throw new VilleApiException("Pas de ville trouvée avec l'id : " + idVille);
        }

        // Vérifie si le nouveau nom existe déjà
        if (!villeExiste.getNom().equals(villeModifiee.getNom())) {
            for (Ville v : extraireVille()) {
                if (v.getNom().equals(villeModifiee.getNom())) {
                    throw new VilleApiException("Une ville avec ce nom existe déjà");
                }
            }
        }
        // Mise à jour
        villeExiste.setNom(villeModifiee.getNom());
        villeExiste.setPopulation(villeModifiee.getPopulation());

        // Sauvegarde en base
        villeRepository.save(villeExiste);

        // Retourne la liste des villes
        return extraireVille();
    }

    /**
     *Supprime une ville par rapport à son Id
     * @param id
     */
    @Transactional
    @Override
    public void supprimerVille(int id) throws VilleApiException {
        if (!villeRepository.existsById(id)) {
            throw new VilleApiException("Ville " + id + " introuvable");
        }
        villeRepository.deleteById(id);
    }

    @Override
    public List<Ville> rechercherVillesParCaracteres(String prefix) {
        return villeRepository.findByNomStartingWithIgnoreCase(prefix);
    }

    @Override
    public List<Ville> villesPopulationSupMin(Integer min) {
        return villeRepository.findByPopulationGreaterThanOrderByPopulationDesc(min);
    }

    @Override
    public List<Ville> villesPopulationsSupMinInfMax(Integer min, Integer max){
        return villeRepository.findByPopulationBetweenOrderByPopulationDesc(min, max);
    }

    @Override
    public List<Ville> villesPopulationDepartementSupMin(String codeDep, Integer min){
        return villeRepository.findByDepartementCodePostaleAndPopulationGreaterThanOrderByPopulationDesc(codeDep, min);
    }

    @Override
    public List<Ville> villesPopulationDepartementSupMinInfMax(String codeDep, Integer min, Integer max){
        return villeRepository.findByDepartementCodePostaleAndPopulationBetweenOrderByPopulationDesc(codeDep, min, max);
    }

    @Override
    public List<Ville> topVillesDepartement(int n, String codeDep){
        return villeRepository.findTopByDepartementCodePostaleOrderByPopulationDesc(codeDep, n);
    }

    @Override
    public Ville rechercheVilleParNom(String nom){
        return villeRepository.findByNomIgnoreCase(nom);
    }


}
