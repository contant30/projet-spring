package fr.diginamic.hello.service;

import fr.diginamic.hello.dao.VilleDao;
import fr.diginamic.hello.entite.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VilleService {

    @Autowired
    private VilleDao villeDao;

    private final List<Ville> villes = new ArrayList<>();
    private int compteur= 1;


    public VilleService() {
                villes.add (new Ville(compteur++,"Paris", 256350, "35"));
                villes.add (new Ville(compteur++,"Lyon", 456982,"30"));
                villes.add (new Ville(compteur++,"Montpellier", 369852,"45"));
    }
    public List<Ville> extraireVille() {
        return villes;
    }

    // ajout d'une ville avec un compteur incrémenter
    public void ajouterVille(Ville ville) {
        ville.setId(compteur++);
        villes.add(ville);
    }

    // supprimer ville
    public void supprimerVille(int id){
        villes.removeIf(ville -> ville.getId()==id);
    }
}
