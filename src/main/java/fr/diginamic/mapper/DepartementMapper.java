package fr.diginamic.mapper;

import fr.diginamic.DTO.DepartementDto;
import fr.diginamic.entites.Departement;
import org.springframework.stereotype.Component;

@Component
public class DepartementMapper {

    /*
    * Base de données vers JSON
    * Convertit Departement (entité JPA persistée) → DepartementDto (JSON propre pour frontend)
     */
    public DepartementDto toDto (Departement departement){
        DepartementDto dto = new DepartementDto();
        dto.setNom(departement.getNom());
        dto.setCode(departement.getCode());
        dto.setId(departement.getId());

        return dto;
    }

    /*
    * JSON vers base de données
    * Convertit DepartementDto (JSON reçu) → Departement (entité JPA persistable)
     */
    public Departement toBean(DepartementDto dto){
        Departement departement = new Departement();
        departement.setNom(dto.getNom());
        departement.setCode(dto.getCode());
        departement.setId(dto.getId());

        return departement;
    }

}
