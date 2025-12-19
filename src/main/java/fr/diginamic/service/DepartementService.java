package fr.diginamic.service;

import fr.diginamic.dao.DepartementDao;
import fr.diginamic.entites.Departement;
import fr.diginamic.exception.VilleApiException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartementService {

    @Autowired
    private DepartementDao departementDao;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void saveDepartement(Departement departement) {
        entityManager.persist(departement);
    }

    /**
     * @return une liste de département
     */
    @Transactional
    public List<Departement> extraireDepartement() {
        return departementDao.getDepartement();
    }

    /**
     * @param idDepartement l'identifiant de la ville recherchée
     * @return un département par son id
     */
    @Transactional
    public Departement extractDepartementID(Long idDepartement) {
        return departementDao.getDepartementId(idDepartement);
    }

    @Transactional
    public Departement extractDepartementCode(String codeDepartement) {
        return departementDao.getDepartementCode(codeDepartement);
    }

    /**
     * @param nomDepartement le nom de la ville recherchée
     * @return une ville par son nom
     */
    @Transactional
    public Departement extractDepartementNom(String nomDepartement){
        return departementDao.getDepartementNom(nomDepartement);
    }

    /**
     * @param departement le nom du département ajouté
     * @return une liste de departement
     * @throws VilleApiException
     */
    @Transactional
    public List<Departement> ajouterDepartement(Departement departement) throws VilleApiException {

        for (Departement d : extraireDepartement()){
            if ((d.getNom().equals(departement.getNom()))){
                throw new VilleApiException("le departement existe déjà");
            }
        }
        departementDao.insertDepartement(departement);
        return extraireDepartement();
    }

    /**
     * Recherche un département par rapport à son nom
     * @param chaine
     * @return
     * @throws VilleApiException
     */
    @Transactional
    public List<Departement> rechercherDepartementNom(String chaine) throws VilleApiException {
        if (chaine == null || chaine.trim().isEmpty()) {
            throw new VilleApiException("La recherche ne peut être vide ou null");
        }
        return extraireDepartement().stream().filter(departement -> departement.getNom().toLowerCase().startsWith(chaine.toLowerCase())).collect(Collectors.toList());
    }

    /**
     * Modifie un département par rapport à son id
     * @param idDepartement
     * @param departementModifiee
     * @return une liste de ville
     * @throws VilleApiException
     */
    @Transactional
    public List<Departement> modifierDepartementNom(Long idDepartement, Departement departementModifiee) throws VilleApiException{

        Departement departementExiste = departementDao.getDepartementId(idDepartement);
        if (departementExiste == null) {
            throw new VilleApiException("Pas de département trouvée avec l'id : " + idDepartement);
        }

        // Vérifie si le nouveau nom du departement existe déjà
        if (!departementExiste.getNom().equals(departementModifiee.getNom())) {
            for (Departement d : extraireDepartement()) {
                if (d.getNom().equals(departementModifiee.getNom())) {
                    throw new VilleApiException("Un departement avec ce nom existe déjà");
                }
            }
        }
        // Mise à jour
        departementExiste.setNom(departementModifiee.getNom());
        departementExiste.setCodePostale(departementModifiee.getCodePostale());

        // Sauvegarde en base
        departementDao.modifierDepartement(departementExiste);

        // Retourne la liste des villes
        return extraireDepartement();
    }
}
