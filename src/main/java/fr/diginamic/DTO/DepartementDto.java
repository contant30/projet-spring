package fr.diginamic.DTO;


public class DepartementDto {

    private Long id;
    private String nom;
    private String codePostale;

    public String getNom() {return nom;}

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodePostale() {return codePostale;}

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }
}
