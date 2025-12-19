package fr.diginamic.mapper;

import fr.diginamic.DTO.DepartementDto;
import fr.diginamic.entites.Departement;
import org.springframework.stereotype.Component;

@Component
public class DepartementMapper {

    public DepartementDto toDto (Departement departement){
        DepartementDto dto = new DepartementDto();
        dto.setNom(departement.getNom());
        dto.setCodePostale(departement.getCodePostale());

        return dto;
    }

    public Departement toBean(DepartementDto dto){
        Departement departement = new Departement();
        departement.setNom(dto.getNom());
        departement.setCodePostale(dto.getCodePostale());

        return departement;
    }

}
