package fr.diginamic.hello.dao;

import fr.diginamic.hello.entites.Departement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class DepartementDao {

    private EntityManager em;

    @Autowired
    public DepartementDao(EntityManager entityManager) {
        this.em = entityManager;
    }

    /**
     *
     * @return une liste de département
     */
    @Transactional
    public List<Departement> getDepartement(){
        TypedQuery<Departement> query = em.createQuery("FROM Departement", Departement.class);
        return query.getResultList();
    }

    /**
     * Insertion d'un département en base
     * @param departement
     */
    @Transactional
    public void insertDepartement(Departement departement){
        em.persist(departement);
    }

    /**
     * @param id l'identifiant du département recherché
     * @return un département par son id
     */
    public Departement getDepartementId(int id) {
        return em.find(Departement.class, id);
    }

    /**
     * @param nom le nom du département recherché
     * @return un département par son nom
     */
    @Transactional
    public Departement getDepartementNom(String nom) {
        TypedQuery<Departement> query = em.createQuery("SELECT d FROM Departement d WHERE d.nom = :nom", Departement.class);
        query.setParameter("nom", nom);
        List<Departement> resultat= query.getResultList();
        return resultat.isEmpty() ? null : resultat.get(0);
    }

    @Transactional
    public void modifierDepartement(Departement departement) {
        em.merge(departement);
    }







}
