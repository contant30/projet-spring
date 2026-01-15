package fr.diginamic.entites;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "ville")
public class Ville {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min =2, message="Le nom de la ville doit avoir au moins 2 caractères ")
    @Column(name = "nom")
    private String nom;

    @Min(value = 1, message="La population de la ville doit être supérieure à 1")
    @Column(name = "population")
    private Integer population;

    @Column(name = "user_maj")
    private String userMaj;

    @Column(name = "date_maj")
    private LocalDateTime dateMaj;

    /**
     * Relation Many-to-one avec l'entité département.
     * représente une ville.
     */
    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

    public Departement getDepartement() {
        return departement;
    }

    // Constructeur sans paramètre pour JPA
    public Ville(){}

    /**
     * Constructeur qui permet de créer une ville
     * @param nom           représente le nom de la ville
     * @param population    représente la population de la ville
     */
    public Ville(Long id,String nom, int population ) {
        this.nom = nom;
        this.population = population;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {return nom;}

    public void setNom(String nom) {this.nom = nom;}

    public void setPopulation(int population) {this.population = population;}

    public int getPopulation() {return population;}

    public void setDepartement(Departement departement) {this.departement = departement;}

    public String getUserMaj() {
        return userMaj;
    }

    public void setUserMaj(String userMaj) {
        this.userMaj = userMaj;
    }

    public LocalDateTime getDateMaj() {
        return dateMaj;
    }

    public void setDateMaj(LocalDateTime dateMaj) {
        this.dateMaj = dateMaj;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", population=" + population +
                ", id=" + id +
                '}';
    }
}
