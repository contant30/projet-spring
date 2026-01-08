package fr.diginamic.entites;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( name = "nom_region")
    private String nom;

    @Column ( name = "code_region")
    private String code;
    /**
     * Relation One-to-many avec Departement
     * Une region peut avoir plusieurs departement.
     */
    @OneToMany(mappedBy = "region")
    private Set<Departement> departements;

    // Constructeur sans paramètre pour JPA
    public Region(){}

    public Region(Long id, String nom, String code) {
        this.id = id;
        this.nom = nom;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Departement> getDepartements() {
        return departements;
    }

    public void setDepartements(Set<Departement> departements) {
        this.departements = departements;
    }
}
