package fr.diginamic.hello.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;


@Entity
@Table(name = "departement")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column ( name = "nom_departement")
    private String nom;

    @Column ( name = "code_departement")
    private String codePostale;


    /**
     * Relation One-to-many avec Ville
     * Un département peut avoir plusieurs villes.
     */
    @OneToMany(mappedBy = "departement")
    private Set<Ville> villes;

    // Constructeur sans paramètre pour JPA
    public Departement() {}

    /**
     * Constructeur qui permet de créer une ville
     * @param nom
     * @param codePostale
     */
    public Departement(String nom, String codePostale) {
        this.nom = nom;
        this.codePostale = codePostale;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodePostale() {
        return codePostale;
    }

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }
}
