package fr.diginamic.service;


import fr.diginamic.dao.VilleDao;
import fr.diginamic.entites.Ville;
import fr.diginamic.exception.VilleApiException;
import fr.diginamic.repository.VilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VilleService {

    @Autowired
    private VilleDao villeDao;

    @Autowired
    private VilleRepository villeRepository;


    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void saveVille(Ville ville) {
        entityManager.persist(ville);
    }

    /**
     * @return une liste de ville
     */
    @Transactional
    public List<Ville> extraireVille() {
        return villeDao.getVilles();
    }

    /**
     * @param idVille l'identifiant de la ville recherchée
     * @return une ville par son id
     */
    @Transactional
    public Ville extractVilleID(int idVille) {
        return villeDao.getVilleId(idVille);
    }

    /**
     * @param nomVille le nom de la ville recherchée
     * @return une ville par son nom
     */
    @Transactional
    public Ville extractVilleNom(String nomVille){
        return villeDao.getVilleNom(nomVille);
    }

    /**
     * @param ville le nom de la ville ajouté
     * @return une liste de ville
     * @throws VilleApiException
     */
    @Transactional
    public List<Ville> ajouterVille(Ville ville) throws VilleApiException {

        for (Ville v : extraireVille()){
            if ((v.getNom().equals(ville.getNom()))){
                throw new VilleApiException("la ville existe déjà");
            }
        }
        villeDao.insertVille(ville);
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
    public List<Ville> modifierVilleNom(Integer idVille, Ville villeModifiee) throws VilleApiException{

        Ville villeExiste = villeDao.getVilleId(idVille);
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
        villeDao.modifierVille(villeExiste);

        // Retourne la liste des villes
        return extraireVille();
    }

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
     *Supprime une ville par rapport à son Id
     * @param id
     */
    @Transactional
    public void supprimerVille(int id) throws VilleApiException {
        villeDao.supprimerVille(id);
    }



    public List<Ville> rechercherVilleParNom(String prefix) {
        return villeRepository.findByNomStartingWithIgnoreCase(prefix);
    }

    public List<Ville> villesPopulationSupMin(Integer min) {
        return villeRepository.findByPopulationGreaterThanOrderByPopulationDesc(min);
    }

    public List<Ville> villesPopulationsSupMinInfMax(Integer min, Integer max){
        return villeRepository.findByPopulationBetweenOrderByPopulationDesc(min, max);
    }

    public List<Ville> villesPopulationDepartementSupMin(String codeDep,Integer min){
        return villeRepository.findByDepartementCodePostaleAndPopulationGreaterThanOrderByPopulationDesc(codeDep, min);
    }

    public List<Ville> villesPopulationDepartementSupMinInfMax(String codeDep,Integer min,Integer max ){
        return villeRepository.findByDepartementCodePostaleAndPopulationBetweenOrderByPopulationDesc(codeDep, min, max);
    }

    public List<Ville> topVillesDepartement (int n, String codeDep){
        return villeRepository.findTopByDepartementCodePostaleOrderByPopulationDesc(codeDep, n);
    }


}
