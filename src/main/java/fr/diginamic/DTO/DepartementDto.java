package fr.diginamic.DTO;


public class DepartementDto {

    private Long id;
    private String nom;
    private String code;
    private String codeRegion;

    public String getNom() {return nom;}

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {return code;}

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeRegion() { return codeRegion; }
    public void setCodeRegion(String codeRegion) { this.codeRegion = codeRegion; }



    @Override
    public String toString() {
        return  "nom = " + nom  +", code=" + code;
    }
}
