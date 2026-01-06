package fr.diginamic.mapper;

import fr.diginamic.DTO.DepartementDto;
import fr.diginamic.DTO.VilleDto;
import fr.diginamic.entites.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VilleMapper {

    @Autowired
    private DepartementMapper departementMapper;

    public VilleDto toDto (Ville ville){
        VilleDto dto = new VilleDto();
        dto.setNom(ville.getNom());
        dto.setPopulation(ville.getPopulation());

        if (ville.getDepartement() !=null){
            DepartementDto depDTO= departementMapper.toDto(ville.getDepartement());
            dto.setDepartementDto(depDTO);
            dto.setCodeDepartement(ville.getDepartement().getCodePostale());
            dto.setIdDepartement(ville.getDepartement().getId());
        }
        return dto;
    }

    public Ville toBean(VilleDto dto) {
        Ville ville = new Ville();
        ville.setNom(dto.getNom());
        ville.setPopulation(dto.getPopulation());

        return ville;
    }


}
