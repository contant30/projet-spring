package fr.diginamic.DTO;


import jakarta.validation.constraints.Min;

public class VilleDto {

    private String nom;

    @Min(value = 1, message="La population de la ville doit être supérieure à 1")
    private Integer population;


    private String codeDepartement;

    private Long idDepartement;

    private DepartementDto departementDto;




    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodeDepartement() {return codeDepartement;}

    public void setCodeDepartement(String codeDepartement) {this.codeDepartement = codeDepartement;}

    public Long getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(Long idDepartement) {
        this.idDepartement = idDepartement;
    }

    public DepartementDto getDepartementDto() {
        return departementDto;
    }

    public void setDepartementDto(DepartementDto departementDto) {
        this.departementDto = departementDto;
    }


}
