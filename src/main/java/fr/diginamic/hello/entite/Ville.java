package fr.diginamic.hello.entite;

public class Ville {

    private String nom;
    private int population;
    private Integer id;


    public Ville(){}

    // Constructeur
    public Ville(Integer id,String nom, int population) {
        this.nom = nom;
        this.population = population;
        this.id = id;
    }

    public Integer getId() {return id;}
    public String getNom() {return nom;}

    public void setId(int id) {this.id = id;}

    public void setNom(String nom) {this.nom = nom;}

    public void setPopulation(int population) {this.population = population;}

    public int getPopulation() {return population;}

    @Override
    public String toString() {
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", population=" + population +
                ", id=" + id +
                '}';
    }
}
