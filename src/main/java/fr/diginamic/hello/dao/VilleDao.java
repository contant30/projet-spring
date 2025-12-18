package fr.diginamic.hello.dao;

import fr.diginamic.hello.entite.Ville;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class VilleDao {

    private EntityManager em;

    public List<Ville> getVilles(){
        TypedQuery<Ville> query = em.createQuery("FROM Ville", Ville.class);
        return query.get
    }

}
