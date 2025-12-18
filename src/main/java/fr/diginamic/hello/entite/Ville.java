package fr.diginamic.hello.entite;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Ville {

    private Integer id;

    @NotNull
    @Size(min =2, message="Le nom de la ville doit avoir au moins 2 caractères ")
    private String nom;

    @Min(value = 1, message="La population de la ville doit être supérieure à 1")
    private Integer population;

    private String departement;


    public Ville(){}

    // Constructeur
    public Ville(Integer id,String nom, int population, String departement) {
        this.nom = nom;
        this.population = population;
        this.id = id;
        this.departement = departement;
    }

    public Integer getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getNom() {return nom;}
    public void setNom(String nom) {this.nom = nom;}

    public void setPopulation(int population) {this.population = population;}
    public int getPopulation() {return population;}

    public String getDepartement() {return departement;}
    public void setDepartement(String departement) {this.departement = departement;}

    @Override
    public String toString() {
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", population=" + population +
                ", id=" + id +
                '}';
    }
}
