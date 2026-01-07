//package fr.diginamic.dao;
//
//
//import fr.diginamic.entites.Ville;
//import fr.diginamic.exception.VilleApiException;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import java.util.List;
//
//@Repository
//public class VilleDao {
//
//    private EntityManager em;
//
//    @Autowired
//    public VilleDao(EntityManager entityManager) {
//            this.em = entityManager;
//        }
//
//    /**
//     *
//      * @return une liste de ville
//     */
//    @Transactional
//    public List<Ville> getVilles(){
//        TypedQuery<Ville> query = em.createQuery("FROM Ville", Ville.class);
//        return query.getResultList();
//    }
//
//    /**
//     * Insertion d'une ville en base
//     * @param ville
//     */
//    @Transactional
//    public void insertVille(Ville ville){
//        em.persist(ville);
//        }
//
//    /**
//     * @param id l'identifiant de la ville recherchée
//     * @return une ville par son id
//     */
//    public Ville getVilleId(int id) {
//        return em.find(Ville.class, id);
//    }
//
//
//    /**
//     * @param nom le nom de la ville recherchée
//     * @return une ville par son nom
//     */
//    @Transactional
//    public Ville getVilleNom(String nom) {
//        TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.nom = :nom", Ville.class);
//        query.setParameter("nom", nom);
//        List<Ville> resultat= query.getResultList();
//        return resultat.isEmpty() ? null : resultat.get(0);
//    }
//
//    /**
//     *Supprime une ville par rapport à son Id
//     * @param id
//     */
//    @Transactional
//    public void supprimerVille(int id) throws VilleApiException {
//        Ville ville = getVilleId(id);
//        if (ville == null) {
//            throw new VilleApiException("La ville avec l'id " + id + " n'existe pas");
//        }
//        em.remove(ville);
//    }
//
//    @Transactional
//    public void modifierVille(Ville ville) {
//        em.merge(ville);
//    }
//
//    }
