package fr.diginamic.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private String code;


    /**
     * Relation One-to-many avec Ville
     * Un département peut avoir plusieurs villes.
     */
    @OneToMany(mappedBy = "departement")
    private Set<Ville> villes;

    /**
     * Relation Many-to-one avec l'entité Region.
     * représente une region.
     */
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;


    // Constructeur sans paramètre pour JPA
    public Departement() {}

    /**
     * Constructeur qui permet de créer une ville
     * @param nom
     * @param code
     */
    public Departement(String nom, String code) {
        this.nom = nom;
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {return id;}

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setId(Long id) {this.id = id;}

    public String getCodeRegion() { return region != null ? region.getCode() : null; }


}
